package app.inspection.com.viewpragerndrawlayoutex;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class OverViewActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("333");
    }
}
