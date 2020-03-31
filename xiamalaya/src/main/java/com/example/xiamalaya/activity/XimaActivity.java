package com.example.xiamalaya.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.xiamalaya.R;
import com.example.xiamalaya.fragment.XimaFragment;

public class XimaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xima_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, XimaFragment.newInstance())
                    .commitNow();
        }
    }
}
