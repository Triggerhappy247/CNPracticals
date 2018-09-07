package SocketTest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTestServer {

    public static void main(String args[]) {
        try {
            ServerSocket serverSocket = new ServerSocket(800);
            ObjectInputStream objectInputStream;
            ObjectOutputStream objectOutputStream;
            Socket socket = serverSocket.accept();
            System.out.println("Connected!" + socket.getInetAddress()+":"+socket.getPort());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Frame received;
            while(true)
            {
                received = PhysicalLayer.fromPhysicalLayer(objectInputStream);
                System.out.println("Recieved frame " + received.getIntData());
                System.out.println("Data is: " + received.getStringData());
                PhysicalLayer.toPhysicalLayer(new Frame("Recieved Frame",received.getIntData()),objectOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
