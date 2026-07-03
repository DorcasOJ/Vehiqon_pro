package com.vehiqon.features.carmgmt.config;

import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import com.vehiqon.features.carmgmt.repository.CarBrandRepository;
import com.vehiqon.features.carmgmt.repository.CarModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CarModelBrandDataSeeder {

    private final CarBrandRepository brandRepository;
    private final CarModelRepository carModelRepository;

    @Bean
    public CommandLineRunner seedMasterData() {
        return args -> {
            if(brandRepository.count() > 0) {
                return;
            }

            seedBrand("Toyota",
                    "Corolla",
                    "Camry",
                    "Hilux",
                    "RAV4");

            seedBrand("Honda",
                    "Civic",
                    "Accord",
                    "CR-V");

            seedBrand("BMW",
                    "3 Series",
                    "5 Series",
                    "X5");

            seedBrand("Mercedes-Benz",
                    "C-Class",
                    "E-Class",
                    "GLC");
            seedBrand("Lexus",
                    "ES350",
                    "RX350",
                    "GX460");

            seedBrand("Hyundai",
                    "Elantra",
                    "Sonata",
                    "Tucson");

            System.out.println("Master data seeded successfully.");
        };
    }

    private void seedBrand(String brandName, String... models) {
        BrandEntity brand = brandRepository.save(
                BrandEntity.builder()
                        .name(brandName)
                        .build()
        );

        for(String model : models) {
            carModelRepository.save(
                    CarModelEntity.builder()
                            .name(model)
                            .brand(brand)
                            .build()
            );
        }
    }
}
