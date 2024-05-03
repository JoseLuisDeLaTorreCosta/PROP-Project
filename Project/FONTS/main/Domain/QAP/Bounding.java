package main.Domain.QAP;

import java.util.*;

public class Bounding {
    private int costActual;
    private int n;
    private int[][] matFreq;
    private int[][] matDist;
    private ArrayList<Character> alfabet;
    private int m;
    private int[] lletraAPosicio;
    private int[] posicioTeLletra;

    /**
     * @param cost
     * @param size
     * @param mF
     * @param mD
     * @param alf
     * @pre size > 0, mF.length = mF[0].length = mD.length = mD[0].length > 0, alf.length > 0
     * @post es crea una instància de Bounding amb matriu de freqüències mF, matriu de distàncies mD i alfabet alf
     */
    Bounding(int cost, int size, int[][] mF, int[][] mD, ArrayList<Character> alf) {
        costActual = cost;
        m = size;
        matFreq = mF;
        matDist = mD;
        n = matFreq.length;
        alfabet = alf;
    }

    /**
     * @param solParcial
     * @post el vector lletraAPosicio conté a cada posició [i] la posició que ocupa la lletra amb índex i a alfabet en la solParcial, o -1 si no hi és
     * @post el vector posicioTeLletra conté a cada posicio [i] l'índex que té a alfabet de la lletra que ocupa la posició i de la solParcial, o -1 si no conté una lletra
     */
    private void initVectorsAuxiliars(char[][] solParcial) {
        lletraAPosicio = new int[n]; //cada element i té la posició on es troba a la solParcial la lletra i d'aquella posició en l'array, -1 altrament
        posicioTeLletra = new int[n]; //cada element i té l'identificador de la lletra que es troba a la posició i de la solParcial
        for (int i = 0; i < n; ++i) lletraAPosicio[i] = -1;
        int alt = solParcial.length;
        int ample = solParcial[0].length;
        int comptadorPosicions = 0;
        for (int i = 0; i < alt; ++i) {
            for (int j = 0; j < ample; ++j) {
                if (solParcial[i][j] != ' ') {
                    if (solParcial[i][j] != '_') {
                        int indexLletra = alfabet.indexOf(solParcial[i][j]);
                        lletraAPosicio[indexLletra] = comptadorPosicions;
                        posicioTeLletra[comptadorPosicions] = indexLletra;
                    }
                    else posicioTeLletra[comptadorPosicions] = -1;
                    ++comptadorPosicions;
                }
            }
        }
        return;
    }

    /**
     * @param vec
     * @post els valors del vector vec han quedat a l'invers
     */
    private void reverse(int[] vec) {
        int last = vec.length - 1;
        int middle = vec.length / 2;
        for (int i = 0; i < middle; ++i) {
            int aux = vec[i];
            vec[i] = vec[last - i];
            vec[last - i] = aux;
        }
        return;
    }

    /**
     * @param posicio
     * @pre la posició posicio de la solParcial no hi té cap lletra col·locada
     * @return retorna un vector de mida n-m que conté les distàncies entre la posició posicio de la solParcial i totes les posicions de la solParcial que encara no tenen cap lletra assignada
     */
    private int[] getVecDist(int posicio) {
        int[] vecDist = new int[n - m];
        int comptador = 0;
        for (int i = 0; i < n; ++i) {
            if(posicioTeLletra[i] == -1) {
                vecDist[comptador] = matDist[posicio][i];
                ++comptador;
            }
        }
        Arrays.sort(vecDist);
        reverse(vecDist);
        return vecDist;
    }

