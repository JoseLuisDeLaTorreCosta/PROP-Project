package main.Domain;

import java.util.*;

public class ReturnArray {

        //Calcula la matriu de distancia mitjançant la distancia manhattan
        public int[][] calcula_dimensions(int F, int C){
            int[][] mat_d = new int[F*C][F*C];
            for (int i = 0; i < F*C; i += C){
                for (int j = 0; j < C; ++j){
                    for (int x = 0; x < F*C; x += C){
                        for (int y = 0; y < C; ++y){
                            mat_d[i+j][x+y] = Math.abs((i/C)-(x/C)) + Math.abs(y-j);
                        }
                    }
                }
            }
            return mat_d;
        }
    
}//Classe implementada per Ariadna Garcia Lorente
