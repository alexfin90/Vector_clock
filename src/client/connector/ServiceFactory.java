/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.connector;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import servizio.RemoteCounterService;

/**
 *
 * @author danielezito
 */
public class ServiceFactory {
    private static ServiceFactory instance = null;
    
    public static synchronized ServiceFactory getInstance(){
        if(instance == null) instance = new ServiceFactory();
        return instance;
    }
    
    public RemoteCounterService getService(){
        
        RemoteCounterService service = null;
      
        
        
        try {
            InetAddress address = InetAddress.getByName("localhost");
            service = new ClientTCPProxy(address, 7889);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServiceFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return service;
    }
}
