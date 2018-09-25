/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GCodeConverter;

/**
 *
 * @author Слава
 */
public class TaskForDriverNdXSdYSdZSM {
    
    //textAreaGcode.appendText("N:0002 \t ΔX:-000.254  S:025.000 \t ΔY:+000.956  S:025.000 \t ΔZ:+001.356  S:025.000 \t M:+0520\r\n");
    public int N;
    
    public float X;
    
    public float F;
    
    public float Y;
    
    public float Z;
    
    public float S;

    public TaskForDriverNdXSdYSdZSM(int N, float X, float Y, float Z, float F, float S) {
        this.N = N;
        this.X = X;
        this.F = F;
        this.Y = Y;
        this.Z = Z;
        this.S = S;
    }
}
