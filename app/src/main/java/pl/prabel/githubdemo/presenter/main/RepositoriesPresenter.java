package pl.prabel.githubdemo.presenter.main;

import android.support.annotation.NonNull;

import com.appunite.rx.ResponseOrError;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

import pl.prabel.githubdemo.api.model.RepoModel;
import pl.prabel.githubdemo.dao.GithubDao;
import rx.Observable;
import rx.Observer;
import rx.subjects.PublishSubject;

public class RepositoriesPresenter {

    @NonNull
    private final Observable<ResponseOrError<ImmutableList<RepoModel>>> repositoriesErrorObservable;
    @NonNull
    private final PublishSubject<Object> refreshSubject;
    @NonNull
    private final PublishSubject<RepoModel> repoClickSubject = PublishSubject.create();

    @Inject
    public RepositoriesPresenter(@NonNull final GithubDao githubDao) {
        repositoriesErrorObservable = githubDao.getRepositoriesErrorObservable();
        refreshSubject = githubDao.getRefreshSubject();
    }

    @NonNull
    public Observable<Boolean> getProgressObservable() {
        return ResponseOrError.combineProgressObservable(
                ImmutableList.of(ResponseOrError.transform(repositoriesErrorObservable)));
    }

    @NonNull
    public Observable<Throwable> getErrorObservable() {
        return repositoriesErrorObservable
                .compose(ResponseOrError.<ImmutableList<RepoModel>>onlyError());
    }

    @NonNull
    public Observable<ImmutableList<RepoModel>> getRepositoriesObservable() {
        return repositoriesErrorObservable
                .compose(ResponseOrError.<ImmutableList<RepoModel>>onlySuccess());
    }

    @NonNull
    public Observer<Object> getRefreshSubject() {
        return refreshSubject;
    }

    @NonNull
    public Observer<RepoModel> repoClickObserver() {
        return repoClickSubject;
    }

    @NonNull
    public Observable<RepoModel> repoClickObservable() {
        return repoClickSubject;
    }

}
