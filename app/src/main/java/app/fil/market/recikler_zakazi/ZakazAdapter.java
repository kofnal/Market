package app.fil.market.recikler_zakazi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.fil.market.Model.ILoadMore;
import app.fil.market.R;


public class ZakazAdapter extends RecyclerView.Adapter<ZakazAdapter.ViewHolder> {


    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_URL = "url";






    // we define a list from the zakazList java class

    private List<ZakazList> zakazLists;
    private Context context;
    TextView tvKorzinaCount1;
    int korzinaCount1;
    String userId1;
    ILoadMore loadMore;
    boolean isLoading;
    int lastVisibleItem;
    int totalItemCount;
    int visibleThreshold = 5;


    public ZakazAdapter(RecyclerView recyclerView, List<ZakazList> zakazLists, Context context,
                        TextView tvKorzinaCount, int korzinaCount,
                        String userId) {

        // generate constructors to initialise the List and Context objects

        this.zakazLists = zakazLists;
        this.context = context;
        tvKorzinaCount1=tvKorzinaCount;
        korzinaCount1=korzinaCount;
        if(korzinaCount1>0){
            tvKorzinaCount1.setVisibility(View.VISIBLE);

            tvKorzinaCount.setText(String.valueOf(korzinaCount1));
        }
        userId1=userId;

        final LinearLayoutManager linearLayoutManager
                = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem +
                        visibleThreshold)) {

                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }


            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method will be called whenever our ViewHolder is created

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_zakazi, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // this method will bind the data to the ViewHolder from whence it'll be shown to other Views

        final ZakazList zakazList = zakazLists.get(position);
        holder.tvNoZakaza.setText(zakazList.getId());
//        Picasso.get()
//                .load(zakazList.getType())
//                .into(holder.imageView3);

    }

    @Override

    //return the size of the listItems (developersList)

    public int getItemCount() {
        return zakazLists.size();
    }


    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }
    public void
    setLoaded() {
        isLoading = false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        // define the View objects

        public TextView tvNoZakaza;
        public ImageView imageView3;

        public ViewHolder(View itemView) {
            super(itemView);

            // initialize the View objects

            tvNoZakaza = (TextView) itemView.findViewById(R.id.tvNoZakaza);
//            imageView3 = (ImageView) itemView.findViewById(R.id.imageView3);
        }

    }
}
