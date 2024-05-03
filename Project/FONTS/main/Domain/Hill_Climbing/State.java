package main.Domain.Hill_Climbing;
import java.util.*;

import main.Domain.*;

public class State {
   private char[][] posicions; //Matriu de posicions
   private static int[][] mat_d; //Matriu de dimensions
   private static int[][] mat_f; //Matriu de frecuencia
   private static ArrayList<Character> Alafabet; //Llista d'Alfabet

   //Creadora per defecte de l'estat de Hill Climbing
   public State(){}

   //Copiadora d'un estat de Hill Climbing
   public State(State og){
        //Crea una copia de la matriu de posicions 
        this.posicions = new char[og.posicions.length][og.posicions[0].length];

        for(int i = 0; i < posicions.length; ++i){
            for(int j = 0; j < posicions[0].length; ++j) posicions[i][j]=og.posicions[i][j];
        }
   }

   //Creem un estat a partir de les dades intoduides
   public State(int C, int F, ArrayList<Character> A, int[][] MAT_D, int[][] MAT_F){
        //Creem una matriu de posicions amb el caracter _
        //Aquest caractes simbolitza una posicio buida
        posicions = new char[C][F];
        for(int i = 0; i < C; ++i){
            for(int j = 0; j < F; ++j) posicions[i][j] = '_';
        }

        //Guardem una copia d'Alfabet
        int p = A.size();
        Alafabet = new ArrayList<Character>();
        for (int i=0; i<p; i++) Alafabet.add(A.get(i));

        //Guardem una copia de la matriu de distancia
        int n = (MAT_D[0].length);
        mat_d=new int[n][n];
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                mat_d[i][j] = MAT_D[i][j];
            }           
        }

        //Guardem una copia de la matriu de frequencia
        int m = (MAT_F[0].length);
        mat_f=new int[m][m];
        for (int i=0; i<m; i++) {
            for (int j=0; j<m; j++) {
                mat_f[i][j] = MAT_F[i][j];
            }           
        }
   }

   //Ens diu si hi ha algun valor del vector a false
   private Boolean all_vist(ArrayList<Boolean> vist){
        Boolean found = false;
        for(int i = 0; i< vist.size() && !found; ++i) if (!vist.get(i)) found = true;
        return found;
   }

   //Ens retorna un estat amb la matriu de posicions inicialitzada 
   //amb valors aleatoris d'alfabet
   public State iniRand(int C, int F, ArrayList<Character> A, int[][] MAT_D, int[][] MAT_F){
        //Creem un nou estat
        State a = new State(C,F,A, MAT_D, MAT_F);
        
        ArrayList<Boolean> visitades = new ArrayList<Boolean>();
        for(int z = 0; z < A.size(); ++z) visitades.add(false);
        
        Random num = new Random();
        
        //Asignem aleatoriament les lletres d'alfabet a la matiru de 
        //posicions, les posicions buides contindran el caracter '_'
        for(int i = 0; i < C; ++i){
            for(int j = 0; j < F; ++j) {
                int stati = num.nextInt(A.size());
                while (visitades.get(stati) && all_vist(visitades)) stati = num.nextInt(A.size());
                if (all_vist(visitades)){
                    a.posicions[i][j] = A.get(stati);
                    visitades.set(stati,true);
                }
            }
        }
        return a;
   }

   //Operador que intercanvia dos posicions a la matriu de posicions
   public void switch_chars(int i, int j, int i2, int j2){
        char aux = posicions[i][j];
        posicions[i][j]=posicions[i2][j2];
        posicions[i2][j2] = aux;
   }

   //Impresora de la matriu de posicions
   public void print_state(){
    for (int i = 0; i < posicions.length; i++) {
        for (int j = 0; j < posicions[i].length; j++) {
            System.out.print(posicions[i][j] + " ");
        }
        System.out.println(); // Seguent fila
    }  
   }

   //Retorna el cost de la configuració actual de 
   //la matiu de posicions
   public double get_cost(){
        HeuristicFunction B  = new HeuristicFunction();
        return (B.getHeuristicValue(this));
   }

   //Retorna l'alfabet s'un estat
   public  ArrayList<Character> get_alfabet(){
    return Alafabet;
   }

   //Retorna la matriu de frequencia
   public  int[][] get_mat_f(){
    return mat_f;
   }

   //Retorna la matriu de distancia
   public  int[][] get_mat_d(){
    return mat_d;
   }

   //Retorna la distribució de l'estat
   public  char[][] get_pos(){
    return posicions;
   }
   
   //Permet canviar els caracters de la matriu de posicions
   public  void set_pos(int i, int j, char a){
    posicions[i][j] = a;
   }
}
//Classe implementada per Ariadna Garcia Lorente