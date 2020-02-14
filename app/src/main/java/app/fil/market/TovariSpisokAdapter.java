package app.fil.market;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.fil.market.Model.ILoadMore;
import app.fil.market.ceni_i_skidki.Ceni;

class LoadingViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView tvRowTowarNaimenovanie, tvRowTovarCenaBezSkidki, tvRowTovarCenaSoSkidkoy, tvRowTovarSkidka, tvRowTovarKKorzine;
    ConstraintLayout conlayCenaBezSkidki, conLayRowTovar, conlayIbKupit;
    ImageView ivRowTovarFoto;
    ImageButton ibRowTovarKupitObj;


    public ItemViewHolder(View itemView) {
        super(itemView);
        tvRowTowarNaimenovanie = itemView.findViewById(R.id.tvRowTowarNaimenovanie);
        tvRowTovarCenaBezSkidki = itemView.findViewById(R.id.tvRowKorzinaCena);
        tvRowTovarCenaSoSkidkoy = itemView.findViewById(R.id.tvRowTovarCenaSoSkidkoy);
        tvRowTovarSkidka = itemView.findViewById(R.id.tvRowTovarSkidka);
        tvRowTovarKKorzine = itemView.findViewById(R.id.tvRowTovarKKorzine);
        conlayIbKupit = itemView.findViewById(R.id.conlayIbKupit);
        conlayCenaBezSkidki = itemView.findViewById(R.id.conlayCenaBezSkidki);
        conLayRowTovar = itemView.findViewById(R.id.conLayRowTovar);
        ivRowTovarFoto = itemView.findViewById(R.id.ivRowTovarFoto);
        ibRowTovarKupitObj = itemView.findViewById(R.id.ibRowTovarKupit);
    }
}

public class TovariSpisokAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadMore;
    boolean isLoading;
    Activity activity;
    ImageButton ibOpisanieKorzina;
    TextView tvCountKorzinaObj;
    ArrayList<Ceni> itemsObj;
    int visibleThreshold = 9;
    int lastVisibleItem;
    int totalItemCount;
    public static ArrayList<String> deletedItemsSQLId=new ArrayList<>();


    public TovariSpisokAdapter(RecyclerView recyclerView, Activity activity, ArrayList<Ceni> items, ImageButton ibOpisanieKorzina, TextView tvCountKorzina) {
        this.activity = activity;
        this.itemsObj = items;
        this.ibOpisanieKorzina = ibOpisanieKorzina;
        this.tvCountKorzinaObj = tvCountKorzina;


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
        return itemsObj.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
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
            final Ceni item = itemsObj.get(position);
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;

            viewHolder.tvRowTowarNaimenovanie.setText(item.getNaimenovanie());
            if(item.getSkidka()>0){
                viewHolder.tvRowTovarCenaBezSkidki.setText(item.getCenaStr());
                viewHolder.tvRowTovarCenaBezSkidki.setPaintFlags(viewHolder.tvRowTovarCenaBezSkidki.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.tvRowTovarSkidka.setText("СКИДКА "+item.getSkidkaStr()+" %");
                viewHolder.tvRowTovarSkidka.setVisibility(View.VISIBLE);
                viewHolder.conlayCenaBezSkidki.setVisibility(View.VISIBLE);
            } else{
                viewHolder.tvRowTovarSkidka.setVisibility(View.GONE);
                viewHolder.conlayCenaBezSkidki.setVisibility(View.GONE);
            }
            viewHolder.tvRowTovarCenaSoSkidkoy.setText(item.getCenaFinalSoSkidkoyStr());
            Picasso.get().load(item.getFoto()).into(viewHolder.ivRowTovarFoto);

            final Intent intentTovarOpisanie = new Intent(viewHolder.conLayRowTovar.getContext(), TovarOpisanie.class);
            viewHolder.conLayRowTovar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentTovarOpisanie.putExtra("ceniObj", item);
                    viewHolder.conLayRowTovar.getContext().startActivity(intentTovarOpisanie);
                }
            });
            viewHolder.conlayIbKupit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setKolihestvo(item.getKolihestvo()+1);
                    viewHolder.ibRowTovarKupitObj.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
                    viewHolder.tvRowTovarKKorzine.setVisibility(View.VISIBLE);
//                    MainActivity.userStatic.setkorzina_count_INT(MainActivity.userStatic.getKorzina_count_INT()+1);
MainActivity.userStatic.setKorzinaCountStr(Integer.toString(MainActivity.userStatic.getKorzina_kountInt()+1), tvCountKorzinaObj);
                }
            });

            if(item.getIsSelected()){
                System.out.println("IF   setBackgroundResource id="+item.getId_sql_tovara_v_baze()+", sel="+item.getIsSelected());
                viewHolder.ibRowTovarKupitObj.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
                viewHolder.tvRowTovarKKorzine.setVisibility(View.VISIBLE);
            } else{
                System.out.println("ELSE setBackgroundResource id="+item.getId_sql_tovara_v_baze()+", sel="+item.getIsSelected());
                viewHolder.ibRowTovarKupitObj.setBackgroundResource(R.drawable.backgrbelizakruglsiniy);
                viewHolder.tvRowTovarKKorzine.setVisibility(View.GONE);
            }
            viewHolder.tvRowTovarKKorzine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ibOpisanieKorzina.performClick();
                }
            });
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
        return itemsObj.size();
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



