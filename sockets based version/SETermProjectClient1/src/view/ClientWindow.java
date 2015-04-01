/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import model.Cell;

/**
 *
 * @author George Wan
 */
public class ClientWindow extends JFrame {
    public JLabel jlbTitle = new JLabel();
    public JLabel jlbStatus = new JLabel();
    public Cell [][] cell = new Cell[3][3];
    
    public ClientWindow(){
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3,3,0,0));
        for(int i=0; i<3; i++)
            for (int j=0; j<3; j++)
                p.add(cell[i][j] = new Cell(i,j));
        
        p.setBorder(new LineBorder(Color.BLACK, 1));
        
        jlbTitle.setHorizontalAlignment(JLabel.CENTER);
        jlbTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        jlbTitle.setBorder(new LineBorder(Color.BLACK, 1));
        jlbStatus.setBorder(new LineBorder(Color.BLACK, 1));
        
        add(jlbTitle, BorderLayout.NORTH);
        add(p, BorderLayout.CENTER);
        add(jlbStatus, BorderLayout.SOUTH);
        
        setSize(500, 500);
        setTitle("Client");
        setLocation(200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
    }
}
