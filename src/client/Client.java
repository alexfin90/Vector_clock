/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import servizio.CounterException;
import servizio.RemoteCounterService;

/**
 *
 * @author danielezito
 */
public class Client {
    
    private RemoteCounterService service;
   
    
    public Client(RemoteCounterService service) {
        this.service = service;
        
    }
    
   public void run(){
       Random r = new Random();
       int globalCount=0;
       int sessionCount=0;
       service.connect();
       for(int i =0; i<5; i++){
           try {
               service.incrementCounter();
               service.getSessionCounter();
               Thread.sleep(r.nextInt(2000));
           } catch (CounterException ex) {
               Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
           } catch (InterruptedException ex) {
               Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       }
       service.disconnect();
   }
}
