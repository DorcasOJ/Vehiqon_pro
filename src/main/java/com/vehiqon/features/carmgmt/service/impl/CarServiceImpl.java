package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.CarDocumentDto;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.entities.CarDocumentEntity;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import com.vehiqon.features.carmgmt.enums.CarStatus;
import com.vehiqon.features.carmgmt.mapper.CarMapper;
import com.vehiqon.features.carmgmt.mapper.CarResponseMapper;
import com.vehiqon.features.carmgmt.repository.CarBrandRepository;
import com.vehiqon.features.carmgmt.repository.CarModelRepository;
import com.vehiqon.features.carmgmt.repository.CarRepository;
import com.vehiqon.features.carmgmt.service.CarService;
import com.vehiqon.features.insights.analytics.dto.requestScope.AnalyticsContext;
import com.vehiqon.features.insights.auditLog.dto.requestScope.AuditContext;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.security.model.CustomerUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final AuthService authService;
    private final CarRepository carRepository;
    private final CarBrandRepository brandRepository;
    private final CarModelRepository modelRepository;
    private final UserRepository userRepository;
    private final CarMapper carMapper;
    private final CarResponseMapper carResponseMapper;
    private final AuditContext auditContext;
    private final AnalyticsContext analyticsContext;



    @Override
    public CarDto.CarResponse registerCar(CarDto.CreateCarRequest request) {
        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
        BrandEntity brand = brandRepository.findById(request.carBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Car brand not found"));
        CarModelEntity model = modelRepository.findById(request.carModelId())
                .orElseThrow(() -> new ResourceNotFoundException("Model not found"));
        if(!model.getCarBrandId().equals(brand.getId())) {
            throw new BadRequestException("Selected model does not belong to selected brand");
        }
        validateUniqueFields(request);

        CarEntity carMapping = carMapper.toEntity(request);
        UUID userId = authenticatedUser.user().getId();
        carMapping.setUserId(userId);
        carMapping.setCarBrandId(brand.getId());
        carMapping.setCarModelId(model.getId());
        carMapping.setStatus(CarStatus.INACTIVE);
        return carResponseMapper.toResponse(carMapper.toResponse(carRepository.save(carMapping)));
    }


    @Override
    public Page<CarDetailsResponse> getMyCars(Pageable pageable) {
        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
        return carRepository.findCarsDetailsByUserId(authenticatedUser.user().getId(),
                pageable).orElseThrow(() -> new ResourceNotFoundException("Cars not found"));
    }

    @Override
    public CarDetailsResponse getCar(UUID carId) {
        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
        return carRepository.findCarDetailsByUser(carId,authenticatedUser.user().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

    }

    @Override
    @Transactional
    public List<CarDto.CarEntityResponse> getCarsDeleted() {
        UUID userId = authService.getAuthenticatedUser().user().getId();
        List<CarEntity> carEntity = carRepository.findByUserIdAndDeletedTrue(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No Deleted Cars")
                );
        return carEntity.stream().map(
                carMapper::toResponse
        ).toList();
    }


    @Override
    public CarDto.CarResponse update(UUID carId, CarDto.UpdateCarRequest request) {
        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
        CarEntity car = carRepository.findByIdAndUserIdAndDeletedFalse(carId, authenticatedUser.user().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        validateUpdateFields(request, car);
        carMapper.updateEntity(request, car);
        return carResponseMapper.toResponse(carMapper.toResponse(carRepository.save(car)));
    }

    @Override
    public CarDto.CarStatisticsResponse getCarStatistics() {
        UUID userId = authService.getAuthenticatedUser().user().getId();
        return carRepository.getCarStatistics(userId);
    }

    @Override
    @Transactional
    public void deleteCar(UUID carId) {
        UUID userId = authService.getAuthenticatedUser().user().getId();
        CarEntity car = carRepository.findByIdAndUserIdAndDeletedFalse(carId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        car.softDelete(userId);
        carRepository.save(car);
        auditContext.recordDelete(car.getId(), userId, "cars");
    }

    @Override
    @Transactional
    public void restoreCar(UUID carId) {
        UUID userId = authService.getAuthenticatedUser().user().getId();
        CarEntity car = carRepository.findByIdAndUserIdAndDeletedTrue(carId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        car.restore();
        carRepository.save(car);
        auditContext.recordRestore(car.getId(), userId,"cars");
    }

    @Override
    @Transactional
    public void deleteMultipleCarsForUser(List<UUID> carIds) {
        LocalDateTime now = LocalDateTime.now();
        UUID userId = authService.getAuthenticatedUser().user().getId();
        List<UUID> distinctIds = carIds.stream().distinct().toList();

        if(!doAllCarsExistForUser(distinctIds, userId)) {
            List<UUID> nonExistingIds = findCarIdsThatDoNotExistInUser(carIds, userId);
            throw new ResourceNotFoundException("Failed. Car Id(s) do not exist for this user." + nonExistingIds);
        }

        int deletedCount = carRepository.softDeleteByIdInAndUserId(carIds, userId, now, userId);
        auditContext.recordMultipleDelete(carIds, userId, deletedCount, "cars");
        if (deletedCount != carIds.size()) {
            throw new BadRequestException("Something went wrong. All cars were not deleted.");
        }
    }


//    ADMIN

    @Override
    public Page<CarDetailsResponse> getAllCars(Pageable pageable) {
        return carRepository.findAllCarDetails(pageable).orElseThrow(
                () -> new ResourceNotFoundException("No car found")
        );
    }

    @Override
    public CarDetailsResponse getCarById(UUID carId) {
        return carRepository.findCarDetailsById(carId).orElseThrow(
                ()-> new ResourceNotFoundException("Car not found")
        );
    }

    @Transactional()
    @Override
    public Page<CarDetailsResponse> searchCars(String query, UUID brandId, CarStatus status, Pageable pageable) {
//        UUID userId = authService.getAuthenticatedUser().user().getId();
        //        analyticsContext.recordSearch(query,  searchResult.getNumberOfElements(),
//                searchResult.getTotalElements(), searchResult.getNumber(), searchResult.getSize());
        return carRepository.searchCarsForAdmin(query, brandId, status, pageable).orElseThrow(() -> new ResourceNotFoundException("No search result"));
    }

    @Override
    public Page<CarDetailsResponse> getCarsByUser(UUID userId, Pageable pageable) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return carRepository.findCarsDetailsByUserId(user.getId(), pageable).orElseThrow(
                () -> new ResourceNotFoundException("Cars not found"));
    }

    @Override
    public CarDetailsResponse getUserCar(UUID userId, UUID carId) {
        return carRepository.findCarDetailsByUser(carId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

    }

    @Override
    public CarDto.CarResponse updateUserCar(UUID carId, CarDto.UpdateCarRequest request) {
        CarEntity car = carRepository.findByIdAndDeletedFalse(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        validateUpdateFields(request, car);
        carMapper.updateEntity(request, car);
        CarEntity savedCar = carRepository.save(car);
        return carResponseMapper.toResponse(carMapper.toResponse(savedCar));

    }

    @Override
    @Transactional
    public void deleteCarByAdmin(UUID carId) {
        UUID userId = authService.getAuthenticatedUser().user().getId();
        CarEntity car = carRepository.findById(carId).orElseThrow(
                () -> new ResourceNotFoundException("Failed. Car does not exist")
        );
        car.softDelete(userId);
        carRepository.save(car);
        auditContext.recordDelete(carId, userId,"cars");
    }

    @Override
    @Transactional
    public void deleteMultipleCarByAdmin(List<UUID> carIds) {
        LocalDateTime now = LocalDateTime.now();
        UUID userId = authService.getAuthenticatedUser().user().getId();
        List<UUID> distinctIds = carIds.stream().distinct().toList();

        if(!doAllCarsExists(distinctIds)) {
            List<UUID> nonExistingIds = findCarIdsThatDoNotExist(distinctIds);
            throw new ResourceNotFoundException("Failed. Car Id(s) do not exist." + nonExistingIds);
        }
        int deletedCount = carRepository.softDeleteAllByIdIn(distinctIds, now, userId);
        auditContext.recordMultipleDelete(carIds, userId, deletedCount, "cars");
        if (deletedCount != distinctIds.size()) {
            throw new BadRequestException("Something went wrong. All cars were not deleted.");
        }
    }

    @Override
    @Transactional
    public void restoreCarByAdmin(UUID carId) {
        UUID userId = authService.getAuthenticatedUser().user().getId();
        CarEntity car = carRepository.findByIdAndDeletedTrue(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        car.restore();
        carRepository.save(car);
        auditContext.recordDelete(car.getId(), userId,"cars");
    }

    @Override
    @Transactional
    public void restoreMultipleCars(List<UUID> carIds) {
        UUID userId = authService.getAuthenticatedUser().user().getId();
        List<UUID> distinctIds = carIds.stream().distinct().toList();

        if(!doAllCarsExists(distinctIds)) {
            List<UUID> nonExistingIds = findCarIdsThatDoNotExist(distinctIds);
            throw new ResourceNotFoundException("Failed. Car Id(s) do not exist." + nonExistingIds);
        }
        int restoredCount = carRepository.restoreAllByIdIn(distinctIds);
        auditContext.recordMultipleRestored(carIds, restoredCount, "cars");
        if (restoredCount != distinctIds.size()) {
            throw new BadRequestException("Something went wrong. All cars were not deleted.");
        }
    }

    private boolean doAllCarsExistForUser(List<UUID> distinctIds, UUID userId){
        if (distinctIds == null || distinctIds.isEmpty()) return true;
        long existingCount = carRepository.countByIdInAndUserIdAndDeletedFalse(distinctIds, userId);
        return existingCount == distinctIds.size();
    }

    private List<UUID> findCarIdsThatDoNotExistInUser(List<UUID> carIds, UUID userId) {
        if (carIds == null || carIds.isEmpty()) return List.of();
        List<UUID> distinctIds = carIds.stream().distinct().toList();
        List<UUID> existingIds =carRepository.findExistingIdsByInAndUserId(carIds, userId);
        return distinctIds.stream()
                .filter( id -> !existingIds.contains(id))
                .toList();
    }

    private boolean doAllCarsExists(List<UUID> distinctIds) {
        if (distinctIds == null || distinctIds.isEmpty()) return true;
        long existingCount = carRepository.countByIdInAndDeletedFalse(distinctIds);
        return existingCount == distinctIds.size();
    }

    private List<UUID> findCarIdsThatDoNotExist(List<UUID> carIds) {
        if (carIds == null || carIds.isEmpty()) return List.of();
        List<UUID> existingIds =carRepository.findExistingIdsByIdIn(carIds);
        return carIds.stream()
                .filter( id -> !existingIds.contains(id))
                .toList();
    }

    private void validateUpdateFields(CarDto.CarRequest request, CarEntity car ){
        BrandEntity brand = brandRepository.findById(
                        request.carBrandId() != null ? request.carBrandId() : car.getCarBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

        CarModelEntity model = modelRepository.findById(
                        request.carModelId() != null ? request.carModelId() : car.getCarModelId() )
                .orElseThrow(() -> new ResourceNotFoundException("Model not found"));

        if (!model.getCarBrandId().equals(brand.getId())) {
            throw new BadRequestException("Selected model does not belong to the selected brand");
        }

        validateUniqueFields(request);
    }

    private void validateUniqueFields(CarDto.CarRequest request) {
        if (request.vin() != null && carRepository.existsByVinAndDeletedFalse(request.vin())) {
            throw new BadRequestException("VIN already exists");
        }

        if (request.plateNumber() != null && carRepository.existsByPlateNumberAndDeletedFalse(request.plateNumber())) {
            throw new BadRequestException("Plate number already exists");
        }

        if (request.engineNumber() != null && request.engineNumber() != null
                && !request.engineNumber().isBlank()
                && carRepository.existsByEngineNumberAndDeletedFalse(request.engineNumber())) {
            throw new BadRequestException("Engine number already exists");
        }
    }
}
