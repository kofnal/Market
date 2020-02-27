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

import app.fil.market.R;

public class PodkategoriiFragment extends Fragment {

    private PodkategoriiViewModel podkategoriiViewModel;
    TextView tvKorzinaCount;
    LinearLayout linLayout ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        podkategoriiViewModel =
                ViewModelProviders.of(this).get(PodkategoriiViewModel.class);
        View root = inflater.inflate(R.layout.fragment_podkat, container, false);
        tvKorzinaCount = getActivity().findViewById(R.id.tvKorzinaCount);
        linLayout = root.findViewById(R.id.linLayTovariVertical);
        try {
            podkategoriiViewModel.showSQL(tvKorzinaCount, getActivity(), linLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;

    }

}