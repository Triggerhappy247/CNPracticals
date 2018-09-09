package Protocol;

import java.io.Serializable;


public class NetworkPacket implements Serializable {
    public transient static final int MAX_PKT_SIZE = 1024;
    public static final int DATA = 0;
    public static final int STOP = 1;
    private int packetType;
    private byte data[];

    public NetworkPacket() {
    }

    public NetworkPacket(int size) {
        setData(new byte[size]);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }
}
