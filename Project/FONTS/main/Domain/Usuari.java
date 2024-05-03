package main.Domain;

import java.util.*;

public class Usuari {
    private String username; //nom d'usuari
    private String password; //contrasenya d'usuari
    private ArrayList<Alfabet> docs_Alfabets; //llista d'alfabets
    private ArrayList<Llista_Paraules> docs_Llista_Paraules; //llista de llistes de paraules
    private ArrayList<Text> docs_Texts; //llista de textos
    private ArrayList<Teclats> keyboards; //llista de teclats

    //Creadora per defecte a partir de usuari i la contrasenya
    public Usuari(String usr, String pwd) {
        username = usr;
        password = pwd;
        docs_Alfabets = new ArrayList<>();
        docs_Llista_Paraules = new ArrayList<>();
        docs_Texts = new ArrayList<>();
        keyboards = new  ArrayList<Teclats>();
    }

    //Retorna username
    public String getUsername() {
        return username;
    }

    //Retorna password
    public String getPassword() {
        return password;
    }

    //Configura Username
    public void setUsername(String newUsr) {
        username = newUsr;
    }

    //Configura Password
    public void setPassword(String newPwd) {
        password = newPwd;
    }

    //Afageix un alfabet a docs_Alfabets
    public void newAlfabet(Alfabet doc) {
        docs_Alfabets.add(doc);
    }

    //Afageix llista de parules a docs_Llista_Paraules
    public void newLlista_Paraules(Llista_Paraules doc) {
        docs_Llista_Paraules.add(doc);
    }

    //Afageix un Text a docs_Text
    public void newText(Text doc) {
        docs_Texts.add(doc);
    }

    //Retorna l'alfabet nameDoc
    public Alfabet getAlfabet(String nameDoc) {
        for (int i = 0; i < docs_Alfabets.size(); ++i){
            if (docs_Alfabets.get(i).get_id().equals(nameDoc)) return docs_Alfabets.get(i);
        }
        return null;
    }

    //Retorna la llista de paraules nameDoc
    public Llista_Paraules getLlista_Paraules(String nameDoc) {
        for (int i = 0; i < docs_Llista_Paraules.size(); ++i){
            if (docs_Llista_Paraules.get(i).get_id().equals(nameDoc)) return docs_Llista_Paraules.get(i);
        }
        return null;
    }

    //Retorna el text namedoc 
    public Text getText(String nameDoc) {
        for (int i = 0; i < docs_Texts.size(); ++i){
            if (docs_Texts.get(i).getId().equals(nameDoc)) return docs_Texts.get(i);
        }
        return null;
    }

    //Retorna tots els alfabets
    public ArrayList<Alfabet> getAllAlfabets(){
        return docs_Alfabets;
    }
    // Retorna totes les llistes de paraules
    public ArrayList<Llista_Paraules> getAllLlista_Paraules(){
        return docs_Llista_Paraules;
    }

    //Retorna tots els textos
    public ArrayList<Text> getAllTexts(){
        return docs_Texts;
    }

    //Afageix un nou teclat a keyboards
    public void newKeyboard(Teclats kbd) {
        keyboards.add(kbd);
    }

    //Retorna el teclat nameKbd
    public Teclats getKeyboard(String nameKbd) {
        for (int i = 0; i < keyboards.size(); ++i){
            if (keyboards.get(i).getId()==nameKbd) return keyboards.get(i);
        }
        return null;
    }

    //Retorna tots els teclats
    public ArrayList<Teclats> getAllKeybords(){
        return keyboards;
    }

    //Elimina l'alfabet nameDoc
    public boolean deleteAlfabet(String nameDoc) {
        for (int i = 0; i < docs_Alfabets.size(); ++i){
            if (docs_Alfabets.get(i).getId()==nameDoc){
                 docs_Alfabets.remove(i);
                 return true;
            }
        }
        return false;
    }

    //Elimina la llista de paraules nameDoc
    public boolean deleteLlista_Paraules(String nameDoc) {
        for (int i = 0; i < docs_Llista_Paraules.size(); ++i){
            if (docs_Llista_Paraules.get(i).getId()==nameDoc) {
                 docs_Llista_Paraules.remove(i);
                 return true;
            }
        }
        return false;
    }
     

    
    //Elimina el text namedoc
    public boolean deleteText(String nameDoc) {
        for (int i = 0; i < docs_Texts.size(); ++i){
            if (docs_Texts.get(i).getId()==nameDoc) {
                 docs_Texts.remove(i);
                 return true;
            }
        }
        return false;
    }
    
    //Elimina el teclat nameKbd
    public boolean deleteKeyboard(String nameKbd){
        for (int i = 0; i < keyboards.size(); ++i){
            if (keyboards.get(i).getId()==nameKbd) {
                keyboards.remove(i);
                return true;
            }
        }
        return false;
    }

    //Modifiquem la disposicio de teclat
    public void modKeyboard(String nameKbd, char[][] disp){
        for (int i = 0; i < keyboards.size(); ++i){
            if (keyboards.get(i).getId()==nameKbd) {
                keyboards.get(i).SetDisposicio(disp);
            }
        }
    }
}
