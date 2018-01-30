package pl.prabel.githubdemo.api;

import com.google.common.collect.ImmutableList;

import pl.prabel.githubdemo.api.model.IssueModel;
import pl.prabel.githubdemo.api.model.RepoModel;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface ApiService {

    @GET("/authorizations")
    Observable<Response> authorizations();

    @GET("/user/repos")
    Observable<ImmutableList<RepoModel>> getRepositories();

    @GET("/repos/{owner}/{repo}/issues")
    Observable<ImmutableList<IssueModel>> getRepoIssues(@Path("owner") String owner, @Path("repo") String repo );
}