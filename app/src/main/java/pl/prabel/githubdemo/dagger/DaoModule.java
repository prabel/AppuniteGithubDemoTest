package pl.prabel.githubdemo.dagger;


import com.appunite.rx.dagger.NetworkScheduler;
import com.appunite.rx.dagger.UiScheduler;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.prabel.githubdemo.api.ApiService;
import pl.prabel.githubdemo.dao.GithubDao;
import rx.Scheduler;

@Module
public class DaoModule {

    @Provides
    @Singleton
    static GithubDao provideCategoriesDao(@Nonnull ApiService apiService,
                                          @Nonnull @NetworkScheduler Scheduler networkSchedule,
                                          @Nonnull @UiScheduler Scheduler uiScheduler) {
        return new GithubDao(apiService, networkSchedule, uiScheduler);
    }
}
