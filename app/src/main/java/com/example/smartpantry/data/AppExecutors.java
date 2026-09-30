package com.example.smartpantry.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Simple executor for database work off the main thread. */
public class AppExecutors {
    private static final ExecutorService DISK_IO = Executors.newSingleThreadExecutor();

    public static ExecutorService diskIO() {
        return DISK_IO;
    }
}
