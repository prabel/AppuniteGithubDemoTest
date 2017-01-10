package pl.prabel.githubdemo.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public final class ApplicationModule {
    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @ForApplication
    @Singleton
    Context provideApplicationContext() {
        return context;
    }
}
