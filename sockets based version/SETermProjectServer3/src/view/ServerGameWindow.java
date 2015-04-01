/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.TTTServer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ServerGameWindow extends JFrame {

    public JButton[][] gameButton = new JButton[3][3];
    public Font font1 = new Font("Courier New", Font.BOLD, 18);
    public Font font2 = new Font("Arial Black", Font.BOLD, 68);
    public JLabel turnLabel, player1TimeLabel, player2TimeLabel;
    public String player1Hostname, player2Hostname;
    int sessionNumber;

    public ServerGameWindow(int sNum, String hstname1, String hstname2) {
        sessionNumber = sNum;
        player1Hostname = hstname1;
        player2Hostname = hstname2;

        Container cp = getContentPane();
        JPanel upperPanel = new JPanel(new GridLayout(3, 1));

        JPanel p0 = new JPanel(new GridLayout(1, 3));
        p0.setBorder(new LineBorder(Color.GRAY, 1));
        JLabel player1Label = new JLabel(player1Hostname);
        player1Label.setHorizontalAlignment(JLabel.CENTER);
        p0.add(player1Label);

        JLabel vsLabel = new JLabel("V.S.");
        vsLabel.setForeground(Color.red);
        vsLabel.setFont(font1);
        vsLabel.setHorizontalAlignment(JLabel.CENTER);
        p0.add(vsLabel);

        JLabel player2Label = new JLabel(player2Hostname);
        player2Label.setHorizontalAlignment(JLabel.CENTER);
        p0.add(player2Label);

        JPanel p1 = new JPanel();
        p1.setBorder(new LineBorder(Color.GRAY, 1));
        turnLabel = new JLabel("Play Tic Tac Toe!", JLabel.CENTER);

        //turnLabel.setFont(font1);
        turnLabel.setBackground(Color.LIGHT_GRAY);

        p1.add(turnLabel);

        JPanel p2 = new JPanel(new GridLayout(1, 2));
        player1TimeLabel = new JLabel("TIME LEFT:" + TTTServer.GMAE_TIME_COUNT + " seconds", JLabel.CENTER);
        player1TimeLabel.setBackground(Color.BLACK);
        player1TimeLabel.setForeground(new Color(0, 165, 255));
        player1TimeLabel.setFont(new Font("DS-Digital", Font.PLAIN, 18));
        player1TimeLabel.setBorder(new LineBorder(Color.GRAY, 1));

        player2TimeLabel = new JLabel("TIME LEFT:" + TTTServer.GMAE_TIME_COUNT + " seconds", JLabel.CENTER);
        player2TimeLabel.setBackground(Color.BLACK);
        player2TimeLabel.setForeground(new Color(0, 165, 255));
        player2TimeLabel.setFont(new Font("DS-Digital", Font.PLAIN, 18));
        player2TimeLabel.setBorder(new LineBorder(Color.GRAY, 1));

        p2.add(player1TimeLabel);
        p2.add(player2TimeLabel);

        upperPanel.add(p0);
        upperPanel.add(p1);
        upperPanel.add(p2);

        JPanel p3 = new JPanel();
        p3.setLayout(new GridLayout(3, 3));// change to 3x3 Grid layout
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameButton[i][j] = new JButton();
                gameButton[i][j].setFont(font2);
                p3.add(gameButton[i][j]);
            }
        }

        cp.add(upperPanel, BorderLayout.NORTH);
        cp.add(p3, BorderLayout.CENTER);

        setSize(500, 500);
        setTitle("TicTacToeServerGame: Session " + sessionNumber);
        setLocation(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}
