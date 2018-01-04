package com.changjiashuai.aidltest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button mBtnBind;
    private Button mBtnUnBind;
    private Button mBtnAdd;
    private Button mBtnMinus;
    private TextView mTvMsg;
    private ICalculateAIDL mICalculateAIDL;
    private boolean binded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnBind = findViewById(R.id.button);
        mBtnUnBind = findViewById(R.id.button2);
        mBtnAdd = findViewById(R.id.button3);
        mBtnMinus = findViewById(R.id.button4);
        mTvMsg = findViewById(R.id.textView);

        mBtnBind.setOnClickListener(this);
        mBtnUnBind.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
        mBtnMinus.setOnClickListener(this);
    }

    private void unbind() {
        if (binded) {
            unbindService(mConnection);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbind();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button:
                Intent intent = new Intent(this, CalculateService.class);
//                intent.setAction("com.changjiashuai.aidltest.calculate");
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.button2:
                unbind();
                break;
            case R.id.button3:
                if (mICalculateAIDL != null) {
                    try {
                        int res = mICalculateAIDL.add(2, 2);
                        mTvMsg.setText("add res=" + res);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "please rebind", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button4:
                if (mICalculateAIDL != null) {
                    try {
                        int res = mICalculateAIDL.minus(2, 2);
                        mTvMsg.setText("add res=" + res);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "please rebind", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "onServiceConnected: ");
            binded = true;
            mICalculateAIDL = ICalculateAIDL.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected: ");
            mICalculateAIDL = null;
            binded = false;
        }
    };
}
