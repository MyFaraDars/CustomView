package mr.ahj.com.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ValueBar valueBar;
    private ValueSelector valueSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valueSelector = (ValueSelector) findViewById(R.id.value_selector);
        valueSelector.setMinValue(0);
        valueSelector.setMaxValue(100);

        valueBar = (ValueBar) findViewById(R.id.value_bar);
        valueBar.setMaxValue(100);
        valueBar.setCurrentValue(0);

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueBar.setCurrentValue(valueSelector.getValue());
            }
        });
    }


}
