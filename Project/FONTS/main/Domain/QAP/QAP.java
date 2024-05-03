package main.Domain.QAP;

import java.util.ArrayList;

import main.Domain.*;

public class QAP {
    private int n; // numero de elements
    private int[][] mat_f; // matriu de les freqüències
    private int[][] mat_d; // matriu de les distàncies
    private ArrayList<Character> Alfabet; //alfabet 
    private char[][] so_far_best_solution; //disposició dels chars amb miilor solució
    private double coste_mejor_actual; // cost de so_far_best_solution

    //Creadora per defecte
    public QAP(){}

    //Creadora a partir d'alfabet i matriu de markov
    public QAP(ArrayList<Character> Al, int[][] TaulaMarkov) {
        this.n = Al.size();
		this.Alfabet = new ArrayList<Character>(Al);
        this.mat_f = TaulaMarkov;
        
        //Calcula numero de columnes i files
        int C = (int) Math.ceil(Math.sqrt(Al.size()));
        int F = (int) Math.round(Math.sqrt(Al.size()));
        
        ReturnArray obj = new ReturnArray();
        mat_d = obj.calcula_dimensions(F, C);
        
        //Troba una primera aproximacio al problema amb un algoritme Greedy
        iniGreedy obj2 = new iniGreedy(TaulaMarkov, Al);
        obj2.calcula();
        this.so_far_best_solution = obj2.getTeclat();
        this.coste_mejor_actual = obj2.calculaCost();
    }

    //Calcula la millor disposició de teclat
    public void calcular() {
       int C = so_far_best_solution.length;
       int F = so_far_best_solution[0].length;
       //Iniciem la matriu sol_temporal amb el caracter '_' en els 
       //llocs on pot existir un element i ' ' son els llocs que 
       //romandran buits
       char[][] sol_temporal = new char[C][F];
       for(int i = 0; i < C; ++i){
        for(int j = 0; j < F; ++j) {
            if ((i*C)+j+i < n) {
                sol_temporal[i][j] = '_';
            }
            else sol_temporal[i][j] = ' ';
            }
       }
       ArrayList<Boolean> vist= new ArrayList<Boolean>();
       for(int i = 0; i < n; ++i) vist.add(false);
       //Cridem a la funcio recursiva
       arbol_recursivo(0,0,sol_temporal, vist);
   }

   //Funcio recursiva que s'encarrega del branching de QAP
   private void arbol_recursivo(int coste_actual, int size_sol_temp, char[][] sol_temporal, ArrayList<Boolean> visitats){
    //Cas Base
    if (size_sol_temp == n){
        //Substituim si tenim un cost millor
	    if (coste_actual <= coste_mejor_actual){
		    for (int i = 0; i < so_far_best_solution.length; ++i){
                for (int j = 0; j < so_far_best_solution[0].length; ++j)so_far_best_solution[i][j] = sol_temporal[i][j];
            }
            coste_mejor_actual = coste_actual;
        }
    }

    //Cas Base 2
    else if (size_sol_temp == 0){
        //Coloquem el primer element
	    for (int i = 0;  i < n; ++i){
		    sol_temporal [0][0] = Alfabet.get(i);
            visitats.set(i, true);
            arbol_recursivo(0, 1, sol_temporal, visitats);
            visitats.set(i, false);
	    }
    }

    else{
        int valor_bound = 0;
        if (size_sol_temp != n-1){
        Bounding obj = new Bounding(coste_actual, size_sol_temp, mat_f, mat_d, Alfabet);
        valor_bound = obj.getBound(sol_temporal);
        }
        //Calculem el valor del bound i si volem continuar
        if (valor_bound < coste_mejor_actual || size_sol_temp == n-1){  
        for (int i = 0;  i < n; ++i){
            if (!visitats.get(i)){
                //afagim un nou caracter a la solucio
                visitats.set(i, true);
                char ayuda = sol_temporal[size_sol_temp/sol_temporal[0].length][size_sol_temp%sol_temporal[0].length];
                sol_temporal[size_sol_temp/sol_temporal[0].length][size_sol_temp%sol_temporal[0].length] = Alfabet.get(i);
                    //Calculem el numero de la posició on es troba el nou element
                    int aux = 0;
                    boolean found = false;
                    for(int j = 0; j < sol_temporal.length && !found; ++j){
                        for(int l = 0; l < sol_temporal[0].length && !found; ++l){
                            if(sol_temporal[j][l] == Alfabet.get(i)) {
                            aux = j*sol_temporal[0].length+l;
                            found = true;
                        }
                    }
                    }
                    //Calculem quant augmenta el cost a l'afegir la lletra i de l'alfabet
                    int cost_increase = 0;
					for(int p = 0; p < n; ++p){
                        if (visitats.get(p)){
                        found = false;
                        int aux2 = 0;
                        //Calculem el numero de la posició on es troba l'element p de l'alfabet
                        for(int j = 0; j < sol_temporal.length && !found; ++j){
                            for(int l = 0; l < sol_temporal[0].length && !found; ++l){
                                if(sol_temporal[j][l] == Alfabet.get(p)) {
                                aux2 = j*sol_temporal[0].length+l;
                                found = true;
                                }
                            }
                        }
                        if (found) {
                            cost_increase += mat_d[p][i]*mat_f[aux2][aux]
									   + mat_d[i][p]*mat_f[aux][aux2];
                        }
                        }
                    }
                    //Fem la crida recursiva
                    arbol_recursivo(coste_actual+cost_increase, size_sol_temp+1, sol_temporal, visitats);
				    //Tornem tot al seu valor original
                    visitats.set(i, false);	
                    sol_temporal[size_sol_temp/sol_temporal[0].length][size_sol_temp%sol_temporal[0].length] = ayuda;		

                }
        }
    }
    }

}


//Impresora de la matriu de posicions
public void print(){
    for (int i = 0; i < so_far_best_solution.length; i++) {
                for (int j = 0; j < so_far_best_solution[i].length; j++) {
                    System.out.print(so_far_best_solution[i][j] + " ");
                }
                System.out.println();
    }
}

//Retorna el cost de la millot configuració de 
//la matiu de posicions
public double get_coste_mejor_actual(){
    return coste_mejor_actual;
}

//Retorna la matriu de posicions
public char[][] get_so_far_best_solution(){
    return so_far_best_solution;
}
}
//Classe implementada per Ariadna Garcia Lorente