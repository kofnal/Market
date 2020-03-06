package app.fil.market.ui.opisanieTovara;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import app.fil.market.BokovoeMenu;
import app.fil.market.MainActivity;
import app.fil.market.R;
import app.fil.market.korzina.KorzinaActivity;
import app.fil.market.ui.podkategorii.PodkategoriiFragment;
import app.fil.market.ui.tovari.TovariSpisokAdapter;
import app.fil.market.ui.tovari.TovariFragment;

public class OpisanieTovaraFragment extends Fragment {

    private OpisanieTovaraViewModel opisanieTovaraViewModel;
    String TAGclass="TovarOpisanieActivity ";
//    RequestQueue requestQueue;
    TextView tvOpisVKorzinu, tvKorzinaCount, tvOpisanieOformit, tvopisMinusTovar;
    EditText etKolvo;
//    ImageButton ibOpisanieKorzina;
    ScrollView scrlOpisanie;
    TextView tvNazvanie, tvOpisCenaTovara, tvOpisZaEdinicuIzmer, tvOpisZnahenieVesaUpakovki,tvOpisZnahenieRazmernogoRiada,
            tvOpisanieZnahenieMestaVilova, tvOpisTypeFasovki, tvOpisanieTovaraRaskaz, tvOpisTovaraCenaZaEdinicu, tvOpisVesUpakPodEditText,
            tvOpisRazmerniyRiad, tvOpisanieMestoVilova;
    ImageView ivOpisTovar;
    //    TovarFromSQL tovarFromSQLObjFromTovariActivity;
//    int indexTovaraDliOpisania;
//    public static PoiskAdapter adapter;
//    public static ArrayList<TovarFromSQL> listTovarovSQLfromAdapterRV = new ArrayList<>();
//    final public static int countLoadItems = 10;
//    String vetkaId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        opisanieTovaraViewModel =
                ViewModelProviders.of(this).get(OpisanieTovaraViewModel.class);
        View root = inflater.inflate(R.layout.activity_tovar_opisanie, container, false);
//        requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Intent intentKorzina = new Intent(getContext(), KorzinaActivity.class);
        tvOpisVKorzinu = root.findViewById(R.id.tvOpisVKorzinu);
//        ibOpisanieKorzina = root.findViewById(R.id.ibOpisanieKorzina);
        tvKorzinaCount = root.findViewById(R.id.tvKorzinaCount);
        tvOpisanieOformit = root.findViewById(R.id.tvOpisanieOformit);
        etKolvo = root.findViewById(R.id.etOpisInputCountTovar);
        tvopisMinusTovar= root.findViewById(R.id.tvopisMinusTovar);
        scrlOpisanie= root.findViewById(R.id.scrlOpisanie);
        tvNazvanie=root.findViewById(R.id.tvOpisNazvanieTovara);
        ivOpisTovar=root.findViewById(R.id.ivOpisTovar);
        tvOpisCenaTovara=root.findViewById(R.id.tvOpisCenaTovara);
        tvOpisZaEdinicuIzmer=root.findViewById(R.id.tvOpisZaEdinicuIzmer);
        tvOpisZnahenieVesaUpakovki=root.findViewById(R.id.tvOpisZnahenieVesaUpakovki);
        tvOpisZnahenieRazmernogoRiada=root.findViewById(R.id.tvOpisZnahenieRazmernogoRiada);
        tvOpisanieZnahenieMestaVilova=root.findViewById(R.id.tvOpisanieZnahenieMestaVilova);
        tvOpisTypeFasovki=root.findViewById(R.id.tvOpisTypeFasovki);
        tvOpisanieTovaraRaskaz=root.findViewById(R.id.tvOpisanieTovaraRaskaz);
        tvOpisTovaraCenaZaEdinicu=root.findViewById(R.id.tvOpisTovaraCenaZaEdinicu);
        tvOpisVesUpakPodEditText=root.findViewById(R.id.tvOpisVesUpakPodEditText);
        tvOpisRazmerniyRiad=root.findViewById(R.id.tvOpisRazmerniyRiad);
        tvOpisanieMestoVilova=root.findViewById(R.id.tvOpisanieMestoVilova);
        TextView tvOpisPlusTovar= root.findViewById(R.id.tvOpisPlusTovar);

//        indexTovaraDliOpisania = getArguments().getInt("ceniObj");
        tvopisMinusTovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tvopisMinusTovarTemp = Integer.parseInt(etKolvo.getText().toString())-1;
                etKolvo.setText(Integer.toString(tvopisMinusTovarTemp));
            }
        });
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
                opisanieTovaraViewModel.buySQL(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getId_sql_tovara_v_baze(), etKolvo.getText().toString(),
                        MainActivity.pokupatelStatic.getSqlId());

            }
        });
//        ibOpisanieKorzina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(intentKorzina);
//            }
//        });
        tvOpisanieOformit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentKorzina);
            }
        });
        tvNazvanie.setText(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getNaimenovanie());
        Picasso.get().load(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getFoto()).into(ivOpisTovar);
        tvOpisCenaTovara.setText(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getCenaZaOdinKgStr());
        tvOpisZnahenieVesaUpakovki.setText(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getKolihestvoV_Upakovke()+" "+
                PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getEdinica_izmerenia_upakovki());
        tvOpisZnahenieRazmernogoRiada.setText(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getZnahrazmriada());
        tvOpisanieZnahenieMestaVilova.setText(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getMestovilova());
        tvOpisTypeFasovki.setText(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getSostoianieTovara());
        tvOpisanieTovaraRaskaz.setText(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getRaskazotovare());
        tvOpisTovaraCenaZaEdinicu.setText("("+ PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getCenaFinalSoSkidkoyZaUpakStr()+" руб. за "+
                PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getTypeUpakovki()+")");
        tvOpisVesUpakPodEditText.setText("("+ PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getKolihestvoV_Upakovke()+" "+
                PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getEdinica_izmerenia_upakovki()+")");
        tvOpisZaEdinicuIzmer.setText("за 1 "+ PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getEdinica_izmerenia_upakovki());
        if(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getZnahrazmriada().equals("")) tvOpisRazmerniyRiad.setVisibility(View.GONE);
        if(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(TovariSpisokAdapter.indexTovaraDliaOpisanieFragment).getMestovilova().equals("")) tvOpisanieMestoVilova.setVisibility(View.GONE);

//        ibKorzina = getActivity().findViewById(R.id.ibOpisanieKorzina);
//        final RecyclerView rvTovariFragm = root.findViewById(R.id.rvTovariFragm);
//        rvTovariFragm.setLayoutManager(new LinearLayoutManager(root.getContext()));
//        adapter = new PoiskAdapter(rvTovariFragm, getActivity());
//        rvTovariFragm.setAdapter(adapter);
//        listTovarovSQLfromAdapterRV.clear();
//        final TextView tvTemp=getActivity().findViewById(R.id.tvBokovI_Find);
//        tvTemp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                adapter.notifyDataSetChanged();
//                System.out.println("notifyDataSetChanged from tvTemp");
//                tvTemp.setText(Integer.toString(adapter.getItemCount()));
//            }
//        });



//        opisanieTovaraViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        vetkaId = getArguments().getString("vetka");
//        System.out.println("getArguments "+vetkaId);
//        opisanieTovaraViewModel.showSQL(0,10, tvKorzinaCount,   rvTovariFragm, vetkaId);
        BokovoeMenu.etPoiskActivaciya(false);
        return root;
    }
}