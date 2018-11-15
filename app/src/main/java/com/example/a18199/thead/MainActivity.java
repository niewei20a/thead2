package com.example.a18199.thead;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    ProgressBar bar;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            bar.setProgress(msg.what);
            if(msg.what==100){
                bt2.setEnabled(false);
            }
            super.handleMessage(msg);
        }
    };
    Thread background=new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while(true){
                    Thread.sleep(500);
                    if (!isThreadRunning.get()){
                        continue;
                    }
                    Message message=handler.obtainMessage();
                    message.what=progressValue.get()+5;
                    progressValue.set(message.what);
                    if (message.what>100){
                        progressValue.set(100);
                        isThreadRunning.set(false);

                    }
                    Log.d("message.what",""+message.what);
                    handler.sendMessage(message);
                }
            }
            catch (Throwable t) {

            }
        }
    });
    AtomicInteger progressValue=new AtomicInteger();
    AtomicBoolean isThreadRunning=new AtomicBoolean();
    Button bt1,bt2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        bar=(ProgressBar)findViewById(R.id.progressBar);
        bt1=(Button) findViewById(R.id.button_start);
        bt2=(Button) findViewById(R.id.button_stop);
        bt1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                progressValue.set(0);
                bar.setProgress(0);
                isThreadRunning.set(true);
                bt2.setText("暂停");
                bt2.setEnabled(true);
            }
        });
        bt2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean isRunning=isThreadRunning.get();
                if (isThreadRunning.get()){
//					background.stop(); //stop()方法不建议使用，若线程已经在sleep状态，stop不掉；
                    bt2.setText("继续");
                    isThreadRunning.set(false);
                }
                else{
                    bt2.setText("暂停");
//					background.start();//若线程已经运行一遍后，用start方法会出错
                    isThreadRunning.set(true);
                }
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
    }

}
