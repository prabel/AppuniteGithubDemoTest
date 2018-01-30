package pl.prabel.githubdemo.presenter.main;

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
import pl.prabel.githubdemo.api.model.RepoModel;
import pl.prabel.githubdemo.api.throwable.NoConnectionException;
import pl.prabel.githubdemo.content.TokenPreferences;
import pl.prabel.githubdemo.dagger.ActivityModule;
import pl.prabel.githubdemo.dagger.ActivityScope;
import pl.prabel.githubdemo.dagger.ApplicationComponent;
import pl.prabel.githubdemo.dagger.BaseActivityComponent;
import pl.prabel.githubdemo.presenter.BaseActivity;
import pl.prabel.githubdemo.presenter.login.LoginActivity;
import pl.prabel.githubdemo.presenter.repo.RepositoryActivity;
import pl.prabel.githubdemo.rx.CustomViewAction;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public class MainActivity extends BaseActivity implements RepositoriesAdapter.Listener {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.progress_view)
    View progressView;
    @Bind(R.id.container)
    View container;

    @Inject
    RepositoriesAdapter adapter;
    @Inject
    RepositoriesPresenter presenter;
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
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);


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
                presenter.getRepositoriesObservable()
                        .subscribe(adapter),
                presenter.repoClickObservable()
                        .subscribe(new Action1<RepoModel>() {
                            @Override
                            public void call(RepoModel repoModel) {
                                if (repoModel.getOpenIssues() == 0) {
                                    CustomViewAction.showInfromationSnackbar(getString(R.string.empty_issues), container);
                                    return;
                                }else if(repoModel.getOwner() != null){
                                    String owner = repoModel.getOwner().getLogin();
                                    Intent intent = new Intent(MainActivity.this, RepositoryActivity.class);
                                    intent.putExtra(RepositoryActivity.OWNER_KEY, owner);
                                    intent.putExtra(RepositoryActivity.REPO_KEY, repoModel.getName());
                                    startActivity(intent);
                                }

                            }
                        }));
    }


    @Nonnull
    @Override
    public Observer<RepoModel> clickRepoAction() {
        return presenter.repoClickObserver();
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
        final Component component = DaggerMainActivity_Component.builder().applicationComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .module(new Module(this))
                .build();

        component.inject(this);
        return component;
    }

    @dagger.Module
    public class Module {
        private MainActivity mainActivity;

        public Module(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Provides
        RepositoriesAdapter.Listener provideListener() {
            return mainActivity;
        }
    }

    @ActivityScope
    @dagger.Component(
            dependencies = ApplicationComponent.class,
            modules = {
                    ActivityModule.class,
                    Module.class
            }
    )
    interface Component extends BaseActivityComponent {
        void inject(MainActivity mainActivity);

        RepositoriesAdapter.Listener listener();
    }
}
