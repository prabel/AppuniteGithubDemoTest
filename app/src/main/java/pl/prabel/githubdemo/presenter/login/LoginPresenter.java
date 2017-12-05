package pl.prabel.githubdemo.presenter.login;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.appunite.rx.ResponseOrError;
import com.appunite.rx.dagger.NetworkScheduler;
import com.appunite.rx.dagger.UiScheduler;
import com.appunite.rx.functions.BothParams;

import javax.inject.Inject;

import pl.prabel.githubdemo.api.ApiService;
import pl.prabel.githubdemo.content.TokenPreferences;
import retrofit.client.Response;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

public class LoginPresenter {

    @NonNull
    private final Observable<ResponseOrError<Response>> loginErrorObservable;
    @NonNull
    private final PublishSubject<BothParams<String, String>> loginObserver = PublishSubject.create();
    @NonNull
    private final TokenPreferences tokenPreferences;

    @Inject
    public LoginPresenter(@NonNull final ApiService apiService,
                          @NonNull @NetworkScheduler final Scheduler networkScheduler,
                          @NonNull @UiScheduler final Scheduler uiScheduler,
                          @NonNull final TokenPreferences tokenPreferences) {
        this.tokenPreferences = tokenPreferences;
        loginErrorObservable = loginObserver
                .map(new MapParamsToToken())
                .doOnNext(new SaveTokenAction(tokenPreferences))
                .flatMap(new Func1<String, Observable<ResponseOrError<Response>>>() {
                    @Override
                    public Observable<ResponseOrError<Response>> call(String authorization) {
                        return apiService.authorizations()
                                .subscribeOn(networkScheduler)
                                .observeOn(uiScheduler)
                                .compose(ResponseOrError.<Response>toResponseOrErrorObservable());
                    }
                });
    }

    @NonNull
    public Observable<Throwable> errorObservable() {
        return loginErrorObservable.compose(ResponseOrError.<Response>onlyError())
                .doOnNext(new ClearPrefsFunc())
                .filter(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable throwable) {
                        return throwable != null;
                    }
                });
    }

    @NonNull
    public Observable<Response> successResponse() {
        return loginErrorObservable.compose(ResponseOrError.<Response>onlySuccess());
    }

    @NonNull
    public Observer<? super BothParams<String, String>> loginObserver() {
        return loginObserver;
    }

    private static class SaveTokenAction implements Action1<String> {
        private final TokenPreferences tokenPreferences;

        public SaveTokenAction(TokenPreferences tokenPreferences) {
            this.tokenPreferences = tokenPreferences;
        }

        @Override
        public void call(String s) {
            tokenPreferences.edit().setToken(s).commit();
        }
    }

    private static class MapParamsToToken implements Func1<BothParams<String, String>, String> {
        @Override
        public String call(BothParams<String, String> usernameWithPassword) {
            final String credentials = usernameWithPassword.param1() +
                    ":" + usernameWithPassword.param2();
            return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        }
    }

    private class ClearPrefsFunc implements Action1<Throwable> {
        @Override
        public void call(Throwable throwable) {
            tokenPreferences.edit().clear();
        }
    }
}
