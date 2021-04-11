/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.webspotify.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.webspotify.controller.SpotifyController;

/**
 *
 * @author psilva
 */
@ServerEndpoint("/spotify")
public class SpotifySocket {

    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen (Session peer) {
        peers.add(peer);
    }

    @OnClose
    public void onClose (Session peer) {
        peers.remove(peer);
    }

    @OnMessage
    public String onMessage(String message) {
        String result = "none";
        try {
        
            
            switch (message) {
                case "art":
                    result = SpotifyController.art();
                    break;
                case "play":
                    result = SpotifyController.playpause();
                    break;
                case "pause":
                    result = SpotifyController.pause();
                    break;
                case "current":
                    result = SpotifyController.current();
                    break;
                case "metadata":
                    result = SpotifyController.metadata();
                    break;
                case "prev":
                    result = SpotifyController.prev();
                    break;
                case "next":
                    result = SpotifyController.next();
                    break;
                default:
                    break;
            }
            
            
        }catch( IOException  ex ){
            System.err.println("erro ao executar o último comando : "+message);
        }
        
        return result;
    }
    
    
}
