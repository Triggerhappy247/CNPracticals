package Protocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PhysicalLayer {

    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private boolean isCommunicationDone;


    public PhysicalLayer(int port) {
        try {
            setServerSocket(new ServerSocket(port));
            setCommunicationDone(false);
            System.out.println("Sender Listening...");
            setSocket(serverSocket.accept());
            System.out.println("Connected to " + socket.getInetAddress() + ":" + socket.getPort());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PhysicalLayer(String host,int port) {
        try {
            setCommunicationDone(false);
            System.out.println("Receiver Requesting...");
            setSocket(new Socket(host,port));
            System.out.println("Connected to " + socket.getInetAddress() + ":" + socket.getPort());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toPhysicalLayer(Frame frame){
        try {
            objectOutputStream.writeObject(frame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Frame fromPhysicalLayer(){
        try {
            return (Frame)objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stopPhysicalLayer(){
        try {
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public void setObjectInputStream(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    public boolean isCommunicationDone() {
        return isCommunicationDone;
    }

    public void setCommunicationDone(boolean communicationDone) {
        isCommunicationDone = communicationDone;
        if(isCommunicationDone)
            stopPhysicalLayer();
    }

}
