package pl.prabel.githubdemo.presenter.issues

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.issue_item.view.*
import pl.prabel.githubdemo.R
import pl.prabel.githubdemo.api.model.RepoIssue

class IssuesAdapter(var issues: List<RepoIssue>) : RecyclerView.Adapter<IssuesAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
        = holder.bind(issues[position])


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.issue_item, parent, false))



    override fun getItemCount(): Int
            = issues.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: RepoIssue) {
                itemView.issueTitle.text = item.title
                itemView.issueBody.text = item.body
        }

    }


}