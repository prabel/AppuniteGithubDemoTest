package pl.prabel.githubdemo.api;

import com.google.common.collect.ImmutableList;

import pl.prabel.githubdemo.api.model.RepoModel;
import retrofit.client.Response;
import retrofit.http.GET;
import rx.Observable;

public interface ApiService {

    @GET("/authorizations")
    Observable<Response> authorizations();

    @GET("/user/repos")
    Observable<ImmutableList<RepoModel>> getRepositories();
}