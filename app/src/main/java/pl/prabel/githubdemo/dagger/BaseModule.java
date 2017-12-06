package pl.prabel.githubdemo.dagger;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.BuildConfig;
import android.util.Log;

import com.appunite.rx.android.MyAndroidSchedulers;
import com.appunite.rx.dagger.NetworkScheduler;
import com.appunite.rx.dagger.UiScheduler;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.prabel.githubdemo.api.ApiService;
import pl.prabel.githubdemo.api.RetroHttpClient;
import pl.prabel.githubdemo.content.AppConst;
import pl.prabel.githubdemo.content.TokenPreferences;
import pl.prabel.githubdemo.parser.ImmutableListDeserializer;
import pl.prabel.githubdemo.parser.ImmutableSetDeserializer;
import pl.prabel.githubdemo.presenter.issues.IssuesPresenter;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.schedulers.Schedulers;

@Module
public class BaseModule {
    private static final int CONNECT_TIMEOUT_MILLIS = 30 * 1000;
    private static final int READ_TIMEOUT_MILLIS = 45 * 1000;

    private static final String TAG = "BaseModule";

    @Singleton
    @Provides
    static OkHttpClient provideOkHttpClient(Cache cacheOrNull) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        if (cacheOrNull != null) {
            okHttpClient.setCache(cacheOrNull);
        }
        okHttpClient.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        return okHttpClient;
    }

    @Singleton
    @Provides
    @Named("picasso")
    static OkHttpClient providePicassoOkHttpClient(Cache cacheOrNull) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        if (cacheOrNull != null) {
            okHttpClient.setCache(cacheOrNull);
        }

        return okHttpClient;
    }

    @Singleton
    @Provides
    static Cache provideCacheOrNull(@ForApplication Context context) {
        try {
            File httpCacheDir = new File(context.getCacheDir(), "cache");
            long httpCacheSize = 150 * 1024 * 1024; // 150 MiB
            return new Cache(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i(TAG, "HTTP response cache installation failed:", e);
            return null;
        }
    }

    @Provides
    @Singleton
    static Picasso providePicasso(@ForApplication Context context, @Named("picasso") OkHttpClient okHttpClient) {
        final LruCache lruCache = new LruCache(context);
        return new Picasso.Builder(context)
                .indicatorsEnabled(BuildConfig.DEBUG)
                .memoryCache(lruCache)
                .loggingEnabled(BuildConfig.DEBUG)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.d("Picasso fail with uri ", uri.toString(), exception);
                    }
                })
                .downloader(new OkHttpDownloader(okHttpClient))
                .build();
    }

    @Singleton
    @Provides
    static Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer())
                .registerTypeAdapter(ImmutableSet.class, new ImmutableSetDeserializer())
                .create();
    }

    @Provides
    @Singleton
    static ApiService provideApiService(Gson gson, RetroHttpClient client, @Nonnull final RequestInterceptor requestInterceptor) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(client)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(requestInterceptor)
                .setEndpoint(AppConst.getHostAddress())
                .build();

        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        return restAdapter.create(ApiService.class);
    }


    @Singleton
    @Provides
    static RequestInterceptor provideRequestInterceptor(final TokenPreferences tokenPreferences) {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                final String token = tokenPreferences.getToken();
                if (!Strings.isNullOrEmpty(token)) {
                    request.addHeader("Authorization", token);
                }
                request.addHeader("Accept", "application/vnd.github.v3+json");
            }
        };
    }

    @Provides
    static Handler provideHandler() {
        return new Handler();
    }

    @Singleton
    @Provides
    static RetroHttpClient provideRetroHttpClient(@ForApplication Context context, OkHttpClient client) {
        return new RetroHttpClient(context, client);
    }

    @UiScheduler
    @Provides
    static rx.Scheduler provideUiScheduler() {
        return MyAndroidSchedulers.mainThread();
    }

    @NetworkScheduler
    @Provides
    static rx.Scheduler provideNetworkScheduler() {
        return Schedulers.io();
    }
}