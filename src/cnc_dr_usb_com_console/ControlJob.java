/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc_dr_usb_com_console;

import cnc_dr_usb_com_console.USB_ST32F103R8T6.StatusData;
import cnc_dr_usb_com_console.USB_ST32F103R8T6.TaskTxStructure;
import cnc_dr_usb_com_console.USB_ST32F103R8T6.USB_Connect;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author slava
 */
public class ControlJob implements Runnable {
    
    
        private static USB_Connect usbConnect = null;
        private static StatusData SD = new StatusData();
        
        private static TaskTxStructure taskTxStructure = new TaskTxStructure();
        
        public void Open(String[] args)
        {
            usbConnect = new USB_Connect();
            usbConnect.PrintSerialList();
            String OsName = System.getProperties().getProperty("os.name");
            System.out.println(OsName);
            if(args != null)
            {
                //java -jar "..\CNC_DR_USB_COM_CONSOLE.jar" 
                for(String a:args)
                {
                    System.out.println(a);
                    if(a.indexOf("-s:") >= 0)
                    {
                        String serial_name = a.split(":")[1];
                        System.out.println("Serial Name : " + serial_name);
                        usbConnect.SetSerialPortName(serial_name);
                    }
                    else if(a.indexOf("-f:") >= 0)
                    {
                        String file_name = a.split(":")[1];
                        System.out.println("File : " + file_name);
                        //open patch file
                    }
                }
            }
            else
            {
                if(OsName.toLowerCase().indexOf("windows") >= 0)
                {
                    usbConnect.SetSerialPortName("COM5"); 
                    //System.out.println("windows");
                }
                else if (OsName.toLowerCase().indexOf("linux") >= 0) 
                {
                    usbConnect.SetSerialPortName("ttyACM0");
                    //System.out.println("linux");
                }
            }
            //System.getProperties().list(System.out); //os.name=Windows 10
            //System.out.println(System.getProperties().getProperty("os.name"));


            System.out.println("Open .... ");
            usbConnect.Open();

        }
        
        public void Close()
        {
            System.out.println("Close .... ");
            usbConnect.Close();
        }
        
        private ArrayList<TaskTxStructure.Data> TaskTxStructureData;
        
        private static boolean stopRun = false;
        private static boolean pauseRun = false;
        
        public Thread TestDoJob()
        {
            
            stopRun = false;
            
            Thread thread;
            //ArrayList<TaskTxStructure.Data> 
            TaskTxStructureData = taskTxStructure.GetNewDataArrayList();
            TaskTxStructure.Data dt;// = taskTxStructure.GetNewData();

            dt = taskTxStructure.GetNewData();
            dt.command = (short)5;
            dt.step_xyzw = (long)250000L;
            dt.speed_x=dt.speed_y=-50;
            dt.speed_z= -20;
            dt.speed_w = -10;
            TaskTxStructureData.add(dt);

            dt = taskTxStructure.GetNewData();
            dt.command = (short)5;
            dt.step_xyzw = (long)250000L;
            dt.speed_x=dt.speed_y=50;
            dt.speed_z= 20;
            dt.speed_w = 10;
            TaskTxStructureData.add(dt);
            
            dt = taskTxStructure.GetNewData();
            dt.command = (short)5;
            dt.step_xyzw = (long)150000L;
            dt.speed_x=dt.speed_y=-50;
            dt.speed_z= -20;
            dt.speed_w = -20;
            TaskTxStructureData.add(dt);

            dt = taskTxStructure.GetNewData();
            dt.command = (short)5;
            dt.step_xyzw = (long)150000L;
            dt.speed_x=dt.speed_y=50;
            dt.speed_z= 20;
            dt.speed_w = 20;
            TaskTxStructureData.add(dt);            

            dt = taskTxStructure.GetNewData();
            dt.command = (short)5;
            dt.step_xyzw = (long)100000L;
            dt.speed_x=dt.speed_y=-50;
            dt.speed_z= -20;
            dt.speed_w = -50;
            TaskTxStructureData.add(dt);

            dt = taskTxStructure.GetNewData();
            dt.command = (short)5;
            dt.step_xyzw = (long)100000L;
            dt.speed_x=dt.speed_y=50;
            dt.speed_z= 20;
            dt.speed_w = 50;
            TaskTxStructureData.add(dt);            
            
            
//            dt = taskTxStructure.GetNewData();
//            dt.command = (short)5;
//            dt.step_xyzw = (long)6000L;
//            dt.speed_x=dt.speed_y=-50;
//            dt.speed_z= -1000;
//            dt.speed_w = 0;
//            TaskTxStructureData.add(dt);
//
//            for(int i=0; i<100; i++){
//                dt = taskTxStructure.GetNewData();
//                dt.command = (short)5;
//                dt.step_xyzw = (long)6000L;
//                dt.speed_x=dt.speed_y=50;
//                dt.speed_z= 100;
//                dt.speed_w = 50;
//                TaskTxStructureData.add(dt);
//            }

            thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
            return thread;
    }
    
