package pl.prabel.githubdemo.presenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.trello.rxlifecycle.ActivityEvent;

import javax.annotation.Nonnull;

import pl.prabel.githubdemo.MainApplication;
import pl.prabel.githubdemo.R;
import pl.prabel.githubdemo.dagger.BaseActivityComponent;
import pl.prabel.githubdemo.dagger.BaseActivityComponentProvider;
import pl.prabel.githubdemo.rx.LifecycleMainObservable;
import rx.subjects.BehaviorSubject;

public abstract class BaseActivity extends AppCompatActivity implements BaseActivityComponentProvider {

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    private final LifecycleMainObservable lifecycleMainObservable = new LifecycleMainObservable(
            new LifecycleMainObservable.LifecycleProviderActivity(lifecycleSubject));

    private BaseActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityComponent = createActivityComponent(savedInstanceState, ((MainApplication) getApplication()).getAppComponent());
        super.onCreate(savedInstanceState);
        getWindow().setAllowEnterTransitionOverlap(true);

        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }

    public LifecycleMainObservable lifecycleMainObservable() {
        return lifecycleMainObservable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Nonnull
    public BaseActivityComponent getActivityComponent() {
        return activityComponent;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.anim_exit_out, R.anim.anim_exit_in);
//    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_exit_out, R.anim.anim_exit_in);
    }
}
