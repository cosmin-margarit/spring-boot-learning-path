package com.mctech.springproxies.dynamic;

import com.mctech.springproxies.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface MyDynamicService {

    User saveUser(String name, boolean external);

    User saveUserWithInverseName(String name);
}
