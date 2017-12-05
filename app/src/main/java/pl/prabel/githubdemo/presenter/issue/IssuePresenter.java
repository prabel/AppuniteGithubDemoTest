package pl.prabel.githubdemo.presenter.issue;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appunite.rx.ObservableExtensions;
import com.appunite.rx.ResponseOrError;
import com.appunite.rx.operators.MoreOperators;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

import pl.prabel.githubdemo.api.ApiService;
import pl.prabel.githubdemo.api.model.IssueModel;
import rx.Observable;
import rx.Scheduler;
import rx.subjects.PublishSubject;

/**
 * Created by damian on 05.12.17.
 */

public class IssuePresenter {
    @NonNull
    private final PublishSubject<Object> refreshSubject = PublishSubject.create();

    @NonNull
    private final Observable<ResponseOrError<ImmutableList<IssueModel>>> issuesErrorObservable;

    @Inject
    public IssuePresenter(@NonNull final ApiService apiService,
                          @NonNull final Scheduler networkScheduler,
                          @NonNull final Scheduler uiScheduler,
                          final String repo_name) {
        issuesErrorObservable = apiService.getIssues(repo_name)
                .subscribeOn(networkScheduler)
                .observeOn(uiScheduler)
                .compose(ResponseOrError.<ImmutableList<IssueModel>>toResponseOrErrorObservable())
                .compose(MoreOperators.<ImmutableList<IssueModel>>repeatOnError(networkScheduler))
                .compose(MoreOperators.<ResponseOrError<ImmutableList<IssueModel>>>cacheWithTimeout(networkScheduler))
                .compose(MoreOperators.<ResponseOrError<ImmutableList<IssueModel>>>refresh(refreshSubject))
                .compose(ObservableExtensions.<ResponseOrError<ImmutableList<IssueModel>>>behaviorRefCount());

    }

    @NonNull
    public Observable<ImmutableList<IssueModel>> getIssuesObservable() {
        return issuesErrorObservable
                .compose(ResponseOrError.<ImmutableList<IssueModel>>onlySuccess());
    }
}
