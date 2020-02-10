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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import app.fil.market.ceni_i_skidki.Ceni;


public class TovariActivity extends AppCompatActivity {
    ScrollView scrollView;
    RequestQueue requestQueue;
    ImageButton ibKorzina;
    TextView tvCountKorzina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovari);
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
        showSQL();
    }
    void showSQL (){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            System.out.println("TovariActivityActivity tovarRow showSQL() "+jsonObject);
                            JSONArray jsonArray = jsonObject.getJSONArray("tovar");
                            JSONArray jsonArrayKorzina = jsonObject.getJSONArray("korzina");
                            JSONObject jsKorz = jsonArrayKorzina.getJSONObject(0);
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
                            LinearLayout linLayout = findViewById(R.id.linLayTovariVertical);
                            LayoutInflater layoutInflater = getLayoutInflater();
                            linLayout.removeAllViews();
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
                                View item = layoutInflater.inflate(R.layout.row_tovar, linLayout, false);
                                TextView tvNazvanie = item.findViewById(R.id.tvRowTowarNaimenovanie);
                                TextView tvCena = item.findViewById(R.id.tvRowTovarCena);
                                TextView tvRub = item.findViewById(R.id.tvRub);
                                tvCena.setPaintFlags(tvCena.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                tvRub.setPaintFlags(tvRub.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                TextView tvCena2= item.findViewById(R.id.tvCenaSoSkidkoy);
                                TextView tvSkidka = item.findViewById(R.id.tvSkidkaTov);
                                ConstraintLayout conlayCenaBezSkidki = item.findViewById(R.id.conlayCenaBezSkidki);
                                final ImageButton btBuy=item.findViewById(R.id.ibKupit);
                                if(ceniObjRowTovar.getKolihestvo()>0){
                                    btBuy.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
                                }
                                btBuy.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        btBuy.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
                                        if(ceniObjRowTovar.getIsSelected()){
                                            if(!MainActivity.userStatic.getKorzinaCountStr().equals("null")&!MainActivity.userStatic.getKorzinaCountStr().equals("")){//
                                                int  tempKorzCount=MainActivity.userStatic.getKorzina_count_INT();
                                                MainActivity.userStatic.setkorzina_count_INT(tempKorzCount+1, tvCountKorzina);
                                            }
                                            ceniObjRowTovar.setVibranLi(true);
                                        }
                                        else {
                                            int  tempKorzCount=MainActivity.userStatic.getKorzina_count_INT();
                                            MainActivity.userStatic.setkorzina_count_INT(tempKorzCount+1, tvCountKorzina);
                                            ceniObjRowTovar.setVibranLi(true);
                                        }
                                        tvCountKorzina.setText(MainActivity.userStatic.getKorzinaCountStr());
                                        tvCountKorzina.setVisibility(View.VISIBLE);
                                        buySQL(ceniObjRowTovar.getId_sql_tovara_v_baze(), MainActivity.userStatic.getSqlId());
                                        System.out.println("Tovari Activity btBuy count korzCount$$= "+MainActivity.userStatic.getKorzinaCountStr());
                                    }
                                });
                                tvNazvanie.setText(ceniObjRowTovar.getNaimenovanie());
                                if(ceniObjRowTovar.getSkidka()>0) {
                                    System.out.println("Tovari set visible skidki >0");
                                    tvCena.setText(ceniObjRowTovar.getCenaStr());
                                    tvSkidka.setText("СКИДКА " + Double.toString(ceniObjRowTovar.getSkidka()) + "%");
                                    conlayCenaBezSkidki.setVisibility(View.VISIBLE);
                                    tvSkidka.setVisibility(View.VISIBLE);
                                }else{
                                    System.out.println("Tovari set visible skidki ==0");
                                }
                                tvCena2.setText(ceniObjRowTovar.getCenaFinalSoSkidkoyStr());

                                ImageView imageView=item.findViewById(R.id.ivTovar);
                                Picasso.get().load(ceniObjRowTovar.getFoto()).into(imageView);
                                item.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createIntent(ceniObjRowTovar);
                                        System.out.println("id + "+ceniObjRowTovar.getId_sql_tovara_v_baze());
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
                Bundle bundle1=getIntent().getExtras();
                String id=bundle1.getString("vetka");
                parameters.put("vetka", id);
                parameters.put("pokupatel", MainActivity.userStatic.getSqlId());
                parameters.put("indexstart", "0");
                parameters.put("count", "100");
                System.out.println("Otpravka na server iz tovar id "+id);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }
    void buySQL (final String tovar, final String pokupatel){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_KOLVO_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            System.out.println("Tovari activ buy SQL = "+jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("serv");

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
                parameters.put("tovar", tovar);
                parameters.put("kolvo", "1");
                parameters.put("pokupatel", pokupatel);
                System.out.println("Tovari activ buy SQL parametrs = "+parameters);
                return parameters;
            }
        }                ;
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
