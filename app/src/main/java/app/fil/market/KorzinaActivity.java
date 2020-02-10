package app.fil.market;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.fil.market.ceni_i_skidki.Ceni;

public class KorzinaActivity extends AppCompatActivity implements View.OnClickListener {
    ScrollView scrollView;
    RequestQueue requestQueue;
    String typeDostavki;
    String adresPost;
    String adresPunkt;
    Double doubleK_OplateSkolkoVsego = 0.0;
    Double korzinaCount = 0.0;
    TextView tvKorzinaCount;
    Button btKupit;
    ImageButton imageButtonDel;
    TextView tvKorzinaKOplateSkolkoVsego;
    CheckBox mainCheckBox;
    ArrayList<Double> arDoubKOplate = new ArrayList<Double>();
    Boolean mainChSwitch = true;
    String tempKol = "";
    JSONArray jsTovari = new JSONArray();
    ConstraintLayout constraintLayoutBotom;
    Button btKorzinaK_Pokupkam;
    JSONObject jsObjTempToSQL;
    JSONArray jsArrTempToSQL;
    ArrayList<String> arrListIdTovarovK_ZakazuSQL = new ArrayList();
    ArrayList<CheckBox> listCheckBox = new ArrayList();
    ArrayList<Ceni> listCeniObj = new ArrayList();
    int countShowToastOt70rub = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korzina);
        //arrListIdTovarovK_ZakazuSQL.add("12");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        jsObjTempToSQL = new JSONObject();
        jsArrTempToSQL = new JSONArray();
        scrollView = (ScrollView) findViewById(R.id.scrolTovari);
        tvKorzinaCount = findViewById(R.id.tvKorzinaCount);
        mainCheckBox = findViewById(R.id.chMain);
        constraintLayoutBotom = findViewById(R.id.conLayKorzinaBottom);
        btKupit = findViewById(R.id.btKupit);
        btKupit.setOnClickListener(this);
        btKorzinaK_Pokupkam = findViewById(R.id.btKorzinaK_Pokupkam);
        btKorzinaK_Pokupkam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), KategoriiPod.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tvKorzinaKOplateSkolkoVsego = findViewById(R.id.tvKorzinaKOplateSkolkoVsego);
        imageButtonDel = findViewById(R.id.ibKorzinaDelTovari);
        imageButtonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idDel = "";
                for (int y = 0; y < listCheckBox.size(); y++) {
                    if (listCheckBox.get(y).isChecked()) {
                        try {
                            idDel = idDel + "'" + jsTovari.getJSONObject(y).getString("id") + "',";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                idDel = idDel.substring(0, idDel.length() - 1);
                System.out.println("del str " + idDel);
                delTovariAllSQL(idDel);
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
                            JSONObject jsonObject = new JSONObject(response.toString());
                            System.out.println("jsObj Korzina " + jsonObject.toString());
                            JSONArray jsArrTovarV_Korzine = jsonObject.getJSONArray("korzina");
                            //  System.out.println(typeDostavki+" TYPE");
                            LinearLayout linLayout = (LinearLayout) findViewById(R.id.linerLayKorzina);
                            LayoutInflater layoutInflater = getLayoutInflater();
                            linLayout.removeAllViews();
                            if(jsArrTovarV_Korzine.length()==0) btKorzinaK_Pokupkam.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsArrTovarV_Korzine.length(); i++) {
                                if (i == 0) {
                                    mainCheckBox.setVisibility(View.VISIBLE);
                                }
                                JSONObject jsonRow = jsArrTovarV_Korzine.getJSONObject(i);
                                //System.out.println("ROW " + jsonRow);
//                    System.out.println("tovar v korzine  "+tovarNaimenovanie);
                                System.out.println("create objekt Cen in korzina");
                                final Ceni ceniRowObj=new Ceni(
                                        jsonRow.getString("naimenovanie"),
                                        jsonRow.getString("foto"),
                                        jsonRow.getString("cena"),
                                        jsonRow.getString("skidka"),
                                        jsonRow.getString("cenaskidka"),
                                        jsonRow.getString("selected"),
                                        jsonRow.getString("tovar"),
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
                                final View item = layoutInflater.inflate(R.layout.row_korzina, linLayout, false);
                                ImageView ivTovar = item.findViewById(R.id.ivTovar);
                                TextView tvRowTowarNaimenovanie = (TextView) item.findViewById(R.id.tvRowTowarNaimenovanie);
                                TextView tvRowTovarCena = (TextView) item.findViewById(R.id.tvRowTovarCena);
                                final EditText etRowKorzinaKolihestvoTovara = (EditText) item.findViewById(R.id.etRowKorzinaKolihestvoTovara);
                                final CheckBox chKorzina = item.findViewById(R.id.chKorzina);

                                listCheckBox.add(i, chKorzina);
                                listCeniObj.add(i, ceniRowObj);
                                chKorzina.setChecked(ceniRowObj.getIsSelected());
                                if (ceniRowObj.getIsSelected()) {
                                    doubleK_OplateSkolkoVsego = doubleK_OplateSkolkoVsego + ceniRowObj.getCenaZaUpakovky() * ceniRowObj.getKolihestvo();
                                    korzinaCount = korzinaCount + ceniRowObj.getKolihestvo();
                                    arDoubKOplate.add(ceniRowObj.getCenaFinalSoSkidkoy() * ceniRowObj.getKolihestvo());
                                }
                                Picasso.get().load(ceniRowObj.getFoto()).into(ivTovar);
                                tvRowTowarNaimenovanie.setText(ceniRowObj.getNaimenovanie());
                                tvRowTovarCena.setText(ceniRowObj.getCenaZaUpakovkyStr());
                                final String kolihestvoFormatedString = String.format("%.0f", ceniRowObj.getKolihestvo());


                                etRowKorzinaKolihestvoTovara.setText(kolihestvoFormatedString);
                                final int finalI = i;

                                //region etRowKorzinaKolihestvoTovara.setOnEditorActionListener
                                etRowKorzinaKolihestvoTovara.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                        if (actionId == Utils.etKolihestvoTovarovV_Korzine_ACTION_ID) {
                                            if (chKorzina.isChecked()) {
                                                korzinaCount = korzinaCount - ceniRowObj.getKolihestvo();
                                                doubleK_OplateSkolkoVsego = doubleK_OplateSkolkoVsego - ceniRowObj.getCenaZaUpakovky() * ceniRowObj.getKolihestvo();
                                                String chKol = etRowKorzinaKolihestvoTovara.getText().toString();
                                                ceniRowObj.setKolihestvo( Double.parseDouble(chKol));
                                                korzinaCount = korzinaCount + ceniRowObj.getKolihestvo();
                                                doubleK_OplateSkolkoVsego = doubleK_OplateSkolkoVsego + ceniRowObj.getCenaZaUpakovky() * ceniRowObj.getKolihestvo();
                                                tvKorzinaKOplateSkolkoVsego.setText(stringKorzinaKOplateSkolkoVsego());

                                                etRowKorzinaKolihestvoTovara.setText(chKol);
                                                sendKolih(ceniRowObj.getId_sql_tovara_v_baze(), MainActivity.userStatic.getSqlId(), chKol, chKorzina);
                                                krasniyShethik();

                                            } else {
                                                String chKol = etRowKorzinaKolihestvoTovara.getText().toString();
                                                ceniRowObj.setKolihestvo( Double.parseDouble(chKol));
                                                etRowKorzinaKolihestvoTovara.setText(chKol);
                                                sendKolih(ceniRowObj.getId_sql_tovara_v_baze(), MainActivity.userStatic.getSqlId(), chKol, chKorzina);
                                            }
                                            System.out.println("Action " + actionId + " ev= " + event);


                                            System.out.println("kolih = " + ceniRowObj.getKolihestvo() + ", kOpl= " + doubleK_OplateSkolkoVsego);

                                        }
                                        return false;
                                    }

                                });
                                //endregion

                                //region etRowKorzinaKolihestvoTovara.setOnFocusChangeListener
                                etRowKorzinaKolihestvoTovara.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View v, boolean hasFocus) {
                                        System.out.println("change focus " + etRowKorzinaKolihestvoTovara.getId() + " " + hasFocus);

                                        String pustStr = "";
                                        if (hasFocus == true) {
                                            tempKol = etRowKorzinaKolihestvoTovara.getText().toString();
                                            etRowKorzinaKolihestvoTovara.setText(pustStr);
                                        }
                                        if (hasFocus == false) {

                                            if (etRowKorzinaKolihestvoTovara.getText().toString().equals(pustStr)) {
                                                etRowKorzinaKolihestvoTovara.setText(tempKol);
                                                System.out.println("//////");
                                            } else {
                                                System.out.println(etRowKorzinaKolihestvoTovara.getText() + " gettext");
                                                ceniRowObj.setKolihestvo( Double.parseDouble(etRowKorzinaKolihestvoTovara.getText().toString()));
                                                krasniyShethik();
                                            }
                                        }
                                    }
                                });
                                //endregion

                                //region chKorzina.setOnCheckedChangeListener
                                chKorzina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        //TextView tvKOplate2 =findViewById(R.id.tvKorzinaKOplateSkolkoVsego);
                                        Boolean allCheck = true;
                                        Boolean allNotCheck = true;
                                        for (int c = 0; c < listCheckBox.size(); c++) {
                                            if (!listCheckBox.get(c).isChecked()) {
                                                allCheck = false;
                                                allNotCheck = false;
                                            }
                                        }
                                        if (allNotCheck || listCheckBox.size() == 0) {
                                            imageButtonDel.setVisibility(View.INVISIBLE);
                                            System.out.println("VIKLUHIT");
                                        } else {
                                            System.out.println("Propusk");
                                        }
                                        if (allCheck) {
                                            mainChSwitch = true;
                                            mainCheckBox.setChecked(true);
                                        } else {
                                            mainChSwitch = false;
                                            mainCheckBox.setChecked(false);
                                            mainChSwitch = true;
                                        }
                                        if (chKorzina.isChecked()) {
                                            imageButtonDel.setVisibility(View.VISIBLE);
                                            korzinaCount = korzinaCount + ceniRowObj.getKolihestvo();
                                            krasniyShethik();
                                            trueSelected(ceniRowObj.getId_sql_tovara_v_korzine_pokupatelia());
                                            doubleK_OplateSkolkoVsego = doubleK_OplateSkolkoVsego + ceniRowObj.getCenaZaUpakovky() * ceniRowObj.getKolihestvo();
                                            // System.out.println("kolih "+kolihestvo[0]+" cena "+finalCena+" doubleK_OplateSkolkoVsego "+doubleK_OplateSkolkoVsego);
                                            tvKorzinaKOplateSkolkoVsego.setText(stringKorzinaKOplateSkolkoVsego());
                                        } else {
                                            korzinaCount = korzinaCount - ceniRowObj.getKolihestvo();
                                            krasniyShethik();
                                            falseSelected(ceniRowObj.getId_sql_tovara_v_korzine_pokupatelia());
                                            doubleK_OplateSkolkoVsego = doubleK_OplateSkolkoVsego - ceniRowObj.getCenaZaUpakovky() * ceniRowObj.getKolihestvo();
                                            tvKorzinaKOplateSkolkoVsego.setText(stringKorzinaKOplateSkolkoVsego());
                                            // System.out.println("kolih "+kolihestvo[0]+" cena "+finalCena+" doubleK_OplateSkolkoVsego "+doubleK_OplateSkolkoVsego);
                                        }
                                    }
                                });
