/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author George Wan
 */
public class ServerGameResultsWindow extends JFrame {

    public JTextArea gameResultsLog;

    public ServerGameResultsWindow() {
        gameResultsLog = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(gameResultsLog);
        add(scrollPane, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setTitle("ServerGameResults");
        setVisible(true);
    }
}
