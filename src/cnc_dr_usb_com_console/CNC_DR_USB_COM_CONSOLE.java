/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc_dr_usb_com_console;

import GCodeConverter.GCode;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextArea;


/**
 *
 * @author slava
 */
public class CNC_DR_USB_COM_CONSOLE {



    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println(args);
        System.out.println("Start .... ");
        
        String pathFile = new String("C:\\Project\\CNC\\soft\\Sample_1_plt_gcode.tap");
        //TextArea textAreaGcode = new TextArea();
        //TextArea textAreaStepCode = new TextArea();
        
        //usbConnect.Test();
        GCode gc = new GCode();
        gc.OpenGcodeFileFrom(pathFile);
        gc.SetTextArrayGCode();
        
        //gc.ArrayTaskForDriverNdXSdYSdZSMToTextAreaGcode(textAreaGcode);
        //gc.ArrayTasckStepToTextArrea(textAreaStepCode);
        //List<TaskStepXYZSM> getArrayTaskStepXYZSM()
        gc.ToArrayTaskForDriverNdXSdYSdZSM();
        gc.ToArrayTasckStep();
        List<GCode.TaskStepXYZSM> TaskStepList = gc.getArrayTaskStepXYZSM();
        //TaskStepList.get(0).Print();
        System.out.println("TaskStepList.size() = "+TaskStepList.size());
        for(GCode.TaskStepXYZSM e : TaskStepList)
        {
            System.out.println(e.Print());
        }
        
        //TestWriteBuffer();
        
        ControlJob CJ = new ControlJob();
        CJ.Open(args);
        Thread th = CJ. TestDoJob();
        
        try {
            java.lang.Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(CNC_DR_USB_COM_CONSOLE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //CJ.SetPause();
        
        //CJ.SetStop();
        
        try {
            java.lang.Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(CNC_DR_USB_COM_CONSOLE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //CJ.SetContinue();
        
        
        try {
            th.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(CNC_DR_USB_COM_CONSOLE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        CJ.Close();
        
        //CNC_DR_USB_COM_CONSOLE tmp = new CNC_DR_USB_COM_CONSOLE();
        //tmp.TestDoJob();
        
//        try {
//        
//        byte[] b = new byte[1];
//        b[0] = 1;//start
//        usbConnect.SendBytes(b);
//        
//        java.lang.Thread.sleep(10);
//                
//        //byte[] b = new byte[1];
//        b[0] = 0;//statist
//        System.out.println("SendBytes .... ");
//        usbConnect.SendBytes(b);
//
//         java.lang.Thread.sleep(3000);
//        
//         b[0] = 2;//stop
//        usbConnect.SendBytes(b);   
//         java.lang.Thread.sleep(100);
//        
//         
//        b[0] = 0;//statist
//        System.out.println("SendBytes .... ");
//        usbConnect.SendBytes(b);
//        java.lang.Thread.sleep(100);
//         
//        } catch (InterruptedException ex) {
//            Logger.getLogger(CNC_DR_USB_COM_CONSOLE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        //System.out.println("ReadByte .... ");
//        //usbConnect.ReadByte();
//        
//        if(USB_Connect.bufferReadBytes != null)
//        {   
//            SD.SetStatusData(USB_Connect.bufferReadBytes);
//            SD.StatusDataPrint();
//            USB_Connect.bufferReadBytes = null;
//        }
//        

    }
    
    
    
    

}
