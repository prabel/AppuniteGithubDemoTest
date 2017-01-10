package pl.prabel.githubdemo.rx;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import rx.Observable;

import static com.appunite.rx.internal.Preconditions.checkNotNull;

public class LifecycleMainObservable {

    @Nonnull
    private final LifecycleProvider lifecycleProvider;

    public interface LifecycleProvider {

        @Nonnull
        <T> Observable.Transformer<T, T> bindLifecycle();
    }

    public static class LifecycleProviderFragment implements LifecycleProvider {

        @Nonnull
        private final Observable<FragmentEvent> lifecycle;

        public LifecycleProviderFragment(final @Nonnull Observable<FragmentEvent> lifecycle) {
            this.lifecycle = checkNotNull(lifecycle);
        }


        @Nonnull
        @Override
        public <T> Observable.Transformer<T, T> bindLifecycle() {
            return RxLifecycle.bindFragment(lifecycle);
        }
    }

    public static class LifecycleProviderActivity implements LifecycleProvider {

        @Nonnull
        private final Observable<ActivityEvent> lifecycle;

        public LifecycleProviderActivity(final @Nonnull Observable<ActivityEvent> lifecycle) {
            this.lifecycle = checkNotNull(lifecycle);
        }

        @Nonnull
        @Override
        public <T> Observable.Transformer<T, T> bindLifecycle() {
            return RxLifecycle.bindActivity(lifecycle);
        }
    }

    @Inject
    public LifecycleMainObservable(@Nonnull LifecycleProvider lifecycleProvider) {
        this.lifecycleProvider = checkNotNull(lifecycleProvider);
    }

    @Nonnull
    public <T> Observable.Transformer<T, T> bindLifecycle() {
        return lifecycleProvider.bindLifecycle();
    }
}
