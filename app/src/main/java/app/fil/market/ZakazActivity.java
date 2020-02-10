package app.fil.market;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.fil.market.ceni_i_skidki.Ceni;

public class ZakazActivity extends AppCompatActivity {
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
        final Bundle bundle = getIntent().getExtras();
//        System.out.println("ZakazActiv arrlist size = "+Integer.toString(bundle.getStringArrayList("arrlist").size()));
        ArrayList<Ceni> listCeniFromKorzinaActivity =  bundle.getParcelableArrayList("ceniList");
        System.out.println("ZakazActiv listCeniFromKorzinaActivity size = "+ Integer.toString(listCeniFromKorzinaActivity.size())+
        " , "+listCeniFromKorzinaActivity.get(0).getId_sql_tovara_v_baze()+", "+listCeniFromKorzinaActivity.get(0).getCenaZaOdinKg());
        for(int i = 0; i<listCeniFromKorzinaActivity.size(); i++){
            if(listCeniFromKorzinaActivity.get(i).getIsSelected()) {
                JSONObject jsObjTovarRow = new JSONObject();
                try {
                    jsObjTovarRow.put("id_tovar_sql", listCeniFromKorzinaActivity.get(i).getId_sql_tovara_v_baze());
                    jsObjTovarRow.put("cena_tovar", listCeniFromKorzinaActivity.get(i).getCenaZaUpakovky());
                    jsObjTovarRow.put("kolihestvo", listCeniFromKorzinaActivity.get(i).getKolihestvo());
                    jsObjTovarRow.put("id_sql_tovara_v_korzine_pokupatelia", listCeniFromKorzinaActivity.get(i).getId_sql_tovara_v_korzine_pokupatelia());
                    System.out.println("ZakazActivity Sozdanie JSON Objekta Na Server " + listCeniFromKorzinaActivity.get(i).getId_sql_tovara_v_baze());
                    jsTovari.put(i, jsObjTovarRow);
                    totalCenaZakaza=totalCenaZakaza+listCeniFromKorzinaActivity.get(i).getCenaFinalSoSkidkoy()*listCeniFromKorzinaActivity.get(i).getKolihestvo();
                } catch (JSONException e) {
                    System.out.println("ERROR ZakazActivity Sozdanie JSON Objekta Na Server " + e.toString());
                    e.printStackTrace();
                }
            }
        }
        try {
            jsObjKorzina.put("korzina", jsTovari);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("listCeniObj - "+ Double.toString(listCeniFromKorzinaActivity.get(0).getCenaZaOdinKg()));
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
                saveDataFromEditTextInPrefData();
            }
        });
    }
    private void saveDataFromEditTextInPrefData() {
        MainActivity.userStatic.setEmailStr(etZakazEmail.getText().toString());
        MainActivity.userStatic.setTelStr(etZakazTelefon.getText().toString());
        MainActivity.userStatic.setFioStr(etZakazFIO.getText().toString());
        MainActivity.userStatic.setAdresStr(etZakazAdresDostavki.getText().toString());
        MainActivity.userStatic.setKomentStr(etZakazKommentZakaza.getText().toString());
    }
    @Override
    protected void onPause() {
        saveDataFromEditTextInPrefData();
        super.onPause();
    }
    private void setTextAllEditText() {
        etZakazEmail.setText(MainActivity.userStatic.getEmailStr());
        etZakazTelefon.setText(MainActivity.userStatic.getTelStr());
        etZakazFIO.setText(MainActivity.userStatic.getFioStr());
        etZakazAdresDostavki.setText(MainActivity.userStatic.getAdresStr());
        etZakazKommentZakaza.setText(MainActivity.userStatic.getKomentStr());
    }
void sendZakaz(final JSONObject jsonArray){
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEND_ZAKAZ,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("ZakazActivity get server response from SendZakaz() - "+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        System.out.println("ZakazActivity get server response from SendZakaz() - "+jsonObject);
                        setResult(Utils.intentResultCODKorzinaToZakazPosleOtpravkiZakaza_OK);
                        MainActivity.userStatic.setkorzina_count_INT(0, tvKorzCountTemp);
                        System.out.println("sendZakaz INT "+Integer.toString(MainActivity.userStatic.getKorzina_count_INT())+" =korzcount");
                        System.out.println("sendZakaz String"+MainActivity.userStatic.getKorzinaCountStr()+" =korzcount");
                        finish();
                    }catch (JSONException e){
                        System.out.println("\n ERR senZakaz ZakazActivity "+response);
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
            parameters.put("pokupatel", MainActivity.userStatic.getSqlId());
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
