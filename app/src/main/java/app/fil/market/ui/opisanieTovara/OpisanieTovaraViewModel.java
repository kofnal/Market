package app.fil.market.ui.opisanieTovara;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.fil.market.BokovoeMenu;
import app.fil.market.MainActivity;
import app.fil.market.Model.Utils;
import app.fil.market.ui.tovari.TovarFromSQL;

public class OpisanieTovaraViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    final int countLoadItems = 10;

    public OpisanieTovaraViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    void showSQL(final int indexStart, final int indexEnd, final TextView tvCountKorzina,
                 final RecyclerView recyclerView, final String vetkaId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("tovar");
                            System.out.println("TovariActivityActivity tovarRow showSQL() " + jsonObject);
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
                                System.out.println("show SQL i="+i+", "+jsonArray.length());
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
//                                System.out.println("TovarFromSQL create foto = "+ OpisanieTovaraFragment.listTovarovSQLfromAdapterRV.size()+", " + tovarFromSQLObjRowTovar.getFoto());
//                                OpisanieTovaraFragment.listTovarovSQLfromAdapterRV.add(tovarFromSQLObjRowTovar);
                            }
                            int razmerZaprosa = indexEnd - indexStart;
                            if (jsonArray.length() < razmerZaprosa || jsonArray.length() == 0) {
//                                if(OpisanieTovaraFragment.listTovarovSQLfromAdapterRV.size()>razmerZaprosa)
//                                    Toast.makeText(tvCountKorzina.getContext(), "Загружены все товары.", Toast.LENGTH_SHORT).show();
                                System.out.println("if345dfs3 bolhe net");
                            } else {
                                System.out.println("ehe est 345dfs3");
                            }
                            System.out.println("adapter.notifyDataSetChanged()");
                            recyclerView.getAdapter().notifyDataSetChanged();
//                            OpisanieTovaraFragment.adapter.setLoaded();

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

                parameters.put("vetka", vetkaId);
                parameters.put("pokupatel", MainActivity.pokupatelStatic.getSqlId());
                parameters.put("indexstart", Integer.toString(indexStart));
                parameters.put("count", Integer.toString(countLoadItems));
                System.out.println("Otpravka na server iz " + vetkaId + ", tovar id  " + parameters.toString());
                return parameters;
            }
        };
        Volley.newRequestQueue(tvCountKorzina.getContext()).add(stringRequest);

    }
    void buySQL (final String tovar, final String kolvo, final String pokupatel){
        final String TAGmetod="buySQL ";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_KOLVO_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            JSONObject jsTotalKorzinaPos=jsonObject.getJSONObject("count");
                            String pos_korzina = jsTotalKorzinaPos.getString("pos_korzina");
                            System.out.println("Total pos korzina = "+pos_korzina+", js="+response);
                            MainActivity.pokupatelStatic.setKorzinaCountStr(pos_korzina);

//                            tvOpisanieOformit.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {

//                            System.out.println("\n ERR pars Json"+TAGclass+TAGmetod+e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                System.out.println("\n ERR Volley"+TAGclass+TAGmetod+error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String,String>();
                int kolvoInt = 1;
                if(kolvo.matches("[-+]?\\d+")){
                    kolvoInt=Integer.valueOf(kolvo);
//                    System.out.println("\n if param to SQL "+TAGclass+TAGmetod+parameters.toString());
                }else{
//                    System.out.println("\n else param to SQL "+TAGclass+TAGmetod+parameters.toString());
                }
                parameters.put("tovar", tovar);
                parameters.put("kolvo", kolvo);
//                parameters.put("kolvo", Integer.toString(kolvoInt+tovarFromSQLObjFromTovariActivity.getKolihestvo()));
                parameters.put("pokupatel", pokupatel);



                return parameters;
            }
        }                ;
        BokovoeMenu.requestQueue.add(stringRequest);
    }
}