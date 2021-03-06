package me.wobblyyyy.pathfinder.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static java.lang.invoke.MethodType.methodType;

/**
 * Utility class designed to deal with bad API versions - 1.8 vs 9, for example.
 *
 * @author Colin Robertson
 * @since 0.0.0
 */
public class BcThread {
    /**
     * Handler for spin wait operation.
     */
    private static final MethodHandle ON_SPIN_WAIT_HANDLE = resolve();

    /**
     * Empty constructor - no construction here.
     */
    private BcThread() {
    }

    /**
     * Resolve the method for the {@code onSpinWait()} functionality.
     *
     * @return a method handle for the {@code onSpinWait()} method.
     */
    private static MethodHandle resolve() {
        try {
            return MethodHandles.lookup().findStatic(
                    Thread.class, "onSpinWait", methodType(void.class));
        } catch (Exception ignore) {
        }

        return null;
    }

    /**
     * If the current version of Java is above 9, indicating we have access
     * to the {@code Thread} class's {@code onSpinWait()} method, we can simply
     * call that method. Otherwise, we just do absolutely nothing to prevent
     * any exceptions from being thrown.
     *
     * @return whether or not the {@code onSpinWait()} method is working as
     * originally intended. True if it's working, false if we have to use
     * our own NOP method.
     */
    public static boolean spin() {
        if (ON_SPIN_WAIT_HANDLE != null) {
            try {
                ON_SPIN_WAIT_HANDLE.invokeExact();
                return true;
            } catch (Throwable ignore) {
            }
        }
        return false;
    }
}
