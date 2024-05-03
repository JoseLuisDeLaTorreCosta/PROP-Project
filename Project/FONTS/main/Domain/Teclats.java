package main.Domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import main.Domain.*;
import main.Domain.Hill_Climbing.HillClimbing;
import main.Domain.Hill_Climbing.State;
import main.Domain.Hill_Climbing.SuccessorFunction;
import main.Domain.QAP.QAP;

/**
 * @author José Luis de la Torre Costa (jose.luis.de.la.torre.costa@estudiantat.upc.edu)
 */

public class Teclats {
    String id;
    char[][] disposicio;
    Document doc;

    //Creadora de teclat amb un identificador id, el tipus d'algorisme segons el segon parametre i un document que es un text amb el contingut passat a l'ultim parametre
    public Teclats(String id, int tipus_algorisme, String text) {
        this.id = id;
        doc = new Text(text,"");
        int[][] MatMarkov = doc.getMatMarkov();
        ArrayList<Character> Alfabet = doc.getAlfabet(); 
        ReturnArray r = new ReturnArray();

        switch(tipus_algorisme) {
            case 1:
                QAP qap = new QAP(Alfabet, MatMarkov);
                qap.calcular();
                disposicio = qap.get_so_far_best_solution();
                break;
            case 2:
                State state = new State();
                state = state.iniRand((int) Math.round(Math.sqrt(Alfabet.size())), (int) Math.ceil(Math.sqrt(Alfabet.size())), Alfabet, r.calcula_dimensions(MatMarkov.length, MatMarkov[0].length), MatMarkov);
                SuccessorFunction successorfunction = new SuccessorFunction();
                HillClimbing aux = new HillClimbing(state, successorfunction);
                State end = aux.getState();
                disposicio = end.get_pos();
                break;
        }
    }
    
    //Creadora de teclat amb un identificador id, el tipus d'algorisme segons el segon parametre i un document que es una llista de paraules amb el contingut passat als dos ultims parametres
    public Teclats(String id, int tipus_algorisme, ArrayList<Paraula> p, String name) {
        this.id = id;
        doc = new Llista_Paraules(p, name);
        int[][] MatMarkov = doc.getMatMarkov();
        ArrayList<Character> Alfabet = doc.getAlfabet(); 
        ReturnArray r = new ReturnArray();

        switch(tipus_algorisme) {
            case 1:
                QAP qap = new QAP(Alfabet, MatMarkov);
                qap.calcular();
                disposicio = qap.get_so_far_best_solution();
                break;
            case 2:
                State state = new State();
                state = state.iniRand((int) Math.round(Math.sqrt(Alfabet.size())), (int) Math.ceil(Math.sqrt(Alfabet.size())), Alfabet, r.calcula_dimensions(MatMarkov.length, MatMarkov[0].length), MatMarkov);
                SuccessorFunction successorfunction = new SuccessorFunction();
                HillClimbing aux = new HillClimbing(state, successorfunction);
                State end = aux.getState();
                disposicio = end.get_pos();
                break;
        }
    }

    //Creadora de teclat amb un identificador id, el tipus d'algorisme segons el segon parametre i un document que es un alfabet amb el contingut passat als tres ultims parametres
    public Teclats(String id, int tipus_algorisme, ArrayList<Character> S, int[][] fr, String name) {
        this.id = id;
        doc = new Alfabet(S, fr, name);
        int[][] MatMarkov = doc.getMatMarkov();
        ArrayList<Character> Alfabet = doc.getAlfabet(); 
        ReturnArray r = new ReturnArray();

        switch(tipus_algorisme) {
            case 1:
                QAP qap = new QAP(Alfabet, MatMarkov);
                qap.calcular();
                disposicio = qap.get_so_far_best_solution();
                break;
            case 2:
                State state = new State();
                state = state.iniRand((int) Math.round(Math.sqrt(Alfabet.size())), (int) Math.ceil(Math.sqrt(Alfabet.size())), Alfabet, r.calcula_dimensions(MatMarkov.length, MatMarkov[0].length), MatMarkov);
                SuccessorFunction successorfunction = new SuccessorFunction();
                HillClimbing aux = new HillClimbing(state, successorfunction);
                State end = aux.getState();
                disposicio = end.get_pos();
                break;
        }
    }

    //Creadora del teclat amb l'identificador id i el document passat per l'ultim parametre. Serveix per a carregar les dades quan s'inicia sessio
    public Teclats(String id, Document doc) {
        this.id = id;
        this.doc = doc;
    }

    //Torna l'identificador del teclat
    public String getId() {
        return id;
    }

    //Torna l'identificador del document amb el que s'ha creat el teclat
    public String getIdDoc() {
        return doc.getId();
    }

    //Torna la disposicio del teclat
    public char[][] getDisposicio() {
        return disposicio;
    }

    //Et permet posar la disposicio del teclat passat per parametre
    public void SetDisposicio(char[][] disposicio) {
        this.disposicio = disposicio;
    }

    //Intercanvia la tecla en pos(i1, j1) amb la tecla en pos (i2, j2)
    public void intercanvi_tecles(int i1, int j1, int i2, int j2) {
        char aux = disposicio[i1][j1];
        disposicio[i1][j1] = disposicio[i2][j2];
        disposicio[i2][j2] = aux;
    }

    //Retorna el cost del teclat
    public double GetCost() {
        double coste = 0;
        ArrayList<Character> Alpha = doc.getAlfabet();
        int[][] MatMarkov = doc.getMatMarkov();
        ReturnArray r = new ReturnArray();
        int[][] Mat_d = r.calcula_dimensions(disposicio.length, disposicio[0].length);
        for(int i = 0; i < Alpha.size(); ++i){
            Boolean found = false;
            int aux = 0;
            for(int j = 0; j < disposicio.length && !found; ++j){
                for(int l = 0; l < disposicio[0].length && !found; ++l){
                    if(disposicio[j][l] == Alpha.get(i)) {
                        aux = j*disposicio[0].length+l;
                        found = true;
                    }
                }
            }
            for(int p = 0; p < Alpha.size(); ++p){
                found = false;
                int aux2 = 0;
                for(int j = 0; j < disposicio.length && !found; ++j){
                    for(int l = 0; l < disposicio[0].length && !found; ++l){
                        if(disposicio[j][l] == Alpha.get(p)) {
                        aux2 = j*disposicio[0].length+l;
                        found = true;
                        }
                    }
                }
                coste += Mat_d[aux][aux2]*MatMarkov[i][p];
            }
        }
        return coste;
    }

    public static void main(String[] a) throws IOException {
        
        
    }
}