    public void DoJob(ArrayList<TaskTxStructure.Data> TaskTxStructureData) throws Exception
    {
        //сбросить индексы буфера записи
        //записать в буфер
        //запуск на выполнение
        //пополнять буфер по заданию
        //остонов по окончанию задания
        
        
        ResetAllStep();
        int nd = TaskTxStructureData.size();
        System.out.println("nd=" + nd);
        int i=0;
//        for(; i<SD.LengthTaskRun-1; i++)
//        {
//            if(i>=nd) break;
//            
//            WriteToBuffer(TaskTxStructureData.get(i));
//            System.out.println("i=" + i);
////            try {
////                java.lang.Thread.sleep(1);
////            } catch (InterruptedException ex) {
////                Logger.getLogger(ControlJob.class.getName()).log(Level.SEVERE, null, ex);
////            }
//            ReadStatusData();
////            try {
////                java.lang.Thread.sleep(1);
////            } catch (InterruptedException ex) {
////                Logger.getLogger(ControlJob.class.getName()).log(Level.SEVERE, null, ex);
////            }
//            SD = GetStatusData();
//            SD.StatusDataPrintToOneLine();
//            
//// 5
//        }
        WriteBufferDataIsFill(TaskTxStructureData.get(i));
        i++;
        
        System.out.println("StartJob");
        StartJob();
        
        boolean run = true;
        while(run)
        {
//            //StatusFlag:2;
//            ReadStatusData();//запрос
//            
//            try {
//                WaitWhileReading();
//                java.lang.Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(CNC_DR_USB_COM_CONSOLE.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (Exception ex) {
//                Logger.getLogger(CNC_DR_USB_COM_CONSOLE.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            //прочитать полеченные данные
//            StatusData std = GetStatusData();
//            std.StatusDataPrintToOneLine();
//            if(std.getStatusFlag() == 2)  run = false;
            if(i<nd)
            {
                if(!WriteBufferDataIsFill(TaskTxStructureData.get(i)))//запись м проверка заполнения буфера
                {
                    i++;//если запись прошла и буфер не полон
                }
            }
            else
            {
                ReadStatusData();
                SD = GetStatusData();
                SD.StatusDataPrintToOneLine();
                if((SD.getStatusFlag() == 2)||(SD.getStatusFlag() == 3))  run = false;
            }
            
//если стоп
             if(stopRun)
             {
                 StopJob();
                 return;
             }
//если пауза
            if(pauseRun)
            {
                PauseJob();
                while(pauseRun)
                {
                    sleep(10);
                    if(stopRun)
                    {
                        StopJob();
                        return;
                    }
                }
                ContinueJob();
                
            }

            sleep(10);

//            
                
            
            //дозапись
//            if(i<nd)
//            if((SD.getIndexWriteTask()-SD.getIndexCurrentRun())<20)
//            {
//                //+15
//                for(int j=0; j<15; j++)
//                {
//                    WriteToBuffer(TaskTxStructureData.get(i));
//                    System.out.println("i=" + i);
//                    ReadStatusData();
//                    SD = GetStatusData();
//                    SD.StatusDataPrintToOneLine();
//                    i++;
//                    if(i>=nd)break;
//                }
//            }
        }
        
        try {
            java.lang.Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(CNC_DR_USB_COM_CONSOLE.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        StopJob();
    }
    
    private void sleep(int x)
    {
            try {
                java.lang.Thread.sleep(x);
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlJob.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    private boolean WriteBufferDataIsFill( TaskTxStructure.Data d) throws Exception
    {
        boolean BufferFill;
        ReadStatusData();
        SD = GetStatusData();
        SD.StatusDataPrintToOneLine();
        BufferFill = (SD.getIndexWriteTask()-SD.getIndexCurrentRun()) >= (TaskTxStructure.getLengthStructure()-1);
        if(!BufferFill)WriteToBuffer(d);

        return BufferFill;
    }
    
    private void WaitWhileSending() throws Exception
    {
        long startTime = System.currentTimeMillis();
        long currentTime=0;
        //long startTime = System.nanoTime();
        while(true)
        {
            
            if(!usbConnect.GetSentPackageFlag())
            {
                java.lang.Thread.sleep(10);
                currentTime = System.currentTimeMillis();
                if((currentTime - startTime)>1000)
                {
                    usbConnect.ClearBuffer();
                    //если привышено ожидание
                    throw new Exception("TimeOurWaitWhileSending");
                }
            }
            else
            {
                break;
            }
        }
    }
    
        private void WaitWhileReading() throws Exception
    {
        long startTime = System.currentTimeMillis();
        long currentTime;
        //long startTime = System.nanoTime();
        while(true)
        {
            if(!usbConnect.GetNewPackageFlag())
            {
                java.lang.Thread.sleep(1);
                currentTime = System.currentTimeMillis();
                if((currentTime - startTime)>1000)
                {
                    usbConnect.ClearBuffer();
                    //если привышено ожидание
                    throw new Exception("TimeOurWaitWhileReading");
                }
            }
            else
            {
                break;
            }
        }
    }
    
    
    private void WaitAndSendByte(byte x)
    {
        try {
            WaitWhileSending();
        } catch (Exception ex) {
            Logger.getLogger(CNC_DR_USB_COM_CONSOLE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] b = new byte[1];
        b[0] = x;
        usbConnect.SendBytes(b);
    }
    
    //0 - запрос на статистику, 1 - старт, 2 - стоп, 3 - пауза, 4 - пуск после паузы, 5 - записать буфер, 6 - сброс суммарных шагов
    
    private void StartJob()
    {
        WaitAndSendByte((byte)1);
    }
    
    private void StopJob()
    {
        WaitAndSendByte((byte)2);
    }
    
    private void PauseJob()
    {
        WaitAndSendByte((byte)3);
    }
    
    private void ContinueJob()
    {
        WaitAndSendByte((byte)4);
    }
    
    private void ReadStatusData()
    {
        WaitAndSendByte((byte)0);
    }
    
    private void ResetAllStep()
    {
        WaitAndSendByte((byte)6);
    }
    
    void WriteToBuffer(TaskTxStructure.Data d) throws Exception
    {
        taskTxStructure.setDataTaskTxStructure(d);
        byte[] b =  taskTxStructure.getBytesTaskTxStructure();

            usbConnect.SendBytes(b);
            WaitWhileSending();

        
    }
    
    private StatusData GetStatusData()
    {
        //if(usbConnect.GetNewPackageFlag())
        byte[] b = usbConnect.ReadByte();
        if(b!=null)
        {
            SD.SetStatusData(b);
            //SD.SetStatusData(USB_Connect.bufferReadBytes);
        }
        
        return SD;
    }

    public void SetPause()
    {
        pauseRun = true;
    }
    
    public void SetStop()
    {
        stopRun = true;
    }
    
    public void SetContinue()
    {
        pauseRun = false;
    }
    
    
    
    @Override
    public void run() {
        try {
            DoJob(TaskTxStructureData);
        } catch (Exception ex) {
            Logger.getLogger(ControlJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
