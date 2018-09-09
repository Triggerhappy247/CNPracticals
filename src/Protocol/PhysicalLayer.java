package Protocol;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PhysicalLayer {

    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private BufferedInputStream bufferedInputStream;
    private PhysicalLayerFrameArrival physicalLayerFrameArrival;
    private List<FrameArrivalListener> frameArrivalListeners = new ArrayList<>();
    private boolean stopFrameArrival = false;
    private int frameSent,frameReceived;

    public PhysicalLayer(int port) {
        try {
            frameSent = frameReceived = 0;
            setServerSocket(new ServerSocket(port));
            System.out.println("Sender Listening...");
            setSocket(serverSocket.accept());
            System.out.println("Connected to " + socket.getInetAddress() + ":" + socket.getPort());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            physicalLayerFrameArrival = new PhysicalLayerFrameArrival(this);
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
            bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            physicalLayerFrameArrival = new PhysicalLayerFrameArrival(this);
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
            Thread.sleep(1000);
            setStopFrameArrival(true);
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addFrameArrivalListeners(FrameArrivalListener frameArrivalListener){
        frameArrivalListeners.add(frameArrivalListener);
    }

    public List<FrameArrivalListener> getFrameArrivalListeners() {
        return frameArrivalListeners;
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

    public BufferedInputStream getBufferedInputStream() {
        return bufferedInputStream;
    }

    public boolean isStopFrameArrival() {
        return stopFrameArrival;
    }

    public void setStopFrameArrival(boolean stopFrameArrival) {
        this.stopFrameArrival = stopFrameArrival;
    }

    public void setBufferedInputStream(BufferedInputStream bufferedInputStream) {
        this.bufferedInputStream = bufferedInputStream;
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
