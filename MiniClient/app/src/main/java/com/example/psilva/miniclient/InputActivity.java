package com.example.psilva.miniclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class InputActivity extends Activity implements View.OnClickListener {
    public String addr ;
    public static Button btnNext ;
    public static Button btnPrevious;
    public static Button btnPlay ;
    public static WebSocket ws = null ;
    //códigos ou comandos

    public static final String  PLAY = "play";
    public static final String  PAUSE = "pause";
    public static final String  PLAY_PAUSE= "playpause";
    public static final String  STOP = "stop";
    public static final String  NEXT = "next";
    public static final String  PREVIOUS = "prev";
    public Request request  = null ;
    private OkHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Bundle bundle  = getIntent().getExtras();
        addr = bundle.getString("address");
        request = new Request.Builder().url("ws://"+addr+":8080/SpotifyWebSocketApp/spotify").build();
        Log.d("Endereço recebido ",addr);
        btnPlay = (Button) findViewById(R.id.buttonPlay);
        btnNext = (Button ) findViewById(R.id.buttonNext);
        btnPrevious =(Button) findViewById(R.id.buttonPrevious);


        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);

        SpotifyWebSocketListener listener = new SpotifyWebSocketListener();
        client = new OkHttpClient();
        ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService();

    }


    @Override
    public void onClick(View v) {
        String data  = "";
       switch(v.getId()) {
           case R.id.buttonPlay: {
               data = PLAY;
               break;
           }

           case R.id.buttonNext:{
               data = NEXT;
               break;
           }

           case R.id.buttonPrevious:{
               data  = PREVIOUS;
               break;
           }
       }

        ws.send(data);


    }

    private void output(final String txt) {

    }


    private final class SpotifyWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            //output("Receiving : " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            //output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            //output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            //output("Error : " + t.getMessage());
        }
    }


}
