package app.fil.market.recikler_tovari;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
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

import app.fil.market.MainActivity;
import app.fil.market.R;
import app.fil.market.Utils;


public class TovariAdapter extends RecyclerView.Adapter<TovariAdapter.ViewHolder> {


    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_URL = "url";






    // we define a list from the TovariList java class

    private List<TovariList> tovariLists;
    private Context context;
    TextView tvKorzinaCount1;
    int korzinaCount1;


    public TovariAdapter(List<TovariList> tovariLists, Context context, TextView tvKorzinaCount, int korzinaCount) {

        // generate constructors to initialise the List and Context objects

        this.tovariLists = tovariLists;
        this.context = context;
        tvKorzinaCount1=tvKorzinaCount;
        korzinaCount1=korzinaCount;
        if(korzinaCount1>0){
            tvKorzinaCount1.setVisibility(View.VISIBLE);

            tvKorzinaCount.setText(String.valueOf(korzinaCount1));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method will be called whenever our ViewHolder is created

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_tovar, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // this method will bind the data to the ViewHolder from whence it'll be shown to other Views

        final TovariList tovariList = tovariLists.get(position);
        holder.tvCenaBezSkidki.setPaintFlags(holder.tvCenaBezSkidki.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
              if((tovariList.getCenaSoSkidkoy()>0)){
                  String skidkaIzDvuxCen = "СКИДКА " + String.format("%, (.0f", 100-tovariList.getCenaSoSkidkoy()/
                          tovariList.getCenaBezSkidki()*100  ) + "%";
                  holder.tvSkidka.setText(skidkaIzDvuxCen);
                  holder.tvCenaBezSkidki.setText(tovariList.getCenaBezSkidki().toString());
                  holder.tvCenaSoSkidkoy.setText(tovariList.getCenaSoSkidkoy().toString());


        }else if (tovariList.getSkidka()>0){
                  holder.tvSkidka.setText("СКИДКА " + String.valueOf(tovariList.getSkidka())+ "%");
                  holder.tvCenaBezSkidki.setText(tovariList.getCenaBezSkidki().toString());
                  Double cenaSoSkidkoy=tovariList.getCenaBezSkidki()-tovariList.getCenaBezSkidki()*tovariList.getSkidka()/100;
                  holder.tvCenaSoSkidkoy.setText(cenaSoSkidkoy.toString());
        }else {
            holder.tvSkidka.setVisibility(View.GONE);
            holder.conlayCenaBezSkidki.setVisibility(View.GONE);
            holder.tvCenaSoSkidkoy.setText(tovariList.getCenaBezSkidki().toString());
        }
        holder.tvNaimenovanie.setText(tovariList.getId()+" "+tovariList.getNaimenovanie());
        Picasso.get()
                .load(tovariList.getFoto_url())
                .into(holder.foto_url);
        holder.frameLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TovariList tovariList1 = tovariLists.get(position);
                Intent skipIntent = new Intent(v.getContext(), TovaraActivity.class);
                skipIntent.putExtra(KEY_NAME, tovariList1.getId());
                skipIntent.putExtra(KEY_URL, tovariList1.getFoto_url());
                skipIntent.putExtra(KEY_IMAGE, tovariList1.getFoto_url());
                v.getContext().startActivity(skipIntent);
            }
        });
        if(tovariList.isSelected()) {
            holder.tvKKorzine.setVisibility(View.VISIBLE);
            holder.ibKupit.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
        }

        holder.tvKKorzine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent korzinaIntent = new Intent(v.getContext(), MainActivity.class);
//                Intent korzinaIntent = new Intent(v.getContext(), Korzina.class);
                v.getContext().startActivity(korzinaIntent);

            }
        });
        holder.ibKupit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tovariList.isSelected()) {
                    holder.tvKKorzine.setVisibility(View.VISIBLE);
                }
                if(korzinaCount1==0){
                    tvKorzinaCount1.setVisibility(View.VISIBLE);
                }
                holder.ibKupit.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);

                korzinaCount1++;
                tvKorzinaCount1.setText(String.valueOf(korzinaCount1));
                buySQL(tovariList.getId(), MainActivity.userStatic.getSqlId() );

            }
        });
    }

    @Override

    //return the size of the listItems (developersList)

    public int getItemCount() {
        return tovariLists.size();
    }

    void buySQL (final String tovar, final String pokupatel){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_KOLVO_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);

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
                parameters.put("pokupatel", pokupatel);
                return parameters;
            }

        }

                ;
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(stringRequest);

    }



    public class ViewHolder extends RecyclerView.ViewHolder  {

        // define the View objects

        public TextView tvSkidka;
        public TextView tvNaimenovanie;
        public TextView tvCenaBezSkidki;
        public TextView tvCenaSoSkidkoy;
        public TextView tvKKorzine;
        public ImageView foto_url;
        public ImageButton ibKupit;
        public FrameLayout frameLay;
        public ConstraintLayout conlayCenaBezSkidki;

        public ViewHolder(View itemView) {
            super(itemView);

            // initialize the View objects

            tvSkidka = itemView.findViewById(R.id.tvSkidkaTov);
            tvNaimenovanie = itemView.findViewById(R.id.tvRowTowarNaimenovanie);
            tvCenaBezSkidki = itemView.findViewById(R.id.tvRowTovarCena);
            tvCenaSoSkidkoy = itemView.findViewById(R.id.tvCenaSoSkidkoy);
            tvKKorzine = itemView.findViewById(R.id.tvKKorzine);
            foto_url = itemView.findViewById(R.id.ivTovar);
            ibKupit = itemView.findViewById(R.id.ibKupit);

            frameLay = itemView.findViewById(R.id.frameLay);
            conlayCenaBezSkidki = itemView.findViewById(R.id.conlayCenaBezSkidki);
        }

    }
}
