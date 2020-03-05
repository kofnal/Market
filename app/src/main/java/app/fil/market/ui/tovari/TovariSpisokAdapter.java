package app.fil.market.ui.tovari;


import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.fil.market.MainActivity;
import app.fil.market.Model.ILoadMore;
import app.fil.market.R;
import app.fil.market.ui.podkategorii.PodkategoriiFragment;

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
//    ImageButton ibOpisanieKorzina;
//    TextView tvCountKorzinaObj;
    int visibleThreshold = 9;
    int lastVisibleItem;
    int totalItemCount;
    public static ArrayList<String> deletedItemsSQLId = new ArrayList<>();
    public static int indexTovaraDliaOpisanieFragment =0;


    public TovariSpisokAdapter(RecyclerView recyclerView, Activity activity
            //, ImageButton ibOpisanieKorzina
    ) {
        this.activity = activity;
//        this.ibOpisanieKorzina = ibOpisanieKorzina;
//        this.tvCountKorzinaObj = tvCountKorzina;
        System.out.println("PoiskAdapter Konstruktor");


        final LinearLayoutManager linearLayoutManager
                = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                System.out.println("Scrol listener last==" + Integer.toString(lastVisibleItem));
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
//        System.out.println("TovariAdapter constructor Array size=" + TovariFragment.listTovarovSQLfromAdapterRV.size());
    }

    @Override
    public int getItemViewType(int position) {
        return PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder");
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.row_tovar, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        System.out.println("Tovari Adapter onBindViewHolder =" + position);
        if (holder instanceof ItemViewHolder) {
            final TovarFromSQL item = PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(position);
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;

            viewHolder.tvRowTowarNaimenovanie.setText(
//                    Integer.toString(position+1)+" "+
                    item.getNaimenovanie());
            if (item.getSkidka() > 0) {
                viewHolder.tvRowTovarCenaBezSkidki.setText(item.getCenaStr());
                viewHolder.tvRowTovarCenaBezSkidki.setPaintFlags(viewHolder.tvRowTovarCenaBezSkidki.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.tvRowTovarSkidka.setText("СКИДКА " + item.getSkidkaStr() + " %");
                viewHolder.tvRowTovarSkidka.setVisibility(View.VISIBLE);
                viewHolder.conlayCenaBezSkidki.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvRowTovarSkidka.setVisibility(View.GONE);
                viewHolder.conlayCenaBezSkidki.setVisibility(View.GONE);
            }
            viewHolder.tvRowTovarCenaSoSkidkoy.setText(item.getCenaFinalSoSkidkoyZaUpakStr());
            Picasso.get().load(item.getFoto()).into(viewHolder.ivRowTovarFoto);

//            final Intent intentTovarOpisanie = new Intent(viewHolder.conLayRowTovar.getContext(), TovarOpisanieActivity.class);
            viewHolder.conLayRowTovar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    indexTovaraDliaOpisanieFragment = position;
//                    viewHolder.conLayRowTovar.getContext().startActivity(intentTovarOpisanie);







//                    Bundle bundle= new Bundle();
//                    bundle.putString("ceniObj",Integer.toString(position));


                    Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.nav_opisanie_tovara);
                    System.out.println("navigate(R.id.nav_opisanie_tovara,");
                }
            });
            viewHolder.conlayIbKupit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setKolihestvoAndSendToSQL(item.getKolihestvo() + 1);
                    item.setVibranLiAndSendSQL(true);
                    viewHolder.ibRowTovarKupitObj.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
                    viewHolder.tvRowTovarKKorzine.setVisibility(View.VISIBLE);
//                    MainActivity.pokupatelStatic.setkorzina_count_INT(MainActivity.pokupatelStatic.getKorzina_count_INT()+1);
                    MainActivity.pokupatelStatic.setKorzinaCountStr(Integer.toString(MainActivity.pokupatelStatic.getKorzina_kountInt() + 1));
                }
            });

            if (item.estLi_V_KorzineObj) {
                System.out.println("IF   setBackgroundResource id=" + item.getId_sql_tovara_v_baze() + ", sel=" + item.getIsSelected());
                viewHolder.ibRowTovarKupitObj.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
                viewHolder.tvRowTovarKKorzine.setVisibility(View.VISIBLE);
            } else {
                System.out.println("ELSE setBackgroundResource id=" + item.getId_sql_tovara_v_baze() + ", sel=" + item.getIsSelected());
                viewHolder.ibRowTovarKupitObj.setBackgroundResource(R.drawable.backgrbelizakruglsiniy);
                viewHolder.tvRowTovarKKorzine.setVisibility(View.GONE);
            }
//            viewHolder.tvRowTovarKKorzine.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    ibOpisanieKorzina.performClick();
//                }
//            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingHolder = (LoadingViewHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        }
        System.out.println("TovariAdapter onBindViewHolder Array size=" + PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size());
    }


    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size();
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



