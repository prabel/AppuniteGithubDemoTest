package pl.prabel.githubdemo.presenter.issues

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.trello.rxlifecycle.ActivityEvent
import kotlinx.android.synthetic.main.activity_issues.*
import pl.prabel.githubdemo.MainApplication
import pl.prabel.githubdemo.R
import pl.prabel.githubdemo.api.model.RepoIssue
import pl.prabel.githubdemo.dagger.ActivityModule
import pl.prabel.githubdemo.dagger.ActivityScope
import pl.prabel.githubdemo.dagger.ApplicationComponent
import rx.subjects.BehaviorSubject
import javax.inject.Inject

class IssuesActivity : AppCompatActivity(), IssuesView {
    override fun updateRecycler(issues: List<RepoIssue>) {
        adapter.issues = issues
        adapter.notifyDataSetChanged()
    }


    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

//    override fun createActivityComponent(savedInstanceState: Bundle?, appComponent: ApplicationComponent): BaseActivityComponent {
//        val issuesComponent = DaggerLoginActivity_IssuesComponent.builder()
//                .applicationComponent(appComponent)
//                .activityModule(ActivityModule(this))
//                .build()
//        issuesComponent.inject(this)
//
//        return issuesComponent
//    }

    @Inject
    lateinit var presenter: IssuesPresenter

    val adapter = IssuesAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).appComponent.inject(this)

        presenter.bind(this, intent.getStringExtra("OWNER"), intent.getStringExtra("ID"))

        setContentView(R.layout.activity_main)
        lifecycleSubject.onNext(ActivityEvent.CREATE)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


    }

    override fun onDestroy() {
        presenter.unbind()
        super.onDestroy()
    }
}