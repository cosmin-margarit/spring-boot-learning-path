package com.mctech.springproxies.cglib;

import com.mctech.springproxies.ExternalDynamicProxy;
import com.mctech.springproxies.commons.UserUtils;
import com.mctech.springproxies.domain.User;
import com.mctech.springproxies.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class MyCglibService {
    private final UserRepository userRepository;
    private final ExternalDynamicProxy externalDynamicProxy;

    @Transactional
    public User saveUser(String name, boolean external) {
        assert TransactionSynchronizationManager.isActualTransactionActive();
        User user = userRepository.save(new User().setName(name));
        if (external) {
            externalDynamicProxy.saveUserWithInverseName(name);
        } else {
            saveUserWithInverseName(name);
        }
        return user;
    }

    @Transactional(propagation = Propagation.NEVER)
    public User saveUserWithInverseName(String name) {
        return UserUtils.saveUserWithInverseName(name, userRepository);
    }

}
