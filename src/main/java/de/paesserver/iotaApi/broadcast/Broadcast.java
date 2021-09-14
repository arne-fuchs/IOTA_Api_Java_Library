package de.paesserver.iotaApi.broadcast;

import de.paesserver.iotaApi.message.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Broadcast {
    public final int port;
    public final String host;
    private Socket socket;
    private boolean terminate = false;

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

    public void terminate(){
        terminate = true;
    }

    public Thread startNewListener(){
        Thread thread = new Thread(()->{
            System.out.println("Starting listener: " + host + ":" + port);
            try (InputStream inputStream = socket.getInputStream()){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while (!terminate){
                    broadCastMessage(Message.parseToMessage(bufferedReader));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print("Listener closed");
        });
        thread.start();
        return thread;
    }

    CopyOnWriteArrayList<MessageEvent> classList = new CopyOnWriteArrayList<>();

    public void register(MessageEvent client){
        classList.add(client);
    }

    public boolean unregister(MessageEvent client){
        return classList.remove(client);
    }

    public void broadCastMessage(Message message){
        classList.stream().parallel().forEach(client -> client.receivedMessageEvent(message));
    }
}