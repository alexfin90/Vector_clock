/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serverConnector;

/**
 *
 * @author danielezito
 */
public class Server {
    static int PORT = 7889;
    public static void main(String args[]){
        CounterTCPProxy server = new CounterTCPProxy(PORT);
        System.out.println("SERVER ATTIVO sulla porta: " + PORT);
        server.run();
    }
}