//endregion
                                final TextView tvTovarRowPlusOdinTovar = item.findViewById(R.id.tvTovarRowPlusOdinTovar);
                                //region tvTovarRowPlusOdinTovar.setOnClickListener
                                tvTovarRowPlusOdinTovar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        korzinaCount = korzinaCount + 1;
                                        krasniyShethik();
                                        chKorzina.setChecked(true);
                                        buyOneTovarSQL(ceniRowObj.getId_sql_tovara_v_baze(), MainActivity.userStatic.getSqlId());
                                        ceniRowObj.setKolihestvo( ceniRowObj.getKolihestvo() + 1);
                                        String formattedDouble = String.format("%.0f", ceniRowObj.getKolihestvo());
                                        etRowKorzinaKolihestvoTovara.setText(formattedDouble);
                                        if (chKorzina.isChecked()) {
                                            doubleK_OplateSkolkoVsego = doubleK_OplateSkolkoVsego + ceniRowObj.getCenaZaUpakovky();
                                            tvKorzinaKOplateSkolkoVsego.setText(stringKorzinaKOplateSkolkoVsego());
//System.out.println("stringKorzinaKOplateSkolkoVsego () "+kolihestvo[0]+" cena "+finalCena+" doubleK_OplateSkolkoVsego "+doubleK_OplateSkolkoVsego);
                                        }
                                    }
                                });
