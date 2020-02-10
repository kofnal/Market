package app.fil.market;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.fil.market.recikler_tovari.TovariAdapter;
import app.fil.market.recikler_tovari.TovariList;

public class SpisokTovarov extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<TovariList> tovariLists;
    TextView tvKorzinaCount;
    TextView tvKKorzine;
    int korzinaCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovari_recikler);

        tvKorzinaCount=findViewById(R.id.tvKorzinaCount);
        tvKKorzine=findViewById(R.id.tvKKorzine);

        recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tovariLists = new ArrayList<>();
        loadUrlData();
    }

    private void loadUrlData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        System.out.println("+++");

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Utils.SHOW_TOVAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("tovar");
                    System.out.println("new JO "+ response);
                    for (int i = 0; i < array.length(); i++){

                        JSONObject jo = array.getJSONObject(i);
                        System.out.println("new JO Row"+ jo);
                        Double  cenaSkidka;
                        if(!jo.getString("cenaskidka").equals("null")){
                            cenaSkidka=jo.getDouble("cenaskidka");
                        }else{
                            cenaSkidka=0.0;
                        }
                        boolean selected=false;
                        if(!jo.getString("selected").equals("null")){
                            selected=jo.getBoolean("selected");
                        }
                        int kolihestvo=0;
                        if(!jo.getString("kolihestvo").equals("null")){
                            kolihestvo=jo.getInt("kolihestvo");
                        }
                        TovariList tovars = new TovariList(jo.getString("id"),
                                jo.getString("naimenovanie"),
                                jo.getInt("skidka"),
                                jo.getString("foto"),
                                jo.getDouble("cena"),
                                cenaSkidka,
                                kolihestvo,
                                selected
                                );
                        tovariLists.add(tovars);


                    }
                    if(!jsonObject.getJSONArray("korzina").getJSONObject(0).
                            getString("SUM(korzina.kolihestvo)").equals("null")) {
                        korzinaCount = jsonObject.getJSONArray("korzina").getJSONObject(0).
                                getInt("SUM(korzina.kolihestvo)");
                    }else {
                        korzinaCount=0;
                    }
                    adapter = new TovariAdapter(tovariLists, getApplicationContext(), tvKorzinaCount, korzinaCount);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {

                    System.out.println("\n ERR SpisokTovarov LoadUrl "+response.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(SpisokTovarov.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String,String>();

                parameters.put("vetka", "1");
                parameters.put("pokupatel", MainActivity.userStatic.getSqlId());
                parameters.put("indexstart", "0");
                parameters.put("count", "10");

                System.out.println("Otpravka na server iz tovar id "+MainActivity.userStatic.getSqlId());
                return parameters;
            }

        }

                ;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
