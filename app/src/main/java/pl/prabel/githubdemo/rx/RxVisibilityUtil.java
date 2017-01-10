package pl.prabel.githubdemo.rx;

import android.view.View;

import javax.annotation.Nonnull;

import rx.functions.Action1;

public class RxVisibilityUtil {
    @Nonnull
    public static Action1<Object> showView(final View progressView) {
        return new Action1<Object>() {
            @Override
            public void call(Object o) {
                progressView.setVisibility(View.VISIBLE);
            }
        };
    }

    @Nonnull
    public static Action1<Object> hideView(final View progressView) {
        return new Action1<Object>() {
            @Override
            public void call(Object o) {
                progressView.setVisibility(View.GONE);
            }
        };
    }
}
