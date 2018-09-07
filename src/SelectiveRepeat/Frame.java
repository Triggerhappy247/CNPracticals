package SelectiveRepeat;

import java.io.Serializable;

public class Frame implements Serializable {
    private NetworkPacket networkPacket;
    private int sequenceNumber;
    private int acknowledgmentNumber;
    private int frameType;

    public Frame(NetworkPacket networkPacket, int sequenceNumber, int acknowledgmentNumber, int frameType) {
        setNetworkPacket(networkPacket);
        setSequenceNumber(sequenceNumber);
        setAcknowledgmentNumber(acknowledgmentNumber);
        setFrameType(frameType);
    }

    public NetworkPacket getNetworkPacket() {
        return networkPacket;
    }

    public void setNetworkPacket(NetworkPacket networkPacket) {
        this.networkPacket = networkPacket;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getAcknowledgmentNumber() {
        return acknowledgmentNumber;
    }

    public void setAcknowledgmentNumber(int acknowledgmentNumber) {
        this.acknowledgmentNumber = acknowledgmentNumber;
    }

    public int getFrameType() {
        return frameType;
    }

    public void setFrameType(int frameType) {
        this.frameType = frameType;
    }
}
