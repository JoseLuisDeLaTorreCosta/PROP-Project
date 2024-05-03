package main.Domain;

import java.util.*;

public class Llista_Paraules extends Document {


         private int [][] taula_marcov; //matriu de frequencia
         private ArrayList<Paraula> llista; //llista de paraules
         private int numero_paraules; //tamany de la llista de paraules
         private ArrayList<Character> alfabet; //llista de caracters
         private String id;// identificador


         //Creadora de llista de paraules a partir de la llista de paraules i un name
         public Llista_Paraules(ArrayList<Paraula> aux, String name){
            String A = " ";
            int B = 0;
            numero_paraules = aux.size();
            llista = aux;
            //Calcula l'alfabet d'aux
            alfabet = new ArrayList<Character>();
            for(int i = 0; i < numero_paraules; ++i ){
                A = aux.get(i).get_nom();
                for (int j = 0; j < A.length(); ++j){
                     if (!alfabet.contains(A.charAt(j))) alfabet.add(A.charAt(j));
                }
            }
            //Calcula la matriu de frequencia
            taula_marcov = new int[alfabet.size()][alfabet.size()];
            for(int i = 0; i < numero_paraules; ++i){
                A = llista.get(i).get_nom();
                B = llista.get(i).get_freq();
                for (int j = 0; j < A.length() -1; ++j){
                        int x = alfabet.indexOf(A.charAt(j));
                        int y = alfabet.indexOf(A.charAt(j+1));
                        taula_marcov[x][y] += B;
                    }
                int x = alfabet.indexOf(A.charAt(A.length()-1));
                int y = alfabet.indexOf(A.charAt(0));
                taula_marcov[x][y] += B;
            }

            id = super.setId(name);
            alfabet=super.setAlfabet(alfabet);
            taula_marcov=super.setMatMarkov(taula_marcov);
        }

         //Impresora de Llista de paraules
         public void Consultar_Llista_Paraules(){
            for (int i = 0; i < numero_paraules; ++i){
                System.out.println(llista.get(i).get_nom() + " " + llista.get(i).get_freq());
            }
            System.out.println(alfabet);
         }

         //Retorna la matriu de Frequencia
         public int[][] calcMatMarkov(){        
            return taula_marcov;
         }

         //Retorna l'alfabet
         public ArrayList<Character> get_alfabet(){
            return alfabet;
         }

         //Retorna la llista de paraules
         public ArrayList<Paraula> getList(){
            return llista;
         }

         //Retorna els String de la llista de paraules
         public ArrayList<String> get_Paraules(){
            ArrayList<String> Results = new ArrayList<>();
            for (int i = 0; i < llista.size(); ++i)Results.add(llista.get(i).get_nom());
            return Results;
         }

         //Retorna les frequencies de la llista de paraules
         public ArrayList<Integer> get_Freq(){
            ArrayList<Integer> Results = new ArrayList<>();
            for (int i = 0; i < llista.size(); ++i)Results.add(llista.get(i).get_freq());
            return Results;
         }

         //Retorna l'identificador
         public String get_id(){
            return id;
         }

         //Retorna True a la pregunta isLlistaParaules()
         public boolean isLlistaParaules() {
            return true;
          }
}
//Classe implementada per Ariadna Garcia Lorente