package com.mctech.springproxies;

import com.mctech.springproxies.commons.UserUtils;
import com.mctech.springproxies.domain.User;
import com.mctech.springproxies.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExternalDynamicProxy {
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.NEVER)
    public User saveUserWithInverseName(String name) {
        return UserUtils.saveUserWithInverseName(name, userRepository);
    }
}
