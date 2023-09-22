package com.mctech.springproxies.commons;

import com.mctech.springproxies.domain.User;
import com.mctech.springproxies.domain.UserRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtils {
    public static final String PREFIX = "aa";

    public static User saveUserWithInverseName(String name, UserRepository userRepository) {
        assert name != null;
        String reversed = getReversed(name);

        return userRepository.save(new User().setName(reversed));

    }

    public static String getReversed(String name) {
        return new StringBuilder(name).reverse().toString();
    }
}
