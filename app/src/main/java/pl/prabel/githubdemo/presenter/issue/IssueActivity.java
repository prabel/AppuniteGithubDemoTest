package pl.prabel.githubdemo.presenter.issue;


import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.appunite.rx.dagger.NetworkScheduler;
import com.appunite.rx.dagger.UiScheduler;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;


import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Provides;
import pl.prabel.githubdemo.R;
import pl.prabel.githubdemo.api.ApiService;
import pl.prabel.githubdemo.api.model.IssueModel;
import pl.prabel.githubdemo.content.TokenPreferences;
import pl.prabel.githubdemo.dagger.ActivityModule;
import pl.prabel.githubdemo.dagger.ActivityScope;
import pl.prabel.githubdemo.dagger.ApplicationComponent;
import pl.prabel.githubdemo.dagger.BaseActivityComponent;
import pl.prabel.githubdemo.presenter.BaseActivity;
import rx.Scheduler;

import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public class IssueActivity extends BaseActivity {
    @Bind(R.id.container)
    View container;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    TokenPreferences tokenPreferences;
    @Inject
    IssuePresenter presenter;
    @Inject
    IssuesAdapter adapter;

    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        compositeSubscription = Subscriptions.from(presenter.getIssuesObservable()
                .compose(lifecycleMainObservable().<ImmutableList<IssueModel>>bindLifecycle())
                .subscribe(adapter));
    }

    @Nonnull
    @Override
    public BaseActivityComponent createActivityComponent(@Nullable Bundle savedInstanceState, ApplicationComponent appComponent) {
        final Component component = DaggerIssueActivity_Component.builder().applicationComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .module(new Module(this))
                .build();

        component.inject(this);
        return component;

    }

    @dagger.Module
    class Module {
        private IssueActivity issueActivity;

        public Module(IssueActivity issueActivity) {
            this.issueActivity = issueActivity;
        }

        @Provides
        IssuePresenter provideIssuePresenter(@Nonnull ApiService apiService,
                                             @Nonnull @NetworkScheduler Scheduler networkSchedule,
                                             @Nonnull @UiScheduler Scheduler uiScheduler
        ) {
            String repo_name = getIntent().getExtras().getString("repo_name");
            return new IssuePresenter(apiService, networkSchedule, uiScheduler, repo_name);
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
        void inject(IssueActivity issueActivity);

        void inject(IssuePresenter issuePresenter);

        ApiService apiService();

        @UiScheduler
        Scheduler uiScheduler();

        @NetworkScheduler
        Scheduler networkScheduler();


    }
}