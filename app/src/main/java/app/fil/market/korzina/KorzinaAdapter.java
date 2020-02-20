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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.fil.market.MainActivity;
import app.fil.market.R;
import app.fil.market.TovariSpisokAdapter;
import app.fil.market.Utils;
import app.fil.market.ceni_i_skidki.Ceni;

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
    ArrayList<Ceni> itemsObj;
    List<CheckBox> chBoxesList;


    public KorzinaAdapter(TextView tvKorzinaKOplateSkolkoVsego,
                          TextView tvCountKorzinaObj,
                          final ConstraintLayout conLayKorzinaBottom,
                          final ImageButton ibKorzinaDelTovari,
                          final CheckBox mainCheckBox,
                          final Button btKorzinaK_Pokupkam,
                          final Activity activity, final ArrayList<Ceni> items) {
        this.activity = activity;
        this.itemsObj = items;
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
                for (Ceni cnL: itemsObj){
                    cnL.setVibranLi(mainCheckBox.isChecked());
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
                for(int i=0; i<itemsObj.size(); i++){
                    if(itemsObj.get(i).getIsSelected()){
                        delTovarIzKorzini(itemsObj.get(i).getId_sql_tovara_v_korzine_pokupatelia());
                        delListItems.add(i);
                        MainActivity.userStatic.setKorzinaCountStr(Integer.toString(MainActivity.userStatic.getKorzina_kountInt()-itemsObj.get(i).getKolihestvo()));
                        TovariSpisokAdapter.deletedItemsSQLId.add(itemsObj.get(i).getId_sql_tovara_v_baze());
                    }
                }

                for(int i=0; i<delListItems.size(); i++){
                   itemsObj.remove(delListItems.get(i)-i);
                   notifyItemRemoved(delListItems.get(i)-i);
                    System.out.println("items size = "+itemsObj.size()+", i="+i+", pos items="+delListItems.get(i));
                }

                mainCheckBox.setChecked(false);
                if(itemsObj.size()==0) {
                    btKorzinaK_Pokupkam.setVisibility(View.VISIBLE);
                    conLayKorzinaBottom.setVisibility(View.INVISIBLE);
                }
                for(Ceni itL: itemsObj){
                    itL.setVibranLi(true);
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
            final Ceni item = itemsObj.get(position);
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
                    item.setVibranLi(b);
                    rashetTotalK_oplate();
                    if(!b){
                        mainCheckBox.setChecked(b);
                    }else{
                        boolean allTrue = true;
                        for (Ceni cnL: itemsObj){
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
                            item.setVibranLi(true);
                        }
                    } else {
                        System.out.println("Et set = (else)");
                    }
                    item.setVibranLi(true);
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
                                item.setVibranLi(true);
                                System.out.println("Et set ()if et789= " + item.getKolihestvoStr());
                            }
                        } else { //ввели не цифру или ничего
                            viewHolder.etRowKorzinaKolihestvoTovara.setText(item.getKolihestvoStr());
                            System.out.println("Et set  ()else et789");
                        }
                        item.setVibranLi(true);
                        viewHolder.chKorzina.setChecked(true);
                        rashetTotalK_oplate();
                    }
                }
            });
            if (position == itemsObj.size() - 1) {
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
                        item.setVibranLi(true);
                        viewHolder.chKorzina.setChecked(true);
                        rashetTotalK_oplate();
                        MainActivity.userStatic.setKorzinaCountStr(MainActivity.userStatic.getKorzina_kountInt()-1);
                    }
                }
            });
            viewHolder.tvTovarRowPlusOdinTovar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setKolihestvoAndSendToSQL(item.getKolihestvo()+1);
                    viewHolder.etRowKorzinaKolihestvoTovara.setText(item.getKolihestvoStr());
                    item.setVibranLi(true);
                    viewHolder.chKorzina.setChecked(true);
                    rashetTotalK_oplate();
                    MainActivity.userStatic.setKorzinaCountStr(MainActivity.userStatic.getKorzina_kountInt()+1);

                }
            });


        }

    }



    private void rashetTotalK_oplate() {
        totalK_OplateDoubl = 0.0;
        for (int i = 0; i < itemsObj.size(); i++) {
            if (itemsObj.get(i).getIsSelected()) {
                totalK_OplateDoubl = totalK_OplateDoubl + itemsObj.get(i).getCenaFinalSoSkidkoyZaUpak()*
                        itemsObj.get(i).getKolihestvo();
            }
        }
        tvKorzinaKOplateSkolkoVsego.setText(Utils.getStringFromDoubleFormated2Zerro(totalK_OplateDoubl) + " руб.");
        System.out.println("Suum TATAl = " + Double.toString(totalK_OplateDoubl));
    }

    @Override
    public int getItemCount() {
        return itemsObj.size();
    }
    void delTovarIzKorzini(final String id_sql_tovara_v_korzine_pokupatelia) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DEL_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sum_korzina=jsonObject.getString("sum_korzina");
                            MainActivity.userStatic.setKorzinaCountStr(sum_korzina);
                            System.out.println("Ceni delTovarIzKorzini = " + jsonObject.toString());

                        } catch (JSONException e) {

                            System.out.println("\n Ceni ERR delTovarIzKorzini = " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("\n Ceni ERR delTovarIzKorzini - " + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("idk", id_sql_tovara_v_korzine_pokupatelia);
                System.out.println("Ceni delTovarIzKorzini  SQL parametrs = " + parameters);

                return parameters;
            }
        };

        MainActivity.requestQueue.add(stringRequest);
    }

}



