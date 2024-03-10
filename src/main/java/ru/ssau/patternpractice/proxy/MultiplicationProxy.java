package ru.ssau.patternpractice.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiplicationProxy {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public MultiplicationProxy() throws IOException {
        this("127.0.0.1", 5000);
    }

    public MultiplicationProxy(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public double multiply(double d1, double d2) throws IOException {
        String msg = String.valueOf(d1) + " " + String.valueOf(d2);
        out.println(msg);
        return Double.parseDouble(in.readLine());
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
