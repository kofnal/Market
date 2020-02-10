package app.fil.market;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.fil.market.Model.ILoadMore;
import app.fil.market.ceni_i_skidki.Ceni;

class LoadingViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView name, number;

    public ItemViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.tvRowTowarNaimenovanie);
        number = (TextView) itemView.findViewById(R.id.tvCenaSoSkidkoy);
    }
}

public class TovariSpisokAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<Ceni> items;
    int visibleThreshold = 9;
    int lastVisibleItem;
    int totalItemCount;

    public TovariSpisokAdapter(RecyclerView recyclerView, Activity activity, List<Ceni> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager
                = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
System.out.println("Scrol listener last=="+Integer.toString(lastVisibleItem));
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    if (loadMore != null) {
                        loadMore.onLoadMore();

                        System.out.println("Scrol listener last==loadMore.onLoadMore();");
                    }
                    isLoading = true;

                    System.out.println("Scrol listener last== isLoading = true");
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.row_tovar, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Ceni item = items.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.name.setText(item.getNaimenovanie());
            viewHolder.number.setText(item.getId_sql_tovara_v_baze()+" / "+position);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingHolder = (LoadingViewHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int layoutPosition = holder.getLayoutPosition();
//        Log.d(TAG, "onViewAttachedToWindow: getayoutPosition = " + layoutPosition);

        layoutPosition = holder.getAdapterPosition();
//        Log.d(TAG, "onViewAttachedToWindow: getAdapterPosition = " + layoutPosition);
    }
}



