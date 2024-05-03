
package main.Domain.QAP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.crypto.Cipher;

/**
 * @author José Luis de la Torre Costa (jose.luis.de.la.torre.costa@estudiantat.upc.edu)
 */

public class HungarianAlgorithm {
    int[][] Dist;
    int[][] Dist_or;

    //Creadora del Hungarian Algorithm. En cas de que la matriu no sigui quadrada, l'ajusta
    public HungarianAlgorithm(int[][] Dist) {
        if (Dist.length == Dist[0].length) this.Dist = Dist;
        else ajustar_matriu(Dist);
        this.Dist_or = new int[this.Dist.length][this.Dist[0].length];
        for (int i = 0; i < this.Dist.length; ++i) {
            for (int j = 0; j < this.Dist[0].length; ++j) this.Dist_or[i][j] = this.Dist[i][j];
        }
    }

    //Algorisme que executa el Hungarian Algorithm a la matriu Dist
    public int Hungarian() {
        //Substreure el mínim de cada fila
        int[] min = new int[Dist.length];
        for (int i = 0; i < Dist.length; ++i) min[i] = -1;
        int[] min2 = min.clone();

        for (int i = 0; i < Dist.length; i++)
            for (int j = 0; j < Dist[0].length; j++)
                if (min[i] > Dist[i][j] || min[i] == -1) min[i] = Dist[i][j];
        for (int i = 0; i < Dist.length; i++)
            for (int j = 0; j < Dist[0].length; j++)
                Dist[i][j] -= min[i];

        //Substreure el mínim de cada columna
        for (int i = 0; i<Dist[0].length; i++)
            for (int j = 0; j<Dist.length; j++)
                    if (min2[i] > Dist[j][i] || min2[i] == -1) min2[i] = Dist[j][i];
        for (int i = 0; i<Dist[0].length; i++)
            for (int j = 0; j<Dist.length; j++) {
                    Dist[j][i] -= min2[i];
        }

        boolean[] rows_covered = new boolean[Dist.length];
        boolean[] cols_covered = new boolean[Dist[0].length];
        int[] rowsStarred = new int[Dist.length];
        MinimalLines mLines = new MinimalLines(Dist, rows_covered, cols_covered, rowsStarred);
        int x;
        //Es mira l'assignacio de linies minimes, si aquesta cobreix totes les files i columnes, sortim del bucle
        while((x = mLines.calcMinimalLines()) < Dist.length) {
            //Aquest for troba el minim no cobert
            int minim = -1;
            for (int i = 0; i < Dist.length; ++i) {
                for (int j = 0; j < Dist[0].length; ++j) {
                    if (!rows_covered[i] && !cols_covered[j] && (minim == -1 || Dist[i][j] < minim)) {
                        minim = Dist[i][j];
                    } 
                }
            }
            //Aquest for suma el minim no cobert a les linies cobertes
            for (int i = 0; i < Dist.length; ++i) {
                for(int j = 0; j < Dist[0].length; ++j) {
                    if (rows_covered[i]) Dist[i][j]+=minim;
                    if (cols_covered[j]) Dist[i][j]+=minim;
                }
            }

            //Aquest for troba el minim de tota la matriu
            minim = -1;
            for (int i = 0; i < Dist.length; ++i) {
                for (int j = 0; j < Dist[0].length; ++j) {
                    if (Dist[i][j] < minim || minim == -1) {
                        minim = Dist[i][j];
                    } 
                }
            }
            //Aquest for resta el minim a tots els elements de la matriu
            for (int i = 0; i < Dist.length; ++i) {
                for (int j = 0; j < Dist[0].length; ++j) {
                    Dist[i][j] -= minim;
                }
            }
        }

        //Calcula el cost de la matriu 
        int cost = 0;

        for (int i = 0; i < Dist.length; ++i) {
            cost += Dist_or[i][rowsStarred[i]];
        }
        
        return cost;
    }

    //Retorna la matriu despres de fer el Hungarian
    public int[][] getMatriu() {
        return Dist;
    }

    //Aquesta funcio ajusta la matriu per a que sigui quadrada
    public void ajustar_matriu(int[][] Dist) {
        //Mira si s'ha d'ajustar les files o les columnes
        if (Dist.length < Dist[0].length) {
            this.Dist = new int[Dist[0].length][Dist[0].length]; 
        }
        else {
            this.Dist = new int[Dist.length][Dist.length];
        }

        //Col·loca tots els elements coneguts a la nova matriu i troba el maxim global
        int maxim = -1;
        for (int i = 0; i < Dist.length; ++i) {
            for (int j = 0; j < Dist[0].length; ++j) {
                this.Dist[i][j] = Dist[i][j];
                if (Dist[i][j] > maxim) maxim = Dist[i][j];
            }
        }

        //Col·loca el maxim global a les posicions buides
        if (Dist.length < Dist[0].length) {
            for (int i = Dist.length; i < Dist[0].length; ++i) {
                for (int j = 0; j < Dist[0].length; ++j) this.Dist[i][j] = maxim;
            }
        }
        else {
            for (int i = 0; i < Dist.length; ++i) {
                for (int j = Dist[0].length; j < Dist.length; ++j) this.Dist[i][j] = maxim;
            }
        }
    }

