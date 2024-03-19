package org.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Utils {

    static PersonInvocationHandler referPersonInvocationHandler;

    static <T> T cache(T t) {

        ClassLoader UniClassLoader = t.getClass().getClassLoader();

        Class[] interfaces = t.getClass().getInterfaces();

        PersonInvocationHandler refPersonInvocationHandler = new PersonInvocationHandler(t);

        referPersonInvocationHandler = refPersonInvocationHandler;

        T proxyObj = (T) Proxy.newProxyInstance(UniClassLoader, interfaces, refPersonInvocationHandler);

        return proxyObj;
    }
}
