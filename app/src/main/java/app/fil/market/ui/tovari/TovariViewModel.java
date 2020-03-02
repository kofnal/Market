package app.fil.market.ui.tovari;

import android.view.View;
import android.widget.Toast;

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
import app.fil.market.ui.podkategorii.PodkategoriiFragment;

public class TovariViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    final int countLoadItems = 10;

    public TovariViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    void showSQL(final int indexStart, final int indexEnd,
                 final RecyclerView recyclerView) {
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
                                    BokovoeMenu.tvKorzinaCount.setVisibility(View.VISIBLE);
//                                    if (MainActivity.pokupatelStatic.getKorzinaCountStr() != null)
//                                        tvCountKorzina.setText(MainActivity.pokupatelStatic.getKorzinaCountStr());
                                } else {
                                    MainActivity.pokupatelStatic.setKorzinaCountStr(0);
                                    BokovoeMenu.tvKorzinaCount.setVisibility(View.INVISIBLE);
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
                                System.out.println("TovarFromSQL create foto = "+PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size()+", " + tovarFromSQLObjRowTovar.getFoto());
                                PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).add(tovarFromSQLObjRowTovar);
                            }
                            int razmerZaprosa = indexEnd - indexStart;
                            if (jsonArray.length() < razmerZaprosa || jsonArray.length() == 0) {
                                if(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size()>razmerZaprosa)
                                    Toast.makeText(BokovoeMenu.tvKorzinaCount.getContext(), "Загружены все товары.", Toast.LENGTH_SHORT).show();
                                System.out.println("if345dfs3 bolhe net");
                            } else {
                                System.out.println("ehe est 345dfs3");
                            }
                            System.out.println("adapter.notifyDataSetChanged()");
                            recyclerView.getAdapter().notifyDataSetChanged();
                            TovariFragment.adapter.setLoaded();

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

                parameters.put("vetka", PodkategoriiFragment.podkatVetkaId);
                parameters.put("pokupatel", MainActivity.pokupatelStatic.getSqlId());
                parameters.put("indexstart", Integer.toString(indexStart));
                parameters.put("count", Integer.toString(countLoadItems));
//                System.out.println("Otpravka na server iz " + vetkaId + ", tovar id  " + parameters.toString());
                return parameters;
            }
        };
        Volley.newRequestQueue(BokovoeMenu.tvKorzinaCount.getContext()).add(stringRequest);

    }
}