    /**
     * @param lletra
     * @pre el caràcter amb índex lletra a alfabet no està col·locat a la solParcial
     * @return retorna un vector de mida n-m que conté el cost entre el caràcter lletra i tots els caràcters d'alfabet que encara no tenen cap posició assignada a la solParcial
     */
    private int[] getVecCost(int lletra) {
        int[] vecCost = new int[n - m];
        int comptador = 0;
        for (int i = 0; i < n; ++i) {
            if (lletraAPosicio[i] == -1) {
                if (lletra == i) vecCost[comptador] = matFreq[lletra][lletra];
                else vecCost[comptador] = matFreq[lletra][i] + matFreq[i][lletra];
                ++comptador;
            }
        }
        Arrays.sort(vecCost);
        return vecCost;
    }

    /**
     * @param vec1
     * @param vec2
     * @pre vec1.length = vec2.length
     * @return retorna el producte escalar dels vectors vec1 i vec2
     */
    private int producteEscalar(int[] vec1, int[] vec2) {
        int prod = 0;
        int m = vec1.length;
        for (int i = 0; i < m; ++i) prod += vec1[i] * vec2[i];
        return prod;
    }

    /**
     * @return retorna la matriu (n-m)*(n-m) C1, on cada posició [x][y] conté el cost de col·locar la lletra encara no col·locada a la solParcial x a la posició y respecte a la resta de lletres ja col·locades
     */
    private int[][] getC1() {
        int[][] C1 = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (lletraAPosicio[i] != -1 || posicioTeLletra[j] != -1) C1[i][j] = -1;
                else {
                    C1[i][j] = 0;
                    for (int k = 0; k < n; ++k) {
                        if (lletraAPosicio[k] != -1) C1[i][j] += matDist[j][lletraAPosicio[k]] * (matFreq[k][i] + matFreq[i][k]);
                    }
                }
            }
        }

        int[][] C1real = new int[n - m][n - m];
        int comptadorFila = 0;
        for (int i = 0; i < n; ++i) {
            int comptadorColumna = 0;
            for (int j = 0; j < n; ++j) {
                if (C1[i][j] != -1) {
                    C1real[comptadorFila][comptadorColumna] = C1[i][j];
                    ++comptadorColumna;
                }
            }
            if (comptadorColumna > 0) ++comptadorFila;
        }
        return C1real;
    }

    /**
     * @return retorna la matriu (n-m)*(n-m) C2, on cada posició [x][y] conté una cota del cost de col·locar la lletra encara no col·locada a la solParcial x a la posició y respecte la resta de lletres encara no col·locades
     */
    private int[][] getC2() {
        int[][] C2 = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (lletraAPosicio[i] != -1 || posicioTeLletra[j] != -1) C2[i][j] = -1;
                else {
                    int[] vecDist = getVecDist(j);
                    int[] vecCost = getVecCost(i);
                    C2[i][j] = producteEscalar(vecCost, vecDist);
                }
            }
        }

        int[][] C2real = new int[n - m][n - m];
        int comptadorFila = 0;
        for (int i = 0; i < n; ++i) {
            int comptadorColumna = 0;
            for (int j = 0; j < n; ++j) {
                if (C2[i][j] != -1) {
                    C2real[comptadorFila][comptadorColumna] = C2[i][j];
                    ++comptadorColumna;
                }
            }
            if (comptadorColumna > 0) ++comptadorFila;
        }
        return C2real;
    }

    /**
     * @param solParcial
     * @pre solParcial conté m characters d'alfabet
     * @return retorna la suma del cost actual i la cota Gilmore-Lawler de la solParcial, segons les matriu de freqüències i distàncies
     */
    public int getBound(char[][] solParcial) {
        initVectorsAuxiliars(solParcial);

        int[][] C1 = getC1();
        int[][] C2 = getC2();
        int[][] sumaC1C2 = new int[C1.length][C2[0].length];
        for (int i = 0; i < C1.length; ++i) {
            for (int j = 0; j < C2[0].length; ++j) sumaC1C2[i][j] = C1[i][j] + C2[i][j];
        }
        HungarianAlgorithm hungAlg = new HungarianAlgorithm(sumaC1C2);
        return costActual + hungAlg.Hungarian();
    }
}
//Classe implementada per Ferran Martínez Vives
