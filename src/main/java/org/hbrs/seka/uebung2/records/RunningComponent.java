package org.hbrs.seka.uebung2;

import java.lang.reflect.Method;
import java.net.URLClassLoader;

public record RunningComponent(
        Object instance,
        Method stopMethod,
        URLClassLoader classLoader,
        Thread thread
) {}