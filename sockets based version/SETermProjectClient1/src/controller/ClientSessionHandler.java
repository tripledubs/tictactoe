/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import model.TTTConstants;

/**
 *
 * @author George Wan
 */
public class ClientSessionHandler implements Runnable, TTTConstants {
    //public static ClientWindow cw;
    // private Cell[][] cell = new Cell[3][3];

    public static boolean myTurn = false;
    private boolean continueToPlay = true;
    public static boolean waiting = true;

    public static char myToken = ' ';
    public static char otherToken = ' ';

    private Socket playerSocket;

    private DataInputStream fromServer;
    private DataOutputStream toServer;

    public static int rowSelected;
    public static int columnSelected;
    //private Cell[][] cell1;

    public ClientSessionHandler(Socket playerSocket) {
        this.playerSocket = playerSocket;
        //cell1 = cw.cell[][];
    }

    @Override
    public void run() {
        try {
            fromServer = new DataInputStream(playerSocket.getInputStream());
            toServer = new DataOutputStream(playerSocket.getOutputStream());

            int player = fromServer.readInt(); // Get notification from the server. Server code line 34

            if (player == PLAYER1) {
                myToken = 'X';
                otherToken = 'O';
                TTTClient.cw.jlbTitle.setText("Player 1 with token 'X'");
                TTTClient.cw.jlbStatus.setText("Wating for player 2 to join");
                String startSingal = fromServer.readUTF();
                if (startSingal.equalsIgnoreCase("Start")) {
                    TTTClient.cw.jlbStatus.setText("Player 2 has joined. I move first");
                    myTurn = true;
                }
            } else if (player == PLAYER2) {
                myToken = 'O';
                otherToken = 'X';
                TTTClient.cw.jlbTitle.setText("Player 2 with token 'O'");
                TTTClient.cw.jlbStatus.setText("Wating for player 1 to make a move");
                //myTurn = true;
            }

            while (continueToPlay) {
                if (player == PLAYER1) {
                    waitForPlayerAction();
                    sendMove();
                    receiveInfoFromServer();
                } else if (player == PLAYER2) {
                    receiveInfoFromServer();
                    waitForPlayerAction();
                    sendMove();
                }

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    private void waitForPlayerAction() throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }
        waiting = true;
    }

    private void sendMove() throws IOException {
        toServer.writeInt(rowSelected);
        toServer.writeInt(columnSelected);
    }

    private void receiveInfoFromServer() throws IOException {
        int status = fromServer.readInt();

        if (status == PLAYER1_WIN) {
            continueToPlay = false;
            if (myToken == 'X') {
                TTTClient.cw.jlbStatus.setText("I won! (X)");
            } else if (myToken == 'O') {
                TTTClient.cw.jlbStatus.setText("Player 1 won! (X)");
                receiveMove();
            }
        } else if (status == PLAYER2_WIN) {
            continueToPlay = false;
            if (myToken == 'O') {
                TTTClient.cw.jlbStatus.setText("I won! (O)");
            } else if (myToken == 'X') {
                TTTClient.cw.jlbStatus.setText("Player 2 won! (X)");
                receiveMove();
            }
        } else if (status == DRAW_GAME) {
            continueToPlay = false;
            TTTClient.cw.jlbStatus.setText("GAME OVER! DRAW GAME!");
            if (myToken == 'O') {
                receiveMove();
            }
        } else if (status == GAME_CONTINUE) {
            receiveMove();
            TTTClient.cw.jlbStatus.setText("My turn");
        }

    }

    private void receiveMove() throws IOException {
        int row = fromServer.readInt();
        int column = fromServer.readInt();
        TTTClient.cw.cell[row][column].setToken(otherToken);
        myTurn = true;
        //System.out.println(cw.jlbStatus);

    }

}
