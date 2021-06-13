package com.yallage.oceanik.loader.util;

import com.google.common.annotations.Beta;
import lombok.SneakyThrows;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URL;

/**
 * Special utilities, which are unsafe.
 *
 * @author Milkory
 */
public class Special {

    private static final Unsafe unsafe = getUnsafe();
    private static final MethodHandles.Lookup lookup =
            (MethodHandles.Lookup) getStatic(MethodHandles.Lookup.class, "IMPL_LOOKUP");

    /** Get the {@link Unsafe} instance by reflecting. */
    @SneakyThrows public static Unsafe getUnsafe() {
        var field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(null);
    }

    /** Get a specified field of an object by unsafe operating. */
    @SneakyThrows public static Object get(Object obj, String name) {
        var field = obj.getClass().getDeclaredField(name);
        var offset = unsafe.objectFieldOffset(field);
        return unsafe.getObject(obj, offset);
    }

    /** Get a specified static field of a class by unsafe operating. */
    @SneakyThrows public static Object getStatic(Class<?> clazz, String name) {
        var field = clazz.getDeclaredField(name);
        var base = unsafe.staticFieldBase(field);
        var offset = unsafe.staticFieldOffset(field);
        return unsafe.getObject(base, offset);
    }

    /** Invoke the method {@link jdk.internal.loader.URLClassPath#addURL(URL)} by unsafe operating. */
    @Beta
    public static void addURL(File file) throws Throwable {
        var ucp = get(ClassLoader.getSystemClassLoader(), "ucp");
        lookup.findVirtual(ucp.getClass(), "addURL", MethodType.methodType(void.class, URL.class))
                .invoke(ucp, file.toURI().toURL());
    }

}
