package com.vehiqon.features.wallet.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceNotCreatedException;
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
    public NombaDto.VirtualAccountResponse.Data getPrimaryAccount() {
        UserEntity user = authService.getAuthenticatedUser();
        return nombaMapper.toVirtualAccResponse(
                virtualAccountRepository.findByUser(user).orElseThrow(
                        () -> new BadRequestException("No account found. Kindly request for a wallet account")
                )
        );
    }

    @Override
    @Transactional
    public NombaDto.VirtualAccountResponse.Data createVirtualAccount() {
        UserEntity user = authService.getAuthenticatedUser();
        Optional<VirtualAccountEntity> existing = virtualAccountRepository.findByUser(user);
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
        account.setUser(user);

        return nombaMapper.toVirtualAccResponse( virtualAccountRepository.save(account));
    }

}
