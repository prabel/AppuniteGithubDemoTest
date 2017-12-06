package pl.prabel.githubdemo.presenter.issues

import pl.prabel.githubdemo.api.model.RepoIssue

interface IssuesView {

    fun updateRecycler(issues: List<RepoIssue>)
}