package pl.prabel.githubdemo.dagger;

import android.content.Context;
import android.view.LayoutInflater;

import dagger.Component;
import pl.prabel.githubdemo.presenter.BaseActivity;

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class
        }
)
public interface BaseActivityComponent {

    BaseActivity baseActivity();
    @ForActivity
    Context context();
    LayoutInflater layoutInflater();
}
