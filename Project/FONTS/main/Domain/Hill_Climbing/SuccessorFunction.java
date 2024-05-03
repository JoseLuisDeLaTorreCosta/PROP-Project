package main.Domain.Hill_Climbing;

import java.util.*;

public class SuccessorFunction {
    //Retorna tots els fills potencialment explorables d'un estat
    public List getSuccessors(Object state) {
        ArrayList<State> retVal = new ArrayList<>();
        State estat = (State) state;
        HeuristicFunction B  = new HeuristicFunction(); 
        //Utilitzem l'optimitzacio best per només introduir a la 
        //llista retVal estats millors al millor que hem trobat
        State best = estat;
        char [][] posicions = estat.get_pos();
        
        //Mira totes les combinacions posibles que es poden fer a 
        //l'estat state. En cas que trobem un estat millor que best
        //guardem el nou estat
        for(int i = 0; i < posicions.length; ++i){
            for(int j = 0; j < posicions.length; ++j){
                for(int i2 = 0; i2 < posicions.length; ++i2){
                    for(int j2 = 0; j2 < posicions.length; ++j2){
                        State nou_estat = new  State(estat);
                        nou_estat.switch_chars(i,j,i2,j2);
                        if(B.getHeuristicValue(best) > B.getHeuristicValue(nou_estat)){
                            best = nou_estat;
                            retVal.add(nou_estat);
                        }
                    }
                }
            }
        }
        return retVal;
    }
}
//Classe implementada per Ariadna Garcia Lorente