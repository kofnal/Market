package app.fil.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Map;

public class KategoriiPod extends AppCompatActivity {
    ScrollView scrollView;
    RequestQueue requestQueue;
    ImageButton ibKorzina;
    String korzCount="";
    TextView tvCountKorzina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovari);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        tvCountKorzina=findViewById(R.id.tvKorzinaCount);
        TovariActivity.setStartCountKorzina(tvCountKorzina);
        scrollView= findViewById(R.id.linerLayTovari);
        ibKorzina =findViewById(R.id.ibOpisanieKorzina);
        final Intent intentKorzinaActivity=new Intent(this,KorzinaActivity.class);
        ibKorzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentKorzinaActivity);
            }
        });
        showSQL();
    }
    void showSQL (){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_PODKATEGORIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            System.out.println("KotegoriiPodActivity jsonObj from SQL Server -"+jsonObject);
                            JSONArray jsonArray = jsonObject.getJSONArray("serv");
                            JSONArray jsonArrayKorzina = jsonObject.getJSONArray("korzina");
                            JSONObject jsKorz = jsonArrayKorzina.getJSONObject(0);
                            korzCount=jsKorz.getString("SUM(korzina.kolihestvo)");
                            System.out.println(jsonArrayKorzina);
                            System.out.println(korzCount);
                            tvCountKorzina.setText(korzCount);
                            MainActivity.userStatic.setKorzinaCountStr(korzCount, tvCountKorzina);
                            if(korzCount.equals("null")|korzCount.equals("0")){
                                tvCountKorzina.setVisibility(View.INVISIBLE);
                            }
                            else {
                                tvCountKorzina.setVisibility(View.VISIBLE);
                            }
                            LinearLayout linLayout = findViewById(R.id.linLayTovariVertical);
                            LayoutInflater layoutInflater = getLayoutInflater();
                            linLayout.removeAllViews();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonRow = jsonArray.getJSONObject(i);
                                String nazvanie     =    jsonRow.getString("nazvanie");
                                final String id     =    jsonRow.getString("id");
                                String foto = Utils.BASE_IP+jsonRow.getString("foto");
                                View item = layoutInflater.inflate(R.layout.row_kategoria, linLayout, false);
                                TextView nameServ = item.findViewById(R.id.tVKat);
                                ImageView imageView=item.findViewById(R.id.ivTovar);
                                Picasso.get().load(foto).into(imageView);
                                nameServ.setText(nazvanie);
                                item.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        System.out.println("id + "+id);
                                        createIntent(id);
                                    }
                                });
                                linLayout.addView(item);
                            }
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                   // scrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        } catch (JSONException e) {
                            System.out.println("\n ERR"+response.toString());
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
        requestQueue.add(stringRequest);
    }
    void createIntent(String id){
        Intent intent=new Intent(getApplicationContext(), TovariActivity.class);
            intent.putExtra("vetka", id);
            intent.putExtra(Utils.bndlStaticCountTovaraV_Korzine, korzCount);
            System.out.println("put extra v podkatActivity v create intent "+id);
            startActivityForResult(intent, Utils.intentRequestCODKategoriiPodToTovariActivityDlia_KolihestvaV_Korzine);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        tvCountKorzina.setText(MainActivity.userStatic.getKorzinaCountStr());
        System.out.println("onResult KategoriiPod in KategoriiActivity = "+MainActivity.userStatic.getKorzinaCountStr());
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        MainActivity.userStatic.setKorzinaCountStr(MainActivity.userStatic.getKorzinaCountStr(), tvCountKorzina);
        super.onResume();
    }
}
