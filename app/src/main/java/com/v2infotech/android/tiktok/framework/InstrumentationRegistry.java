package com.v2infotech.android.tiktok.framework;

import android.annotation.SuppressLint;
import android.app.Instrumentation;

import java.util.concurrent.atomic.AtomicReference;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by sonali on 5/4/2018.
 */

public final class InstrumentationRegistry {

    private static final AtomicReference<Instrumentation> sInstrumentationRef =
            new AtomicReference<Instrumentation>(null);

    /**
     * Returns the instrumentation currently running.
     *
     * @throws IllegalStateException if instrumentation hasn't been registered
     */
    @SuppressLint("RestrictedApi")
    public static Instrumentation getInstance() {
        return checkNotNull(sInstrumentationRef.get(), "No instrumentation registered. " +
                "Must run under a registering instrumentation.");
    }

    /**
     * Records/exposes the instrumentation currently running.
     * <p>
     * This is a global registry - so be aware of the impact of calling this method!
     * </p>
     */
    public static void registerInstance(Instrumentation instrumentation) {
        sInstrumentationRef.set(instrumentation);
    }

    private InstrumentationRegistry() { }
}
