package pl.prabel.githubdemo.presenter.main;

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
import butterknife.BindInt;
import butterknife.ButterKnife;
import pl.prabel.githubdemo.R;
import pl.prabel.githubdemo.api.model.RepoModel;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>
        implements Action1<ImmutableList<RepoModel>>, ChangesDetector.ChangesAdapter {

    public interface Listener {
        @Nonnull
        Observer<RepoModel> clickRepoAction();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Subscription subscription;
        @Bind(R.id.repo_description_text_view)
        TextView repoDescriptionTextView;
        @BindInt(R.id.repo_name_text_view)
        TextView repoNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final RepoModel item) {
            repoDescriptionTextView.setText(item.getFullName());
            repoNameTextView.setText(item.getDescription());
            subscription = RxView.clicks(itemView)
                    .map(new Func1<Void, RepoModel>() {
                        @Override
                        public RepoModel call(Void aVoid) {
                            return item;
                        }
                    })
                    .subscribe(listener.clickRepoAction());
        }

        public void recycle() {
            if (subscription != null) {
                subscription.unsubscribe();
                subscription = null;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.repo_item, parent, false));
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
    public void onViewRecycled(ViewHolder holder) {
        holder.recycle();
        super.onViewRecycled(holder);
    }

    @Nonnull
    private ImmutableList<RepoModel> items = ImmutableList.of();
    @Nonnull
    private final ChangesDetector<RepoModel, RepoModel> changesDetector;
    @Nonnull
    private Listener listener;
    @Nonnull
    private final LayoutInflater inflater;

    @Inject
    public RepositoriesAdapter(@Nonnull final LayoutInflater inflater,
                               @Nonnull final Listener listener) {
        this.inflater = inflater;
        this.listener = listener;
        this.changesDetector = new ChangesDetector<>(new SimpleDetector<RepoModel>());
    }

    @Override
    public void call(ImmutableList<RepoModel> repoModelImmutableList) {
        this.items = repoModelImmutableList;
        changesDetector.newData(this, items, false);
    }
}
