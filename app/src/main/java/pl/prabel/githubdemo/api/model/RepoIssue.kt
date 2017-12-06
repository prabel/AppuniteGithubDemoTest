package pl.prabel.githubdemo.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RepoIssue {

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null
}