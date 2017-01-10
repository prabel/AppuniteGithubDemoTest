package pl.prabel.githubdemo;

import android.support.multidex.MultiDexApplication;

import pl.prabel.githubdemo.dagger.ApplicationComponent;
import pl.prabel.githubdemo.dagger.ApplicationModule;
import pl.prabel.githubdemo.dagger.DaggerApplicationComponent;

public class MainApplication extends MultiDexApplication {

    private ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        appComponent.inject(this);
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }
}