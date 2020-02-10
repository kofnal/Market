package app.fil.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.fil.market.ceni_i_skidki.Ceni;

public class TovarOpisanie extends AppCompatActivity {
    RequestQueue requestQueue;
    TextView tvOpisVKorzinu, tvKorzinaCount, tvOpisanieOformit, tvopisMinusTovar;
    EditText etKolvo;
    ImageButton ibOpisanieKorzina;
    ScrollView scrlOpisanie;
    TextView tvNazvanie, tvOpisCenaTovara, tvOpisZaEdinicuIzmer, tvOpisZnahenieVesaUpakovki,tvOpisZnahenieRazmernogoRiada,
            tvOpisanieZnahenieMestaVilova, tvOpisTypeFasovki, tvOpisanieTovaraRaskaz, tvOpisTovaraCenaZaEdinicu, tvOpisVesUpakPodEditText,
            tvOpisRazmerniyRiad, tvOpisanieMestoVilova;
    ImageView ivOpisTovar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar_opisanie);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Intent intentKorzina = new Intent(this, KorzinaActivity.class);
        tvOpisVKorzinu = findViewById(R.id.tvOpisVKorzinu);
        ibOpisanieKorzina = findViewById(R.id.ibOpisanieKorzina);
        tvKorzinaCount = findViewById(R.id.tvKorzinaCount);
        tvOpisanieOformit = findViewById(R.id.tvOpisanieOformit);
        etKolvo = findViewById(R.id.etOpisInputCountTovar);
        tvopisMinusTovar= findViewById(R.id.tvopisMinusTovar);
        scrlOpisanie= findViewById(R.id.scrlOpisanie);
        tvNazvanie=findViewById(R.id.tvOpisNazvanieTovara);
        ivOpisTovar=findViewById(R.id.ivOpisTovar);
        tvOpisCenaTovara=findViewById(R.id.tvOpisCenaTovara);
        tvOpisZaEdinicuIzmer=findViewById(R.id.tvOpisZaEdinicuIzmer);
        tvOpisZnahenieVesaUpakovki=findViewById(R.id.tvOpisZnahenieVesaUpakovki);
        tvOpisZnahenieRazmernogoRiada=findViewById(R.id.tvOpisZnahenieRazmernogoRiada);
        tvOpisanieZnahenieMestaVilova=findViewById(R.id.tvOpisanieZnahenieMestaVilova);
        tvOpisTypeFasovki=findViewById(R.id.tvOpisTypeFasovki);
        tvOpisanieTovaraRaskaz=findViewById(R.id.tvOpisanieTovaraRaskaz);
        tvOpisTovaraCenaZaEdinicu=findViewById(R.id.tvOpisTovaraCenaZaEdinicu);
        tvOpisVesUpakPodEditText=findViewById(R.id.tvOpisVesUpakPodEditText);
        tvOpisRazmerniyRiad=findViewById(R.id.tvOpisRazmerniyRiad);
        tvOpisanieMestoVilova=findViewById(R.id.tvOpisanieMestoVilova);

        final Ceni ceniObjFromTovariActivity = getIntent().getParcelableExtra("ceniObj");
        System.out.println("Tovari Opisanie Activity Ceni obj = "+ceniObjFromTovariActivity.getNaimenovanie());
        TovariActivity.setStartCountKorzina(tvKorzinaCount);
        tvopisMinusTovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tvopisMinusTovarTemp = Integer.parseInt(etKolvo.getText().toString())-1;
                etKolvo.setText(Integer.toString(tvopisMinusTovarTemp));
            }
        });
        TextView tvOpisPlusTovar= findViewById(R.id.tvOpisPlusTovar);
        tvOpisPlusTovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tvopisPlusTovarTemp = Integer.parseInt(etKolvo.getText().toString())+1;
                etKolvo.setText(Integer.toString(tvopisPlusTovarTemp));
            }
        });
        tvOpisVKorzinu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buySQL(ceniObjFromTovariActivity.getId_sql_tovara_v_baze(), etKolvo.getText().toString(),
                        MainActivity.userStatic.getSqlId());
            }
        });
        ibOpisanieKorzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentKorzina);
            }
        });
        tvOpisanieOformit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentKorzina);
            }
        });
        tvNazvanie.setText(ceniObjFromTovariActivity.getNaimenovanie());
        Picasso.get().load(ceniObjFromTovariActivity.getFoto()).into(ivOpisTovar);
        tvOpisCenaTovara.setText(ceniObjFromTovariActivity.getCenaFinalSoSkidkoyStr());
        tvOpisZnahenieVesaUpakovki.setText(ceniObjFromTovariActivity.getKolihestvoV_Upakovke()+" "+
                ceniObjFromTovariActivity.getEdinica_izmerenia_upakovki());
        tvOpisZnahenieRazmernogoRiada.setText(ceniObjFromTovariActivity.getZnahrazmriada());
        tvOpisanieZnahenieMestaVilova.setText(ceniObjFromTovariActivity.getMestovilova());
        tvOpisTypeFasovki.setText(ceniObjFromTovariActivity.getSostoianieTovara());
        tvOpisanieTovaraRaskaz.setText(ceniObjFromTovariActivity.getRaskazotovare());
        tvOpisTovaraCenaZaEdinicu.setText("("+ceniObjFromTovariActivity.getCenaZaUpakovkyStr()+" руб. за "+
                ceniObjFromTovariActivity.getTypeUpakovki()+")");
        tvOpisVesUpakPodEditText.setText("("+ceniObjFromTovariActivity.getKolihestvoV_Upakovke()+" "+
                ceniObjFromTovariActivity.getEdinica_izmerenia_upakovki()+")");
        tvOpisZaEdinicuIzmer.setText("за 1 "+ceniObjFromTovariActivity.getEdinica_izmerenia_upakovki());
        if(ceniObjFromTovariActivity.getZnahrazmriada().equals("")) tvOpisRazmerniyRiad.setVisibility(View.GONE);
        if(ceniObjFromTovariActivity.getMestovilova().equals("")) tvOpisanieMestoVilova.setVisibility(View.GONE);
    }
    void buySQL (final String tovar, final String kolvo, final String pokupatel){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_KOLVO_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            JSONObject jsTotalKorzinaPos=jsonObject.getJSONObject("count");
                            String pos_korzina = jsTotalKorzinaPos.getString("pos_korzina");
                            System.out.println("Total pos korzina = "+pos_korzina);
                            MainActivity.userStatic.setKorzinaCountStr(pos_korzina, tvKorzinaCount);
                            tvKorzinaCount.setVisibility(View.VISIBLE);
                            tvOpisanieOformit.setVisibility(View.VISIBLE);
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
                parameters.put("kolvo", kolvo);
                parameters.put("pokupatel", pokupatel);
                return parameters;
            }
        }                ;
        requestQueue.add(stringRequest);
    }
    @Override
    protected void onResume() {
        MainActivity.userStatic.setKorzinaCountStr(MainActivity.userStatic.getKorzinaCountStr(), tvKorzinaCount);
        super.onResume();
    }
}