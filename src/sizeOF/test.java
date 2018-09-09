package sizeOF;
import net.sourceforge.sizeof.SizeOf;

public class test {

    public static void main(String args[])
    {
        Frame frame = new Frame(new NetworkPacket(),0,0,0);
        SizeOf.skipStaticField(true);
        SizeOf.setMinSizeToLog(10);

        //calculate object size
        System.out.println(SizeOf.deepSizeOf(frame));
    }
}
