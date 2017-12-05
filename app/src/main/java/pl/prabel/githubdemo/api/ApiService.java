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

    @GET("/repos/sweeky/{repo_name}/issues")
    Observable<ImmutableList<IssueModel>> getIssues(@Path("repo_name") String repo_name);
}