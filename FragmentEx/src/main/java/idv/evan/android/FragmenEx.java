package idv.evan.android;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by EVAN on 2016/6/14.
 */
public class FragmenEx extends Fragment {

    private TextView textView;
    private int i;

    public FragmenEx(int i) {
        this.i = i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = null;
        if (v == null) {
            v = View.inflate(getActivity(), R.layout.fragmen_ex, null);
        }
        textView = (TextView) v.findViewById(R.id.textView);
        return v;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        textView.setText(i + 1 + "");
    }
}
