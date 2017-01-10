package pl.prabel.githubdemo.dagger;

import android.content.Context;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;
import pl.prabel.githubdemo.presenter.BaseActivity;

@Module
public class ActivityModule {

    private BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    BaseActivity activity() {
        return activity;
    }

    @Provides
    @ForActivity
    Context context() {
        return activity;
    }

    @Provides
    static LayoutInflater provideLayoutInflater(BaseActivity activity) {
        return activity.getLayoutInflater();
    }
}
