package app.fil.market.ui.podkategorii;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.fil.market.MainActivity;
import app.fil.market.Model.Utils;
import app.fil.market.R;
import app.fil.market.tovari.TovariActivity;

public class PodkategoriiViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    String korzCount;

    public LiveData<String> getText() {
        return mText;
    }
    void showSQL (final TextView tvKorzinaCount, final Activity activity, final LinearLayout linLayout){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_PODKATEGORIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            System.out.println("KotegoriiPodActivity jsonObj from SQL Server -"+jsonObject);
                            JSONArray jsonArray = jsonObject.getJSONArray("serv");
                            JSONArray jsonArrayKorzina = jsonObject.getJSONArray("korzina");
                            JSONObject jsKorz = jsonArrayKorzina.getJSONObject(0);
                            korzCount=jsKorz.getString("SUM(korzina.kolihestvo)");
                            System.out.println(jsonArrayKorzina);
                            System.out.println(korzCount);
                            tvKorzinaCount.setText(korzCount);
                            MainActivity.userStatic.setKorzinaCountStr(korzCount, tvKorzinaCount);
                            if(korzCount.equals("null")|korzCount.equals("0")){
                                tvKorzinaCount.setVisibility(View.INVISIBLE);
                            }
                            else {
                                tvKorzinaCount.setVisibility(View.VISIBLE);
                            }

                            LayoutInflater layoutInflater = activity.getLayoutInflater();
                            linLayout.removeAllViews();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonRow = jsonArray.getJSONObject(i);
                                String nazvanie     =    jsonRow.getString("nazvanie");
                                final String id     =    jsonRow.getString("id");
                                String foto = Utils.BASE_IP+jsonRow.getString("foto");
                                View item = layoutInflater.inflate(R.layout.row_kategoria, linLayout, false);
                                TextView nameServ = item.findViewById(R.id.tVKat);
                                ImageView imageView=item.findViewById(R.id.ivRowTovarFoto);
                                Picasso.get().load(foto).into(imageView);
                                nameServ.setText(nazvanie);
                                item.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        System.out.println("id + "+id);
                                        createIntent(id, activity);
                                    }
                                });
                                System.out.println("Home Fragm addView "+i);
                                linLayout.addView(item);
                            }
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
                parameters.put("id_vetka", "1");
                parameters.put("pokupatel", MainActivity.userStatic.getSqlId());
                System.out.println("Send param from PodkatClass "+parameters);
                return parameters;
            }
        };
        MainActivity.requestQueue.add(stringRequest);
    }

    void createIntent(String id, Activity context){
        Bundle bundle= new Bundle();
        bundle.putString("vetka",id);
        bundle.putString(Utils.bndlStaticCountTovaraV_Korzine, korzCount);

        Navigation.findNavController(context, R.id.nav_host_fragment).navigate(R.id.nav_tovari, bundle);
    }
}