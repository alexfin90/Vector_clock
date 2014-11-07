/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servizio_impl;

/**
 *
 * @author danielezito
 */
public class SessionState {
    
    private int SessionCounter=0;

    public int getSessionCounter() {
        return SessionCounter;
    }
    
    public void incrementSessionCounter(){
        this.SessionCounter++;
    }
    
}
