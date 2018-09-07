package SelectiveRepeat;

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
    private Frame frame;
    private PhysicalLayerThread physicalLayerThread;
    private boolean isCommunicationDone,isFrameSet;
    private List<PhysicalLayerListener> physicalLayerListeners = new ArrayList<>();


    public PhysicalLayer(int port) {
        try {
            setServerSocket(new ServerSocket(port));
            setCommunicationDone(false);
            System.out.println("Sender Listening...");
            setSocket(serverSocket.accept());
            System.out.println("Connected to " + socket.getInetAddress() + ":" + socket.getPort());
            setDuplexStreams();
            setPhysicalLayerThread(new PhysicalLayerThread(this));
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
            setDuplexStreams();
            setPhysicalLayerThread(new PhysicalLayerThread(this));
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
        Frame frame = getFrame();
        notify();
        return frame;
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

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        try {
            this.frame = frame;
            wait();
            for (PhysicalLayerListener pll : physicalLayerListeners) {
                pll.onFrameArrival();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public PhysicalLayerThread getPhysicalLayerThread() {
        return physicalLayerThread;
    }

    public void setPhysicalLayerThread(PhysicalLayerThread physicalLayerThread) {
        this.physicalLayerThread = physicalLayerThread;
    }

    public boolean isCommunicationDone() {
        return isCommunicationDone;
    }

    public void setCommunicationDone(boolean communicationDone) {
        isCommunicationDone = communicationDone;
    }

    public boolean isFrameSet() {
        return isFrameSet;
    }

    public void setFrameSet(boolean frameSet) {
        isFrameSet = frameSet;
    }

    public void addPhysicalLayerListener(PhysicalLayerListener physicalLayerListener){
        physicalLayerListeners.add(physicalLayerListener);
    }

}
