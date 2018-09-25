/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc_dr_usb_com_console.USB_ST32F103R8T6;

import java.nio.ByteBuffer;

/**
 *
 * @author slava
 */
public class StatusData {    
    
    public class Struct
    {
    	public short   StatusFlag;
	public long    IndexWriteTask;
	public long    IndexCurrentRun;
	public int     AllStepX, AllStepY, AllStepZ, AllStepW;
	public long    StepTask;
    }
    
    public final int LengthTaskRun = 64;
    
    public Struct struct = new Struct();
    
    private short   StatusFlag;
    private long    IndexWriteTask;
    private long    IndexCurrentRun;
    private int     AllStepX, AllStepY, AllStepZ, AllStepW;
    private long    StepTask;
    private final int lengtch = 32;
        
    public short getStatusFlag() {
        return StatusFlag;
    }

    public long getIndexWriteTask() {
        return IndexWriteTask;
    }

    public long getIndexCurrentRun() {
        return IndexCurrentRun;
    }

    public int getAllStepX() {
        return AllStepX;
    }

    public int getAllStepY() {
        return AllStepY;
    }

    public int getAllStepZ() {
        return AllStepZ;
    }

    public int getAllStepW() {
        return AllStepW;
    }

    public long getStepTask() {
        return StepTask;
    }
    
    private ByteBuffer buf = ByteBuffer.allocate(lengtch);
    
    public void SetStatusData(byte[] b)
    {
        if(b == null) return; 
        if(b.length > lengtch) return; 
        
        buf.clear();
        //System.out.println("b=" + b.length);
        buf.put(b);
        buf.rewind();
        int ii = buf.getInt(0);
        ii = Integer.reverseBytes(ii);
        StatusFlag = (short)Integer.toUnsignedLong(ii);
        ii = buf.getInt(4);
        ii = Integer.reverseBytes(ii);
        IndexWriteTask = Integer.toUnsignedLong(ii);
        ii = buf.getInt(8);
        ii = Integer.reverseBytes(ii);
        IndexCurrentRun = Integer.toUnsignedLong(ii);
        ii =  buf.getInt(12);        
        AllStepX = Integer.reverseBytes(ii);
        ii = buf.getInt(16);
        AllStepY = Integer.reverseBytes(ii);
        ii = buf.getInt(20);        
        AllStepZ = Integer.reverseBytes(ii);
        ii = buf.getInt(24);  
        AllStepW = Integer.reverseBytes(ii);
        ii = buf.getInt(28);
        ii = Integer.reverseBytes(ii);
        StepTask = Integer.toUnsignedLong(ii);
        
        struct.StatusFlag = StatusFlag;
        struct.IndexWriteTask = IndexWriteTask;
        struct.IndexCurrentRun = IndexCurrentRun;
        struct.AllStepX = AllStepX;
        struct.AllStepY = AllStepY;
        struct.AllStepZ = AllStepZ;
        struct.AllStepW = AllStepW;
        struct.StepTask = StepTask;
        
        
        //buf = null;
    }
      
    public void StatusDataPrint()
    {
        System.out.println("StatusData .... ");
        System.out.println("-------------------------------------");
        System.out.println("StatusFlag:" + StatusFlag);
        System.out.println("IndexWriteTask:" + IndexWriteTask);
        System.out.println("IndexCurrentRun:" + IndexCurrentRun);
        System.out.println("AllStepX:" + AllStepX);
        System.out.println("AllStepY:" + AllStepY);
        System.out.println("AllStepZ:" + AllStepZ);
        System.out.println("AllStepW:" + AllStepW);
        System.out.println("StepTask:" + StepTask);
        System.out.println("-------------------------------------");
    }
    
        public void StatusDataPrintToOneLine()
    {

        System.out.println("StatusFlag:" + StatusFlag+
        "  IndexWriteTask:" + IndexWriteTask +
        "  IndexCurrentRun:" + IndexCurrentRun+
        "  AllStepX:" + AllStepX+
        "  AllStepY:" + AllStepY+
        "  AllStepZ:" + AllStepZ+
        "  AllStepW:" + AllStepW+
        "  StepTask:" + StepTask);
    }
}
