package app.fil.market;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
import java.util.Map;

import app.fil.market.Model.ILoadMore;
import app.fil.market.ceni_i_skidki.Ceni;
import app.fil.market.korzina.KorzinaActivity;

public class TovariActivity extends AppCompatActivity {
    ArrayList<Ceni> ceniList = new ArrayList<>();
    TovariSpisokAdapter adapter;
    RequestQueue requestQueue;
    ImageButton ibKorzina;
    TextView tvCountKorzina;
    final int countLoadItems = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovari);
        ibKorzina = findViewById(R.id.ibOpisanieKorzina);
        tvCountKorzina = findViewById(R.id.tvKorzinaCount);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TovariSpisokAdapter(recyclerView, this, ceniList, ibKorzina, tvCountKorzina);
        recyclerView.setAdapter(adapter);
        MainActivity.userStatic.updateTextViewTotalKorzinaCount(tvCountKorzina);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        final Intent intentKorzina = new Intent(this, KorzinaActivity.class);

        ibKorzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentKorzina);
                finish();
            }
        });
        showSQL(0, countLoadItems);
        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                ceniList.add(null);
                adapter.notifyItemInserted(ceniList.size() - 1);
                ceniList.remove(ceniList.size() - 1);
                adapter.notifyItemRemoved(ceniList.size());
                int index = ceniList.size();
                int end = index + countLoadItems;
                showSQL(index, end);
            }
        });
    }

    void showSQL(final int indexStart, final int indexEnd) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println("TovariActivityActivity tovarRow showSQL() " + jsonObject);
                            JSONArray jsonArray = jsonObject.getJSONArray("tovar");
                            JSONArray jsonArrayKorzina = jsonObject.getJSONArray("korzina");
                            if (jsonArrayKorzina.length() > 0) {
                                JSONObject jsKorz = jsonArrayKorzina.getJSONObject(0);
                                final String[] korzCount = {jsKorz.getString("SUM(korzina.kolihestvo)")};
                                if (!korzCount[0].equals("null")) {
                                    MainActivity.userStatic.setKorzinaCountStr(Integer.parseInt(korzCount[0]), tvCountKorzina);
                                    tvCountKorzina.setVisibility(View.VISIBLE);
//                                    if (MainActivity.userStatic.getKorzinaCountStr() != null)
//                                        tvCountKorzina.setText(MainActivity.userStatic.getKorzinaCountStr());
                                } else {
                                    MainActivity.userStatic.setKorzinaCountStr(0, tvCountKorzina);
                                    tvCountKorzina.setVisibility(View.INVISIBLE);
                                }
                            }
                            int razmerZaprosa = indexEnd - indexStart;
                            if (jsonArray.length() < razmerZaprosa || jsonArray.length() == 0) {
                                Toast.makeText(TovariActivity.this, "Load data completed !", Toast.LENGTH_SHORT).show();
                                System.out.println("if345dfs3 bolhe net");
                            } else {
                                System.out.println("ehe est 345dfs3");
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonRow = jsonArray.getJSONObject(i);
                                System.out.println("TovarActvity Tovar row " + jsonRow);
                                final Ceni ceniObjRowTovar = new Ceni(
                                        jsonRow.getString("naimenovanie"),
                                        jsonRow.getString("foto"),
                                        jsonRow.getString("cena"),
                                        jsonRow.getString("skidka"),
                                        jsonRow.getString("cenaskidka"),
                                        jsonRow.getString("selected"),
                                        jsonRow.getString("id"),
                                        jsonRow.getString("id"),
                                        jsonRow.getString("kolihestvo"),
                                        jsonRow.getString("kolvovupakovke"),
                                        jsonRow.getString("znahrazmriada"),
                                        jsonRow.getString("mestovilova"),
                                        jsonRow.getString("v_hchem_prodaetsia"),
                                        jsonRow.getString("typeupakovki"),
                                        jsonRow.getString("edinica_izmerenia_upakovki"),
                                        jsonRow.getString("raskazotovare")
                                );
                                System.out.println("Ceni create foto = " + ceniObjRowTovar.getFoto());
                                ceniList.add(ceniObjRowTovar);
                            }

                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                        } catch (JSONException e) {
                            System.out.println("\n ERR Try TovariActiv showSQL " + e.toString() + " <--->" + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Err TovariActiv showSQL " + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                Bundle bundle1 = getIntent().getExtras();
                String id = bundle1.getString("vetka");
                parameters.put("vetka", id);
                parameters.put("pokupatel", MainActivity.userStatic.getSqlId());
                parameters.put("indexstart", Integer.toString(indexStart));
                parameters.put("count", Integer.toString(countLoadItems));
                System.out.println("Otpravka na server iz " + id + ", tovar id  " + parameters.toString());
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MainActivity.userStatic.updateTextViewTotalKorzinaCount(tvCountKorzina);

        super.onActivityResult(requestCode, resultCode, data);
    }

}
