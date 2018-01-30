package pl.prabel.githubdemo.presenter.repo;

import android.support.annotation.NonNull;

import com.appunite.rx.ResponseOrError;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

import pl.prabel.githubdemo.api.model.IssueModel;
import pl.prabel.githubdemo.dao.GithubDao;
import rx.Observable;
import rx.Observer;
import rx.subjects.PublishSubject;

/**
 * Created by testday on 30/01/2018.
 */

public class IssuesPresenter {

    @NonNull
    private Observable<ResponseOrError<ImmutableList<IssueModel>>> issuesErrorObservable;
    @NonNull
    private PublishSubject<Object> refreshSubject;
    @NonNull
    private final PublishSubject<IssueModel> issueClickSubject = PublishSubject.create();


    private final GithubDao githubDao;

    @Inject
    public IssuesPresenter(@NonNull final GithubDao githubDao) {
        this.githubDao = githubDao;
    }

    public void fetchIssues(String login, String repo){
        issuesErrorObservable = githubDao.getIssuesErrorObservable(login, repo);
        refreshSubject = githubDao.getRefreshSubject();
    }

    @NonNull
    public Observable<Boolean> getProgressObservable() {
        return ResponseOrError.combineProgressObservable(
                ImmutableList.of(ResponseOrError.transform(issuesErrorObservable)));
    }

    @NonNull
    public Observable<Throwable> getErrorObservable() {
        return issuesErrorObservable
                .compose(ResponseOrError.<ImmutableList<IssueModel>>onlyError());
    }

    @NonNull
    public Observable<ImmutableList<IssueModel>> getIssuesObservable() {
        return issuesErrorObservable
                .compose(ResponseOrError.<ImmutableList<IssueModel>>onlySuccess());
    }

    @NonNull
    public Observer<Object> getRefreshSubject() {
        return refreshSubject;
    }

}
