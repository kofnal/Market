package app.fil.market.ui.poisk.podkategorii;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.fil.market.R;

class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView tVKat;
    ImageView ivRowTovarFoto;
    ConstraintLayout conLayPodkatRow;



    public ItemViewHolder(View itemView) {
        super(itemView);
        tVKat = itemView.findViewById(R.id.tVKat);
        ivRowTovarFoto = itemView.findViewById(R.id.ivRowTovarFoto);
        conLayPodkatRow = itemView.findViewById(R.id.conLayPodkatRow);
    }
}

public class PoiskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;
//    ImageButton ibOpisanieKorzina;
//    TextView tvCountKorzinaObj;
    int visibleThreshold = 9;
    int lastVisibleItem;
    int totalItemCount;
    public static ArrayList<String> deletedItemsSQLId = new ArrayList<>();


    public PoiskAdapter(RecyclerView recyclerView, Activity activity
                        //, ImageButton ibOpisanieKorzina
    ) {
        this.activity = activity;
//        this.ibOpisanieKorzina = ibOpisanieKorzina;
//        this.tvCountKorzinaObj = tvCountKorzina;
        System.out.println("PoiskAdapter Konstruktor");



//        System.out.println("TovariAdapter constructor Array size=" + PoiskFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PoiskFragment.podkatVetkaId)).size());
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder");
            View view = LayoutInflater.from(activity).inflate(R.layout.row_kategoria, parent, false);
            return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        System.out.println("Tovari Adapter onBindViewHolder =" + position);

            final PoiskObjectSQL item = PoiskFragment.poiskObjectSQLSList.get(position);
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;


            viewHolder.tVKat.setText(item.getName());
            Picasso.get().load(item.getFoto()).into(viewHolder.ivRowTovarFoto);
        viewHolder.conLayPodkatRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PoiskFragment.podkatVetkaId=item.getId();
                Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.nav_tovari);
            }
        });


//        System.out.println("TovariAdapter onBindViewHolder Array size=" + PoiskFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PoiskFragment.podkatVetkaId)).size());
    }


    @Override
    public int getItemCount() {
        return PoiskFragment.poiskObjectSQLSList.size();
    }

}



