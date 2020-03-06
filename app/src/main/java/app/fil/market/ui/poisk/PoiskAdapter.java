package app.fil.market.ui.poisk;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import app.fil.market.R;

class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView tvRowPoisk;



    public ItemViewHolder(View itemView) {
        super(itemView);
        tvRowPoisk = itemView.findViewById(R.id.tvRowPoisk);
    }
}

public class PoiskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;


    public PoiskAdapter( Activity activity
    ) {
        this.activity = activity;
        System.out.println("PoiskAdapter Konstruktor");



//        System.out.println("TovariAdapter constructor Array size=" + PoiskFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PoiskFragment.podkatVetkaId)).size());
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder");
            View view = LayoutInflater.from(activity).inflate(R.layout.row_poisk, parent, false);
            return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            System.out.println("Poisk Adapter onBindViewHolder =" + position);

            final PoiskObjectSQL item = PoiskFragment.poiskObjectSQLSList.get(position);
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;


            viewHolder.tvRowPoisk.setText(item.getName());
            viewHolder.tvRowPoisk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.nav_tovari);
                    System.out.println("poisk click " + position);
                }
            });


//        System.out.println("TovariAdapter onBindViewHolder Array size=" + PoiskFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PoiskFragment.podkatVetkaId)).size());

    }


    @Override
    public int getItemCount() {
        return PoiskFragment.poiskObjectSQLSList.size();
    }

}



