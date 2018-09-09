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
    private int frameSent,frameReceived;

    public PhysicalLayer(int port) {
        try {
            frameSent = frameReceived = 0;
            setServerSocket(new ServerSocket(port));
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
            frameSent = frameReceived = 0;
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
            frameSent++;
            System.out.println(frameSent + " frames Sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Frame fromPhysicalLayer(){
        try {
            frameReceived++;
            System.out.println(frameReceived + " frames Received");
            return (Frame)objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stopPhysicalLayer(){
        System.out.println("Physical Layer Done");
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

    public int getFrameSent() {
        return frameSent;
    }

    public void setFrameSent(int frameSent) {
        this.frameSent = frameSent;
    }

    public int getFrameReceived() {
        return frameReceived;
    }

    public void setFrameReceived(int frameReceived) {
        this.frameReceived = frameReceived;
    }
}
