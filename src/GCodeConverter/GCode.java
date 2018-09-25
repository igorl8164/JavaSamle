/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GCodeConverter;

import cnc_dr_usb_com_console.USB_ST32F103R8T6.TaskTxStructure;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author slava
 */
public class GCode {
    
    //https://3deshnik.ru/wiki/index.php/G-%D0%BA%D0%BE%D0%B4%D1%8B#.D0.9A.D0.BE.D0.BC.D0.BC.D0.B5.D0.BD.D1.82.D0.B0.D1.80.D0.B8.D0.B8
    //http://postprocessor.su/kod_iso7bit.html
//M03 – вкл. шпинделя по часовой стрелке;
//M04 - вкл. шпинделя против часовой стрелке;
//M05 – выкл. шпинделя;
//M08 - вкл. охлаждение;
//M09 - выкл. охлаждение;
//M06 – сменить инструмент;
//M02 – конец программы, модальные функции сохраняются;
//M30 - конец программы, модальные функции отменяются;
    
//S - скорость шпинделя
    
//F - скорость подачи
 
//G00 – ускоренное перемещение (холостой ход);
//G01 – линейное перемещение (рабочий ход);
//G02 – круговая интерполяция с движением по часовой стрелке;
//G03 - круговая интерполяция с движением по против часовой стрелки;
//G70 – программирование перемещений в дюймах;
//G71 – программирование перемещений в мм;
    
//Специальные символы
//N Номер линии
//* Контрольная сумма

//; Комментарий
    
//Tnnn	Выберите инструмент NNN
    
//G0 : Быстрое (холостое) линейное перемещение
//G1 : Линейное перемещение
//Использование
//G0 Xnnn Ynnn Znnn Ennn Fnnn Snnn
//G1 Xnnn Ynnn Znnn Ennn Fnnn Snnn
//Parameters
//Не все параметры могут быть использованы, но по крайней мере один должен быть задан
//Xnnn Позиция для перемещения по оси X
//Ynnn Позиция для перемещения по оси Y
//Znnn Позиция для перемещения по оси Z
//Ennn Количество пластика, которое нужно выдавить между начальной и конечной точкой перемещения
//Fnnn Скорость перемещения в минуту между начальной и конечной точкой
//Snnn Флаг для проверки срабатывания концевых выключателей EndStop (S1 проверить, S0 игнорировать, S2 смотри примечания, по умолчанию S0)1


//G2 & G3: Контроль криволинейного движения
//Использование
//G2 Xnnn Ynnn Innn Jnnn Ennn Fnnn (Дуга по часовой стрелке)
//G3 Xnnn Ynnn Innn Jnnn Ennn Fnnn (Дуга против часовой стрелке)
//Параметры
//Xnnn Позиция для перемещения по оси X
//Ynnn Позиция для перемещения по оси Y
//Innn The point in X space from the current X position to maintain a constant distance from
//Jnnn The point in Y space from the current Y position to maintain a constant distance from
//Ennn Количество пластика, которое нужно выдавить между начальной и конечной точкой перемещения
//Fnnn Скорость перемещения в минуту между начальной и конечной точкой (если поддерживается)
//Пример
//G2 X90.6 Y13.8 I5 J10 E22.4 (Переместится по часовой стрелке от начальной точки (X=90.6,Y=13.8), с центром в точке (X=current_X+5, Y=current_Y+10), выдавить 22.4 мм материала между началом и концом движения)
//G3 X90.6 Y13.8 I5 J10 E22.4 (Переместится против часовой стрелке от начальной точки(X=90.6,Y=13.8), с центром в точке (X=current_X+5, Y=current_Y+10), выдавить 22.4 мм материала между началом и концом движения)

//----------------------------------------------------------------------------//
    //работа с файлом Gcode 
    //получить путь к файлу 
    //FileChooser 
    
//FileChooser fileChooser = new FileChooser();
//fileChooser.setTitle("Open Resource File");
//File file = fileChooser.showOpenDialog(stage);
//if (file != null) {
//openFile(file);
//}
                    
    //открыть файл
//    Charset charset = Charset.forName("US-ASCII");
//try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
//    String line = null;
//    while ((line = reader.readLine()) != null) {
//        System.out.println(line);
//    }
//} catch (IOException x) {
//    System.err.format("IOException: %s%n", x);
//}
//https://docs.oracle.com/javase/tutorial/essential/io/file.html
    
private List<String> ArrayStringGcode = new ArrayList<String>();
private String textStringGCode;// = "code 2 learn java tutorial";
private List<TaskForDriverNdXSdYSdZSM> ArrayTaskForDriverNdXSdYSdZSM;
private ArrayList<TaskTxStructure.Data> TaskTxStructureData;
private static TaskTxStructure taskTxStructure = new TaskTxStructure();
private  TaskTxStructure.Data dt;// = taskTxStructure.GetNewData();
private Window stage ;
private File file;

