package SocketTest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketTestClient {

    public static void main(String args[]){
        try {
            Socket clientSocket = new Socket("127.0.0.1",800);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            Scanner scanner = new Scanner(System.in);
            int i = 0;
            Frame send,ack;
            while (true)
            {
                System.out.println("Enter Message: ");
                send = new Frame(scanner.nextLine(),i++);
                PhysicalLayer.toPhysicalLayer(send,objectOutputStream);
                ack = PhysicalLayer.fromPhysicalLayer(objectInputStream);
                System.out.println(ack.getStringData() + " " + ack.getIntData());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
