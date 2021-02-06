package com.iyang.springboot.condition.test;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

/**
 * @author Yang
 * 当前服务 : spring-boot-condition
 * @date 2021/1/5 / 17:17
 */

public class SelfLaunchedUrlClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {



        return super.loadClass(name,false);
    }

/*    protected Class<?> findClass(String name ) throws ClassNotFoundException {

        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
                @Override
                public Class<?> run() throws Exception {
                    String path = name.replace(".", "/").concat(".class");

                }
            })
        }

    }*/

}
