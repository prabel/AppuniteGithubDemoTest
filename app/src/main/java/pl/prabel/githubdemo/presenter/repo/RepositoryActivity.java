package pl.prabel.githubdemo.presenter.repo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.common.base.Strings;
import com.jakewharton.rxbinding.view.RxView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Provides;
import pl.prabel.githubdemo.R;
import pl.prabel.githubdemo.api.model.IssueModel;
import pl.prabel.githubdemo.api.throwable.NoConnectionException;
import pl.prabel.githubdemo.content.TokenPreferences;
import pl.prabel.githubdemo.dagger.ActivityModule;
import pl.prabel.githubdemo.dagger.ActivityScope;
import pl.prabel.githubdemo.dagger.ApplicationComponent;
import pl.prabel.githubdemo.dagger.BaseActivityComponent;
import pl.prabel.githubdemo.presenter.BaseActivity;
import pl.prabel.githubdemo.presenter.login.LoginActivity;
import pl.prabel.githubdemo.presenter.main.DaggerMainActivity_Component;
import pl.prabel.githubdemo.presenter.main.RepositoriesAdapter;
import pl.prabel.githubdemo.rx.CustomViewAction;
import rx.Observer;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by testday on 30/01/2018.
 */

public class RepositoryActivity extends BaseActivity{

    public static final String OWNER_KEY = "owner";
    public static final String REPO_KEY = "repo";

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.progress_view)
    View progressView;
    @Bind(R.id.container)
    View container;

    @Inject
    IssuesAdapter adapter;
    @Inject
    IssuesPresenter presenter;
    @Inject
    TokenPreferences tokenPreferences;

    private CompositeSubscription compositeSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (Strings.isNullOrEmpty(tokenPreferences.getToken())) {
            finish();
            startActivity(new Intent(RepositoryActivity.this, LoginActivity.class));
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        if(getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String owner = bundle.getString(OWNER_KEY);
            String repo = bundle.getString(REPO_KEY);
            presenter.fetchIssues(owner, repo);


            compositeSubscription = Subscriptions.from(
                    presenter.getProgressObservable()
                            .subscribe(RxView.visibility(progressView)),
                    presenter.getErrorObservable()
                            .map(new Func1<Throwable, String>() {
                                @Override
                                public String call(Throwable throwable) {
                                    if (throwable != null && throwable.getCause() instanceof NoConnectionException) {
                                        return getString(R.string.no_connection_error);
                                    }
                                    return getString(R.string.unknown_error);
                                }
                            })
                            .subscribe(new CustomViewAction.SnackBarErrorMessageAction(container)),
                    presenter.getIssuesObservable()
                            .subscribe(adapter));
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
        final RepositoryActivity.RepositoryComponent component = DaggerRepositoryActivity_RepositoryComponent.builder().applicationComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .build();

        component.inject(this);
        return component;
    }


    @dagger.Module
    public class Module {
        private RepositoryActivity repositoryActivity;

        public Module(RepositoryActivity repositoryActivity) {
            this.repositoryActivity = repositoryActivity;
        }

    }

    @ActivityScope
    @dagger.Component(
            dependencies = ApplicationComponent.class,
            modules = {
                    ActivityModule.class,
                    RepositoryActivity.Module.class
            }
    )
    interface RepositoryComponent extends BaseActivityComponent {
        void inject(RepositoryActivity repositoryActivity);
    }
}
