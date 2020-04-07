package app.fil.market.ui.tovari;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.fil.market.BokovoeMenu;
import app.fil.market.Model.ILoadMore;
import app.fil.market.R;
import app.fil.market.ui.podkategorii.PodkategoriiFragment;
import app.fil.market.ui.poisk.PoiskFragment;

public class TovariFragment extends Fragment {

    private TovariViewModel tovariViewModel;
//    TextView tvKorzinaCount;
    public static TovariSpisokAdapter adapter;
//    public static ArrayList<TovarFromSQL> listTovarovSQLfromAdapterRV = new ArrayList<>();
    final public static int countLoadItems = 10;
//    String vetkaId;
    public static String vetkaIdprevios=" ";



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tovariViewModel =
                ViewModelProviders.of(this).get(TovariViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tovari, container, false);
//        tvKorzinaCount = getActivity().findViewById(R.id.tvKorzinaCount);
//        ibKorzina = getActivity().findViewById(R.id.ibOpisanieKorzina);
        System.out.println("TovariActiv PodkategoriiFragment.podkatVetkaId= "+PodkategoriiFragment.podkatVetkaId);
        System.out.println("TovariActiv PodkategoriiFragment.vetkaIdprevios= "+vetkaIdprevios);
        for(int i =0; i<PodkategoriiFragment.listTovarovSQLfromAdapterRV.size(); i++){
//            for(int y=0; y<PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(i).size(); y++){
                System.out.println("Arr "+i+" size = "+PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(i).size());
//            }
        }
        final RecyclerView rvTovariFragm = root.findViewById(R.id.rvTovariFragm);
        rvTovariFragm.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new TovariSpisokAdapter(rvTovariFragm, getActivity());
        rvTovariFragm.setAdapter(adapter);




//        tovariViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        vetkaId = getArguments().getString("vetka");
//        System.out.println("getArguments "+vetkaId);


        if(PodkategoriiFragment.firstStartListBool.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId))){
            PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).add(null);

                TovariFragment.adapter.isLoading=true;
            TovariFragment.adapter.notifyDataSetChanged();
            tovariViewModel.showSQL(0,countLoadItems,    rvTovariFragm);
            adapter.isLoading=true;

            PodkategoriiFragment.firstStartListBool.set(Integer.valueOf(PodkategoriiFragment.podkatVetkaId), false);
            vetkaIdprevios= PodkategoriiFragment.podkatVetkaId;
            System.out.println("firstStart if");

        } else if (!PodkategoriiFragment.firstStartListBool.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId))
                & PodkategoriiFragment.podkatVetkaId.equals(vetkaIdprevios)){
            System.out.println("firstStart else if "+PodkategoriiFragment.podkatVetkaId+", "+vetkaIdprevios);

        }
//        else{
//            System.out.println("firstStart else "+ PoiskFragment.podkatVetkaId+", "+vetkaIdprevios+")");
//            listTovarovSQLfromAdapterRV.clear();
//            tovariViewModel.showSQL(0,10,    rvTovariFragm);
//            vetkaIdprevios=PoiskFragment.podkatVetkaId;
//        }
        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                int index;
//                PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).add(null);
//                adapter.notifyItemInserted(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size() - 1);

try {
    if (PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).get(
            PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size() - 1) == null) {
//                    PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).remove(
//                            PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size() - 1);
//                    TovariFragment.adapter.notifyDataSetChanged();
        index = PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size() - 1;
    } else {
        index = PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size();
    }
//
//
//                adapter.notifyItemRemoved(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size());
    System.out.println("Otpravka na server iz TovariActiv arr size = " + PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size());
    for (int i = 0; i < PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).
            size(); i++) {
        try {
            System.out.println("Otpravka na server iz TovariActiv arrsize= " +
                    Integer.toString(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size()) +
                    ", id=" + PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).
                    get(i).id_sql_tovara_v_baze);
        } catch (Exception e) {
            System.out.println("Otpravka na server iz TovariActiv arrsize= null");
        }
    }


    int end = index + countLoadItems;
    if (!adapter.finishLoading) tovariViewModel.showSQL(index, end, rvTovariFragm);
} catch (Exception e){

}
            }
        });
        BokovoeMenu.etPoiskActivaciya(false);
        return root;
    }
}