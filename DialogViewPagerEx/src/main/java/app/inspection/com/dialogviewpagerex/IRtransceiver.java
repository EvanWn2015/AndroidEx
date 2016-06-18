package app.inspection.com.dialogviewpagerex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by EVAN on 2016/6/17.
 */
public class IRtransceiver extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        if (v == null) {
            v = View.inflate(getActivity(), R.layout.ir_transceiver_content, null);
        }
        return v;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
