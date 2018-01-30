package pl.prabel.githubdemo.presenter.repo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appunite.detector.ChangesDetector;
import com.appunite.detector.SimpleDetector;
import com.google.common.collect.ImmutableList;
import com.jakewharton.rxbinding.view.RxView;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.prabel.githubdemo.R;
import pl.prabel.githubdemo.api.model.IssueModel;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by testday on 30/01/2018.
 */

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder>
        implements Action1<ImmutableList<IssueModel>>, ChangesDetector.ChangesAdapter {

    public interface Listener {
        @Nonnull
        Observer<IssueModel> clickRepoAction();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Subscription subscription;
        @Bind(R.id.issue_name_text_view)
        TextView issueNameTextView;
        @Bind(R.id.issue_description_text_view)
        TextView issueBodyTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final IssueModel item) {
            String title = "";
            if(item.getNumber() > 0){
                title = title + "#" + item.getNumber() + " ";
            }
            title = title + item.getTitle();
            issueBodyTextView.setText(item.getBody());
            issueNameTextView.setText(title);
        }

    }

    @Override
    public IssuesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IssuesAdapter.ViewHolder(inflater.inflate(R.layout.issue_item, parent, false));
    }

    @Override
    public void onBindViewHolder(IssuesAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onViewRecycled(IssuesAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Nonnull
    private ImmutableList<IssueModel> items = ImmutableList.of();
    @Nonnull
    private final ChangesDetector<IssueModel, IssueModel> changesDetector;
    @Nonnull
    private final LayoutInflater inflater;

    @Inject
    public IssuesAdapter(@Nonnull final LayoutInflater inflater) {
        this.inflater = inflater;
        this.changesDetector = new ChangesDetector<>(new SimpleDetector<IssueModel>());
    }

    @Override
    public void call(ImmutableList<IssueModel> repoModelImmutableList) {
        this.items = repoModelImmutableList;
        changesDetector.newData(this, items, false);
    }
}
