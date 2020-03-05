package app.fil.market.ui.poisk.podkategorii;

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

import app.fil.market.R;
import app.fil.market.ui.tovari.TovarFromSQL;

public class PoiskFragment extends Fragment {

    private PoiskViewModel poiskViewModel;
    public static ArrayList<PoiskObjectSQL> poiskObjectSQLSList = new ArrayList<>();
    public static ArrayList<ArrayList<TovarFromSQL>> listTovarovSQLfromAdapterRV = new ArrayList<>();
    public  static  ArrayList <Boolean> firstStartListBool = new ArrayList<>();
    public  static boolean firstStart=true;
//    TextView tvKorzinaCount;
//    public  static  LinearLayout linLayout ;
    PoiskAdapter adapter;
    public static String podkatVetkaId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        poiskViewModel =
                ViewModelProviders.of(this).get(PoiskViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tovari, container, false);
        final RecyclerView rvTovariFragm = root.findViewById(R.id.rvTovariFragm);
        rvTovariFragm.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new PoiskAdapter(rvTovariFragm, getActivity());
        rvTovariFragm.setAdapter(adapter);
        if(firstStart){
            poiskViewModel.showSQLPodkat(rvTovariFragm);
            firstStart = false;
        }else{

        }
        return root;

    }

}