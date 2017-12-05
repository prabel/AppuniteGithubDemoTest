package pl.prabel.githubdemo.presenter.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appunite.rx.functions.BothParams;
import com.google.common.base.Strings;
import com.jakewharton.rxbinding.view.RxView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.prabel.githubdemo.R;
import pl.prabel.githubdemo.api.throwable.NoConnectionException;
import pl.prabel.githubdemo.dagger.ActivityModule;
import pl.prabel.githubdemo.dagger.ActivityScope;
import pl.prabel.githubdemo.dagger.ApplicationComponent;
import pl.prabel.githubdemo.dagger.BaseActivityComponent;
import pl.prabel.githubdemo.helper.KeyboardHelper;
import pl.prabel.githubdemo.presenter.BaseActivity;
import pl.prabel.githubdemo.presenter.main.MainActivity;
import pl.prabel.githubdemo.rx.CustomViewAction;
import pl.prabel.githubdemo.rx.RxVisibilityUtil;
import retrofit.client.Response;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public class    LoginActivity extends BaseActivity {
    @Bind(R.id.password_edit_text)
    EditText passwordEditText;
    @Bind(R.id.username_edit_text)
    EditText usernameEditText;
    @Bind(R.id.password_text_input)
    TextInputLayout passwordTextInput;
    @Bind(R.id.username_text_input)
    TextInputLayout usernameTextInput;
    @Bind(R.id.login_button)
    View loginButton;
    @Bind(R.id.progress_view)
    View progressView;
    @Bind(R.id.container)
    View container;

    @Inject
    LoginPresenter presenter;

    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        compositeSubscription = Subscriptions.from(
                RxView.clicks(loginButton)
                        .map(new MapToUsernameWithPasswordFunc())
                        .filter(new ValidateUserCredentialsFunc())
                        .doOnNext(RxVisibilityUtil.showView(progressView))
                        .doOnNext(new KeyboardHelper.HideKeyboardAction(this))
                        .subscribe(presenter.loginObserver()),
                presenter.errorObservable()
                        .map(new Func1<Throwable, String>() {
                            @Override
                            public String call(Throwable throwable) {
                                progressView.setVisibility(View.GONE);
                                if (throwable != null && throwable.getCause() instanceof NoConnectionException) {
                                    return getString(R.string.no_connection_error);
                                }
                                return getString(R.string.invalid_login_error);
                            }
                        })
                        .subscribe(new CustomViewAction.SnackBarErrorMessageAction(container)),
                presenter.successResponse()
                        .subscribe(new Action1<Response>() {
                            @Override
                            public void call(Response response) {
                                progressView.setVisibility(View.GONE);
                                finish();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        }));
    }

    private class ValidateUserCredentialsFunc implements Func1<BothParams<String, String>, Boolean> {
        @Override
        public Boolean call(BothParams<String, String> stringStringBothParams) {
            final String username = stringStringBothParams.param1();
            final String password = stringStringBothParams.param2();
            if (Strings.isNullOrEmpty(username)) {
                usernameTextInput.setError(getString(R.string.empty_username_error));
                return false;
            }
            if (Strings.isNullOrEmpty(password)) {
                passwordTextInput.setError(getString(R.string.empty_password_error));
                return false;
            }

            return true;
        }
    }

    private class MapToUsernameWithPasswordFunc implements Func1<Void, BothParams<String, String>> {

        @Override
        public BothParams<String, String> call(Void aVoid) {
            return new BothParams<>(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.clear();
        }
    }

    @Nonnull
    @Override
    public BaseActivityComponent createActivityComponent(@Nullable Bundle savedInstanceState, ApplicationComponent appComponent) {
        final LoginComponent loginComponent = DaggerLoginActivity_LoginComponent.builder()
                .applicationComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .build();
        loginComponent.inject(this);
        return loginComponent;
    }

    @ActivityScope
    @dagger.Component(
            dependencies = ApplicationComponent.class,
            modules = ActivityModule.class
    )
    interface LoginComponent extends BaseActivityComponent {
        void inject(LoginActivity loginActivity);
    }
}
