package app.fil.market.ui.poisk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import app.fil.market.BokovoeMenu;
import app.fil.market.R;

public class PoiskFragment extends Fragment {

    private PoiskViewModel poiskViewModel;
    public static ArrayList<PoiskObjectSQL> poiskObjectSQLSList = new ArrayList<>();
    public  static boolean firstStart=true;
    public static PoiskAdapter adapter;
    RecyclerView rvPoiskHistoryItems;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        poiskViewModel =
                ViewModelProviders.of(this).get(PoiskViewModel.class);
        View root = inflater.inflate(R.layout.fragment_poisk, container, false);
        rvPoiskHistoryItems = root.findViewById(R.id.rvPoiskHistoryItems);


        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.CENTER);
        rvPoiskHistoryItems.setLayoutManager(layoutManager);
        adapter = new PoiskAdapter(getActivity());
        rvPoiskHistoryItems.setAdapter(adapter);
        if (firstStart) {


            Gson gson = new Gson();
            String json = BokovoeMenu.sharedPreferences.getString(BokovoeMenu.prefTagNameRowData, "");
            if (json.isEmpty()) {
                Toast.makeText(getContext(), "There is something error", Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<ArrayList<PoiskObjectSQL>>() {
                }.getType();
                poiskObjectSQLSList = gson.fromJson(json, type);
//                for(PoiskObjectSQL data:poiskObjectSQLSList) {
//                    result.setText(data);
//                }


//            for (int i =0 ; i<12; i++){
//                PoiskObjectSQL poiskObjectSQL = new PoiskObjectSQL(Integer.toString(i+1));
//                poiskObjectSQLSList.add(0, poiskObjectSQL);
//            }
                adapter.notifyDataSetChanged();
                firstStart = false;
            }
        }

            BokovoeMenu.etPoiskActivaciya(true);
            return root;

        }


}