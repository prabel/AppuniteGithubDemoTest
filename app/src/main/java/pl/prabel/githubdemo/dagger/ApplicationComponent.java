package pl.prabel.githubdemo.dagger;

import android.os.Handler;

import com.appunite.rx.dagger.NetworkScheduler;
import com.appunite.rx.dagger.UiScheduler;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import pl.prabel.githubdemo.MainApplication;
import pl.prabel.githubdemo.api.ApiService;
import pl.prabel.githubdemo.content.TokenPreferences;
import pl.prabel.githubdemo.dao.GithubDao;
import rx.Scheduler;

@Singleton
@Component(modules = {ApplicationModule.class, BaseModule.class, DaoModule.class})
public interface ApplicationComponent {
    Picasso picasso();
    Gson gson();
    ApiService apiService();
    Handler handler();
    @UiScheduler
    Scheduler uiScheduler();
    @NetworkScheduler
    Scheduler networkScheduler();
    TokenPreferences tokenPreferences();

    GithubDao githubDao();

    void inject(MainApplication app);
}
