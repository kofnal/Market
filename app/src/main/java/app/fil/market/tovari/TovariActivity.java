package app.fil.market.tovari;


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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.fil.market.BokovoeMenu;
import app.fil.market.MainActivity;
import app.fil.market.Model.ILoadMore;
import app.fil.market.R;
import app.fil.market.Model.Utils;
import app.fil.market.korzina.KorzinaActivity;
import app.fil.market.ui.podkategorii.PodkategoriiFragment;
import app.fil.market.ui.tovari.TovarFromSQL;
import app.fil.market.ui.tovari.TovariFragment;
import app.fil.market.ui.tovari.TovariSpisokAdapter;

public class TovariActivity extends AppCompatActivity {
    TovariSpisokAdapter adapter;
    ImageButton ibKorzina;
    TextView tvCountKorzina;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovari);
        ibKorzina = findViewById(R.id.ibOpisanieKorzina);
        tvCountKorzina = findViewById(R.id.tvKorzinaCount);
        RecyclerView recyclerView = findViewById(R.id.rvTovariFragm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TovariSpisokAdapter(recyclerView, this);
        recyclerView.setAdapter(adapter);
        MainActivity.pokupatelStatic.updateTextViewTotalKorzinaCount();

        final Intent intentKorzina = new Intent(this, KorzinaActivity.class);

        ibKorzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentKorzina);
                finish();
            }
        });
        showSQL(0, TovariFragment.countLoadItems);
        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
//                TovariFragment.listTovarovSQLfromAdapterRV.add(null);
//                adapter.notifyItemInserted(TovariFragment.listTovarovSQLfromAdapterRV.size() - 1);
//                TovariFragment.listTovarovSQLfromAdapterRV.remove(TovariFragment.listTovarovSQLfromAdapterRV.size() - 1);
//                adapter.notifyItemRemoved(TovariFragment.listTovarovSQLfromAdapterRV.size());
//                int index = TovariFragment.listTovarovSQLfromAdapterRV.size();
//                int end = index + TovariFragment.countLoadItems;
//                showSQL(index, end);
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
                                    MainActivity.pokupatelStatic.setKorzinaCountStr(Integer.parseInt(korzCount[0]));
                                    tvCountKorzina.setVisibility(View.VISIBLE);
//                                    if (MainActivity.pokupatelStatic.getKorzinaCountStr() != null)
//                                        tvCountKorzina.setText(MainActivity.pokupatelStatic.getKorzinaCountStr());
                                } else {
                                    MainActivity.pokupatelStatic.setKorzinaCountStr(0);
                                    tvCountKorzina.setVisibility(View.INVISIBLE);
                                }
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonRow = jsonArray.getJSONObject(i);
                                System.out.println("TovarActvity Tovar row " + jsonRow);
                                final TovarFromSQL tovarFromSQLObjRowTovar = new TovarFromSQL(
                                        jsonRow.getString("naimenovanie"),
                                        jsonRow.getString("foto"),
                                        jsonRow.getString("cena"),
                                        jsonRow.getString("skidka"),
                                        jsonRow.getString("cenaskidka"),
                                        jsonRow.getString("selected"),
                                        jsonRow.getString("id"),
                                        jsonRow.getString("idk"),
                                        jsonRow.getString("kolihestvo"),
                                        jsonRow.getString("kolvovupakovke"),
                                        jsonRow.getString("znahrazmriada"),
                                        jsonRow.getString("mestovilova"),
                                        jsonRow.getString("v_hchem_prodaetsia"),
                                        jsonRow.getString("typeupakovki"),
                                        jsonRow.getString("edinica_izmerenia_upakovki"),
                                        jsonRow.getString("raskazotovare")
                                );
                                System.out.println("TovarFromSQL create foto = " + tovarFromSQLObjRowTovar.getFoto());
                                PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).add(tovarFromSQLObjRowTovar);
                            }
                            int razmerZaprosa = indexEnd - indexStart;
                            if (jsonArray.length() < razmerZaprosa || jsonArray.length() == 0) {
                                if(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size()>razmerZaprosa)
                                    Toast.makeText(TovariActivity.this, "Загружены все товары.", Toast.LENGTH_SHORT).show();
                                System.out.println("if345dfs3 bolhe net");
                            } else {
                                System.out.println("ehe est 345dfs3");
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
                parameters.put("pokupatel", MainActivity.pokupatelStatic.getSqlId());
                parameters.put("indexstart", Integer.toString(indexStart));
                parameters.put("count", Integer.toString(TovariFragment.countLoadItems));
                System.out.println("Otpravka na server iz " + id + ", tovar id  " + parameters.toString());
                return parameters;
            }
        };
        BokovoeMenu.requestQueue.add(stringRequest);
    }
    @Override
    protected void onResume() {
        if(tvCountKorzina!=null){
            MainActivity.pokupatelStatic.updateTextViewTotalKorzinaCount();
        }
        super.onResume();

    }

}