    public GCode() {
        
        TaskXYZSM t = new TaskXYZSM();
        
//        t.N = 1;
//        t.DeltaX = -10.5f;
//        t.SpeedX = 3.5f;
//        
//        t.DeltaY = -10.5f;
//        t.SpeedY = 3.5f;
//        
//        t.DeltaZ = -10.5f;
//        t.SpeedZ = 3.5f;
//        
//        t.gearS = +253.0f;
//        
//        System.out.println(t.Print());
        
        ArrayTaskForDriverNdXSdYSdZSM = new ArrayList<TaskForDriverNdXSdYSdZSM>();
        
        //ArrayList<TaskTxStructure.Data> 
            TaskTxStructureData = taskTxStructure.GetNewDataArrayList();
            
        
        F = Fmax;
    }


public void setWindow(Window stage)
{
    this.stage = stage;
}

public void OpenGcodeFile()
{

    OpenGcodeFile(stage);

}

private List<String> listStringTextArea = null;
        
public void OpenGcodeFile(Window stage)
{
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    /*File*/ file = fileChooser.showOpenDialog(stage);
    
}
    
public void OpenGcodeFileFrom(String pathFile)
{
    file = new File(pathFile);
}


public void SetTextArrea(TextArea ta)
{
    if(file == null) return;
    Path path = Paths.get(file.getPath());
    System.out.println(file.getPath());
//     открыть файл
//     Path path = FileSystems.getDefault().getPath("logs", "access.log");
//     BufferReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
    Charset charset = Charset.forName("UTF-8");//"US-ASCII"
    System.out.println(charset);
    try (BufferedReader reader = Files.newBufferedReader(path, charset))//StandardCharsets.UTF_8
    {
    String line = null;
    listStringTextArea = new ArrayList<String>();
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
        ta.appendText(line);
        ta.appendText("\r\n");
        listStringTextArea.add(line);
    }
} catch (IOException x) {
    System.err.format("IOException: %s%n", x);
}

}

private List<TextFlow> ArrayTextFlow;

public void SetTextArrayGCodeToVBox(VBox vb)
{
    if(file == null) return;
    Path path = Paths.get(file.getPath());
    System.out.println(file.getPath());
    vb.getChildren().clear();
    Charset charset = Charset.forName("UTF-8");//"US-ASCII"
    System.out.println(charset);
    ClearStringGcodeToArrayStringGcode();
    ClearArrayTaskForDriverNdXSdYSdZSM();
    
    try (BufferedReader reader = Files.newBufferedReader(path, charset))//StandardCharsets.UTF_8
    {
        String line = null;
        listStringTextArea = new ArrayList<String>();
        ArrayTextFlow = new ArrayList<TextFlow>();
        int i=0;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            //ta.appendText(line);

            listStringTextArea.add(line);

            //TextFlow id="myTextFlow" fx:id="myTextFlow" style="-fx-background-color: #458522;"
            //<Text lineSpacing="5.0" text="Text" />
            TextFlow e = new TextFlow();
            //transparent = rgba(0,0,0,0)    mintcream = #f5fffa silver = #c0c0c0 lightsteelblue = #b0c4de lightcyan = #e0ffff
            if(CheckMembershipGroupCommands(line)) {
                AddStringGcodeToArrayStringGcode(); 
                //парсить строчку G кода в список
                ParseStringGcodeToArrayTaskForDriverNdXSdYSdZSM();
            };
            e.setStyle(getStyleCommfndGroup());
            ArrayTextFlow.add(e);
            Text t = new Text(line);
            e.getChildren().add(t);
            vb.getChildren().add(ArrayTextFlow.get(i));
            i++;
        }
    } catch (IOException x) {
    System.err.format("IOException: %s%n", x);
    }
    
}

