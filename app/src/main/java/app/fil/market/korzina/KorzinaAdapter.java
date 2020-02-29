package app.fil.market.korzina;


import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.fil.market.MainActivity;
import app.fil.market.R;
import app.fil.market.ui.tovari.TovariSpisokAdapter;
import app.fil.market.Model.Utils;
import app.fil.market.ui.tovari.TovarFromSQL;

class ItemViewHolder extends RecyclerView.ViewHolder {

    CheckBox chKorzina;
    ImageView ivRowTovarFoto;
    TextView tvRowTowarNaimenovanie, tvTovarRowMinusOdinTovar, tvTovarRowPlusOdinTovar, tvRowKorzinaCena;
    EditText etRowKorzinaKolihestvoTovara;




    public ItemViewHolder(View itemView) {
        super(itemView);
        chKorzina = itemView.findViewById(R.id.chKorzina);
        ivRowTovarFoto = itemView.findViewById(R.id.ivRowTovarFoto);
        tvRowTowarNaimenovanie = itemView.findViewById(R.id.tvRowTowarNaimenovanie);
        tvTovarRowMinusOdinTovar = itemView.findViewById(R.id.tvTovarRowMinusOdinTovar);
        tvTovarRowPlusOdinTovar = itemView.findViewById(R.id.tvTovarRowPlusOdinTovar);
        tvRowKorzinaCena = itemView.findViewById(R.id.tvRowKorzinaCena);
        etRowKorzinaKolihestvoTovara = itemView.findViewById(R.id.etRowKorzinaKolihestvoTovara);

    }
}

