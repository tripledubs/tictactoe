package model;

import java.io.Serializable;

public class GameResults implements TTTConstants, Serializable {

    public String player1IPAddress;
    public String player2IPAddress;
    public String player1HostName;
    public String player2HostName;
    public int sessionNum;
    public int record;

    public GameResults(int snum, String IP1, String IP2, String hn1, String hn2, int grst) {
        sessionNum = snum;
        player1IPAddress = IP1;
        player2IPAddress = IP2;
        player1HostName = hn1;
        player2HostName = hn2;
        record = grst;
    }

}