public void SetTextArrayGCode()
{
    if(file == null) return;
    Path path = Paths.get(file.getPath());
    System.out.println(file.getPath());
    
    Charset charset = Charset.forName("UTF-8");//"US-ASCII"
    System.out.println(charset);
    ClearStringGcodeToArrayStringGcode();
    ClearArrayTaskForDriverNdXSdYSdZSM();
    
    try (BufferedReader reader = Files.newBufferedReader(path, charset))//StandardCharsets.UTF_8
    {
        String line = null;
        listStringTextArea = new ArrayList<String>();

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            //ta.appendText(line);

            listStringTextArea.add(line);

            if(CheckMembershipGroupCommands(line)) {
                AddStringGcodeToArrayStringGcode();                
                //парсить строчку G кода в список
                ParseStringGcodeToArrayTaskForDriverNdXSdYSdZSM();
                //System.out.println("CheckMembership " + line);
            };

        }
    } catch (IOException x) {
    System.err.format("IOException: %s%n", x);
    }
    
}

//----------------------------------------------------------------------------//
    //разбор строчки кода файла Gcode 
//http://info.javarush.ru/translation/2015/02/19/%D0%A0%D0%B5%D0%B3%D1%83%D0%BB%D1%8F%D1%80%D0%BD%D1%8B%D0%B5-%D0%B2%D1%8B%D1%80%D0%B0%D0%B6%D0%B5%D0%BD%D0%B8%D1%8F-%D0%B2-Java.html
//https://regex101.com/  онлаин проверка.
//https://www.debuggex.com/  отладка и проверка регулярных выражений онлайн
//java.util.regex.  классы – Pattern и Matcher 
//String pattern = "[a-z]+";
//String text = "code 2 learn java tutorial";
//Pattern pattern = Pattern.compile(pattern);
//Matcher matcher = pattern.matcher(text);
//boolean matches = matcher.matches();

//Gxxx ...  ^[G]{1}\\d{1,3}     ^[G]\d{1,3}[ ]
//Gxxx Sxxx Txxx Mxxx Fxxx  ^[(G|M|S|T|F)]\d+   
//... X  253.548    [X][ ]*[(-|+)]?\d{0,4}[(\\.|\\,)]\d{1,4}
//| - или
//() - растановка приоритета
// // - слэш
// /d или [0-9] - любая цифра
// [] диапазон символов или цифр
// {1,} -количество символов, указанных ранее минимальное значение (единица) максимальное (если пусто нет максимума)
// ? Соответствует 0 или 1 местонахождению предыдущего выражения
//^ соответствует началу строки
// + одно или более совпадений предыдущего выражения
//matcher.find() text.substring(matcher.start(), matcher.end())

//m.matches() возвращает true, если шаблон соответствует всей строке, иначе false.
//m.lookingAt() возвращает true, если шаблон соответствует началу строки, и false в противном случае.
//m.find () возвращает true, если шаблон совпадает с любой частью текста.

//определяем является ли строчка командой G code Gxxx Sxxx Txxx Mxxx Fxxx 
//если является определяем тип команды G S T F M 
//определяем цифровой код команды G M T (или цифровой параметр для S и F )
//для команд G0 или G1 определяем значения X Y Z


    private int N;
    private float S;
    private float X;
    private float F;
    private float Y;
    private float Z;

    
//    public int N_Previous;
//    public float M_Previous;
//    public float X_Previous;
//    public float F_Previous;
//    public float Y_Previous;
//    public float Z_Previous;
    
private void ParseStringGcodeToArrayTaskForDriverNdXSdYSdZSM()
{
    if(CheckMembershipCommandGroupG()) {ParseStringCommandGroupG();};
    if(CheckMembershipCommandGroupF()) {ParseStringCommandGroupF();};
    if(CheckMembershipCommandGroupM()) {ParseStringCommandGroupM();};
    if(CheckMembershipCommandGroupT()) {ParseStringCommandGroupT();};
    if(CheckMembershipCommandGroupS()) {ParseStringCommandGroupS();};
}

//определяем номер группы команд G
private void ParseNumberCommandGroupG()
{
    int Number = 0;
    String tmp;
    String patternString = "^[G]{1}\\d{1,3}";
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(textStringGCode);
    boolean matches = matcher.lookingAt();
    if(!matches) { return; };
    //System.out.println("CheckTemplate: " + matches);
    //return matches;
    tmp = textStringGCode.substring(matcher.start(), matcher.end());
    tmp = tmp.replace('G', '+');
    //System.out.println("substring: " + tmp);
    Number = Integer.parseUnsignedInt(tmp);
    //System.out.println("parseInt: " + Number);
    
    if(Number == 0) ParseStringCommand_G0();
    if(Number == 1) ParseStringCommand_G1();
    
}
        
