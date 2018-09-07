package SelectiveRepeat;

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

    public PhysicalLayer(int port) {
        try {
            setServerSocket(new ServerSocket(port));
            System.out.println("Sender Listening...");
            setSocket(serverSocket.accept());
            System.out.println("Connected to " + socket.getInetAddress() + ":" + socket.getPort());
            setDuplexStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PhysicalLayer(String host,int port) {
        try {
            System.out.println("Receiver Requesting...");
            setSocket(new Socket(host,port));
            System.out.println("Connected to " + socket.getInetAddress() + ":" + socket.getPort());
            setDuplexStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDuplexStreams() throws IOException{
        setObjectInputStream((ObjectInputStream) socket.getInputStream());
        setObjectOutputStream((ObjectOutputStream) socket.getOutputStream());
    }

    public void toPhysicalLayer(Frame frame){
        try {
            objectOutputStream.writeObject(frame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Frame fromPhysicalLayer(){
        Frame frame;
        try {
            frame = (Frame) objectInputStream.readObject();
            return frame;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
}
