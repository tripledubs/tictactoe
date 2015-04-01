package controller;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.Timer;
import model.GameResults;
import model.TTTConstants;
import view.ServerGameWindow;

public class ServerSessionHandler implements Runnable, TTTConstants {

    private Socket player1;
    private Socket player2;

    public ServerGameWindow sgw;

    private char[][] cell = new char[3][3];

    private DataInputStream fromPlayer1;
    private DataOutputStream toPlayer1;
    private DataInputStream fromPlayer2;
    private DataOutputStream toPlayer2;

    private boolean continueToPlay = true;
    private boolean player1Turn;
    private boolean player2Turn;

    private ArrayList<GameResults> gameRecords = new ArrayList<>();
    private int sessionNo;

    Timer timer;

    ServerSessionHandler(Socket player1, Socket player2, int sessionNo, ServerGameWindow sgw) {
        this.player1 = player1;
        this.player2 = player2;
        this.sessionNo = sessionNo;
        this.sgw = sgw;

        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < 3; j++) {
                cell[k][j] = ' ';
            }
        }

    }

    @Override
    public void run() {
        try {
            fromPlayer1 = new DataInputStream(player1.getInputStream());
            toPlayer1 = new DataOutputStream(player1.getOutputStream());
            fromPlayer2 = new DataInputStream(player2.getInputStream());
            toPlayer2 = new DataOutputStream(player2.getOutputStream());

            toPlayer1.writeUTF("Start");

            TimeClass tc = new TimeClass(sgw.player1TimeLabel, sgw.player2TimeLabel);
            timer = new Timer(1000, tc);
            timer.start();

            while (continueToPlay) {
                //if(player1MoveCounter==0)player1Timer.start();
                player1Turn = true;
                player2Turn = false;

                int row = fromPlayer1.readInt();
                int column = fromPlayer1.readInt();

                player1Turn = false;

                //player1MoveCounter++;
                cell[row][column] = 'X';
                sgw.gameButton[row][column].setText("X");
                sgw.gameButton[row][column].setEnabled(false);
                sgw.turnLabel.setText("Player2's turn: 'O'");

                if (isWon('X')) {
                    toPlayer1.writeInt(PLAYER1_WIN);
                    toPlayer2.writeInt(PLAYER1_WIN);
                    sendMove(toPlayer2, row, column);
                    setResultLabel('X');
                    recordGameResult('X');
                    timer.stop();
                    break;

                } else if (isFull()) {
                    toPlayer1.writeInt(DRAW_GAME);
                    toPlayer2.writeInt(DRAW_GAME);
                    sendMove(toPlayer2, row, column);
                    setResultLabel(' ');
                    recordGameResult(' ');
                    timer.stop();
                    break;
                } else {
                    toPlayer2.writeInt(GAME_CONTINUE);
                    sendMove(toPlayer2, row, column);

                    player2Turn = true;
                }

                row = fromPlayer2.readInt();
                column = fromPlayer2.readInt();
                player2Turn = false;
                cell[row][column] = 'O';
                sgw.gameButton[row][column].setText("O");
                sgw.gameButton[row][column].setEnabled(false);
                sgw.turnLabel.setText("Player1's turn: 'X'");

                if (isWon('O')) {
                    toPlayer1.writeInt(PLAYER2_WIN);
                    toPlayer2.writeInt(PLAYER2_WIN);
                    sendMove(toPlayer1, row, column);
                    setResultLabel('O');
                    recordGameResult('O');
                    timer.stop();
                    break;

                } else if (isFull()) {
                    toPlayer1.writeInt(DRAW_GAME);
                    toPlayer2.writeInt(DRAW_GAME);
                    sendMove(toPlayer1, row, column);
                    setResultLabel(' ');
                    recordGameResult(' ');
                    timer.stop();
                    break;
                } else {
                    toPlayer1.writeInt(GAME_CONTINUE);
                    player1Turn = true;
                    sendMove(toPlayer1, row, column);
                }
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private class TimeClass implements ActionListener, TTTConstants {

        JLabel timeLabel1, timeLabel2;
        int player1Counter = TTTServer.GMAE_TIME_COUNT;
        int player2Counter = TTTServer.GMAE_TIME_COUNT;

        public TimeClass(JLabel jlb1, JLabel jlb2) {
            timeLabel1 = jlb1;
            timeLabel2 = jlb2;

        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (player1Turn == true) {
                player1Counter--;
                if (player1Counter >= 1) {
                    timeLabel1.setText("Time left: " + player1Counter + " seconds");
                } else {
                    timer.stop();
                    timeLabel1.setText("Time is up");
                    continueToPlay = false;
                    try {
                        toPlayer1.writeInt(PLAYER2_WIN);
                        toPlayer2.writeInt(PLAYER2_WIN);
                    } catch (IOException ex) {
                        System.err.println(ex);
                    }
                    setResultLabel('O');
                    recordGameResult('O');
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            if (player2Turn == true) {
                player2Counter--;
                if (player2Counter >= 1) {
                    timeLabel2.setText("Time left: " + player2Counter + " seconds");
                } else {
                    timer.stop();
                    timeLabel2.setText("Time is up");
                    continueToPlay = false;
                    try {
                        toPlayer1.writeInt(PLAYER1_WIN);
                        toPlayer2.writeInt(PLAYER1_WIN);
                    } catch (IOException ex) {
                        System.err.println(ex);
                    }
                    setResultLabel('X');
                    recordGameResult('X');
                    Toolkit.getDefaultToolkit().beep();
                }
            }

        }

    }

    private void recordGameResult(char result) {
        String IP1 = player1.getInetAddress().getHostAddress();
        String IP2 = player2.getInetAddress().getHostAddress();
        String hn1 = player1.getInetAddress().getHostName();
        String hn2 = player2.getInetAddress().getHostName();
        GameResults gameResults;
        if (result == 'X') {
            gameResults = new GameResults(sessionNo, IP1, IP2, hn1, hn2, PLAYER1_WIN);
        } else if (result == 'O') {
            gameResults = new GameResults(sessionNo, IP1, IP2, hn1, hn2, PLAYER2_WIN);
        } else {
            gameResults = new GameResults(sessionNo, IP1, IP2, hn1, hn2, DRAW_GAME);
        }
        synchronized (gameRecords) {
            try {
                gameRecords.add(gameResults);
                for (Iterator it = gameRecords.iterator(); it.hasNext();) {
                    SerializationUtil.serialize(it.next(), "gameRecords.txt");
                    GameResults gamePrint = (GameResults) SerializationUtil.deserialize("gameRecords.txt");
                    TTTServer.sgrw.gameResultsLog.append("Session#  |  Player1 HostName  |   Player2 HostName   |   Game  Result    |" + '\n');
                    TTTServer.sgrw.gameResultsLog.append("          " + gamePrint.sessionNum + "              " + gamePrint.player1HostName + "         " + gamePrint.player2HostName + "            ");
                    if (gamePrint.record == 1) {
                        TTTServer.sgrw.gameResultsLog.append(gamePrint.player1HostName + " won" + '\n' + '\n');
                    } else if (gamePrint.record == 2) {
                        TTTServer.sgrw.gameResultsLog.append(gamePrint.player2HostName + " won" + '\n' + '\n');
                    } else {
                        TTTServer.sgrw.gameResultsLog.append("Draw Game" + '\n' + '\n');
                    }

                    System.out.println("Session Number: " + gamePrint.sessionNum + " Player1's IP: " + gamePrint.player1IPAddress + " Player2's IP: " + gamePrint.player2IPAddress);
                    System.out.println("Player1 HostName: " + gamePrint.player1HostName + " Player2 HostName: " + gamePrint.player2HostName);
                    if (gamePrint.record == 1) {
                        System.out.println("Game Result: Player 1 won");
                    } else if (gamePrint.record == 2) {
                        System.out.println("Game Result: Player 2 won");
                    } else {
                        System.out.println("Game Result: Draw Game");
                    }

                }
            } catch (IOException ie) {
                System.err.println(ie);
            } catch (ClassNotFoundException ex) {
                System.err.println(ex);
            }
        }
    }

    private void setResultLabel(char token) {
        sgw.turnLabel.setFont(sgw.font1);
        sgw.turnLabel.setForeground(Color.red);
        if (token == 'X') {
            sgw.turnLabel.setText("Winer is " + token + " " + sgw.player1Hostname);
        } else if (token == 'O') {
            sgw.turnLabel.setText("Winer is " + token + " " + sgw.player2Hostname);
        } else {
            sgw.turnLabel.setText("No Winner!!Draw Game!!");

        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sgw.gameButton[i][j].setEnabled(false);
            }
        }
    }

    private void sendMove(DataOutputStream out, int row, int column) throws IOException {
        out.writeInt(row);
        out.writeInt(column);
    }

    private boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cell[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isWon(char token) {
        for (int i = 0; i < 3; i++) {
            if ((cell[i][0] == token) && (cell[i][1] == token) && (cell[i][2] == token)) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if ((cell[0][i] == token) && (cell[1][i] == token) && (cell[2][i] == token)) {
                return true;
            }
        }

        if ((cell[0][0] == token) && (cell[1][1] == token) && (cell[2][2] == token)) {
            return true;
        }

        else if ((cell[0][2] == token) && (cell[1][1] == token) && (cell[2][0] == token)) {
            return true;
        }

        return false;
    }

}
