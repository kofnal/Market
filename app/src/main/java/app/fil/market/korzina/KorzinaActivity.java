package app.fil.market.korzina;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.fil.market.BokovoeMenu;
import app.fil.market.MainActivity;
import app.fil.market.MyDialog;
import app.fil.market.R;
import app.fil.market.Model.Utils;
import app.fil.market.ui.tovari.TovariFragment;
import app.fil.market.zakazi.Zakaz_Activity;
import app.fil.market.ui.tovari.TovarFromSQL;

public class KorzinaActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    TextView tvKorzinaCount;
    Button btKupit;
    ImageButton ibKorzinaDelTovari;
    TextView tvKorzinaKOplateSkolkoVsego;
    CheckBox mainCheckBox;
    ConstraintLayout conLayKorzinaBottom;
    Button btKorzinaK_Pokupkam;
    JSONObject jsObjTempToSQL;
    JSONArray jsArrTempToSQL;
    RecyclerView rvKorzina;
    KorzinaAdapter korzinaAdapter;
    public static ArrayList<TovarFromSQL> tovariList2 = new ArrayList<>();
    public static ArrayList<TovarFromSQL> tovariListBezOtpravlennogoZakaza = new ArrayList<>();
    int countShowToastOt70rub=0;

    public static void obnovitDannieKorzini() {
//        MainActivity.pokupatelStatic.setKorzinaCountStr(0);
        KorzinaActivity.tovariList2=KorzinaActivity.tovariListBezOtpravlennogoZakaza;
        for(int i = 0; i<TovariFragment.listTovarovSQLfromAdapterRV.size(); i++){

            for(int y = 0; y<KorzinaActivity.tovariList2.size(); y++){
               if( TovariFragment.listTovarovSQLfromAdapterRV.get(i).getId_sql_tovara_v_baze()==
               KorzinaActivity.tovariList2.get(y).getId_sql_tovara_v_baze()){
                   TovariFragment.listTovarovSQLfromAdapterRV.get(i).setEstLi_V_KorzineObj(true);
               } else{
                   TovariFragment.listTovarovSQLfromAdapterRV.get(i).setEstLi_V_KorzineObj(false);
               }
            }
        }
        TovariFragment.adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korzina);
        mainCheckBox = findViewById(R.id.chMain);
        ibKorzinaDelTovari = findViewById(R.id.ibKorzinaDelTovari);
        tvKorzinaCount = findViewById(R.id.tvKorzCountVibrannix);
        btKupit = findViewById(R.id.btKupit);
        btKorzinaK_Pokupkam = findViewById(R.id.btKorzinaK_Pokupkam);
        rvKorzina = findViewById(R.id.rvKorzina);
        tvKorzinaKOplateSkolkoVsego = findViewById(R.id.tvKorzinaKOplateSkolkoVsego);
        conLayKorzinaBottom = findViewById(R.id.conLayKorzinaBottom);
        tovariList2.clear();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        jsObjTempToSQL = new JSONObject();
        jsArrTempToSQL = new JSONArray();
        rvKorzina.setLayoutManager(new LinearLayoutManager(this));

        korzinaAdapter = new KorzinaAdapter(tvKorzinaKOplateSkolkoVsego,
                tvKorzinaCount,
                conLayKorzinaBottom,
                ibKorzinaDelTovari,
                mainCheckBox,
                btKorzinaK_Pokupkam,
                this);
        rvKorzina.setAdapter(korzinaAdapter);


        tvKorzinaCount.setVisibility(View.GONE);

        btKorzinaK_Pokupkam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BokovoeMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        final Intent intentZakaz = new Intent(this, Zakaz_Activity.class);
        btKupit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("btKupit " + KorzinaActivity.tovariList2.size());
                if (korzinaAdapter.totalK_OplateDoubl >= 70.0) {

//                    System.out.println("KorzActiv arrlist size = " + Integer.toString(arrListIdTovarovK_ZakazuSQL.size()));
//                    System.out.println("KorzActiv put Bundle TovarFromSQL Obj ID==" + listCeniObj.get(0).getId_sql_tovara_v_baze());
                    startActivity(intentZakaz);
                    finish();

                } else {

                    if (countShowToastOt70rub == 0) {
                        Toast.makeText(btKupit.getContext(), R.string.zakaz_ot_70_rub, Toast.LENGTH_LONG).show();
                        countShowToastOt70rub++;
                    } else {
                        final MyDialog myDialoge = new MyDialog(KorzinaActivity.this);
                        myDialoge.showDialog(getString(R.string.zakaz_ot_70_rub));
                        myDialoge.dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                myDialoge.dimiss();
                                return false;
                            }
                        });
                    }
                }
            }
        });


        showSQL();

    }

    void showSQL() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_KORZINA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            tovariList2.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println("jsObj Korzina " + jsonObject.toString());
                            JSONArray jsArrTovarV_Korzine = jsonObject.getJSONArray("korzina");
                            if (jsArrTovarV_Korzine.length() == 0) {
                                btKorzinaK_Pokupkam.setVisibility(View.VISIBLE);
                            }

                            for (int i = 0; i < jsArrTovarV_Korzine.length(); i++) {
                                if (i == 0) {
                                    mainCheckBox.setVisibility(View.VISIBLE);
                                }
                                JSONObject jsonRow = jsArrTovarV_Korzine.getJSONObject(i);
                                System.out.println("create objekt Cen in korzina "+jsonRow);
                                final TovarFromSQL tovarFromSQLRowObj = new TovarFromSQL(
                                        jsonRow.getString("naimenovanie"),
                                        jsonRow.getString("foto"),
                                        jsonRow.getString("cena"),
                                        jsonRow.getString("skidka"),
                                        jsonRow.getString("cenaskidka"),
                                        jsonRow.getString("selected"),
                                        jsonRow.getString("id"),
                                        jsonRow.getString("idk"),
                                        jsonRow.getString("kolihestvo"),
                                        jsonRow.getString("kolvovupakovke")
                                );
                                tovariList2.add(i, tovarFromSQLRowObj);
                            }
                            korzinaAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            System.out.println("\n ERR 1 ShowSQL in KorzinaActivity "
                                    + e.toString() + "\n"
                                    + response);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Korzina ShowSQL ERROR - " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("pokupatel", MainActivity.pokupatelStatic.getSqlId());
                System.out.println("Korzina showSQL send param " + parameters.toString());
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.intentRequestCODKorzinaToZakazPosleOtpravkiZakaza) {
            if (resultCode == Utils.intentResultCODKorzinaToZakazPosleOtpravkiZakaza_OK) {
                finish();
                Toast.makeText(this, getString(R.string.spasibo_zakaz), Toast.LENGTH_LONG).show();
            } else {
                onRestart();
            }
        }
    }
}