//парсим команды G0 G1 группы команд Gnnn
float tmp ;
private void ParseStringCommand_G0()
{
    tmp = F;
    F=Fmax;
    ParseStringCommand_G1();
    F=tmp;
}

private void ParseStringCommand_G1()
{
    
            //X
    
        //textStringGCode;
        float f;
        String tmp;
        String patternStringA = "[X]";
        String patternStringB = "[ ]*[(-|+)]?(\\d{0,4}[(\\.|\\,)]){0,}\\d{1,4}";
        String patternString = patternStringA + patternStringB;
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(textStringGCode);
        boolean matches = matcher.find();
        //System.out.println("CheckTemplate: " + matches);
        //return matches;
        if(matches) {
        //System.out.println("CheckTemplate: " + matches);
        //return matches;
            tmp = textStringGCode.substring(matcher.start(), matcher.end());
            tmp = tmp.replace('X', ' ');
            //System.out.println("substring: " + tmp);
            
            f = Float.parseFloat(tmp.replace(',', '.'));
            //System.out.println("parseFloat: " + f);
            X = f;
        }
        
        
        //Y
        patternStringA = "[Y]";
        patternString = patternStringA + patternStringB;
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(textStringGCode);
        matches = matcher.find();
        if(matches) {
            tmp = textStringGCode.substring(matcher.start(), matcher.end());
            tmp = tmp.replace('Y', ' ');
            f = Float.parseFloat(tmp.replace(',', '.'));
            Y = f;
        }
        
        //Z
        patternStringA = "[Z]";
        patternString = patternStringA + patternStringB;
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(textStringGCode);
        matches = matcher.find();
        if(matches) {
            tmp = textStringGCode.substring(matcher.start(), matcher.end());
            tmp = tmp.replace('Z', ' ');
            //System.out.println(tmp);
            f = Float.parseFloat(tmp.replace(',', '.'));
            Z = f;
        }
        
        
        //--------------------------------------------------------------------//
        TaskForDriverNdXSdYSdZSM e = 
                new TaskForDriverNdXSdYSdZSM(N, X, Y, Z, F, S);
        ArrayTaskForDriverNdXSdYSdZSM.add(e);
        
        //X_Previous = X;
        N++;
        //N_Previous = N;
}

private void ParseStringCommandGroupG()
{
    
    ParseNumberCommandGroupG();
}

private int IntParseStringCommand(String x) throws Exception
{
    int Number = 0;
    String tmp;
    String patternString = "^["+ x +"]{1}\\d{1,4}";
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(textStringGCode);
    boolean matches = matcher.lookingAt();
    if(!matches) { throw new Exception(); };
    //System.out.println("CheckTemplate: " + matches);
    //return matches;
    tmp = textStringGCode.substring(matcher.start()+1, matcher.end());
    //tmp = tmp.replace(x.charAt(0), '+');
    //tmp = tmp.substring(1);
    //System.out.println("substring: " + tmp);
    Number = Integer.parseUnsignedInt(tmp);
    //System.out.println("parseInt: " + Number);
    
    return Number;
}

private void ParseStringCommandGroupF()
{

//    int Number = 0;
//    String tmp;
//    String patternString = "^[F]{1}\\d{1,4}";
//    Pattern pattern = Pattern.compile(patternString);
//    Matcher matcher = pattern.matcher(textStringGCode);
//    boolean matches = matcher.lookingAt();
//    if(!matches) { return; };
//    //System.out.println("CheckTemplate: " + matches);
//    //return matches;
//    tmp = textStringGCode.substring(matcher.start(), matcher.end());
//    tmp = tmp.replace('F', '+');
//    //System.out.println("substring: " + tmp);
//    Number = Integer.parseUnsignedInt(tmp);
//    //System.out.println("parseInt: " + Number);
//    F = Number;
    try {
        F = IntParseStringCommand("F");
        //System.out.println("F: " + F);
    } catch (Exception ex) {
        Logger.getLogger(GCode.class.getName()).log(Level.SEVERE, null, ex);
    }
    

}

