package controller;

import model.TTTConstants;
import view.ServerConnectionWindow;
import view.ServerGameResultsWindow;

public class TTTServer implements TTTConstants {

    public static ServerConnectionWindow scw;
    public static ServerGameResultsWindow sgrw;
    public static int GMAE_TIME_COUNT;

    public static void main(String[] args) {
        scw = new ServerConnectionWindow();
    }

}
