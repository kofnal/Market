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
        final RecyclerView rvTovariFragm = root.findViewById(R.id.rvTovariFragm);
        rvTovariFragm.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new TovariSpisokAdapter(rvTovariFragm, getActivity());
        rvTovariFragm.setAdapter(adapter);

        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).add(null);
                adapter.notifyItemInserted(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size() - 1);
                PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).remove(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size() - 1);
                adapter.notifyItemRemoved(PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size());
                int index = PodkategoriiFragment.listTovarovSQLfromAdapterRV.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId)).size();
                int end = index + countLoadItems;
                tovariViewModel.showSQL(index, end,    rvTovariFragm);
            }
        });


//        tovariViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        vetkaId = getArguments().getString("vetka");
//        System.out.println("getArguments "+vetkaId);


        if(PodkategoriiFragment.firstStartListBool.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId))){
            tovariViewModel.showSQL(0,10,    rvTovariFragm);
            PodkategoriiFragment.firstStartListBool.set(Integer.valueOf(PodkategoriiFragment.podkatVetkaId), false);
            vetkaIdprevios= PodkategoriiFragment.podkatVetkaId;
            System.out.println("firstStart if");
        } else if (!PodkategoriiFragment.firstStartListBool.get(Integer.valueOf(PodkategoriiFragment.podkatVetkaId))
                & PodkategoriiFragment.podkatVetkaId.equals(vetkaIdprevios)){
            System.out.println("firstStart else if "+PodkategoriiFragment.podkatVetkaId+", "+vetkaIdprevios);

        }
//        else{
//            System.out.println("firstStart else "+PoiskFragment.podkatVetkaId+", "+vetkaIdprevios+")");
//            listTovarovSQLfromAdapterRV.clear();
//            tovariViewModel.showSQL(0,10,    rvTovariFragm);
//            vetkaIdprevios=PoiskFragment.podkatVetkaId;
//        }
        BokovoeMenu.etPoiskActivaciya(false);
        return root;
    }
}