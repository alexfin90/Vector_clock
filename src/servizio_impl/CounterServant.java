/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servizio_impl;
import java.util.logging.Level;
import java.util.logging.Logger;
import servizio.CounterException;
import servizio.CounterService;

/**
 *
 * @author Alex
 */
public class CounterServant implements CounterService{

    private CounterState appState;
    private SessionState sessionState;
   

    public CounterServant() {
        appState = CounterState.getInstance();
        sessionState = new SessionState();
    }
    
    
    
    @Override
    public void incrementCounter() throws CounterException {
        try {
            appState.incrementCounter();
            sessionState.incrementSessionCounter();
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(CounterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getGlobalCounter() throws CounterException {
        return appState.getGlobalCounter();
    }

    @Override
    public int getSessionCounter() throws CounterException {
        return sessionState.getSessionCounter();
    }
    
    //per richiamare la funzione per l'incremento del  vector clock
    public void incrementClock(String id) throws CounterException{
        appState.incrementClock(id);
    }
    //per richiamare la funzione per il settaggio del  vector clock
    public VectorClock getVectorClock() throws CounterException {
        return appState.getVectorClock();
    }

    //per richiamare la funzione per il settaggio del  vector clock
    public void setLogicalClock(VectorClock vc) throws CounterException{
        appState.setLogicalClock(vc);
    }
    
    //per richiamare la funzione per la stampa in stringa del vector clock
    public String PrintClockVector(){
       return appState.PrintClockVector();
      
    }
}
