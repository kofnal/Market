package app.fil.market.ui.podkategorii;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.fil.market.BokovoeMenu;
import app.fil.market.MainActivity;
import app.fil.market.Model.Utils;
import app.fil.market.R;
import app.fil.market.ui.tovari.TovarFromSQL;

public class PodkategoriiViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    String korzCount;

    public LiveData<String> getText() {
        return mText;
    }
    void showSQLPodkat (final RecyclerView recyclerView){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_PODKATEGORIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("KotegoriiPodActivity jsonObj from SQL Server -"+response);
                            JSONObject jsonObject= new JSONObject(response);
//                            System.out.println("KotegoriiPodActivity jsonObj from SQL Server -"+jsonObject);
                            JSONArray jsonArray = jsonObject.getJSONArray("serv");
                            JSONArray jsonArrayKorzina = jsonObject.getJSONArray("korzina");
                            JSONObject jsKorz = jsonArrayKorzina.getJSONObject(0);
                            korzCount=jsKorz.getString("SUM(korzina.kolihestvo)");
//                            System.out.println(jsonArrayKorzina);
//                            System.out.println(korzCount);
                            BokovoeMenu.tvKorzinaCount.setText(korzCount);
                            MainActivity.pokupatelStatic.setKorzinaCountStr(korzCount);
                            if(korzCount.equals("null")|korzCount.equals("0")){
                                BokovoeMenu.tvKorzinaCount.setVisibility(View.INVISIBLE);
                            }
                            else {
                                BokovoeMenu.tvKorzinaCount.setVisibility(View.VISIBLE);
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
//                            for (int i = 0; i < 1; i++) {
                                JSONObject jsonRow = jsonArray.getJSONObject(i);
                                String nazvanie     =    jsonRow.getString("nazvanie");
                                final String id     =    jsonRow.getString("id");
                                String foto = Utils.BASE_IP+jsonRow.getString("foto");
                               PodkategoriaObjectSQL podkategoriaObjectSQL= new PodkategoriaObjectSQL(id, nazvanie, foto);
                               PodkategoriiFragment.podkategoriaObjectSQLSList.add(podkategoriaObjectSQL);
                               PodkategoriiFragment.listTovarovSQLfromAdapterRV.add(new ArrayList<TovarFromSQL>());
                               PodkategoriiFragment.firstStartListBool.add(true);
                            }
                            recyclerView.getAdapter().notifyDataSetChanged();
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
                parameters.put("pokupatel", MainActivity.pokupatelStatic.getSqlId());
                System.out.println("Send param from PodkatClass "+parameters);
                return parameters;
            }
        };
        BokovoeMenu.requestQueue.add(stringRequest);
    }
}