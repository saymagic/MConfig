package tech.saymagic.mconfig.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tech.saymagic.mconfig.demo.R;
import tech.saymagic.mconfig.demo.config.ConfigManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mValueTv;
    private EditText mKeyEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mValueTv = (TextView) findViewById(R.id.value_tv);
        mKeyEt = (EditText) findViewById(R.id.key_et);
        findViewById(R.id.submit_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                mValueTv.setText(ConfigManager.getInstance()
                        .getDefault()
                        .getProperty(String.valueOf(mKeyEt.getText().toString())));
                break;
        }
    }

}
