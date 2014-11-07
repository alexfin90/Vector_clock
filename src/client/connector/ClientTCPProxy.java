/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.connector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import servizio.CounterException;
import servizio.RemoteCounterService;
import servizio_impl.VectorClock;

/**
 *
 * @author Alex
 */
public class ClientTCPProxy implements RemoteCounterService {
  
    private InetAddress address;
    private int port;
    private int logicalClock =0;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream oout;
    // il client ha un vectorclock
    private VectorClock clock;
   
    private String Id;
    private final String ServerID="idServer";
    
            
            
    public ClientTCPProxy(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        this.clock= new VectorClock();
      
        clock.incrementClock(ServerID);
        // avevo inizialmento usato Thread.currentThread().getId(); come identificatore
        // ma in questo caso il Tread.currentThread().getId(); dal lato client è sempre 1 
        // quando istanzierò nuovi ClientTCPProxy , una combinazione porta-indirizzo è un alternativa
        // nel caso non siamo in localhost. 
        
        // nel mio caso sto facendo scegliere al utente l'identificativo del client quando avvia il client
        // come stringa visto che siamo in localhost , ovviamente questa non è una buona soluzione 
        // se non eravamo in localhost perchè l'utente non può sapere quale id sono già stati usati ecc
        // in quel caso la combinazione IP-Porta credo dovrebbe essere la migliore soluzione da usare.
       this.Id=JOptionPane.showInputDialog(null,"Hai Creato un client!Per favore assegna un id univoco al processo sarà usato nel vectorclock:","primo");
       clock.incrementClock(Id);
    }

    @Override
    public void incrementCounter() throws CounterException {
        doOperation("incrementCounter");
    }

    @Override
    public int getGlobalCounter() throws CounterException {
      return new Scanner(doOperation("getGlobalCounter")).nextInt();   
    }

    @Override
    public int getSessionCounter() throws CounterException {
        return new Scanner(doOperation("getSessionCounter")).nextInt();
    }

    @Override
    public void connect() {
        try {
            this.socket = new Socket(address, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            oout= new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientTCPProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        doOperation("CONNECT");
    }

    @Override
    public void disconnect() {
    
        doOperation("DISCONNECT");
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientTCPProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    // invia e riceve l'evento di invio implica un aumnento del clock logico
    private String doOperation(String op) {
        VectorClock vc = null;
        //aggiorno il clock del client.
        
        clock.incrementClock(this.Id);
        
        String request = op; 
        System.out.println("Request: " + request);
        String reply = null;
        try {
            //invio la richiesta di operazione per il server
            out.writeUTF(request);
            //invio il vector clock nella forma (id = valore,...)
            System.out.println("Sto inviando"+clock.toString());
            //pulisco a ogni uso il canale se no il serialize da problemi mandando valore non aggiornato
            oout.reset(); 
            //serializzo il mio vectorclock 
            oout.writeObject(clock);
            oout.flush();       
            // ricevo l'ack dal server
            reply = in.readUTF();
            
            /* Se eventualmente vorrei renderei parieteci client e server scenario (P2P) 
            qui potrei arricchire il client delle funzionalità del server in modo che alla
            ricezione del'ack o comunque a una altra possibile richiesta di operazione da parte 
            del server i vector clock vengano aggiornati basta usare i metodi statici della classe
            VectorClock come già fatto lato server e dovrei anche impostare un canale di serializzazione
            inverso per l'invio del vector clock da server verso client.
            */
        } catch (IOException ex) {
            Logger.getLogger(ClientTCPProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Reply: " + reply);
        return reply;
    }

       
    public int getLogicalClock() throws CounterException {
        return new Scanner(doOperation("getLogicalClock")).nextInt(); 
    
    }
    
    
    
}
