package app.fil.market;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.fil.market.Model.ILoadMore;
import app.fil.market.ceni_i_skidki.Ceni;


public class TovariActivity extends AppCompatActivity {
    List<Ceni> ceniList = new ArrayList<>();
    TovariSpisokAdapter adapter;
    ScrollView scrollView;
    RequestQueue requestQueue;
    ImageButton ibKorzina;
    TextView tvCountKorzina;
    final int countLoadItems=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovari);
        //10.02.2020-
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TovariSpisokAdapter(recyclerView, this, ceniList);
        recyclerView.setAdapter(adapter);


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        tvCountKorzina=findViewById(R.id.tvKorzinaCount);
        setStartCountKorzina(tvCountKorzina);
        scrollView= (ScrollView) findViewById(R.id.linerLayTovari);
        final Intent intentKorzina=new Intent(this,KorzinaActivity.class);
        ibKorzina=findViewById(R.id.ibOpisanieKorzina);
        ibKorzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentKorzina);
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
    void showSQL (final int indexStart, final int indexEnd){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            System.out.println("TovariActivityActivity tovarRow showSQL() "+jsonObject);
                            JSONArray jsonArray = jsonObject.getJSONArray("tovar");
                            JSONArray jsonArrayKorzina = jsonObject.getJSONArray("korzina");
                            if(jsonArrayKorzina.length()>0) {JSONObject jsKorz = jsonArrayKorzina.getJSONObject(0);
                                final String[] korzCount = {jsKorz.getString("SUM(korzina.kolihestvo)")};
                            if(!korzCount[0].equals("null")){
                                MainActivity.userStatic.setkorzina_count_INT(Integer.parseInt(korzCount[0]), tvCountKorzina);
                                tvCountKorzina.setVisibility(View.VISIBLE);
                                tvCountKorzina.setText(MainActivity.userStatic.getKorzinaCountStr());
                            }
                            else {
                                MainActivity.userStatic.setkorzina_count_INT(0, tvCountKorzina);
                                tvCountKorzina.setVisibility(View.INVISIBLE);
                            }
                            }
//
//                            LinearLayout linLayout = findViewById(R.id.linLayTovariVertical);
//                            LayoutInflater layoutInflater = getLayoutInflater();
//                            linLayout.removeAllViews();

                            int razmerZaprosa = indexEnd - indexStart;
                            if (jsonArray.length() < razmerZaprosa || jsonArray.length() == 0) {
                                Toast.makeText(TovariActivity.this, "Load data completed !", Toast.LENGTH_SHORT).show();
                                System.out.println("if345dfs3 bolhe net");
                            } else {
                                System.out.println("ehe est 345dfs3");
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonRow = jsonArray.getJSONObject(i);
                                System.out.println("TovarActvity Tovar row "+jsonRow);
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
                                System.out.println("Ceni create foto = "+ceniObjRowTovar.getFoto());
                                ceniList.add(ceniObjRowTovar);
//                                View item = layoutInflater.inflate(R.layout.row_tovar2, linLayout, false);
//                                TextView tvNazvanie = item.findViewById(R.id.tvRowTowarNaimenovanie);
//                                TextView tvCena = item.findViewById(R.id.tvRowTovarCena);
//                                TextView tvRub = item.findViewById(R.id.tvRub);
//                                tvCena.setPaintFlags(tvCena.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                                tvRub.setPaintFlags(tvRub.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                                TextView tvCena2= item.findViewById(R.id.tvCenaSoSkidkoy);
//                                TextView tvSkidka = item.findViewById(R.id.tvSkidkaTov);
//                                ConstraintLayout conlayCenaBezSkidki = item.findViewById(R.id.conlayCenaBezSkidki);
//                                final ImageButton btBuy=item.findViewById(R.id.ibKupit);
//                                if(ceniObjRowTovar.getKolihestvo()>0){
//                                    btBuy.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
//                                }
//                                btBuy.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        btBuy.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
//                                        if(ceniObjRowTovar.getIsSelected()){
//                                            if(!MainActivity.userStatic.getKorzinaCountStr().equals("null")&!MainActivity.userStatic.getKorzinaCountStr().equals("")){//
//                                                int  tempKorzCount=MainActivity.userStatic.getKorzina_count_INT();
//                                                MainActivity.userStatic.setkorzina_count_INT(tempKorzCount+1, tvCountKorzina);
//                                            }
//                                            ceniObjRowTovar.setVibranLi(true);
//                                        }
//                                        else {
//                                            int  tempKorzCount=MainActivity.userStatic.getKorzina_count_INT();
//                                            MainActivity.userStatic.setkorzina_count_INT(tempKorzCount+1, tvCountKorzina);
//                                            ceniObjRowTovar.setVibranLi(true);
//                                        }
//                                        tvCountKorzina.setText(MainActivity.userStatic.getKorzinaCountStr());
//                                        tvCountKorzina.setVisibility(View.VISIBLE);
//                                        buySQL(ceniObjRowTovar.getId_sql_tovara_v_baze(), MainActivity.userStatic.getSqlId());
//                                        System.out.println("Tovari Activity btBuy count korzCount$$= "+MainActivity.userStatic.getKorzinaCountStr());
//                                    }
//                                });
//                                tvNazvanie.setText(ceniObjRowTovar.getNaimenovanie());
//                                if(ceniObjRowTovar.getSkidka()>0) {
//                                    System.out.println("Tovari set visible skidki >0");
//                                    tvCena.setText(ceniObjRowTovar.getCenaStr());
//                                    tvSkidka.setText("СКИДКА " + Double.toString(ceniObjRowTovar.getSkidka()) + "%");
//                                    conlayCenaBezSkidki.setVisibility(View.VISIBLE);
//                                    tvSkidka.setVisibility(View.VISIBLE);
//                                }else{
//                                    System.out.println("Tovari set visible skidki ==0");
//                                }
//                                tvCena2.setText(ceniObjRowTovar.getCenaFinalSoSkidkoyStr());
//
//                                ImageView imageView=item.findViewById(R.id.ivTovar);
//                                Picasso.get().load(ceniObjRowTovar.getFoto()).into(imageView);
//                                item.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        createIntent(ceniObjRowTovar);
//                                        System.out.println("id + "+ceniObjRowTovar.getId_sql_tovara_v_baze());
//                                    }
//                                });
//                                linLayout.addView(item);
                            }

                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
//                            scrollView.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    // scrollView.fullScroll(View.FOCUS_DOWN);
//                                }
//                            });
                        } catch (JSONException e) {

                            System.out.println("\n ERR Try TovariActiv showSQL "+e.toString()+" <--->"+response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Err TovariActiv showSQL "+error.toString());

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String,String>();
                Bundle bundle1=getIntent().getExtras();
                String id=bundle1.getString("vetka");
                parameters.put("vetka", id);
                parameters.put("pokupatel", MainActivity.userStatic.getSqlId());
                parameters.put("indexstart", Integer.toString(indexStart));
                parameters.put("count", Integer.toString(countLoadItems));
                System.out.println("Otpravka na server iz "+id+", tovar id  "+parameters.toString());
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }

    void createIntent(Ceni ceniObj){
        Intent intent=new Intent(getApplicationContext(), TovarOpisanie.class);
        intent.putExtra("ceniObj", ceniObj);
        startActivityForResult(intent, Utils.intentRequestCODTovarActivToOpisanie);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    tvCountKorzina.setText(MainActivity.userStatic.getKorzinaCountStr());
    System.out.println("onResult Tovari  = "+MainActivity.userStatic.getKorzinaCountStr());
    super.onActivityResult(requestCode, resultCode, data);
}
    public static void setStartCountKorzina(TextView startCountKorzina) {
        if(!MainActivity.userStatic.getKorzinaCountStr().equals("") & MainActivity.userStatic.getKorzinaCountStr()!=null &
                !MainActivity.userStatic.getKorzinaCountStr().equals("null")  ){
            System.out.println("Tovari set korzina count "+ MainActivity.userStatic.getKorzinaCountStr() );
            startCountKorzina.setVisibility(View.VISIBLE);
            startCountKorzina.setText(MainActivity.userStatic.getKorzinaCountStr());
        }else{
            System.out.println("Tovari NOT set korzina count "+ MainActivity.userStatic.getKorzinaCountStr() );
        }
    }
    @Override
    protected void onResume() {
        MainActivity.userStatic.setKorzinaCountStr(MainActivity.userStatic.getKorzinaCountStr(), tvCountKorzina);
        super.onResume();
    }
}
