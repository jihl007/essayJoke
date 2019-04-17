package com.pzdf.essayjoke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * author  jihl on 2019/3/23.
 * version 1.0
 * Description:
 */
public class WelcomeActivity extends Activity {
    private static final long WAIT_TIME=3000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(WelcomeActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },WAIT_TIME);
    }
}
