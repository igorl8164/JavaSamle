/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc_dr_usb_com_console.USB_ST32F103R8T6;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 *
 * @author slava
 */
public class USB_Connect {
    //http://www.quizful.net/post/java-serial-ports
    private static jssc.SerialPort serialPort=null;
    private String SerialPortName = "";
    private boolean isOpenPort = false;
    public void SetSerialPortName(String Name)
    {
        SerialPortName = Name;
    }
    
    private static final boolean debug_print = false;//false;//true;//выводить сообщения в консоль
    
    public void PrintSerialList()
    {
       /*
    Another possibility is to make a rules file in /etc/udev/rules.d/ directory. I had similar problem and I have created 50-myusb.rules 
    file in the above directory with this content:

KERNEL=="ttyACM[0-9]*",MODE="0666"
Note that this will give any device connected to ttyACM socket read/write permissions. If you need only specific device to get read/write permissions 
    you must also check idVendor and idProduct. You can find those by running lsusb command twice, once without your device connected and once when it is connected, 
    then observe the additional line in the output. There you will see something like Bus 003 Device 005: ID ffff:0005. In this case idVendor = ffff and idProduct = 0005. 
    Yours will be different. Than you modify the rules file to:

ACTION=="add", KERNEL=="ttyACM[0-9]*", ATTRS{idVendor}=="ffff", ATTRS{idProduct}=="0005", MODE="0666"
    
    или каждый раз при подключении
    sudo chmod a+rw /dev/ttyACM0

     cd /etc/udev/rules.d/
     sudo nano 50-myusb.rules
    KERNEL=="ttyACM[0-9]*",MODE="0666"
    ctrl+O, ctrl+X
    sudo udevadm control --reload-rules

    
    slava@slava-All-Series:/etc/udev/rules.d$ ls -la /dev/ttyACM0
    crw-rw---- 1 root dialout 166, 0 мар 11 19:23 /dev/ttyACM0   #group  dialout
    slava@slava-All-Series:/etc/udev/rules.d$ 
    $ sudo usermod -a -G dialout <username>  #your user name in Ubuntu
    $ sudo chmod a+rw /dev/ttyACM0
    
sudo usermod -a -G dialout slava
sudo chmod a+rw /dev/ttyACM0
    
    http://reactivated.net/writing_udev_rules.html
    */
        
        System.out.println("Serial Port Name");
            String[] portNames = jssc.SerialPortList.getPortNames();
            for(int i = 0; i < portNames.length; i++){
                System.out.println(portNames[i]);
            };    
    }
    
    
    public void Open()
    {
        //открытие порта 
        if(SerialPortName.isEmpty()) return;
        serialPort = new jssc.SerialPort(SerialPortName);//ttyACM0 ttyUSB0 /dev/ttyACM0
        try {
            
            serialPort.openPort();//Open serial port
            serialPort.setParams(   jssc.SerialPort.BAUDRATE_115200,
                            jssc.SerialPort.DATABITS_8,
                            jssc.SerialPort.STOPBITS_1, 
                            jssc.SerialPort.PARITY_NONE);
            
            //serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
            //serialPort.addEventListener(new EventListener());
            //serialPort.addEventListener(new EventListener(), SerialPort.MASK_RXCHAR);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
             
            serialPort.purgePort(SerialPort.PURGE_TXABORT |
                       SerialPort.PURGE_RXABORT |
                       SerialPort.PURGE_TXCLEAR |
                       SerialPort.PURGE_RXCLEAR);
                             
            isOpenPort = true;
        } catch (SerialPortException ex) {
            Logger.getLogger(USB_Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void Close()
    {
        //закрытие порта
        if(serialPort != null)
        {
            try {
                
                serialPort.purgePort(   SerialPort.PURGE_TXABORT |
                                        SerialPort.PURGE_RXABORT |
                                        SerialPort.PURGE_TXCLEAR |
                                        SerialPort.PURGE_RXCLEAR);
                
                //serialPort.removeEventListener();//EventListener(new EventListener());
                serialPort.closePort();//Close serial port
            } catch (SerialPortException ex) {
                Logger.getLogger(USB_Connect.class.getName()).log(Level.SEVERE, null, ex);
            }
            serialPort = null;
        }
        isOpenPort = false;
    }
    
    public void SendBytes(byte[] b)
    {
        if(!isOpenPort) return;
        try {
            //System.out.println(serialPort.getOutputBufferBytesCount());
            serialPort.writeBytes(b);//Write data to port
            //System.out.println(serialPort.getOutputBufferBytesCount());//количество байт в очереди
            //java.lang.Thread.sleep(100);
            SentPackageFlag = false;
        } catch (SerialPortException ex) {
            Logger.getLogger(USB_Connect.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public byte[] ReadByte()
    {
        
        byte[] bytes = null;
        //if(!NewPackageFlag) return bytes;
         //прием
        try {
            //System.out.println(serialPort.getInputBufferBytesCount());
            int n = serialPort.getInputBufferBytesCount();
            
            //if( bufferReadBytes != null )
            if(n>0)
            {
                bytes = new byte[n];
                //bytes = bufferReadBytes;
                bytes=serialPort.readBytes(n, 1000);//
                NewPackageFlag = false;
                //bufferReadBytes = null;
                //System.out.println(serialPort.getInputBufferBytesCount());
                if(debug_print) 
                {
                    System.out.println("readBytes:" + n);
                
                    if(bytes != null)
                    {
                        for(byte b:bytes/*stmp.getBytes()*/)
                        {
                            System.out.print("0x" + Integer.toHexString(Byte.toUnsignedInt(b)).toUpperCase()+" ");
                        }
                    }
                    System.out.println();
                }
            }
        } 
        catch (SerialPortException ex) {
            Logger.getLogger(USB_Connect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SerialPortTimeoutException ex) {
            Logger.getLogger(USB_Connect.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return bytes;
    }
    
    public void ClearBuffer()
    {
        try {
            serialPort.purgePort(SerialPort.PURGE_TXCLEAR | SerialPort.PURGE_RXCLEAR);
        } catch (SerialPortException ex) {
            Logger.getLogger(USB_Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Test()
    {
        if(!isOpenPort) return;
        //13.03.2018
        ByteBuffer buf = ByteBuffer.allocate(1+4+2*4);
        buf.put(new Short((short)5).byteValue());//0..255
        buf.putInt(Integer.reverseBytes(new Long(40000).intValue()));//0..4294967295
        buf.putShort(Short.reverseBytes((short)-50));//-32768 до 32767
        buf.putShort(Short.reverseBytes((short)50));
        buf.putShort(Short.reverseBytes((short)-50));
        buf.putShort(Short.reverseBytes((short)50));
        byte[] bytes = buf.array();
        
        ByteBuffer.allocate(4).putShort(Short.reverseBytes((short)-50)).array();
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
        
        //отправка данных
        //String stmp = "Test Message!!!";
        try {
                serialPort.writeBytes(bytes/*stmp.getBytes()*/);//Write data to port
                if(debug_print)
                {
                System.out.println("writeBytes:");
                for(byte b:bytes/*stmp.getBytes()*/)
                {
                    System.out.print("0x" + Integer.toHexString(Byte.toUnsignedInt(b)).toUpperCase()+" ");
                }
                System.out.println();
            }
        } catch (SerialPortException ex) {
            Logger.getLogger(USB_Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //прием
//        try {
//            
//            bytes = serialPort.readBytes(bytes.length, 1000);
//            System.out.println("readBytes:");
//            for(byte b:bytes/*stmp.getBytes()*/)
//            {
//                System.out.print("0x" + Integer.toHexString(Byte.toUnsignedInt(b)).toUpperCase()+" ");
//            }
//            System.out.println();
//        } catch (SerialPortException ex) {
//            Logger.getLogger(USB_Connect.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SerialPortTimeoutException ex) {
//            Logger.getLogger(USB_Connect.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }
    
    private static boolean SentPackageFlag = false;
    private static boolean NewPackageFlag = false;
    
    public boolean GetNewPackageFlag()
    {
        //serialPort.getOutputBufferBytesCount();
        return NewPackageFlag;
    }
    
    public boolean GetSentPackageFlag()
    {
        SentPackageFlag = false;
        try {
            SentPackageFlag = serialPort.getOutputBufferBytesCount()==0;
        } catch (SerialPortException ex) {
            Logger.getLogger(USB_Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SentPackageFlag;
    }
    
    public static byte[] bufferReadBytes = null;
    
     private static class EventListener implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            int et = event.getEventType();
            if(debug_print) System.out.println("serialEvent   " + event.getEventType() + "   " + event.getEventValue());
            if(SerialPortEvent.TXEMPTY == et) {
                SentPackageFlag = true;
            };//TXEMPTY = 4;
            
            if(SerialPortEvent.RXCHAR == et)//RXCHAR = 1;
            {
                if(event.isRXCHAR() && event.getEventValue() > 0){                
                    try {                    
                        int n = event.getEventValue();//serialPort.getInputBufferBytesCount();
                        byte[] buffer = serialPort.readBytes(n);
                        bufferReadBytes = buffer;
                        NewPackageFlag = true;
                        if(debug_print)
                        {
                            for(byte b:buffer/*stmp.getBytes()*/)
                            {
                                System.out.print("0x" + Integer.toHexString(Byte.toUnsignedInt(b)).toUpperCase()+" ");
                            }
                            System.out.println();
                        }
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }        
                }
            }
        }
     }
     
     
}
