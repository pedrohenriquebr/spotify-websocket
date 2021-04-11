package org.webspotify.discovery;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author psilva
 */
public class DiscoveryThread implements Runnable {

    private DatagramSocket socket;
    private boolean established = false;
    private String REQUEST  = "DISCOVER_REQUEST";
    private String RESPONSE  = "DISCOVER_RESPONSE";
    
    @Override
    public void run() {

        try {

            socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);//ativar broadcasting 

            while (true) {
                //log 
                System.out.println(getClass().getName() + ">>> Ready to receive broadcast clients !");

                //Receive clients
                byte[] recvBuf = new byte[15000];
                DatagramPacket client = DatagramMessage.recvData(socket,recvBuf);
                String data = new String(client.getData()).trim().replace("\n", "");
                //Packet received
                System.out.println(getClass().getName() + ">>>Discovery client received from: " + client.getAddress().getHostAddress());
                System.out.println(getClass().getName() + ">>>Packet received; data: " + data);

                //See if the client holds the right command (message)
                String message = new String(client.getData()).trim().replace("\n", "");
                
                if (message.equals(REQUEST)) 
                {
                    DatagramPacket sendPacket = null;
                    
                    try {
                        sendPacket = DatagramMessage.sendData(RESPONSE.getBytes(), client.getAddress(), client.getPort());
                    } catch (IOException ex) {
                        System.err.println("Can't send discover response !");
                    }
                    
                    if (sendPacket == null) {
                        return;
                    }
                    
                    System.out.println(getClass().getName() + ">>>Sent client to: " + sendPacket.getAddress().getHostAddress() + " port:" + client.getPort());
                }else {
                    System.out.println(getClass().getName() + ">>>");
                }
                
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static DiscoveryThread getInstance() {
        return DiscoveryThreadHolder.INSTANCE;
    }

    private static class DiscoveryThreadHolder {

        private static final DiscoveryThread INSTANCE = new DiscoveryThread();
    }

}