public class KorzinaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Double totalK_OplateDoubl = 0.0;

    ConstraintLayout conLayKorzinaBottom;
    CheckBox mainCheckBox;
    Button btKorzinaK_PokupkamObj;

    byte chMainNeverSelfPres = 0;// 1-self 2-othersimpleChBoxes
    byte chMainChangeHistory = 0; //0-never mainCheckBox presed, 1-false pres Self, 2-true pres Self,
                                                                //3-false other chBox, 4-true other chBox
                                                                // что бы неподгруженные назначить согласно главному чекбоксу

    Activity activity;
    TextView tvKorzinaKOplateSkolkoVsego, tvCountKorzinaObj;
    ImageButton ibKorzinaDelTovari;
    List<CheckBox> chBoxesList;


    public KorzinaAdapter(TextView tvKorzinaKOplateSkolkoVsego,
                          TextView tvCountKorzinaObj,
                          final ConstraintLayout conLayKorzinaBottom,
                          final ImageButton ibKorzinaDelTovari,
                          final CheckBox mainCheckBox,
                          final Button btKorzinaK_Pokupkam,
                          final Activity activity) {
        this.activity = activity;
        this.tvKorzinaKOplateSkolkoVsego = tvKorzinaKOplateSkolkoVsego;
        this.conLayKorzinaBottom = conLayKorzinaBottom;
        this.mainCheckBox = mainCheckBox;
        this.btKorzinaK_PokupkamObj = btKorzinaK_Pokupkam;
        this.ibKorzinaDelTovari = ibKorzinaDelTovari;
        this.chBoxesList = new ArrayList<>();
        mainCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("mainCH click="+mainCheckBox.isChecked());
                for ( CheckBox chL: chBoxesList){
                    chL.setChecked(mainCheckBox.isChecked());
                }
                for (TovarFromSQL cnL: KorzinaActivity.tovariList2){
                    cnL.setVibranLiAndSendSQL(mainCheckBox.isChecked());
                }
            }
        });
        mainCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println("mainCH change");
            }
        });
        ibKorzinaDelTovari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList <Integer> delListItems=new ArrayList();
                for(int i=0; i<KorzinaActivity.tovariList2.size(); i++){
                    if(KorzinaActivity.tovariList2.get(i).getIsSelected()){
                        delTovarIzKorzini(KorzinaActivity.tovariList2.get(i).getId_sql_tovara_v_korzine_pokupatelia(), ibKorzinaDelTovari);
                        delListItems.add(i);
                        MainActivity.pokupatelStatic.setKorzinaCountStr(Integer.toString(MainActivity.pokupatelStatic.getKorzina_kountInt()-KorzinaActivity.tovariList2.get(i).getKolihestvo()));
                        TovariSpisokAdapter.deletedItemsSQLId.add(KorzinaActivity.tovariList2.get(i).getId_sql_tovara_v_baze());
                    }
                }

                for(int i=0; i<delListItems.size(); i++){
                    KorzinaActivity.tovariList2.remove(delListItems.get(i)-i);
                   notifyItemRemoved(delListItems.get(i)-i);
                    System.out.println("items size = "+KorzinaActivity.tovariList2.size()+", i="+i+", pos items="+delListItems.get(i));
                }

                mainCheckBox.setChecked(false);
                if(KorzinaActivity.tovariList2.size()==0) {
                    btKorzinaK_Pokupkam.setVisibility(View.VISIBLE);
                    conLayKorzinaBottom.setVisibility(View.INVISIBLE);
                }
                for(TovarFromSQL itL: KorzinaActivity.tovariList2){
                    itL.setVibranLiAndSendSQL(true);
                }
                for (CheckBox chL: chBoxesList){
                    chL.setChecked(true);
                }
                rashetTotalK_oplate();

            }
        });
        System.out.println("items size = NEW CONSTRUCTOR");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_korzina, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final TovarFromSQL item = KorzinaActivity.tovariList2.get(position);
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;

            viewHolder.tvRowTowarNaimenovanie.setText(item.getNaimenovanie());
            Picasso.get().load(item.getFoto()).into(viewHolder.ivRowTovarFoto);
            viewHolder.tvRowKorzinaCena.setText(item.getCenaFinalSoSkidkoyZaUpakStr());
            viewHolder.etRowKorzinaKolihestvoTovara.setText(item.getKolihestvoStr());

            if (chMainNeverSelfPres == 0)//chMain never presed
            {
                viewHolder.chKorzina.setChecked(item.getIsSelected());
                if(!item.getIsSelected()){//выключить chMain, если хоть один товар не выбран
                    chMainChangeHistory=3; //chMain set False other checkBox
                    mainCheckBox.setChecked(false);
                }

            }
            chBoxesList.add(viewHolder.chKorzina);
            viewHolder.chKorzina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    item.setVibranLiAndSendSQL(b);
                    rashetTotalK_oplate();
                    if(!b){
                        mainCheckBox.setChecked(b);
                    }else{
                        boolean allTrue = true;
                        for (TovarFromSQL cnL: KorzinaActivity.tovariList2){
                            if(!cnL.getIsSelected()){
                                allTrue=false;
                            }
                        }
                        if(allTrue){
                            mainCheckBox.setChecked(true);
                        }
                    }


                }
            });
            viewHolder.etRowKorzinaKolihestvoTovara.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {//нажали кнопку Enter в клавиатуре
                    if (viewHolder.etRowKorzinaKolihestvoTovara.getText().toString().matches("[-+]?\\d+")) {
                        if(Integer.valueOf(viewHolder.etRowKorzinaKolihestvoTovara.getText().toString())>0) {
                            item.setKolihestvoStr(viewHolder.etRowKorzinaKolihestvoTovara.getText().toString());
                            System.out.println("Et set = (if)" + item.getKolihestvoStr() + ",  " + actionId + ",  ");
                            item.setVibranLiAndSendSQL(true);
                        }
                    } else {
                        System.out.println("Et set = (else)");
                    }
                    item.setVibranLiAndSendSQL(true);
                    viewHolder.chKorzina.setChecked(true);
                    rashetTotalK_oplate();
                    conLayKorzinaBottom.setVisibility(View.VISIBLE);
                    return false;
                }
            });
            viewHolder.etRowKorzinaKolihestvoTovara.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    System.out.println("Focus change " + b);
                    if (b) {//нажали на editText
                        viewHolder.etRowKorzinaKolihestvoTovara.setText("");
                        conLayKorzinaBottom.setVisibility(View.INVISIBLE);
                    } else//убрался фокус с editText
                    {
                        conLayKorzinaBottom.setVisibility(View.VISIBLE);
                        if (viewHolder.etRowKorzinaKolihestvoTovara.getText().toString().matches("[-+]?\\d+")) { //ввели цифру число
                            if(Integer.valueOf(viewHolder.etRowKorzinaKolihestvoTovara.getText().toString())>0) {
                                item.setKolihestvoStr(viewHolder.etRowKorzinaKolihestvoTovara.getText().toString());
                                item.setVibranLiAndSendSQL(true);
                                System.out.println("Et set ()if et789= " + item.getKolihestvoStr());
                            }
                        } else { //ввели не цифру или ничего
                            viewHolder.etRowKorzinaKolihestvoTovara.setText(item.getKolihestvoStr());
                            System.out.println("Et set  ()else et789");
                        }
                        item.setVibranLiAndSendSQL(true);
                        viewHolder.chKorzina.setChecked(true);
                        rashetTotalK_oplate();
                    }
                }
            });
            if (position == KorzinaActivity.tovariList2.size() - 1) {
                rashetTotalK_oplate();
                conLayKorzinaBottom.setVisibility(View.VISIBLE);
                ibKorzinaDelTovari.setVisibility(View.VISIBLE);
            }
            viewHolder.tvTovarRowMinusOdinTovar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(item.getKolihestvo()>1){
                        item.setKolihestvoAndSendToSQL(item.getKolihestvo()-1);
                        viewHolder.etRowKorzinaKolihestvoTovara.setText(item.getKolihestvoStr());
                        item.setVibranLiAndSendSQL(true);
                        viewHolder.chKorzina.setChecked(true);
                        rashetTotalK_oplate();
                        MainActivity.pokupatelStatic.setKorzinaCountStr(MainActivity.pokupatelStatic.getKorzina_kountInt()-1);
                    }
                }
            });
            viewHolder.tvTovarRowPlusOdinTovar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setKolihestvoAndSendToSQL(item.getKolihestvo()+1);
                    viewHolder.etRowKorzinaKolihestvoTovara.setText(item.getKolihestvoStr());
                    item.setVibranLiAndSendSQL(true);
                    viewHolder.chKorzina.setChecked(true);
                    rashetTotalK_oplate();
                    MainActivity.pokupatelStatic.setKorzinaCountStr(MainActivity.pokupatelStatic.getKorzina_kountInt()+1);

                }
            });


        }

    }



    private void rashetTotalK_oplate() {
        totalK_OplateDoubl = 0.0;
        for (int i = 0; i < KorzinaActivity.tovariList2.size(); i++) {
            if (KorzinaActivity.tovariList2.get(i).getIsSelected()) {
                totalK_OplateDoubl = totalK_OplateDoubl + KorzinaActivity.tovariList2.get(i).getCenaFinalSoSkidkoyZaUpak()*
                        KorzinaActivity.tovariList2.get(i).getKolihestvo();
            }
        }
        tvKorzinaKOplateSkolkoVsego.setText(Utils.getStringFromDoubleFormated2Zerro(totalK_OplateDoubl) + " руб.");
        System.out.println("Suum TATAl = " + Double.toString(totalK_OplateDoubl));
    }

    @Override
    public int getItemCount() {
        return KorzinaActivity.tovariList2.size();
    }
    void delTovarIzKorzini(final String id_sql_tovara_v_korzine_pokupatelia, ImageButton ibKorzinaDelTovari) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DEL_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sum_korzina=jsonObject.getString("sum_korzina");
                            MainActivity.pokupatelStatic.setKorzinaCountStr(sum_korzina);
                            System.out.println("TovarFromSQL delTovarIzKorzini = " + jsonObject.toString());

                        } catch (JSONException e) {

                            System.out.println("\n TovarFromSQL ERR delTovarIzKorzini = " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("\n TovarFromSQL ERR delTovarIzKorzini - " + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("idk", id_sql_tovara_v_korzine_pokupatelia);
                System.out.println("TovarFromSQL delTovarIzKorzini  SQL parametrs = " + parameters);

                return parameters;
            }
        };

        Volley.newRequestQueue(ibKorzinaDelTovari.getContext()).add(stringRequest);
    }

}



