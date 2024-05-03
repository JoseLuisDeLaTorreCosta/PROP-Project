package main.Domain;

import java.util.*;

public class Alfabet extends Document{
    private ArrayList<Character> simbols; //llista de caracters
    private int[][] frequencia; //matriu de frequencia
    private String id; // identificador

    //Creadora per defecte en funcio d'alfabet, una matriu de frequencia i name
    public Alfabet(ArrayList<Character> S, int[][] fr, String name) {
        simbols = S;
        frequencia = fr;
        super.setAlfabet(S);
        super.setMatMarkov(fr);
        super.setId(name);
        id = name;
    }
    
    //Retorna la matriu de Frequencia
    public int[][] calcMatMarkov() {
        return frequencia;
    }

    //Retorna el tamany de n
    public int get_n(){
        return simbols.size();
    }

    //Retorna l'alfabet
    public ArrayList<Character> get_alfabet(){
        return simbols;
    }

    //Retorna la matriu de Frequencia
    public int[][] get_matriu(){
        return frequencia;
    }

    //Retorna l'identificador
    public String get_id(){
        return id;
     }

    //Retorna True a la pregunta isAlfabet()
    public boolean isAlfabet() {
        return true;
    }
}

//Classe implementada per Ariadna Garcia Lorente