private void ParseStringCommandGroupS()
{
    try {
        S = IntParseStringCommand("S");
    } catch (Exception ex) {
        Logger.getLogger(GCode.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void ParseStringCommandGroupT()
{
    int T;
    try {
        T = IntParseStringCommand("T");
    } catch (Exception ex) {
        Logger.getLogger(GCode.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void ParseStringCommandGroupM()
{
    int M;
    try {
        M = IntParseStringCommand("M");
    } catch (Exception ex) {
        Logger.getLogger(GCode.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void ClearArrayTaskForDriverNdXSdYSdZSM()
{
     N = 0;//N_Previous = 0;
     S = //M_Previous =
     X = //X_Previous =
     Y = //Y_Previous =
     Z = 0;//Z_Previous = 0;
     //Fmax = 500;
     F = Fmax;//F_Previous =
    
     ArrayTaskForDriverNdXSdYSdZSM.clear();
}

List<GCode.TaskXYZSM> ArrayTasktXYZSM =  new ArrayList<TaskXYZSM>();
TaskForDriverNdXSdYSdZSM tfd;
public void ArrayTaskForDriverNdXSdYSdZSMToTextAreaGcode(TextArea ta)
{
    
        ArrayTasktXYZSM.clear();
        ta.clear();
        //GCode.TaskXYZSM fd;
        TaskForDriverNdXSdYSdZSM tfdp = new TaskForDriverNdXSdYSdZSM(1, 0, 0, 0, Fmax, 0);
        GCode.TaskXYZSM t;// = new TaskXYZSM(1, 0, 0, 0, 0, 0, 0, 0);
        //ArrayTasktXYZSM.add(t);
        for(int i=0; i<ArrayTaskForDriverNdXSdYSdZSM.size() ;i++)
        {
            tfd = ArrayTaskForDriverNdXSdYSdZSM.get(i);
            float dx = tfd.X - tfdp.X;
            float dy = tfd.Y - tfdp.Y;
            float dz = tfd.Z - tfdp.Z;
            float L;
            float Fx;
            float Fy;
            float Fz;
//1 Lxy(float x, float y)
//2 Fx = F*(x/Lxy);  скорость по X mm/sec
//Fy = F*(y/Lxy); скорость по Y mm/sec
//Fz = F*(1); скорость по Z
//3 SreedMmSecToHz(float x)
            //F = tfd.F;
            L = 1;
            if((dx != 0)|| (dy != 0)) L = Lxy(dx, dy);
            if(dx != 0) Fx = tfd.F*(dx/L); else Fx = 0;
            if(dy != 0) Fy = tfd.F*(dy/L); else Fy = 0;
            if(dz != 0) Fz = tfd.F*1; else Fz = 0;
            
            t = new TaskXYZSM();
            t.N = tfd.N+1;
            t.DeltaX = dx;
            t.SpeedX = Math.abs(Fx);

            t.DeltaY = dy;
            t.SpeedY = Math.abs(Fy);

            t.DeltaZ = dz;
            t.SpeedZ = Math.abs(Fz);

            t.gearS = tfd.S;

            ta.appendText(t.Print());
            ArrayTasktXYZSM.add(t);
            tfdp = tfd;
        }
}


public void ToArrayTaskForDriverNdXSdYSdZSM()
{
    
        ArrayTasktXYZSM.clear();

        //GCode.TaskXYZSM fd;
        TaskForDriverNdXSdYSdZSM tfdp = new TaskForDriverNdXSdYSdZSM(1, 0, 0, 0, Fmax, 0);
        GCode.TaskXYZSM t;// = new TaskXYZSM(1, 0, 0, 0, 0, 0, 0, 0);
        //ArrayTasktXYZSM.add(t);
        for(int i=0; i<ArrayTaskForDriverNdXSdYSdZSM.size() ;i++)
        {
            tfd = ArrayTaskForDriverNdXSdYSdZSM.get(i);
            float dx = tfd.X - tfdp.X;
            float dy = tfd.Y - tfdp.Y;
            float dz = tfd.Z - tfdp.Z;
            float L;
            float Fx;
            float Fy;
            float Fz;
//1 Lxy(float x, float y)
//2 Fx = F*(x/Lxy);  скорость по X mm/sec
//Fy = F*(y/Lxy); скорость по Y mm/sec
//Fz = F*(1); скорость по Z
//3 SreedMmSecToHz(float x)
            //F = tfd.F;
            L = 1;
            if((dx != 0)|| (dy != 0)) L = Lxy(dx, dy);
            if(dx != 0) Fx = tfd.F*(dx/L); else Fx = 0;
            if(dy != 0) Fy = tfd.F*(dy/L); else Fy = 0;
            if(dz != 0) Fz = tfd.F*1; else Fz = 0;
            
            t = new TaskXYZSM();
            t.N = tfd.N+1;
            t.DeltaX = dx;
            t.SpeedX = Math.abs(Fx);

            t.DeltaY = dy;
            t.SpeedY = Math.abs(Fy);

            t.DeltaZ = dz;
            t.SpeedZ = Math.abs(Fz);

            t.gearS = tfd.S;

            System.out.print(t.Print());
            ArrayTasktXYZSM.add(t);
            tfdp = tfd;
        }
}

private List<TaskStepXYZSM> ArrayTaskStepXYZSM = new ArrayList<TaskStepXYZSM>();

public List<TaskStepXYZSM> getArrayTaskStepXYZSM() {

    return ArrayTaskStepXYZSM;
}

public void ArrayTasktXYZSMToTaskTxStructureData()
{
    TaskTxStructureData.clear();
    ArrayTasktXYZSM.get(0);
         
    dt = taskTxStructure.GetNewData();
    dt.command = (short)5;
    dt.step_xyzw = (long)6000L;
    dt.speed_x=dt.speed_y=-50;
    dt.speed_z= -100;
    dt.speed_w = 0;
    TaskTxStructureData.add(dt);
}

public void ArrayTasckStepToTextArrea(TextArea textAreaStepCode)
{
    //выводим задания в шагах в TextArea
    textAreaStepCode.clear();
    ArrayTaskStepXYZSM.clear();
    for(int i=0; i<ArrayTasktXYZSM.size();i++)
    {
        //3 SreedMmSecToHz(float x)
        //4 length to step  Step_x=X*StepXmm    Step_y=Y*StepYmm  Step_z=Z*StepZmm
        GCode.TaskXYZSM t =ArrayTasktXYZSM.get(i);
        
        TaskStepXYZSM ts = new TaskStepXYZSM();
        
        ts.N = t.N;
        ts.DeltaX = (int)(t.DeltaX*StepXmm);
        ts.SpeedX = (int)SreedMmSecToHz(t.SpeedX);

        ts.DeltaY = (int)(t.DeltaY*StepYmm);;
        ts.SpeedY = (int)SreedMmSecToHz(t.SpeedY);;

        ts.DeltaZ = (int)(t.DeltaZ*StepZmm);;
        ts.SpeedZ = (int)SreedMmSecToHz(t.SpeedZ);;

        ts.gearS = (int)t.gearS;
        ArrayTaskStepXYZSM.add(ts);
        textAreaStepCode.appendText(ts.Print());
    }
}

public void ToArrayTasckStep()
{
    //выводим задания в шагах в TextArea
    
    ArrayTaskStepXYZSM.clear();
    for(int i=0; i<ArrayTasktXYZSM.size();i++)
    {
        //3 SreedMmSecToHz(float x)
        //4 length to step  Step_x=X*StepXmm    Step_y=Y*StepYmm  Step_z=Z*StepZmm
        GCode.TaskXYZSM t =ArrayTasktXYZSM.get(i);
        
        TaskStepXYZSM ts = new TaskStepXYZSM();
        
        ts.N = t.N;
        ts.DeltaX = (int)(t.DeltaX*StepXmm);
        ts.SpeedX = (int)SreedMmSecToHz(t.SpeedX);

        ts.DeltaY = (int)(t.DeltaY*StepYmm);;
        ts.SpeedY = (int)SreedMmSecToHz(t.SpeedY);;

        ts.DeltaZ = (int)(t.DeltaZ*StepZmm);;
        ts.SpeedZ = (int)SreedMmSecToHz(t.SpeedZ);;

        ts.gearS = (int)t.gearS;
        ts.Print();
        ArrayTaskStepXYZSM.add(ts);
    }
}
//----------------------------------------------------------------------------//
//проверка на принадлежность к группе команд G S T F M 

//проверка на принодлежнасть к командам группы G
//проверка на принодлежнасть к командам группы S
//проверка на принодлежнасть к командам группы T
//проверка на принодлежнасть к командам группы F
//проверка на принодлежнасть к командам группы M

//вернуть стиль цвет в зависимости от принодлежности группы

//преводим единици мм в шаги шим 



private void AddStringGcodeToArrayStringGcode()
{
    ArrayStringGcode.add(textStringGCode);
}

private void ClearStringGcodeToArrayStringGcode()
{
    ArrayStringGcode.clear();
}



//проверка на принадлежность к группе команд G S T F M 
private boolean CheckMembershipGroupCommands(String text)
{
    textStringGCode = text;
    String patternString = "^[(G|M|S|T|F)][0-9]{1,6}";
    boolean matches = CheckTemplate(patternString);
    //System.out.println("CheckMembershipGroupCommands: " + matches);
    return matches;
}

private boolean CheckTemplate(String patternString)
{
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(textStringGCode);
    boolean matches = matcher.lookingAt();
    //System.out.println("CheckTemplate: " + matches);
    return matches;
}

//проверка на принодлежнасть к командам группы G
private boolean CheckMembershipCommandGroupG()
{
    String patternString = "^[G]\\d+";
    boolean matches = CheckTemplate(patternString);
    return matches;
}

//проверка на принодлежнасть к командам группы S
private boolean CheckMembershipCommandGroupS()
{
    String patternString = "^[S]\\d+";
    boolean matches = CheckTemplate(patternString);
    return matches;
}

//проверка на принодлежнасть к командам группы T
private boolean CheckMembershipCommandGroupT()
{
    String patternString = "^[T]\\d+";
    boolean matches = CheckTemplate(patternString);
    return matches;
}

//проверка на принодлежнасть к командам группы F
private boolean CheckMembershipCommandGroupF()
{
    String patternString = "^[F]\\d+";
    boolean matches = CheckTemplate(patternString);
    return matches;
}

//проверка на принодлежнасть к командам группы M
private boolean CheckMembershipCommandGroupM()
{
    String patternString = "^[M]\\d+";
    boolean matches = CheckTemplate(patternString);
    return matches;
}

//вернуть стиль цвет в зависимости от принодлежности группы
private String getStyleCommfndGroup()
{
    //"-fx-background-color: #1FFdAA; "
    ////transparent = rgba(0,0,0,0)    mintcream = #f5fffa silver = #c0c0c0 lightsteelblue = #b0c4de lightcyan = #e0ffff cornflowerblue = #6495ed 
            
    if(CheckMembershipCommandGroupG()) {return "-fx-background-color: lawngreen;";}
    if(CheckMembershipCommandGroupS()) {return "-fx-background-color: cornflowerblue;";}
    if(CheckMembershipCommandGroupT()) {return "-fx-background-color: silver;";}
    if(CheckMembershipCommandGroupF()) {return "-fx-background-color: lightsteelblue;";}
    if(CheckMembershipCommandGroupM()) {return "-fx-background-color: lightskyblue;";}
    return "-fx-background-color: transparent;";
}
//----------------------------------------------------------------------------//

//преводим единици мм в шаги шим 

//----------------------------------------------------------------------------//
    //вспомогательные элементы
    
//масштаб скорости
    //speed scale
    //перемищение мм/сек -> количество тактируемых импульсов
//масштаб расстояния
    //distance scale

//длина вектора по X и Y 
private float Lxy(float x, float y)
{
    float l;// = x*x+y*y;
    l = (float)Math.hypot(x, y);
    
    return l;
}

//Fx = F*(x/Lxy);  скорость по X mm/sec
//Fy = F*(y/Lxy); скорость по Y mm/sec
//Fz = F*(1); скорость по Z

private float SreedMmSecToHz(float x) //уравнеие прямой по двум точкам
{
    //(y-y1)/(y2-y1)=(x-x1)/(x2-x1)
    //y=((x-x1)/(x2-x1))*(y2-y1)+y1
    float y;
//    float y2=2;//max speed step period
//    float y1=65534;//min speed step period
//    float x2=500;//max speed mm/sec
//    float x1=0;//min speed mm/sec
//    y=((x-x1)/(x2-x1))*(y2-y1)+y1;

    //mm/sec  -входные данные
    if(x == 0) return 0;
    float Speed_mm_sec=x; //скорость линейного перемищения
    final float K_mm_rev = 100.0f;// перемищение за один оборот миллиметров
    float Speed_prm_sec = Speed_mm_sec/K_mm_rev;//скорость в оборотах за секунду
    //System.out.println("Speed_prm_sec "+Speed_prm_sec);
    final float K_rev_Hz = 360.0f/1.8f;//оличество импульсов в одном обороте
    float SpeedHz = K_rev_Hz*Speed_prm_sec;
    //System.out.println("SpeedHz "+SpeedHz);
    final float max_Hz = 2000.0f;//максимальная частота тактирования
    float SpeedX = max_Hz/SpeedHz;
    //System.out.println("SpeedX "+SpeedX);
    if(SpeedX<2) SpeedX = 2;
    //Hz - выходные данные
    y = SpeedX;
    return y;
}

private final float StepXmm = 10.0f;//дискретных шагов в одном миллиметре 
private final float StepYmm = 10.0f;//
private final float StepZmm = 10.0f;//
private final float Fmax = 500.0f;//максимальная скорость в мм/сек

//1 Lxy(float x, float y)
//2 Fx = F*(x/Lxy);  скорость по X mm/sec
//Fy = F*(y/Lxy); скорость по Y mm/sec
//Fz = F*(1); скорость по Z
//3 SreedMmSecToHz(float x)
//4 length to step  Step_x=X*StepXmm    Step_y=Y*StepYmm  Step_z=Z*StepZmm
//

//----------------------------------------------------------------------------//
//из файла G code формируем задание List<TaskXYZSM>
//номер задания
//прирощение по координате  скорость приращения по коорденате  .... 
//исполнитель положительное или отрицательное усилие или ноль (шпиндель, экструдер, лазер, воздушная помпа)
//.appendText("N:0001 \t dX:000.254  S:025 \t dY:000.956  S:205 \t dZ:001.356  S:025 \t M:+0520\r\n");

    public class TaskXYZSM
    {
        public int N;
        public float DeltaX;
        public float SpeedX;

        public float DeltaY;
        public float SpeedY;

        public float DeltaZ;
        public float SpeedZ;

        public float gearS;

        public String Print()
        {
            StringBuilder sb = new StringBuilder();
            //N:0001 \t ΔX:+000.254  S:025.000 \t ΔY:-000.956  S:005.205 \t ΔZ:+001.356  S:025.000 \t M:+0520\r\n
            sb.append("N:");
            sb.append(String.format("%06d", N));

            sb.append(" \t ΔX:");
            sb.append(String.format("%+08.3f", DeltaX));

            sb.append("  F:");
            sb.append(String.format("%07.3f", SpeedX));

            sb.append(" \t ΔY:");
            sb.append(String.format("%+08.3f", DeltaY));

            sb.append("  F:");
            sb.append(String.format("%07.3f", SpeedY));

            sb.append(" \t ΔZ:");
            sb.append(String.format("%+08.3f", DeltaZ));

            sb.append("  F:");
            sb.append(String.format("%07.3f", SpeedZ));

            sb.append(" \t S:");
            sb.append(String.format("%+05.0f", gearS));

            sb.append("\r\n");

            return sb.toString();
        }

        public TaskXYZSM(int N, float DeltaX, float SpeedX, float DeltaY, float SpeedY, float DeltaZ, float SpeedZ, float gearS) {
            this.N = N;
            this.DeltaX = DeltaX;
            this.SpeedX = SpeedX;
            this.DeltaY = DeltaY;
            this.SpeedY = SpeedY;
            this.DeltaZ = DeltaZ;
            this.SpeedZ = SpeedZ;
            this.gearS = gearS;
        }

        public TaskXYZSM()
        {

        }
    }

public class TaskStepXYZSM
{
        public int N;
        public int DeltaX;
        public int SpeedX;

        public int DeltaY;
        public int SpeedY;

        public int DeltaZ;
        public int SpeedZ;

        public int gearS;
        
        public String Print()
        {
            StringBuilder sb = new StringBuilder();
            //N:0001 \t ΔX:+000.254  S:025.000 \t ΔY:-000.956  S:005.205 \t ΔZ:+001.356  S:025.000 \t M:+0520\r\n
            sb.append("N:");
            sb.append(String.format("%06d", N));

            sb.append(" \t ΔX:");
            sb.append(String.format("%+06d", DeltaX));

            sb.append("  F:");
            sb.append(String.format("%05d", SpeedX));

            sb.append(" \t ΔY:");
            sb.append(String.format("%+06d", DeltaY));

            sb.append("  F:");
            sb.append(String.format("%05d", SpeedY));

            sb.append(" \t ΔZ:");
            sb.append(String.format("%+06d", DeltaZ));

            sb.append("  F:");
            sb.append(String.format("%05d", SpeedZ));

            sb.append(" \t S:");
            sb.append(String.format("%+05d", gearS));

            sb.append("\r\n");

            return sb.toString();
        }

        public TaskStepXYZSM() {
        }

        public TaskStepXYZSM(int N, int DeltaX, int SpeedX, int DeltaY, int SpeedY, int DeltaZ, int SpeedZ, int gearS) {
            this.N = N;
            this.DeltaX = DeltaX;
            this.SpeedX = SpeedX;
            this.DeltaY = DeltaY;
            this.SpeedY = SpeedY;
            this.DeltaZ = DeltaZ;
            this.SpeedZ = SpeedZ;
            this.gearS = gearS;
        }
        
        
}
//----------------------------------------------------------------------------//
}
//elements installer