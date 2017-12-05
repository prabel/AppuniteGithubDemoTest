package pl.prabel.githubdemo.presenter.issue;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appunite.detector.ChangesDetector;
import com.appunite.detector.SimpleDetector;
import com.google.common.collect.ImmutableList;



import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.prabel.githubdemo.R;
import pl.prabel.githubdemo.api.model.IssueModel;
import rx.functions.Action1;

/**
 * Created by damian on 05.12.17.
 */

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder>
        implements Action1<ImmutableList<IssueModel>>, ChangesDetector.ChangesAdapter {
    @Nonnull
    private final LayoutInflater inflater;
    @Nonnull
    private ImmutableList<IssueModel> items = ImmutableList.of();
    @Nonnull
    private final ChangesDetector<IssueModel, IssueModel> changesDetector;

    @Inject
    public IssuesAdapter(@Nonnull final LayoutInflater inflater) {
        this.inflater = inflater;
        this.changesDetector = new ChangesDetector<>(new SimpleDetector<IssueModel>());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IssuesAdapter.ViewHolder(inflater.inflate(R.layout.issue_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void call(ImmutableList<IssueModel> issueModels) {
        this.items = issueModels;
        changesDetector.newData(this, items, false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.issue_title)
        TextView issueTitleTextView;
        @Bind(R.id.issue_body)
        TextView issueBodyTextView;
        @Bind(R.id.issue_comments)
        TextView issueCommentstTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final IssueModel item) {
            issueTitleTextView.setText(item.getTitle());
            issueBodyTextView.setText(item.getBody());
            issueCommentstTextView.setText(String.valueOf(item.getComments()));
        }

    }
}
