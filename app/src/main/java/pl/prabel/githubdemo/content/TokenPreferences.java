package pl.prabel.githubdemo.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import pl.prabel.githubdemo.dagger.ForApplication;

@Singleton
public class TokenPreferences {
    private static final String PREFERENCES_NAME = "github_prefs";

    public static final String PREFS_TOKEN = "prefs_token";

    public static class TokenPreferencesEditor {

        private SharedPreferences.Editor mEditor;

        @SuppressLint("CommitPrefEdits")
        TokenPreferencesEditor(SharedPreferences preferences) {
            mEditor = preferences.edit();
        }

        public boolean commit() {
            return mEditor.commit();
        }

        public TokenPreferencesEditor clear() {
            mEditor.clear();
            return this;
        }

        public TokenPreferencesEditor setToken(@Nonnull final String token) {
            mEditor.putString(PREFS_TOKEN, token);
            return this;
        }
    }

    private SharedPreferences preferences;

    @Inject
    public TokenPreferences(@ForApplication Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
    }

    public TokenPreferencesEditor edit() {
        return new TokenPreferencesEditor(preferences);
    }

    @Nullable
    public String getToken() {
        return preferences.getString(PREFS_TOKEN, null);
    }
}

