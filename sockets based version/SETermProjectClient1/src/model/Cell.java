
package model;

import controller.ClientSessionHandler;
import controller.TTTClient;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public  class Cell extends JPanel{
        private int row;
        private int column;

        private char token = ' ';

    public Cell(int row, int column){
            this.row = row;
            this.column = column;
            this.setBorder(new LineBorder(Color.BLACK, 1));
            this.addMouseListener(new ClickListener());
            //setBackground(Color.blue);
        }

   
        public char getToken(){
            return token;
        }
        public void setToken(char token){
            this.token = token;
            repaint();
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setColor(Color.red);

            if(token =='X') {
                g.drawLine(10, 10, getWidth()-10, getHeight()-10);
                g.drawLine(getWidth()-10, 10, 10, getHeight()-10);
            }else if(token == 'O'){
                g.drawOval(10, 10, getWidth()-20, getHeight()-20);
            }
        }

        private class ClickListener extends MouseAdapter{
            @Override
            public void mouseClicked(MouseEvent e){
                if(token == ' ' && ClientSessionHandler.myTurn){
                    setToken(ClientSessionHandler.myToken);
                    ClientSessionHandler.myTurn = false;
                    ClientSessionHandler.rowSelected = row;
                    ClientSessionHandler.columnSelected = column;
                    TTTClient.cw.jlbStatus.setText("Wating for the other player to move");
                    ClientSessionHandler.waiting = false;
                    
                }
            }
        }
    }