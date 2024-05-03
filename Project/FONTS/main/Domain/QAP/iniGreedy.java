package main.Domain.QAP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import main.Domain.*;

import java.util.ArrayList;

/**
 * @author José Luis de la Torre Costa (jose.luis.de.la.torre.costa@estudiantat.upc.edu)
 */

public class iniGreedy {

    char[][] teclat;
    ArrayList<Character> Alpha;
    int[][] MatMarkov;
    int[][] Mat_d;

    //Classe privada que serveix com una estructura de dades que guarda tres elements
    private class Trioint{
        int first;
        int second;
        int third;

        //Creadora de Trioint que assigna els parametres passats a l'objecte
        public Trioint(int first, int second, int third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        //Consultora que retorna el primer element del Trioint
        public int first() {
            return first;
        }

        //Consultora que retorna el segon element del Trioint
        public int second() {
            return second;
        }

        //Consultora que retorna el tercer element del Trioint
        public int third() {
            return third;
        }
    }
    

    //Creadora que assigna els parametres passats a l'objecte i crea un teclat buit
    public iniGreedy(int[][] MatMarkov, ArrayList<Character> Alpha) {
        this.Alpha = Alpha;
        this.MatMarkov = MatMarkov;
        ReturnArray r = new ReturnArray();
        this.Mat_d = r.calcula_dimensions((int) Math.round(Math.sqrt(Alpha.size())), (int) Math.ceil(Math.sqrt(Alpha.size())));

        teclat = new char[(int)Math.round(Math.sqrt(Alpha.size()))][(int) Math.ceil(Math.sqrt(Alpha.size()))];
        for(int i = 0; i < teclat.length; ++i){
            for(int j = 0; j < teclat[0].length; ++j) teclat[i][j] = '_';
        }
    }

    //Funcio que s'encarrega de executar l'algorisme greedy i tindre una primera disposicio de teclat per al QAP
    public void calcula() {
        //Aquests dos primers fors s'encarreguen de trobar el caracter mes frequent, gracies a la matriu de markov, i el posem en mig del teclat
        char mes_frequent = '_';
        int pos = -1;
        int max_freq = -1;
        for (int i = 0; i < MatMarkov.length; ++i) {
            int suma = 0;
            for (int j = 0; j < MatMarkov[0].length; ++j) {
                suma += MatMarkov[i][j];
            }
            if (suma > max_freq) {
                max_freq = suma;
                mes_frequent = Alpha.get(i);
                pos = i;
            }
        }
        teclat[teclat.length/2][teclat.length/2] = mes_frequent;

        //Ara, a partir d'aquesta caracter, posem a les posicions adjacents els caracters mes frequents que no estan en el teclat
        //Despres, a partir de la segona tecla col·locada fem el mateix i fem aquesta accio fins que no tinguem tecles disponibles
        boolean[] colocats = new boolean[Alpha.size()];
        for (int i = 0; i < Alpha.size(); ++i) colocats[i] = false;

        colocats[pos] = true;

        Queue<Trioint> pos_tract = new LinkedList<Trioint>();
        pos_tract.add(new Trioint(teclat.length/2, teclat.length/2, pos));

        while (!pos_tract.isEmpty()) {
            Trioint top = pos_tract.poll();
            int p = top.third();

            //Col·loca el caracter respecte a l'actual mes frequent a la posicio de la esquerra si esta disponible
            if (top.second() > 0 && teclat[top.first()][top.second() - 1] == '_' && (pos = mes_frequent_no_col(colocats, p)) != -1) {
                colocats[pos] = true;
                teclat[top.first()][top.second()-1] = Alpha.get(pos);

                pos_tract.add(new Trioint(top.first(), top.second()-1, pos));
            }
            //Col·loca el caracter respecte a l'actual mes frequent a la posicio de dalt si esta disponible
            if (top.first() > 0 && teclat[top.first() - 1][top.second()] == '_' && (pos = mes_frequent_no_col(colocats, p)) != -1) {
                colocats[pos] = true;
                teclat[top.first()-1][top.second()] = Alpha.get(pos);

                pos_tract.add(new Trioint(top.first()-1, top.second(), pos));
            }
            //Col·loca el caracter respecte a l'actual mes frequent a la posicio de la dreta si esta disponible
            if (top.second() < (teclat[0].length - 1) && teclat[top.first()][top.second() + 1] == '_' && (pos = mes_frequent_no_col(colocats, p)) != -1) {
                colocats[pos] = true;
                teclat[top.first()][top.second()+1] = Alpha.get(pos);

                pos_tract.add(new Trioint(top.first(), top.second()+1, pos));
            }
            //Col·loca el caracter respecte a l'actual mes frequent a la posicio de baix si esta disponible
            if (top.first() < (teclat.length - 1) && teclat[top.first() + 1][top.second()] == '_' && (pos = mes_frequent_no_col(colocats, p)) != -1) {
                colocats[pos] = true;
                teclat[top.first()+1][top.second()] = Alpha.get(pos);

                pos_tract.add(new Trioint(top.first()+1, top.second(), pos));
            }
        }
    }

