/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.StartServer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.GrayFilter;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author George Wan
 */
public class ServerConnectionWindow extends JFrame {

    public static JTextArea jtaLog;
    private JMenuItem startMenuItem;
    private JMenuItem exitMenuItem;

    private JMenuItem connectionMenuItem;
    private JMenuItem resultMenuItem;

    private final Image launcherImage;

    public ServerConnectionWindow() {
        String imagePath = System.getProperty("user.dir");
        // separator: Windows '\', Linux '/'
        String separator = System.getProperty("file.separator");
        launcherImage = getImage(imagePath + separator + "ImagesFolder" + separator
                + "tictactoe3.jpg");

        Container contentPane = getContentPane();
        jtaLog = new JTextArea() {
            Image grayImage = GrayFilter.createDisabledImage(launcherImage);

            {
                setOpaque(false);
            }

            @Override
            public void paint(Graphics g) {
                g.drawImage(grayImage, 0, 0, this);
                super.paint(g);
            }
        };
        Font font = new Font("Verdana", Font.BOLD, 11);
        jtaLog.setFont(font);
        //setForeground(Color.BLUE);
        //jtaLog.setForeground(Color.BLUE);

        JScrollPane scrollPane = new JScrollPane(jtaLog);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu serverMenu = new JMenu("Server");
        menuBar.add(serverMenu);

        ServerConnectionWindow.MenuListener listener = new ServerConnectionWindow.MenuListener();

        startMenuItem = new JMenuItem("Start");
        serverMenu.add(startMenuItem);
        startMenuItem.addActionListener(listener);

        exitMenuItem = new JMenuItem("Exit");
        serverMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(listener);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenu saveMenu = new JMenu("Save");
        fileMenu.add(saveMenu);

        connectionMenuItem = new JMenuItem("Login Records");
        saveMenu.add(connectionMenuItem);
        connectionMenuItem.addActionListener(listener);

        resultMenuItem = new JMenuItem("Game Results");
        saveMenu.add(resultMenuItem);
        resultMenuItem.addActionListener(listener);

        //setFont(new Font("Courier New", Font.BOLD, 18)); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 550);
        setResizable(false);
        setTitle("TicTacToeServer");
        setVisible(true);

    }

    public static Image getImage(String fileName) {
        Image image = null;
        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException ioe) {
            System.out.println("Error: Cannot open image:" + fileName);
            JOptionPane.showMessageDialog(null, "Error: Cannot open image:" + fileName);
        }
        return image;
    }

    private class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            Object source = event.getSource();

            if (source == startMenuItem) {
                new Thread(new StartServer()).start();
            } else if (source == exitMenuItem) {
                int answer = JOptionPane.showConfirmDialog(null, "Are you sure to exit?", "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
                if (answer == 0) {
                    System.exit(0);
                }

            } else if (source == connectionMenuItem) {
                //canvas.moveRectangle(0, -1);
            } else if (source == resultMenuItem) {

            }
        }
    }

}