//endregion
                                TextView tvTovarRowMinusOdinTovar = item.findViewById(R.id.tvTovarRowMinusOdinTovar);
                                //region tvTovarRowMinusOdinTovar.setOnClickListener
                                tvTovarRowMinusOdinTovar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ceniRowObj.getKolihestvo() > 1) {
                                            delTovarOneSQL(ceniRowObj.getId_sql_tovara_v_baze(), MainActivity.userStatic.getSqlId());
                                            ceniRowObj.setKolihestvo( ceniRowObj.getKolihestvo() - 1);
                                            String formattedDouble = String.format("%.0f", ceniRowObj.getKolihestvo());
                                            etRowKorzinaKolihestvoTovara.setText(formattedDouble);
                                            krasniyShethik();
                                            TextView tvKOplate2 = findViewById(R.id.tvKorzinaKOplateSkolkoVsego);
                                            if (chKorzina.isChecked()) {
                                                korzinaCount = korzinaCount - 1;
                                                String korzCountFormat = String.format("%, (.0f", korzinaCount);
                                                tvKorzinaCount.setText(korzCountFormat);
                                                doubleK_OplateSkolkoVsego = doubleK_OplateSkolkoVsego - ceniRowObj.getCenaZaUpakovky();
                                                // System.out.println("kolih "+kolihestvo[0]+" cena "+finalCena+" doubleK_OplateSkolkoVsego "+doubleK_OplateSkolkoVsego);
                                                tvKOplate2.setText(stringKorzinaKOplateSkolkoVsego());
                                            }
                                        }
                                    }
                                });
                                //endregion
                                item.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //createIntentAdres(id);
                                    }
                                });
                                jsTovari.put(i, jsonRow);
                                linLayout.addView(item);
                            }
                            //region Включить главный чекбокс , если выбраны все товары
                            boolean chAllShowIn = true;
                            for (int b = 0; b < listCheckBox.size(); b++) {
                                if (!listCheckBox.get(b).isChecked()) {
                                    chAllShowIn = false;
                                }
                            }
                            if (chAllShowIn) {
                                mainCheckBox.setChecked(true);
                            }
                            //endregion
                            krasniyShethik();
                            tvKorzinaKOplateSkolkoVsego.setText(stringKorzinaKOplateSkolkoVsego());
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    // scrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        } catch (JSONException e) {
                            System.out.println("\n ERR 1 ShowSQL in KorzinaActivity "
                                    +e.toString()+"\n"
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
                parameters.put("pokupatel", MainActivity.userStatic.getSqlId());
                System.out.println("Korzina showSQL send param " + parameters.toString());
                return parameters;
            }
        };
        mainCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mainChSwitch) {
                    if (mainCheckBox.isChecked()) {
                        for (int z = 0; z < listCheckBox.size(); z++) {
                            listCheckBox.get(z).setChecked(true);
                        }
                    } else {
                        for (int z = 0; z < listCheckBox.size(); z++) {
                            listCheckBox.get(z).setChecked(false);
                        }
                    }
                }
            }
        });
        requestQueue.add(stringRequest);
    }

    private String stringKorzinaKOplateSkolkoVsego() {
        return Utils.getStringFromDoubleFormated(doubleK_OplateSkolkoVsego) + " руб.";
    }

    void krasniyShethik() {
        String korzCountFormat = String.format("%, .0f", korzinaCount);
        tvKorzinaCount.setText(korzCountFormat);
        int korzCountTemp = Integer.valueOf(korzinaCount.intValue());
        MainActivity.userStatic.setkorzina_count_INT(korzCountTemp, tvKorzinaCount);
        tvKorzinaKOplateSkolkoVsego.setText(stringKorzinaKOplateSkolkoVsego());
        if (korzinaCount == 0) {
            tvKorzinaCount.setVisibility(View.INVISIBLE);
            constraintLayoutBotom.setVisibility(View.GONE);
            imageButtonDel.setVisibility(View.INVISIBLE);
            mainCheckBox.setChecked(false);

        } else {
            tvKorzinaCount.setVisibility(View.VISIBLE);
            constraintLayoutBotom.setVisibility(View.VISIBLE);
            imageButtonDel.setVisibility(View.VISIBLE);
        }
    }
    void buyOneTovarSQL(final String tovar, final String pokupatel) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_KOLVO_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            System.out.println("\n ERR buy SQL" + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("kolvo", "1");
                parameters.put("tovar", tovar);
                parameters.put("pokupatel", pokupatel);
                // System.out.println("BUY "+tovar+" "+pokupatel);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }
    void sendKolih(final String tovar, final String pokupatel, final String count, final CheckBox checkBox) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEND_COUNT_TOVARA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String jsonArray = jsonObject.getString("error");
                            System.out.println(jsonArray);
                            if (!checkBox.isChecked()) {
                                checkBox.setChecked(true);
                                System.out.println("set cheBox");
                            } else {
                                System.out.println("no ch Box " + checkBox.toString());
                            }
                        } catch (JSONException e) {

                            System.out.println("\n ERR Send kolih" + response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("tovar", tovar);
                parameters.put("count", count);
                parameters.put("pokupatel", pokupatel);
                System.out.println(tovar + " " + count + " " + pokupatel);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }
    void delTovarOneSQL(final String tovar, final String pokupatel) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DEL_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            System.out.println("\n ERR Del SQL" + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("tovar", tovar);
                parameters.put("pokupatel", pokupatel);
                // System.out.println("BUY "+tovar+" "+pokupatel);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }
    void delTovariAllSQL(final String tovari) {
        final Intent intent = new Intent(this, KorzinaActivity.class);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DEL_TOVAR_ALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            startActivity(intent);
                            MainActivity.userStatic.setkorzina_count_INT(0, tvKorzinaCount);
                            finish();
                        } catch (JSONException e) {
                            System.out.println("\n ERR Del TovariActivity" + response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("tovari", tovari);
                parameters.put("pokupatel", MainActivity.userStatic.getSqlId());
                System.out.println("Param  " + parameters);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }
    void trueSelected(final String idKorzina) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.TRUE_SELECTED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                        } catch (JSONException e) {
                            System.out.println("\n ERR True sel" + response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("idKorzina", idKorzina);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }
    void falseSelected(final String idKorzina) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.FALSE_SELECTED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                        } catch (JSONException e) {
                            System.out.println("\n ERR False sel" + response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("idKorzina", idKorzina);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }
    // btKupit
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btKupit:
                if(doubleK_OplateSkolkoVsego>=70.0) {
                    Intent intent = new Intent(this, ZakazActivity.class);
                    intent.putExtra("typeDostavki", typeDostavki);
                    intent.putExtra("adresPost", adresPost);
                    intent.putExtra("adresPunkt", adresPunkt);
                    intent.putExtra("ceniList", listCeniObj);
                    System.out.println("KorzActiv arrlist size = " + Integer.toString(arrListIdTovarovK_ZakazuSQL.size()));
                    System.out.println("KorzActiv put Bundle Ceni Obj ID==" + listCeniObj.get(0).getId_sql_tovara_v_baze());
                    startActivityForResult(intent, Utils.intentRequestCODKorzinaToZakazPosleOtpravkiZakaza);
                    break;
                }else{
                    if(countShowToastOt70rub==0) {
                        Toast.makeText(this, R.string.zakaz_ot_70_rub, Toast.LENGTH_LONG).show();
                        countShowToastOt70rub++;
                    }
                    else {
                        final MyDialog myDialoge = new MyDialog(KorzinaActivity.this);
                        myDialoge.showDialog(  getString(R.string.zakaz_ot_70_rub));
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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Utils.intentRequestCODKorzinaToZakazPosleOtpravkiZakaza){
            if(resultCode==Utils.intentResultCODKorzinaToZakazPosleOtpravkiZakaza_OK) {
                finish();
                Toast.makeText(this, getString(R.string.spasibo_zakaz), Toast.LENGTH_LONG).show();}
            else{
                onRestart();
            }
        }

    }
}
