/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc_dr_usb_com_console.USB_ST32F103R8T6;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 *
 * @author slava
 */
public class TaskTxStructure {
    
    private final static int length = 1+4+2*4;
    
    private byte[] byteTaskTxStructure = new byte[length];
    
    public static int getLengthStructure()
    {
        return length;
    }
    
    public class Data
    {
        public short   command;
        public long    step_xyzw; 
        public int     speed_x;
        public int     speed_y;
        public int     speed_z;
        public int     speed_w;
    }
    public Data GetNewData()
    {
        return new Data();
    }
    
    public Data[] GetNewDataList(int count)
    {
        Data[] d = new Data[count];
        for(int i=0; i< d.length; i++) d[i] = new Data();
        return d;
    }
    
    private ArrayList<Data> DataArrayList;
    
    public ArrayList<Data> GetNewDataArrayList()
    {
        DataArrayList = new ArrayList<Data>();
        return DataArrayList;
    }
    
    private short   command;
    private long    step_xyzw; 
    private int     speed_x;
    private int     speed_y;
    private int     speed_z;
    private int     speed_w;
        
//        ByteBuffer buf = ByteBuffer.allocate(1+4+2*4);
//        buf.put(new Short((short)5).byteValue());//0..255
//        buf.putInt(Integer.reverseBytes(new Long(40000).intValue()));//0..4294967295
//        buf.putShort(Short.reverseBytes((short)-50));//-32768 до 32767
//        buf.putShort(Short.reverseBytes((short)50));
//        buf.putShort(Short.reverseBytes((short)-50));
//        buf.putShort(Short.reverseBytes((short)50));
//        byte[] bytes = buf.array();
       
//        ByteBuffer.allocate(4).putShort(Short.reverseBytes((short)-50)).array();
    
//        List<Byte> fg = new ArrayList<Byte>();
//        Byte[] byteArr = new Byte[5];
//        List<Byte> byteList;
//        byteList = Arrays.asList(byteArr);
//        Collections.reverse(byteList);
        //Long.intValue();
        //short	16 бит	от -32768 до 32767
        //byte	8 бит	от -128 до 127
        //int	32 бит	от -2147483648 до 2147483647
//        Long ll = new Long(40000);
//        System.out.println(ll.shortValue());
//        Short sh = new Short((short)200);
//        System.out.println(sh.byteValue());
          //Byte.toUnsignedInt(-120);    
        
       public byte[] getBytesTaskTxStructure()
       {
            byte[] bytes = null;
           
            ByteBuffer buf = ByteBuffer.allocate(length);
            buf.put(new Short((short)command).byteValue());//0..255
            buf.putInt(Integer.reverseBytes(new Long(step_xyzw).intValue()));//0..4294967295
            buf.putShort(Short.reverseBytes((short)speed_x));//-32768 до 32767
            buf.putShort(Short.reverseBytes((short)speed_y));
            buf.putShort(Short.reverseBytes((short)speed_z));
            buf.putShort(Short.reverseBytes((short)speed_w));
            bytes = buf.array();
            int i = 0;
            for(byte b:bytes) {byteTaskTxStructure[i] = b; i++;}
           return bytes;
       }
        
       public void setDataTaskTxStructure(short command, long step_xyzw, int speed_x, int speed_y, int speed_z, int speed_w)
       {
            this.command = command;
            this.step_xyzw = step_xyzw; 
            this.speed_x = speed_x;
            this.speed_y = speed_y;
            this.speed_z = speed_z;
            this.speed_w = speed_w;
       }
       
       public void setDataTaskTxStructure(Data w)
       {
            this.command = w.command;
            this.step_xyzw = w.step_xyzw; 
            this.speed_x = w.speed_x;
            this.speed_y = w.speed_y;
            this.speed_z = w.speed_z;
            this.speed_w = w.speed_w;
       }
}
