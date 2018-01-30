package pl.prabel.githubdemo.dao;

import android.support.annotation.NonNull;

import com.appunite.rx.ObservableExtensions;
import com.appunite.rx.ResponseOrError;
import com.appunite.rx.operators.MoreOperators;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.prabel.githubdemo.api.ApiService;
import pl.prabel.githubdemo.api.model.IssueModel;
import pl.prabel.githubdemo.api.model.RepoModel;
import rx.Observable;
import rx.Scheduler;
import rx.subjects.PublishSubject;

@Singleton
public class GithubDao {

    @NonNull
    private final PublishSubject<Object> refreshSubject = PublishSubject.create();
    private final ApiService apiService;
    private final Scheduler networkScheduler;
    private final Scheduler uiScheduler;

    @Inject
    public GithubDao(@NonNull final ApiService apiService,
                     @NonNull final Scheduler networkScheduler,
                     @NonNull final Scheduler uiScheduler) {
        this.apiService = apiService;
        this.networkScheduler = networkScheduler;
        this.uiScheduler = uiScheduler;
    }

    @NonNull
    public Observable<ResponseOrError<ImmutableList<RepoModel>>> getRepositoriesErrorObservable() {
        return apiService.getRepositories()
                .subscribeOn(networkScheduler)
                .observeOn(uiScheduler)
                .compose(ResponseOrError.<ImmutableList<RepoModel>>toResponseOrErrorObservable())
                .compose(MoreOperators.<ImmutableList<RepoModel>>repeatOnError(networkScheduler))
                .compose(MoreOperators.<ResponseOrError<ImmutableList<RepoModel>>>cacheWithTimeout(networkScheduler))
                .compose(MoreOperators.<ResponseOrError<ImmutableList<RepoModel>>>refresh(refreshSubject))
                .compose(ObservableExtensions.<ResponseOrError<ImmutableList<RepoModel>>>behaviorRefCount());
    }

    @NonNull
    public Observable<ResponseOrError<ImmutableList<IssueModel>>> getIssuesErrorObservable(String owner, String repo) {
        return apiService.getRepoIssues(owner, repo)
                .subscribeOn(networkScheduler)
                .observeOn(uiScheduler)
                .compose(ResponseOrError.<ImmutableList<IssueModel>>toResponseOrErrorObservable())
                .compose(MoreOperators.<ImmutableList<IssueModel>>repeatOnError(networkScheduler))
                .compose(MoreOperators.<ResponseOrError<ImmutableList<IssueModel>>>cacheWithTimeout(networkScheduler))
                .compose(MoreOperators.<ResponseOrError<ImmutableList<IssueModel>>>refresh(refreshSubject))
                .compose(ObservableExtensions.<ResponseOrError<ImmutableList<IssueModel>>>behaviorRefCount());
    }

    @NonNull
    public PublishSubject<Object> getRefreshSubject() {
        return refreshSubject;
    }
}
