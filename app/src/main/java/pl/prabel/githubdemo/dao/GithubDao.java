package pl.prabel.githubdemo.dao;

import android.support.annotation.NonNull;

import com.appunite.rx.ObservableExtensions;
import com.appunite.rx.ResponseOrError;
import com.appunite.rx.operators.MoreOperators;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.prabel.githubdemo.api.ApiService;
import pl.prabel.githubdemo.api.model.RepoModel;
import rx.Observable;
import rx.Scheduler;
import rx.subjects.PublishSubject;

@Singleton
public class GithubDao {

    @NonNull
    private final Observable<ResponseOrError<ImmutableList<RepoModel>>> repositoriesErrorObservable;
    @NonNull
    private final PublishSubject<Object> refreshSubject = PublishSubject.create();

    @Inject
    public GithubDao(@NonNull final ApiService apiService,
                     @NonNull final Scheduler networkScheduler,
                     @NonNull final Scheduler uiScheduler) {
        repositoriesErrorObservable = apiService.getRepositories()
                .subscribeOn(networkScheduler)
                .observeOn(uiScheduler)
                .compose(ResponseOrError.<ImmutableList<RepoModel>>toResponseOrErrorObservable())
                .compose(MoreOperators.<ImmutableList<RepoModel>>repeatOnError(networkScheduler))
                .compose(MoreOperators.<ResponseOrError<ImmutableList<RepoModel>>>cacheWithTimeout(networkScheduler))
                .compose(MoreOperators.<ResponseOrError<ImmutableList<RepoModel>>>refresh(refreshSubject))
                .compose(ObservableExtensions.<ResponseOrError<ImmutableList<RepoModel>>>behaviorRefCount());
    }

    @NonNull
    public Observable<ResponseOrError<ImmutableList<RepoModel>>> getRepositoriesErrorObservable() {
        return repositoriesErrorObservable;
    }

    @NonNull
    public PublishSubject<Object> getRefreshSubject() {
        return refreshSubject;
    }
}
