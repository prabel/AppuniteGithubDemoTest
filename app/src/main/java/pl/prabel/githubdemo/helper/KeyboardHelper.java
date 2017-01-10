package pl.prabel.githubdemo.helper;

import android.app.Activity;
import android.content.Context;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import rx.functions.Action1;

public class KeyboardHelper {

	public static class HideKeyboardAction implements Action1<Object> {
		private final Activity activity;

		public HideKeyboardAction(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void call(Object object) {
			KeyboardHelper.hideKeyboard(activity);
		}
	}

	public static void showKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	public static void showKeyboard(Fragment fragment) {
		Activity activity = fragment.getActivity();
		if (activity == null) {
			return;
		}
		showKeyboard(activity);
	}

    public static void hideKeyboard(Activity activity, View view, ResultReceiver resultReceiver) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0, resultReceiver);
    }

    public static void hideKeyboard(Activity activity, ResultReceiver resultReceiver) {
        View view = activity.getCurrentFocus();
        if(view == null){
            return;
        }
        hideKeyboard(activity, view, resultReceiver);
    }


    public static void hideKeyboard(Activity activity, View view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if(view == null){
            return;
        }
		hideKeyboard(activity, view);
	}

    public static void hideKeyboard(Fragment fragment, View view, ResultReceiver resultReceiver) {
        Activity activity = fragment.getActivity();
        if(activity == null){
            return;
        }
        hideKeyboard(activity, view, resultReceiver);
    }

    public static void hideKeyboard(Fragment fragment, ResultReceiver resultReceiver){
        Activity activity = fragment.getActivity();
        if(activity == null){
            return;
        }
        hideKeyboard(activity, resultReceiver);
    }

	public static void hideKeyboard(Fragment fragment, View view) {
		Activity activity = fragment.getActivity();
		if (activity == null) {
			return;
		}

		hideKeyboard(activity, view);
	}

	public static void hideKeyboard(Fragment fragment) {
		Activity activity = fragment.getActivity();
		if (activity == null) {
			return;
		}
		hideKeyboard(activity);
	}
}
