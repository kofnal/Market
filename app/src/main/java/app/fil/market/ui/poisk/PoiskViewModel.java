package app.fil.market.ui.poisk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

public class PoiskViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    String korzCount;

    public LiveData<String> getText() {
        return mText;
    }
    void showSQLPodkat (final RecyclerView recyclerView){
////        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_PODKATEGORIA,
////                new Response.Listener<String>() {
////                    @Override
////                    public void onResponse(String response) {
////                        try {
////                            JSONObject jsonObject= new JSONObject(response);
//////                            System.out.println("KotegoriiPodActivity jsonObj from SQL Server -"+jsonObject);
////                            JSONArray jsonArray = jsonObject.getJSONArray("serv");
////                            JSONArray jsonArrayKorzina = jsonObject.getJSONArray("korzina");
////                            JSONObject jsKorz = jsonArrayKorzina.getJSONObject(0);
////                            korzCount=jsKorz.getString("SUM(korzina.kolihestvo)");
//////                            System.out.println(jsonArrayKorzina);
//////                            System.out.println(korzCount);
////                            BokovoeMenu.tvKorzinaCount.setText(korzCount);
////                            MainActivity.pokupatelStatic.setKorzinaCountStr(korzCount);
////                            if(korzCount.equals("null")|korzCount.equals("0")){
////                                BokovoeMenu.tvKorzinaCount.setVisibility(View.INVISIBLE);
////                            }
////                            else {
////                                BokovoeMenu.tvKorzinaCount.setVisibility(View.VISIBLE);
////                            }
////
////                            for (int i = 0; i < jsonArray.length(); i++) {
//////                            for (int i = 0; i < 1; i++) {
////                                JSONObject jsonRow = jsonArray.getJSONObject(i);
////                                String nazvanie     =    jsonRow.getString("nazvanie");
////                                final String id     =    jsonRow.getString("id");
////                                String foto = Utils.BASE_IP+jsonRow.getString("foto");
////                               PoiskObjectSQL poiskObjectSQL = new PoiskObjectSQL(id, nazvanie, foto);
////                               PoiskFragment.poiskObjectSQLSList.add(poiskObjectSQL);
////                               PoiskFragment.listTovarovSQLfromAdapterRV.add(new ArrayList<TovarFromSQL>());
////                               PoiskFragment.firstStartListBool.add(true);
////                            }
////                            recyclerView.getAdapter().notifyDataSetChanged();
////                        } catch (JSONException e) {
////                            System.out.println("\n ERR"+response);
////                        }
////                    }
////                }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////
////            }
////        })
////        {
////            @Override
////            protected Map<String, String> getParams() throws AuthFailureError {
////                Map<String,String> parameters = new HashMap<String,String>();
////                parameters.put("id_vetka", "1");
////                parameters.put("pokupatel", MainActivity.pokupatelStatic.getSqlId());
////                System.out.println("Send param from PodkatClass "+parameters);
////                return parameters;
////            }
//        };
//        BokovoeMenu.requestQueue.add(stringRequest);
    }
}