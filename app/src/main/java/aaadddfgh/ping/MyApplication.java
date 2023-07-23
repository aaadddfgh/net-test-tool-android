package aaadddfgh.ping;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@HiltAndroidApp
public final class MyApplication extends Application {
    ExecutorService executorService = Executors.newFixedThreadPool(2);

}
