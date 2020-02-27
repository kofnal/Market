package app.fil.market.ui.tovari;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.fil.market.Model.ILoadMore;
import app.fil.market.R;
import app.fil.market.tovari.TovarFromSQL;
import app.fil.market.tovari.TovariSpisokAdapter;

public class TovariFragment extends Fragment {

    private TovariViewModel tovariViewModel;
    TextView tvKorzinaCount;
    ImageButton ibKorzina;
    public static TovariSpisokAdapter adapter;
    public static ArrayList<TovarFromSQL> tovarFromSQLList = new ArrayList<>();
    final public static int countLoadItems = 10;
    String vetkaId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tovariViewModel =
                ViewModelProviders.of(this).get(TovariViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tovari, container, false);
        tvKorzinaCount = getActivity().findViewById(R.id.tvKorzinaCount);
        ibKorzina = getActivity().findViewById(R.id.ibOpisanieKorzina);
        final RecyclerView rvTovariFragm = root.findViewById(R.id.rvTovariFragm);
        rvTovariFragm.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new TovariSpisokAdapter(rvTovariFragm, getActivity(),  ibKorzina, tvKorzinaCount);
        rvTovariFragm.setAdapter(adapter);
        tovarFromSQLList.clear();
//        final TextView tvTemp=getActivity().findViewById(R.id.tvBokovI_Find);
//        tvTemp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                adapter.notifyDataSetChanged();
//                System.out.println("notifyDataSetChanged from tvTemp");
//                tvTemp.setText(Integer.toString(adapter.getItemCount()));
//            }
//        });
        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                tovarFromSQLList.add(null);
                adapter.notifyItemInserted(tovarFromSQLList.size() - 1);
                tovarFromSQLList.remove(tovarFromSQLList.size() - 1);
                adapter.notifyItemRemoved(tovarFromSQLList.size());
                int index = tovarFromSQLList.size();
                int end = index + countLoadItems;
                tovariViewModel.showSQL(index, end, tvKorzinaCount,   rvTovariFragm, vetkaId);
            }
        });


//        tovariViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        vetkaId = getArguments().getString("vetka");
        System.out.println("getArguments "+vetkaId);
        tovariViewModel.showSQL(0,10, tvKorzinaCount,   rvTovariFragm, vetkaId);
        return root;
    }
}