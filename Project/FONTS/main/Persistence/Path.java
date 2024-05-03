package main.Persistence;

public class Path {
    public Path() {}

    //Retorna el path des de EXE fins al usuari nomUsuari
    public String path_usuari(String nomUsuari) {
        String path = "../FONTS/dades_programa/" + nomUsuari;
        return path;
    }

    //Retorna el path des de EXE fins al password del usuari nomUsuari
    public String path_password(String nomUsuari) {
        String path = path_usuari(nomUsuari) + "/Password";
        return path;
    }

    //Retorna el path des de EXE fins a la carpeta de tecleats de l'usuari nomUsuari
    public String path_Teclats(String nomUsuari) {
        String path = path_usuari(nomUsuari) + "/Teclats";
        return path;
    }

    //Retorna el path des de EXE fins al teclat newTeclat guardat dins de l'usuari nomUsuari
    public String path_new_Teclat(String nomUsuari, String newTeclat) {
        String path = path_Teclats(nomUsuari) + "/" + newTeclat + ".txt";
        return path;
    }

    //Retorna el path des de EXE fins a l'usuari nomUsuari
    public String path_Documents(String nomUsuari) {
        String path = path_usuari(nomUsuari) + "/Documents";
        return path;
    }

    //Retorna el path des de EXE fins a la carpeta d'alfabets de l'usuari nomUsuari
    public String path_Alfabet(String nomUsuari) {
        String path = path_Documents(nomUsuari) + "/Alfabets";
        return path;
    }

    //Retorna el path des de EXE fins al alfabet newAlfabet guardat dins de l'usuari nomUsuari
    public String path_new_Alfabet(String nomUsuari, String newAlfabet) {
        String path = path_Alfabet(nomUsuari) + "/" + newAlfabet + ".txt";
        return path;
    }

    //Retorna el path des de EXE fins a la carpeta de llista de paraules de l'usuari nomUsuari
    public String path_Llista_Paraules(String nomUsuari) {
        String path = path_Documents(nomUsuari) + "/Llista_Paraules";
        return path;
    }

    //Retorna el path des de EXE fins a la llista de paraules newLlista_Paraules guardat dins de l'usuari nomUsuari
    public String path_new_Llista_Paraules(String nomUsuari, String newLlista_Paraules) {
        String path = path_Llista_Paraules(nomUsuari) + "/" + newLlista_Paraules + ".txt";
        return path;
    }

    //Retorna el path des de EXE fins a la carpeta de textos de l'usuari nomUsuari
    public String path_Text(String nomUsuari) {
        String path = path_Documents(nomUsuari) + "/Texts";
        return path;
    }

    //Retorna el path des de EXE fins al text newText guardat dins de l'usuari nomUsuari
    public String path_new_Text(String nomUsuari, String newText) {
        String path = path_Text(nomUsuari) + "/" + newText + ".txt";
        return path;
    }
}