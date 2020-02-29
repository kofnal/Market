package app.fil.market.zakazi;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

import app.fil.market.MainActivity;
import app.fil.market.R;
import app.fil.market.Model.Utils;
import app.fil.market.korzina.KorzinaActivity;

public class Zakaz_Activity extends AppCompatActivity {
    EditText etZakazEmail, etZakazTelefon, etZakazFIO, etZakazAdresDostavki, etZakazKommentZakaza, etZakazVvediPromo;
    TextView tvZakazOformitButt, tvKorzCountTemp;
    RequestQueue requestQueue;
    Double totalCenaZakaza=0.0;
    JSONArray jsTovari= new JSONArray();
    final JSONObject jsObjKorzina = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakaz);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        etZakazEmail=findViewById(R.id.etZakazEmail);
        etZakazTelefon=findViewById(R.id.etZakazTelefon);
        etZakazFIO=findViewById(R.id.etZakazFIO);
        etZakazAdresDostavki=findViewById(R.id.etZakazAdresDostavki);
        etZakazKommentZakaza=findViewById(R.id.etZakazKommentZakaza);
        etZakazVvediPromo=findViewById(R.id.etZakazVvediPromo);
        tvZakazOformitButt=findViewById(R.id.tvZakazOformitButt);
        etZakazEmail=findViewById(R.id.etZakazEmail);
        tvKorzCountTemp=findViewById(R.id.tvKorzCountTemp);
        setTextAllEditText();
//        final Bundle bundle = getIntent().getExtras();
//        System.out.println("ZakazActiv arrlist size = "+Integer.toString(bundle.getStringArrayList("arrlist").size()));
//        ArrayList<TovarFromSQL> listTovarFromSQLFromKorzinaActivity =  bundle.getParcelableArrayList(Utils.tovarsFromSQLList);
        System.out.println("ZakazActiv listTovarFromSQLFromKorzinaActivity size = "+ Integer.toString(KorzinaActivity.tovariList2.size())+
        " , "+ KorzinaActivity.tovariList2.get(0).getId_sql_tovara_v_baze()+", "+ KorzinaActivity.tovariList2.get(0).getCenaZaUpak());
        KorzinaActivity.tovariListBezOtpravlennogoZakaza.clear();
        for(int i = 0; i< KorzinaActivity.tovariList2.size(); i++){
            if(KorzinaActivity.tovariList2.get(i).getIsSelected()) {
                JSONObject jsObjTovarRow = new JSONObject();
                try {
                    jsObjTovarRow.put("id_tovar_sql", KorzinaActivity.tovariList2.get(i).getId_sql_tovara_v_baze());
                    jsObjTovarRow.put("cena_tovar", KorzinaActivity.tovariList2.get(i).getCenaZaOdinKg());
                    jsObjTovarRow.put("kolihestvo", KorzinaActivity.tovariList2.get(i).getKolihestvo());
                    jsObjTovarRow.put("id_sql_tovara_v_korzine_pokupatelia",
                            KorzinaActivity.tovariList2.get(i).getId_sql_tovara_v_korzine_pokupatelia());
                    System.out.println("Zakaz_Activity Sozdanie JSON Objekta Na Server " +
                            KorzinaActivity.tovariList2.get(i).getId_sql_tovara_v_baze());
                    jsTovari.put(i, jsObjTovarRow);
                    totalCenaZakaza=totalCenaZakaza+ KorzinaActivity.tovariList2.get(i).getCenaFinalSoSkidkoyZaUpak()*
                            KorzinaActivity.tovariList2.get(i).getKolihestvo();
                } catch (JSONException e) {
                    System.out.println("ERROR Zakaz_Activity Sozdanie JSON Objekta Na Server " + e.toString());
                    e.printStackTrace();
                }
            } else {
                KorzinaActivity.tovariListBezOtpravlennogoZakaza.add(KorzinaActivity.tovariList2.get(i));
            }
        }
        try {
            jsObjKorzina.put("korzina", jsTovari);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("listCeniObj - "+ Double.toString(KorzinaActivity.tovariList2.get(0).getCenaZaUpak()));
        System.out.println("listCeniObj jsObjKorzina.toString() - "+jsObjKorzina.toString());
        //etZakazTelefon.setText(bundle.getStringArrayList("arrlist").get(0));
        etZakazTelefon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        tvZakazOformitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendZakaz(jsObjKorzina);

                tvZakazOformitButt.setVisibility(View.GONE);
            }
        });
    }
    private void saveDataFromEditTextInPrefData() {
        MainActivity.pokupatelStatic.setEmail(etZakazEmail.getText().toString());
        MainActivity.pokupatelStatic.setTel(etZakazTelefon.getText().toString());
        MainActivity.pokupatelStatic.setFioUser(etZakazFIO.getText().toString());
        MainActivity.pokupatelStatic.setAdres_string(etZakazAdresDostavki.getText().toString());
        MainActivity.pokupatelStatic.setKoment_string(etZakazKommentZakaza.getText().toString());
    }
    @Override
    protected void onPause() {
        saveDataFromEditTextInPrefData();
        super.onPause();
    }
    private void setTextAllEditText() {
        etZakazEmail.setText(MainActivity.pokupatelStatic.getEmail());
        etZakazTelefon.setText(MainActivity.pokupatelStatic.getTel());
        etZakazFIO.setText(MainActivity.pokupatelStatic.getFioUser());
        etZakazAdresDostavki.setText(MainActivity.pokupatelStatic.getAdres_string());
        etZakazKommentZakaza.setText(MainActivity.pokupatelStatic.getKoment_string());
    }
void sendZakaz(final JSONObject jsonArray){
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEND_ZAKAZ,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Zakaz_Activity get server response from SendZakaz() - "+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        //проверить вставку на сервер желательно

                        KorzinaActivity.obnovitDannieKorzini();
                        System.out.println("Zakaz_Activity get server response from SendZakaz() - "+jsonObject);
                        setResult(Utils.intentResultCODKorzinaToZakazPosleOtpravkiZakaza_OK);

                        System.out.println("sendZakaz INT "+Integer.toString(MainActivity.pokupatelStatic.getKorzina_kountInt())+" =korzcount");
//                        System.out.println("sendZakaz String"+MainActivity.pokupatelStatic.getKorzinaCountStr()+" =korzcount");
                        finish();
                    }catch (JSONException e){
                        System.out.println("\n ERR senZakaz Zakaz_Activity "+response);
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
            System.out.println("Zakaz Activity VOHLO jsonArray =   "+jsonArray);
            parameters.put("jsarr", jsonArray.toString());
            parameters.put("pokupatel", MainActivity.pokupatelStatic.getSqlId());
            parameters.put("idadres", "43");
            parameters.put("typedostavki", "pohta");
            parameters.put("zakaz_email_input_user", etZakazEmail.getText().toString());
            parameters.put("zakaz_telefon_input_user", etZakazTelefon.getText().toString());
            parameters.put("zakaz_fio_input_user", etZakazFIO.getText().toString());
            parameters.put("zakaz_adres_dostavki_input_user", etZakazAdresDostavki.getText().toString());
            parameters.put("zakaz_komentariy_input_user", etZakazKommentZakaza.getText().toString());
            parameters.put("zakaz_promo_input_user", etZakazVvediPromo.getText().toString());
            System.out.println("Zakaz Activity VOHLO parametrs =   "+parameters.toString());
            return parameters;
        }
    };
    requestQueue.add(stringRequest);
}
}
