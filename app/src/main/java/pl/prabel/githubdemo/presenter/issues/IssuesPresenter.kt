package pl.prabel.githubdemo.presenter.issues

import android.util.Log
import com.appunite.rx.android.MyAndroidSchedulers
import pl.prabel.githubdemo.api.ApiService
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IssuesPresenter @Inject constructor(val api: ApiService) {

    var view: IssuesView? = null

    var subscription: CompositeSubscription? = null

    fun bind(view: IssuesView, owner: String, repo: String) {
        this.view = view
        getIssues(owner, repo)
    }

    fun getIssues(owner: String, repo: String) {
        api.getIssues(owner, repo)
                .subscribeOn(Schedulers.io())
                .observeOn(MyAndroidSchedulers.mainThread())
                .subscribe( {
                    if (!it.isEmpty()) {
                        view?.updateRecycler(it)
                    }
                }, {
                    Log.e("TAG", "getIssues error: ", it)
                }
                )
    }

    fun unbind() {
        view = null
        subscription?.clear()
    }
}