package controller;

import static controller.TTTServer.sgrw;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javax.swing.JOptionPane;
import model.TTTConstants;
import static model.TTTConstants.PLAYER1;
import static model.TTTConstants.PLAYER2;
import view.ServerConnectionWindow;
import view.ServerGameResultsWindow;
import view.ServerGameWindow;

public class StartServer implements Runnable, TTTConstants {

    @Override
    public void run() {
        String[] timeOptions = {"30", "60", "90", "120","180"};
        String choice = (String) JOptionPane.showInputDialog(null, "Input Game Time", "TicTacToe Server", JOptionPane.INFORMATION_MESSAGE, null, timeOptions, timeOptions[0]);

        switch (choice) {
            case "30":
                TTTServer.GMAE_TIME_COUNT = 30;
                break;
            case "60":
                TTTServer.GMAE_TIME_COUNT = 60;
                break;
            case "90":
                TTTServer.GMAE_TIME_COUNT = 90;
                break;
            case "120":
                TTTServer.GMAE_TIME_COUNT = 120;
                break;
            case "180":
                TTTServer.GMAE_TIME_COUNT = 120;
                break;
            default:
                System.exit(-1);
        }

        ServerGameWindow sgw;
        try {

            ServerSocket serverSocket = new ServerSocket(8000);
            ServerConnectionWindow.jtaLog.append(new Date() + ": Server started at port 8000\n");
            sgrw = new ServerGameResultsWindow();
            int sessionNo = 1;

            while (true) {
                ServerConnectionWindow.jtaLog.append(new Date() + ": Server waiting for players to join session " + sessionNo + '\n');
                Socket player1 = serverSocket.accept();
                ServerConnectionWindow.jtaLog.append(new Date() + ": Player 1 connected the server and joined session " + sessionNo + '\n');
                ServerConnectionWindow.jtaLog.append("Player 1's IP address" + player1.getInetAddress().getHostAddress() + '\n');

                String player1HostName = player1.getInetAddress().getHostName();

                new DataOutputStream(player1.getOutputStream()).writeInt(PLAYER1);

                Socket player2 = serverSocket.accept();
                ServerConnectionWindow.jtaLog.append(new Date() + ": Player 2 connected the server and joined session " + sessionNo + '\n');
                ServerConnectionWindow.jtaLog.append("Player 2's IP address" + player2.getInetAddress().getHostAddress() + '\n');

                String player2HostName = player2.getInetAddress().getHostName();

                new DataOutputStream(player2.getOutputStream()).writeInt(PLAYER2);

                sgw = new ServerGameWindow(sessionNo, player1HostName, player2HostName);

                //Thread clockThread = new Thread(new CountDownClock(sgw.timeLabel));
                //clockThread.start();
                ServerSessionHandler gameSession = new ServerSessionHandler(player1, player2, sessionNo, sgw);

                ServerConnectionWindow.jtaLog.append(new Date() + ": Start a thread for session " + sessionNo++ + '\n' + '\n' + '\n');

                new Thread(gameSession).start();

            }

        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

}
