package wind.ibroker;

import wind.ibroker.socket.MsgConnect;

import wind.ibroker.view.BrokerLogin;

import java.io.*;

public class Main {

    public static void main(String[] args){

        BrokerLogin.getInstance().start();
        MsgConnect.login();
    }
 }
