package pl.prabel.githubdemo.api;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import pl.prabel.githubdemo.api.throwable.NoConnectionException;
import pl.prabel.githubdemo.helper.Util;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.client.UrlConnectionClient;

public class RetroHttpClient extends UrlConnectionClient {

    private OkUrlFactory generateDefaultOkUrlFactory(OkHttpClient client) {
        return new OkUrlFactory(client);
    }

    private final OkUrlFactory factory;
    private Context mContext;

    @Inject
    public RetroHttpClient(Context context, OkHttpClient client) {
        factory = generateDefaultOkUrlFactory(client);
        mContext = context;
    }

    @Override
    protected HttpURLConnection openConnection(Request request) throws IOException {
        return factory.open(new URL(request.getUrl()));
    }

    @Override
    public Response execute(Request request) throws IOException {
        if (!Util.hasConnection(mContext)) {
            throw new NoConnectionException();
        }

        return super.execute(request);
    }
}
