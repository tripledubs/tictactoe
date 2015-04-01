/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.Socket;
import model.TTTConstants;
import view.ClientWindow;

/**
 *
 * @author George Wan
 */
public class TTTClient implements TTTConstants {

    public static ClientWindow cw;

    public static void main(String[] args) {
        cw = new ClientWindow();

        try {
            Socket socket = new Socket("LocalHost", 8000);
            ClientSessionHandler gameSession = new ClientSessionHandler(socket);
            new Thread(gameSession).start();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

}
