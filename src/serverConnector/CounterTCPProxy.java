/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serverConnector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import servizio_impl.CounterServant;

/**
 *
 * @author danielezito
 */
public class CounterTCPProxy {
    int port;

    public CounterTCPProxy(int port) {
        this.port = port;
    }
    
    public void run(){
        try {
            ServerSocket listenSocket = new ServerSocket(port);
            while(true){
                Socket clientSocket = listenSocket.accept();
                ServantThread thread = new ServantThread(clientSocket, new CounterServant());
            }
        } catch (IOException ex) {
            Logger.getLogger(CounterTCPProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
