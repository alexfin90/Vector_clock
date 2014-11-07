/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servizio_impl;


/**
 *
 * @author Alex
 */
public class CounterState  {
    
    private static CounterState instance = null;
    private int globalCounter = 0;
    private VectorClock clockvector; 
    
    
    // ho aggiunto un costruttore privato essendo che è singleton 
    // dove faccio il new del vector clock
    private CounterState(){
        this.clockvector= new VectorClock();
    }
    
    public static synchronized CounterState getInstance(){
      if(instance == null) instance = new CounterState();
       return instance;
    }
    
    public int getGlobalCounter(){
        return this.globalCounter;
    }
    public void incrementCounter(){                                     
        this.globalCounter++;
    }

    //per incrementare clock
    public void incrementClock(String id){
        this.clockvector.incrementClock(id);
    }
    //per ottenere vectorclock
    public VectorClock getVectorClock(){
        return this.clockvector;
    }
   // per settare vectorclock
    public void setLogicalClock(VectorClock vc){
        this.clockvector=vc;
    }
    // per stampare vectorclock
    public String PrintClockVector(){
          return this.clockvector.toString();
    }
    }
    

    
