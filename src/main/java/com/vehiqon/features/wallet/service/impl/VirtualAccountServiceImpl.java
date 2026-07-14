package com.vehiqon.features.wallet.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceNotCreatedException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.features.wallet.entity.VirtualAccountEntity;
import com.vehiqon.features.wallet.mapper.NombaMapper;
import com.vehiqon.features.wallet.repository.VirtualAccountRepository;
import com.vehiqon.features.wallet.service.VirtualAccountService;
import com.vehiqon.integrations.nomba.client.NombaClient;
import com.vehiqon.integrations.nomba.dto.NombaDto;
import com.vehiqon.integrations.nomba.service.NombaAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VirtualAccountServiceImpl implements VirtualAccountService{
    private final NombaAuthService nombaAuthService;
    private final NombaClient nombaClient;
    private final AuthService authService;
    private  final NombaMapper nombaMapper;
    private final VirtualAccountRepository virtualAccountRepository;


    @Override
    public NombaDto.VirtualAccountResponse.Data getVirtualAccount() {
        UserEntity user = authService.getAuthenticatedUser();
        return nombaMapper.toVirtualAccResponse(
                virtualAccountRepository.findByUserId(user.getId()).orElseThrow(
                        () -> new BadRequestException("No account found. Kindly request for a wallet account")
                )
        );
    }

    @Override
    @Transactional
    public NombaDto.VirtualAccountResponse.Data updateVirtualAccountName(NombaDto.UpdateVirtualAccountName request) {
        UserEntity user = authService.getAuthenticatedUser();
        VirtualAccountEntity existing = virtualAccountRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Virtual account not found. " +
                "Kindly request for a virtual account"));

        NombaDto.UpdateVirtualAccountResponse updateResponse = nombaClient.updateVirtualAccountName(request, existing.getAccountNumber());
        if (updateResponse == null || updateResponse.data() == null) {
            throw new BadRequestException("Unable to update virtual account.");
        }
//        update local db
        existing.setAccountName(request.accountName());
        return nombaMapper.toVirtualAccResponse(virtualAccountRepository.save(existing));
    }


    @Override
    @Transactional
    public NombaDto.VirtualAccountResponse.Data createVirtualAccount() {
        UserEntity user = authService.getAuthenticatedUser();
        Optional<VirtualAccountEntity> existing = virtualAccountRepository.findByUserId(user.getId());
        if(existing.isPresent()) {
            return nombaMapper.toVirtualAccResponse(existing.get());
        }

        String accountRef = "VEHIQON_" + user.getId();
        String userName = user.getFirstName()+ " " + user.getLastName();
        NombaDto.CreateVirtualAccountRequest request =
                new NombaDto.CreateVirtualAccountRequest(
                        accountRef, userName, "NGN", user.getBvn(), null, null
                );
        NombaDto.VirtualAccountResponse virtualAccount = nombaClient.createVirtualAccount(request);
        if(virtualAccount.data() == null) {
            throw new ResourceNotCreatedException("Virtual account not created. Try again later");

        }
        VirtualAccountEntity account = nombaMapper.toVirtualAccEntity(virtualAccount.data());
        account.setUserId(user.getId());

        return nombaMapper.toVirtualAccResponse( virtualAccountRepository.save(account));
    }


}
