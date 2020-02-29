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

import app.fil.market.Model.ILoadMore;
import app.fil.market.R;
import app.fil.market.ui.podkategorii.PodkategoriiFragment;

public class TovariFragment extends Fragment {

    private TovariViewModel tovariViewModel;
//    TextView tvKorzinaCount;
    public static TovariSpisokAdapter adapter;
    public static ArrayList<TovarFromSQL> listTovarovSQLfromAdapterRV = new ArrayList<>();
    final public static int countLoadItems = 10;
//    String vetkaId;
    public static String vetkaIdprevios=" ";
    public static boolean firstStart = true;


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
                listTovarovSQLfromAdapterRV.add(null);
                adapter.notifyItemInserted(listTovarovSQLfromAdapterRV.size() - 1);
                listTovarovSQLfromAdapterRV.remove(listTovarovSQLfromAdapterRV.size() - 1);
                adapter.notifyItemRemoved(listTovarovSQLfromAdapterRV.size());
                int index = listTovarovSQLfromAdapterRV.size();
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


        if(firstStart){
            tovariViewModel.showSQL(0,10,    rvTovariFragm);
            firstStart = false;
            vetkaIdprevios= PodkategoriiFragment.podkatVetkaId;
            System.out.println("firstStart if");
        } else if (!firstStart & PodkategoriiFragment.podkatVetkaId.equals(vetkaIdprevios)){
            System.out.println("firstStart else if "+PodkategoriiFragment.podkatVetkaId+", "+vetkaIdprevios);

        } else{
            System.out.println("firstStart else "+PodkategoriiFragment.podkatVetkaId+", "+vetkaIdprevios+")");
            listTovarovSQLfromAdapterRV.clear();
            tovariViewModel.showSQL(0,10,    rvTovariFragm);
            vetkaIdprevios=PodkategoriiFragment.podkatVetkaId;
        }
        return root;
    }
}