    //Retorna el teclat creat amb l'algorisme greedy
    public char[][] getTeclat() {
        return teclat;
    }

    //Retorna la matriu de distancies
    public int[][] getMat_d() {
        return Mat_d;
    }

    //Calcula i retorna el cost del teclat creat
    public double calculaCost() {
        double coste = 0;
        for(int i = 0; i < Alpha.size(); ++i){
            Boolean found = false;
            int aux = 0;
            for(int j = 0; j < teclat.length && !found; ++j){
                for(int l = 0; l < teclat[0].length && !found; ++l){
                    if(teclat[j][l] == Alpha.get(i)) {
                        aux = j*teclat[0].length+l;
                        found = true;
                    }
                }
            }
            for(int p = 0; p < Alpha.size(); ++p){
                found = false;
                int aux2 = 0;
                for(int j = 0; j < teclat.length && !found; ++j){
                    for(int l = 0; l < teclat[0].length && !found; ++l){
                        if(teclat[j][l] == Alpha.get(p)) {
                        aux2 = j*teclat[0].length+l;
                        found = true;
                        }
                    }
                }
                coste += Mat_d[aux][aux2]*MatMarkov[i][p];
                }
            }
            //System.out.println(coste);
          return coste;
    }

    //Retorna el caracter mes frequent respecte al passat per l'ultim parametre (es passa la seva posicio en l'alfabet) i que
    //estigui disponible
    public int mes_frequent_no_col(boolean[] colocats, int pos_char_en_TM) {
        int freq = -1;
        int pos = -1;
        for (int i = 0; i < Alpha.size(); ++i) {
            if (!colocats[i] && MatMarkov[pos_char_en_TM][i] > freq) {
                freq = MatMarkov[pos_char_en_TM][i];
                pos = i;
            }
        }
        return pos;
    }

    
    public static void main(String[] a) {
        ArrayList<Character> S = new ArrayList<Character>();
        int[][] fr;
        Scanner input = new Scanner(System.in);
        
        System.out.println("Posa el nombre de carácters de l'alfabet");
        int n = input.nextInt();
        fr = new int[n][n];
        for (int i = 0; i < n; ++i) {
            System.out.println("Introdueix el simbol i el seu vector de frequencia:");
            char A = input.next().charAt(0);
            S.add(A);
            int B;
            for(int j = 0; j < n; ++j){
                B = input.nextInt();
                fr[i][j] = B;
            }
        }
        
        input.close();

        iniGreedy x = new iniGreedy(fr, S);
        x.calcula();
        char[][] teclat_resultant = x.getTeclat();

        for (int i = 0; i < teclat_resultant.length; ++i) {
            for (int j = 0; j < teclat_resultant[0].length; ++j) {
                System.out.print(teclat_resultant[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println(x.calculaCost());
    }
}