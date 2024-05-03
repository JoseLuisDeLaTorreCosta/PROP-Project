package main.Domain.Hill_Climbing;

import java.util.*;
public class HeuristicFunction {
    
    //Retorna el cost de l'estat
    public double getHeuristicValue(Object state){
        State estat = (State) state;
        double coste = 0;
        ArrayList<Character> Alafabet = estat.get_alfabet();
        char[][] posicions = estat.get_pos();
        int[][] mat_d = estat.get_mat_d();
        int[][] mat_f = estat.get_mat_f();
        
        //Recorrerem la llista d'Alfabet
        for(int i = 0; i < Alafabet.size(); ++i){
            Boolean found = false;
            int aux = 0;
            //Busquem en quina posicio es troba la lletra i d'alfabet
            for(int j = 0; j < posicions.length && !found; ++j){
                for(int l = 0; l < posicions[0].length && !found; ++l){
                    if(posicions[j][l] == Alafabet.get(i)) {
                        aux = j*posicions[0].length+l;
                        found = true;
                    }
                }
            }
            //Comparem la lletra i d'alfabet amb la resta de lletres
            for(int p = 0; p < Alafabet.size(); ++p){
                found = false;
                int aux2 = 0;
                //Busquem en quina posicio es troba la lletra p d'alfabet
                for(int j = 0; j < posicions.length && !found; ++j){
                    for(int l = 0; l < posicions[0].length && !found; ++l){
                        if(posicions[j][l] == Alafabet.get(p)) {
                        aux2 = j*posicions[0].length+l;
                        found = true;
                        }
                    }
                }
                //Augmantem la variable coste amb el cost entre la lletra i
                //d'alfabet i la lletra p d'alfabet
                coste += mat_d[aux][aux2]*mat_f[i][p];
            }
        }
          //retornem la variable coste
          return coste;  
    }
            
}//Classe implementada per Ariadna Garcia Lorente
