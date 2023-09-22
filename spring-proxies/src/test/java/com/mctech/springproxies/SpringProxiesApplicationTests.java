package com.mctech.springproxies;

import com.mctech.springproxies.cglib.MyCglibService;
import com.mctech.springproxies.commons.UserUtils;
import com.mctech.springproxies.domain.User;
import com.mctech.springproxies.domain.UserRepository;
import com.mctech.springproxies.dynamic.MyDynamicService;
import com.mctech.springproxies.dynamic.MyDynamicServiceImpl;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringProxiesApplicationTests {

    @Autowired
    private MyDynamicService dynamicService;

    @Autowired
    private MyCglibService cglibService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    void proxiesAreCreatedCorrectly() {
        final String cglibProxy = cglibService.getClass().getName();
        final String dynamicProxy = dynamicService.getClass().getName();

        Assertions.assertTrue(cglibProxy.contains("CGLIB"));
        Assertions.assertFalse(dynamicProxy.contains("CGLIB"));
    }

    @Test
    void givenDynamicProxy_whenSelfInvocation_thenNoNewTransactionCreated() {
        String randomName = RandomString.make(5);
        String reversed = UserUtils.getReversed(randomName);
        boolean internalTransactionCall = false;
        User user = dynamicService.saveUser(randomName, internalTransactionCall);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(randomName, user.getName());
        Assertions.assertTrue(userRepository.findByName(reversed).isPresent());
        Assertions.assertEquals(2, userRepository.findAll().size());
    }

    @Test
    void givenDynamicProxy_whenExternalInvocation_thenTransactionCreated() {
        String randomName = RandomString.make(5);
        String reversed = UserUtils.getReversed(randomName);
        boolean externalTransactionCall = true;

        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> dynamicService.saveUser(randomName, externalTransactionCall));
        Assertions.assertEquals("Existing transaction found for transaction marked with propagation 'never'", exception.getMessage());
        Assertions.assertTrue(userRepository.findByName(reversed).isEmpty());
        Assertions.assertEquals(0, userRepository.findAll().size());
    }

    @Test
    void givenCglibProxy_whenSelfInvocation_thenNoNewTransactionCreated() {
        String randomName = RandomString.make(5);
        String reversed = UserUtils.getReversed(randomName);
        boolean internalTransactionCall = false;
        User user = cglibService.saveUser(randomName, internalTransactionCall);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(randomName, user.getName());
        Assertions.assertTrue(userRepository.findByName(reversed).isPresent());
        Assertions.assertEquals(2, userRepository.findAll().size());
    }

    @Test
    void givenCglibProxy_whenExternalInvocation_thenTransactionCreated() {
        String randomName = RandomString.make(5);
        String reversed = UserUtils.getReversed(randomName);
        boolean externalTransactionCall = true;

        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> cglibService.saveUser(randomName, externalTransactionCall));
        Assertions.assertEquals("Existing transaction found for transaction marked with propagation 'never'", exception.getMessage());
        Assertions.assertTrue(userRepository.findByName(reversed).isEmpty());
        Assertions.assertEquals(0, userRepository.findAll().size());
    }


}
