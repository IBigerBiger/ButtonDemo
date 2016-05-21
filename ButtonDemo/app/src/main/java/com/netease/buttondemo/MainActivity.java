package com.netease.buttondemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private SendVoiceView sendVoiceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        sendVoiceView = (SendVoiceView) findViewById(R.id.sendvoice_view);
    }

    public void onClick(View view){
        sendVoiceView.doSendAnim();
    }
}
