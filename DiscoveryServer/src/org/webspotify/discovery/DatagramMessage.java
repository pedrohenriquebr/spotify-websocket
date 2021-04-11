package org.webspotify.discovery;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author psilva
 */
public class DatagramMessage {
    public static DatagramPacket sendData(byte[] data, InetAddress address, int port) throws IOException {
        DatagramSocket socket  = new DatagramSocket();
        if (socket != null) {
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            return packet;
        }

        return null;
    }

    public static DatagramPacket recvData(DatagramSocket socket, byte[] buff) throws IOException {
        if (socket != null) {
            DatagramPacket packet = new DatagramPacket(buff, buff.length);
            socket.receive(packet);
            return packet;
        }

        return null;
    }
}
