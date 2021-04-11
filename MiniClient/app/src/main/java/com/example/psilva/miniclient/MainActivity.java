package com.example.psilva.miniclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View.*;
import android.view.*;
import android.widget.*;

import java.io.IOException;
import java.net.*;
import java.util.*;


public class MainActivity extends Activity {
    public static Button btnEnv;
    public static TextView txtStatus;
    public DiscoveryThread discover = null;
    public InetAddress address;
    public static final String DISCOVER_REQUEST = "DISCOVER_REQUEST";
    public static final String DISCOVER_RESPONSE = "DISCOVER_RESPONSE";
    public static final int DISCOVER_SERVER_PORT = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Pega os widgets
        btnEnv = (Button) findViewById(R.id.buttonEnviar);
        txtStatus = (TextView) findViewById(R.id.textStatus);

        btnEnv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                discover = new DiscoveryThread();
                txtStatus.setText("esperando resposta");
                discover.setMessage(DISCOVER_REQUEST);
                discover.setPort(DISCOVER_SERVER_PORT);
                Thread thread = new Thread(discover);
                thread.start();

            }

        });


    }


    class DiscoveryThread implements Runnable {
        private DatagramSocket socket;
        private String message;
        private String ipDiscover = null;
        private int port;
        private Handler handler;

        public DiscoveryThread() {

            //porta padrão a ser enviado os pacotes udp
            port = 8888;
            handler = new Handler();
            try {
                socket = new DatagramSocket(1234);

            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        public void close() {
            socket.close();
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getIpDiscover() {
            return this.ipDiscover;
        }

        /**
         * @return broadcast de cada interface de rede do dispositivo
         * @throws SocketException
         */
        public List<InetAddress> listAllBroadcastAddresses() throws SocketException {
            List<InetAddress> broadcastList = new ArrayList<>();

            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();

            while (networks.hasMoreElements()) {
                NetworkInterface networkinterface = networks.nextElement();

                if (networkinterface.isLoopback() || !networkinterface.isUp()) {
                    continue;
                }

                List<InterfaceAddress> addrs = networkinterface.getInterfaceAddresses();

                for (InterfaceAddress addr : addrs) {
                    InetAddress broadcast = addr.getBroadcast();
                    if (broadcast != null) {
                        broadcastList.add(broadcast);
                    }
                }
            }


            return broadcastList;
        }

        @Override
        public void run() {

            try {

                //mensagem a ser enviada
                byte[] message = this.message.getBytes();
                //enviou o pacote pelo
                List<InetAddress> broadcasts = this.listAllBroadcastAddresses();

                //percorre todos os ips broadcasts
                for (InetAddress broadcast : broadcasts) {
                    DatagramPacket sendPacket = new DatagramPacket(message, message.length, broadcast, port);
                    socket.send(sendPacket);
                }

                byte[] recv = new byte[500];
                //esperar que alguma máquina responda
                final DatagramPacket packet = new DatagramPacket(recv, recv.length);
                socket.receive(packet);
                final String strMsg = new String(packet.getData(),0,packet.getLength()).trim().replace("\n", "");


                this.ipDiscover = packet.getAddress().getHostAddress();
                if (ipDiscover != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            txtStatus.setText((CharSequence) "Descoberto " + ipDiscover);

                            if (strMsg.equals(DISCOVER_RESPONSE)) {
                                try {
                                    Thread.sleep(1000);
                                    Intent intent = new Intent(MainActivity.this, InputActivity.class);
                                    intent.putExtra("address", ipDiscover);
                                    startActivity(intent);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    ;
                                }
                            }

                        }
                    });

                }


            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }


        }


    }
}

