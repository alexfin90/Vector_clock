/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servizio;

import servizio_impl.VectorClock;



/**
 *
 * @author danielezito
 */
public interface CounterService {
    public void incrementCounter() throws CounterException;
    public int getGlobalCounter() throws CounterException;
    public int getSessionCounter() throws CounterException;
  
    
   
    
}
