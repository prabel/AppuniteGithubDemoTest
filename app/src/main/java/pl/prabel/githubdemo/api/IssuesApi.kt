package pl.prabel.githubdemo.api

import pl.prabel.githubdemo.api.model.RepoIssue
import retrofit.http.GET
import retrofit.http.Path
import rx.Observable

interface IssuesApi {

    @GET("/repos/{owner}/{repo}/issues")
    fun getIssues(
            @Path("owner") owner: Int,
            @Path("repo") repo: Int
    ): Observable<RepoIssue>

}