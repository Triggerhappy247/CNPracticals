package SocketTest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PhysicalLayer {

    public static Frame fromPhysicalLayer(ObjectInputStream objectInputStream){
        Frame frame = null;
        try {
            frame = (Frame) objectInputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return frame;
    }

    public static void toPhysicalLayer(Frame frame, ObjectOutputStream objectOutputStream){
        try {
            objectOutputStream.writeObject(frame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
