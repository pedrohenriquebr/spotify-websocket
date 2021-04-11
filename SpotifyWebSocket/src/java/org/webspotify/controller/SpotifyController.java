/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.webspotify.controller;

import java.io.IOException;
import java.util.Scanner;


public class SpotifyController {
    
    public final static String SP_COMMAND = "sp";
    
    public static String run(String action) throws IOException{
        
        String result = "";
        Process proc = Runtime.getRuntime().exec(SP_COMMAND + " "+action);
        Scanner scan = new Scanner(proc.getInputStream());
        
        while(scan.hasNextLine()){
            result += scan.nextLine()+"\n";
        }
        
        return result;
    }
    
    public static String playpause() throws IOException{
        return run("play");
    }
    
    public static String pause() throws IOException{
        return run("pause");
    }
    
    public static String next() throws IOException {
        return run("next");
    }
    
    public static String prev() throws IOException{
        return run("prev");
    }
    
    public static String current() throws IOException{
        return run("current");
    }
    
    public static String metadata() throws IOException{
        return run("metadata");
    }
    
    public static String art() throws  IOException {
        return run("art");
    }
    
    
    
}
