package main.Domain;

public class Paraula {
    //Creem una classe especial per una paraula
            private String nom; //Nom de la Paraula
            private int freq; //Frequencia de la Paraula

            //Creadora de Paraula
            public Paraula(String A, int B){
                this.nom = A;
                this.freq = B;
            }

            //Consultora de nom
            public String get_nom(){
                return nom;
            }

            //Consultora de frequencia
            public int get_freq(){
                return freq;
            }

            
}
//Classe implementada per Ariadna Garcia Lorente
