package com.liqi.imapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private Socket s = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    boolean bConnected = false;
    recvThread r = new recvThread(); //线程类
    private TextView tv = null;
    private EditText et = null;
    private Button bt = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.show_view);
        et = (EditText) findViewById(R.id.input_view);
        bt = (Button) findViewById(R.id.bt_view);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et.getText().toString();
                //ta.setText(str);
                et.setText("");
                try {
                    dos.writeUTF(str);
                    dos.flush();
                    //dos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }});

        new Thread(r).start();
    }

    public void connect() {
        try {
            s = new Socket("192.168.1.3", 8888); //建立客户端对象
            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            bConnected = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("liqix", e.getMessage());
        }
    }

    public void disconnect() { //窗口关闭时关闭客户端，输入，输出
        try {
            dos.close();
            dis.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        disconnect();
        super.onPause();
    }

    private class recvThread implements Runnable { //客户端线程接收数据
        public void run() {
            try {
                s = new Socket(); //建立客户端对象
                s.connect(new InetSocketAddress("192.168.1.3", 8888), 5000);
                dos = new DataOutputStream(s.getOutputStream());
                dis = new DataInputStream(s.getInputStream());
                bConnected = true;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("liqix",e.getMessage());
            }
            try {
                while (bConnected) {
                    String str;
                    str = dis.readUTF(); //拿到数据
//                    tv.setText(tv.getText() + str + '\n');//显示到显示框中
                }
            } catch (SocketException e) {
                System.out.println("退出了");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
