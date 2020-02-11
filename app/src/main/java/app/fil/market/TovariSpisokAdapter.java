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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    TextView tvRowTowarNaimenovanie, tvRowTovarCenaBezSkidki, tvRowTovarCenaSoSkidkoy, tvRowTovarSkidka;
    ConstraintLayout conlayCenaBezSkidki, conLayRowTovar, conlayIbKupit;
    ImageView ivRowTovarFoto;


    public ItemViewHolder(View itemView) {
        super(itemView);
        tvRowTowarNaimenovanie = itemView.findViewById(R.id.tvRowTowarNaimenovanie);
        tvRowTovarCenaBezSkidki = itemView.findViewById(R.id.tvRowTovarCenaBezSkidki);
        tvRowTovarCenaSoSkidkoy = itemView.findViewById(R.id.tvRowTovarCenaSoSkidkoy);
        tvRowTovarSkidka = itemView.findViewById(R.id.tvRowTovarSkidka);
        conlayIbKupit = itemView.findViewById(R.id.conlayIbKupit);
        conlayCenaBezSkidki = itemView.findViewById(R.id.conlayCenaBezSkidki);
        conLayRowTovar = itemView.findViewById(R.id.conLayRowTovar);
        ivRowTovarFoto = itemView.findViewById(R.id.ivRowTovarFoto);
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
            final Ceni item = items.get(position);
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
                    buySQL(item.getId_sql_tovara_v_baze());
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingHolder = (LoadingViewHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        }
    }

    void buySQL (final String tovar){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_KOLVO_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            System.out.println("Tovari spisok adapter buy SQL = "+jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("serv");

                        } catch (JSONException e) {

                            System.out.println("\n ERR"+response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("tovar", tovar);
                parameters.put("kolvo", "1");
                parameters.put("pokupatel", MainActivity.userStatic.getSqlId());
                System.out.println("Tovari spisok adapter buy SQL parametrs = "+parameters);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        requestQueue.add(stringRequest);
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



