package de.paesserver.goShimmerApi.broadcast;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public class Broadcast {
    public final int port;
    public final String host;
    private Socket socket;

    public Broadcast(String host, int port){
        this.port = port;
        this.host = host;
    }

    public Broadcast(){
        this.port = 5050;
        this.host = "localhost";
    }

    public Broadcast connect() throws IOException {
        System.out.print("Connecting to socket... ");
        socket = new Socket(host,port);
        System.out.print("done\n");
        return this;
    }

    public Thread getNewListener(){
        return new Thread(()->{
            System.out.println("Starting listener: " + host + ":" + port);
            try ( InputStream inputStream = socket.getInputStream()){
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                for (int result = bufferedInputStream.read(); result != -1; result = bufferedInputStream.read()) {
                    System.out.print(new String(new byte[]{(byte) result}));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
