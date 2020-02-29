package app.fil.market.ui.podkategorii;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.fil.market.R;
import app.fil.market.ui.tovari.TovarFromSQL;
import app.fil.market.ui.tovari.TovariSpisokAdapter;

public class PodkategoriiFragment extends Fragment {

    private PodkategoriiViewModel podkategoriiViewModel;
    public static ArrayList<PodkategoriaObjectSQL> podkategoriaObjectSQLSList = new ArrayList<>();
//    public static ArrayList<ArrayList<TovarFromSQL>> podkategoriaObjectSQLSList2 = new ArrayList<>();
//    TextView tvKorzinaCount;
//    public  static  LinearLayout linLayout ;
    public static boolean firstStart=true;
    PodkategoriiAdapter adapter;
    public static String podkatVetkaId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        podkategoriiViewModel =
                ViewModelProviders.of(this).get(PodkategoriiViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tovari, container, false);
//        tvKorzinaCount = getActivity().findViewById(R.id.tvKorzinaCount);
//        ibKorzina = getActivity().findViewById(R.id.ibOpisanieKorzina);
        final RecyclerView rvTovariFragm = root.findViewById(R.id.rvTovariFragm);
        rvTovariFragm.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new PodkategoriiAdapter(rvTovariFragm, getActivity());
        rvTovariFragm.setAdapter(adapter);

        if(firstStart){
            podkategoriiViewModel.showSQLPodkat(rvTovariFragm);
            firstStart = false;
        }else{

        }


        return root;

    }

}