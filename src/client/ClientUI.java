/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import client.connector.ServiceFactory;
import javax.swing.JOptionPane;
import servizio.RemoteCounterService;

/**
 *
 * @author danielezito
 */
public class ClientUI {
    
    public static void main(String args[]){
        RemoteCounterService service = ServiceFactory.getInstance().getService();
        
      
        
        Client client = new Client(service);
        client.run();
        
        
        
    }
}