    public static void main(String[] a) {
        /*
        Scanner Input = new Scanner(System.in);
        System.out.println("Coloca el nombre de files i columnes de la matriu C1+C2:");
        int C = Input.nextInt();

        int[][] C1_C2 = new int[C][C];

        System.out.println("Introdueix els valors de la matriu");
        for (int i = 0; i < C; ++i) {
            for (int j = 0; j < C; ++j) C1_C2[i][j] = Input.nextInt();
        }
        */
        int[][] C1_C2 = {{20, 58, 75, 89, 98, 99, 19, 18, 20, 58, 54, 32, 20, 53, 7, 1, 90, 65, 61, 61, 72, 68, 8, 81, 28},
        {41, 43, 35, 30, 80, 75, 12, 41, 32, 25, 70, 90, 26, 36, 79, 19, 29, 75, 15, 6, 64, 19, 27, 81, 96},
        {17, 98, 98, 77, 6, 54, 38, 94, 69, 0, 8, 25, 98, 54, 46, 12, 9, 55, 34, 38, 75, 76, 83, 36, 94},
        {8, 36, 55, 70, 41, 4, 37, 74, 6, 64, 70, 0, 1, 37, 69, 95, 35, 15, 25, 98, 65, 64, 84, 41, 60},
        {4, 29, 8, 48, 23, 55, 44, 7, 5, 85, 62, 8, 21, 28, 9, 86, 4, 34, 16, 69, 18, 18, 88, 46, 32},
        {63, 98, 88, 10, 81, 92, 91, 3, 97, 31, 4, 98, 20, 41, 64, 17, 86, 8, 69, 9, 39, 99, 3, 44, 44},
        {1, 68, 6, 31, 18, 17, 57, 57, 80, 73, 1, 66, 18, 85, 28, 39, 16, 32, 38, 83, 26, 49, 56, 43, 43},
        {20, 69, 58, 36, 23, 54, 59, 93, 93, 85, 51, 75, 35, 36, 42, 32, 77, 20, 4, 42, 11, 37, 89, 77, 26},
        {20, 22, 37, 30, 44, 1, 25, 93, 49, 33, 18, 33, 90, 32, 95, 17, 33, 8, 69, 51, 80, 22, 41, 60, 95},
        {61, 59, 91, 9, 83, 38, 44, 19, 2, 98, 57, 93, 11, 94, 50, 60, 2, 56, 45, 65, 87, 79, 48, 46, 33},
        {92, 62, 16, 77, 63, 8, 92, 79, 84, 93, 69, 54, 56, 31, 36, 20, 37, 37, 47, 8, 23, 66, 85, 54, 57},
        {38, 19, 81, 27, 26, 14, 47, 92, 31, 99, 45, 91, 67, 56, 50, 95, 89, 30, 71, 82, 54, 9, 97, 95, 19},
        {27, 89, 44, 55, 96, 17, 77, 82, 16, 2, 41, 68, 98, 57, 36, 42, 65, 17, 72, 58, 28, 82, 61, 89, 71},
        {71, 63, 48, 1, 77, 1, 76, 40, 82, 86, 54, 5, 12, 40, 51, 1, 23, 97, 49, 7, 4, 12, 1, 68, 81},
        {65, 88, 42, 61, 50, 21, 15, 51, 45, 78, 2, 71, 70, 7, 44, 66, 73, 54, 0, 10, 98, 6, 19, 56, 49},
        {51, 34, 65, 78, 94, 13, 69, 49, 79, 72, 15, 85, 32, 57, 35, 92, 88, 54, 39, 59, 5, 48, 19, 47, 70},
        {7, 17, 7, 8, 91, 80, 36, 78, 64, 74, 74, 8, 88, 9, 57, 61, 75, 9, 19, 46, 39, 24, 26, 39, 89},
        {29, 40, 49, 24, 63, 71, 54, 85, 16, 96, 49, 79, 26, 82, 48, 14, 23, 62, 45, 95, 34, 33, 14, 5, 93},
        {56, 85, 84, 88, 89, 31, 98, 29, 17, 81, 45, 96, 28, 52, 57, 34, 2, 80, 76, 73, 11, 13, 40, 63, 60},
        {12, 39, 6, 65, 94, 40, 87, 36, 11, 46, 80, 90, 27, 25, 3, 9, 80, 92, 84, 99, 72, 5, 44, 34, 46},
        {50, 38, 55, 18, 48, 69, 73, 25, 2, 7, 51, 75, 25, 50, 63, 38, 77, 26, 54, 82, 20, 43, 32, 85, 70},
        {66, 62, 72, 16, 79, 1, 7, 78, 19, 60, 37, 44, 51, 89, 34, 53, 51, 19, 72, 16, 84, 55, 58, 58, 40},
        {10, 29, 94, 45, 35, 48, 85, 31, 98, 88, 55, 10, 57, 59, 67, 30, 2, 73, 16, 7, 56, 76, 97, 17, 27},
        {76, 54, 51, 44, 51, 88, 18, 75, 21, 39, 46, 78, 10, 41, 9, 85, 25, 94, 58, 92, 73, 11, 98, 49, 59},
        {9, 60, 52, 1, 21, 22, 19, 46, 48, 75, 51, 57, 67, 64, 59, 58, 19, 53, 32, 14, 74, 32, 20, 74, 61}};
        HungarianAlgorithm h = new HungarianAlgorithm(C1_C2);
        int x = h.Hungarian();

        System.out.println("Cost: " + x);
    }
}
