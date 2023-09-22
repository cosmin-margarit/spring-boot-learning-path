# Spring Proxy

In the context of Spring beans, "proxy" usually refers to the dynamic proxies that Spring creates around beans to
provide features such as transaction management, security, and AOP (Aspect-Oriented Programming). Here are the main
types of proxies used by Spring:

## JDK Dynamic Proxies:

These proxies are built using Java's built-in Proxy class. They work by implementing the interfaces of the target bean.
This means that in order to create a proxy, the target bean must implement at least one interface, and the proxy will
only expose methods from that interface.
You can usually recognize a JDK dynamic proxy because its class name will be something like $ProxyXXX.

## CGLIB Proxies:

**CGLIB (Code Generation Library)** is a bytecode generation library that Spring can use to create proxies for target
beans.
Unlike JDK proxies, CGLIB can create a proxy for any class, not just interfaces. It does this by creating a subclass of
the target bean and overriding methods. If a bean does not implement any interfaces, Spring will automatically use CGLIB
to proxy the bean if proxying is required. You might recognize a CGLIB proxy by the $$ in its class name, like
`SomeBean$$EnhancerBySpringCGLIB$$XXXXXX`.

## Scoped Proxies:

In Spring, certain scopes (like the 'request' and 'session' scopes in a web application) are shorter-lived than the
singleton scope that Spring beans typically live in.
If a singleton bean needs to reference a bean with a shorter lifespan (like a session-scoped bean), Spring can't
directly inject it because the lifespan discrepancy can lead to unexpected behavior.
To solve this, Spring can create a "scoped proxy" around the shorter-lived bean. When the singleton bean interacts with
this proxy, the proxy ensures that the correct instance of the short-lived bean (e.g., the one associated with the
current HTTP session) is used.

# Choosing Between JDK and CGLIB Proxies:

By default, Spring will use JDK dynamic proxies if the target bean implements at least one interface. Otherwise, it will
use CGLIB. However, you can force Spring to use CGLIB proxies by setting the proxyTargetClass attribute of the
`@EnableAspectJAutoProxy` annotation (or <aop:config>) to true. For example:

```java

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {
    // bean definitions
}
```

When dealing with Spring proxies, be aware of the potential pitfalls, such as "self-invocation" where a method inside
the proxied bean calls another method of the same bean. Since the call doesn't go through the proxy, aspects (like
transactions) might not be applied as expected.

Seems like `spring.aop.proxy-target-class` is set to `true` as default and spring will create CGLIB proxies even for
interface-based proxies, so this must be set to `false` to have JDK dynamic proxies