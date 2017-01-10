package pl.prabel.githubdemo.rx;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;


import javax.annotation.Nonnull;

import pl.prabel.githubdemo.R;
import rx.functions.Action1;

public class CustomViewAction {

    public static class SnackBarMessageAction implements Action1<String> {

        @Nonnull
        private View loginContainer;

        public SnackBarMessageAction(@Nonnull View loginContainer) {
            this.loginContainer = loginContainer;
        }

        @Override
        public void call(String message) {
            Snackbar.make(loginContainer, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static class SnackBarErrorMessageAction implements Action1<String> {

        @Nonnull
        private View containerView;

        public SnackBarErrorMessageAction(@Nonnull View view) {
            this.containerView = view;
        }

        @Override
        public void call(String message) {
            showErrorSnackbar(message, containerView);
        }
    }

    public static void showErrorSnackbar(final @Nonnull String message,
                                         final @Nonnull View containerView) {
        final Snackbar snackbar = Snackbar.make(containerView, message, Snackbar.LENGTH_SHORT);
        final View view = snackbar.getView();
        view.setBackgroundColor(view.getResources().getColor(R.color.error_snackbar));
        final TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(view.getResources().getColor(android.R.color.white));
        snackbar.show();
    }

    public static void showInfromationSnackbar(final @Nonnull String message,
                                         final @Nonnull View containerView) {
        final Snackbar snackbar = Snackbar.make(containerView, message, Snackbar.LENGTH_SHORT);
        final View view = snackbar.getView();
        view.setBackgroundColor(view.getResources().getColor(R.color.colorAccent));
        final TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(view.getResources().getColor(android.R.color.white));
        snackbar.show();
    }

}
