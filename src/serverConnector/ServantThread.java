/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serverConnector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import servizio.CounterException;
import servizio_impl.CounterServant;
import servizio_impl.VectorClock;


/**
 *
 * @author Alex
 */
public class ServantThread extends Thread{
    private Socket clientSocket;
    private CounterServant service;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectInputStream iin;
    private String idserver;
    boolean done = false;

    public ServantThread(Socket clientSocket, CounterServant service) {
        this.clientSocket = clientSocket;
        this.service = service;
        this.idserver= "idServer";
        
        try {
            this.service.incrementClock(idserver);
        } catch (CounterException ex) {
            Logger.getLogger(ServantThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            iin= new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServantThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.start();
        
    }
    
    public void run(){
        String request =null;
        String reply = null;
        
        while(!done){
            try {
                request = in.readUTF();              
                reply = this.executeOperation(request);
                //risponde
                out.writeUTF(reply);
            } catch (IOException ex) {
                Logger.getLogger(ServantThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CounterException ex) {
                Logger.getLogger(ServantThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }

     /* estrae l'operazione dalla richiesta */ 
    private String getOp(String request) {
        /* la richiesta ha la forma "operazione$parametro" */
        int sep = request.indexOf("$"); 
        String op = request.substring(0,sep);
        return op; 
    }
    
    /* estrae il parametro dalla richiesta */ 
    private String getParam(String request) {
        /* la richiesta ha la forma "operazione$parametro" */
        int sep = request.indexOf("$"); 
        String param = request.substring(sep+1); 
        return param; 
    }
    
    private void updateLogicalClock(){
        
        
        VectorClock vc=null;
        try {
          // deserializzo vector clock client!
            vc = (VectorClock)iin.readObject();
            System.out.println("RICEVUTO vector clock del client nella forma (id = valore,...):"+vc.toString());
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServantThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        try {
            
          // Confronto i due vector clock per stabilirne la consistenza temporale!
          // come visto a lezione in teoria potrebbero infatti succedere diverse situazioni
          // tra cui anche la concorenza in caso il vettore non sia ne minore ne maggiore
          
          System.out.println("CONFRONTO TEMPORALE DEI VECTOR CLOCK...");
          switch(VectorClock.compare(service.getVectorClock(), vc)){
              case -1: System.out.println("IL VECTOR CLOCK DEL SERVER E' MINORE!");
              break;
              case 0: System.out.println("I VECTOR CLOCK SONO UGUALI");
              break;
              case 1: System.out.println("IL VECTOR CLOCK DEL SERVER E' MAGGIORE!");  
              break;
              case 2: System.out.println("SITUAZIONE DI CONCORRENZA!"); 
              break;    
          }
          
        System.out.println("questo era il mio vectorclock:"+ service.PrintClockVector()+ " allora prenderò il merging dei 2 Clock Vector e incremento di 1 il clock del server");  
        // A questo punto devo prendere il merging tra i due Vectorclock del CLIENT E SERVER
        // e setto il nuovo vector clock
        service.setLogicalClock(VectorClock.max(service.getVectorClock(), vc)); 
        //e in più siccome  il server esegue un operazione interna incremento  il suo clock
        service.incrementClock(idserver);
        } catch (CounterException ex) {
            Logger.getLogger(ServantThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private String executeOperation(String op) throws CounterException {
       
        
        
       
        
        String reply = null;
        System.out.println("*****");
        System.out.println("Server: " + Thread.currentThread().getId() + " Vector Clock locale: " + service.PrintClockVector());
        System.out.println("Request: " + op);
        
       
        
        // aggiorno il vectorclock
        updateLogicalClock();
       
        System.out.println("nuovo valore VectorClock: " +service.PrintClockVector());
        
        if(op.equals("CONNECT")){
            done = false;
            reply="ACK";
            
        }
        else if(op.equals("DISCONNECT")){
            done = true;
            reply = "ACK";
        }else if(op.equals("getGlobalCounter")){
            reply = (String.valueOf(service.getGlobalCounter()));
            
        }else if(op.equals("getSessionCounter")){
            reply= (String.valueOf(service.getSessionCounter()));
            
        }else if(op.equals("incrementCounter")){
            service.incrementCounter();
            reply="ACK";
        }else throw new CounterException("Operation " + op + "non supportata");
        
        System.out.println("Reply: " + reply);
        return reply;
    }
    
    
    
}
