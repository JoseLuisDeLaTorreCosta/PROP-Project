package main.Persistence;

import org.json.*;
import java.util.*;

public class Persistence_Controller {
    Path p;
    Files f;

    //Creadora del contolador de persistencia
    public Persistence_Controller() {
        this.p = new Path();
        this.f = new Files();
    }

    //Funcio que s'encarrega de crear l'usuari, retornara false si ja existeix l'usuari amb nom Usuari
    //En cas de que no n'hi hagi cap usuari amb nom Usuari, generara les carpetes de Teclats, Documents,
    //Alfabets, Llistes_Paraules i Texts. A mes, genera un arxiu .txt que guarda en format JSON la contrasenya Password
    public boolean registrar(String Usuari, String Password) {
        String path = p.path_usuari(Usuari);
        if (f.existFile(path)) return false;
        f.c_folder(path);
        path = p.path_password(Usuari);
        JSONArray jc = new JSONArray();
        jc.put(Password);
        String content = jc.toString();
        f.c_file(path, content);
        path = p.path_Teclats(Usuari);
        f.c_folder(path);
        path = p.path_Documents(Usuari);
        f.c_folder(path);
        path = p.path_Alfabet(Usuari);
        f.c_folder(path);
        path = p.path_Llista_Paraules(Usuari);
        f.c_folder(path);
        path = p.path_Text(Usuari);
        f.c_folder(path);
        return true;
    }

    //Funcio que s'encarrega de carregar les dades de l'usuari si existeix i si coincideix la
    //seva contrasenya amb Password
    public boolean iniciar_sessio(String Usuari, String Password) {
        String path = p.path_password(Usuari);
        if (!f.existFile(path)) return false;
        String content = f.r_file(path);
        JSONArray jc = new JSONArray(content);
        String p = jc.get(0).toString();
        return p.equals(Password);
    }

    //Funcio que s'encarrega de guardar totes les dades despres de tancar la aplicacio.
    //Basicament esborra l'usuari i crea un nou amb el mateix nom, contrasenya, pero amb les dades actualitzades
    public boolean sign_out(String Usuari, String Password, ArrayList<String> idTeclats, ArrayList<char[][]> disposicioTeclats, ArrayList<String> idDocTeclats, ArrayList<String> idAlfabets, ArrayList<ArrayList<Character>> sAlfabets, ArrayList<int[][]> frAlfabets, ArrayList<String> idTexts, ArrayList<String> textTexts, 
    ArrayList<String> idLlistaParaules, ArrayList<ArrayList<String>> nom_ParaulesLlistaParaules, ArrayList<ArrayList<Integer>> frec_ParaulesLlistaParaules) {
        if (!iniciar_sessio(Usuari, Password)) return false;
        String path = p.path_usuari(Usuari);
        f.e_archive(path);
        registrar(Usuari, Password);

        for (int i = 0; i < idTeclats.size(); ++i) {
            save_Teclat(Usuari, idTeclats.get(i), disposicioTeclats.get(i), idDocTeclats.get(i));
        }

        for (int i = 0; i < idAlfabets.size(); ++i) {
            save_Alfabet(Usuari, idAlfabets.get(i), sAlfabets.get(i), frAlfabets.get(i));
        }
        
        for (int i = 0; i < idTexts.size(); ++i) {
            save_Text(Usuari, idTexts.get(i), textTexts.get(i));
        }

        for (int i = 0; i < idLlistaParaules.size(); ++i) {
            save_Llista_Paraules(Usuari, idLlistaParaules.get(i), nom_ParaulesLlistaParaules.get(i), frec_ParaulesLlistaParaules.get(i));
        }

        return true;
    }

    //Funcio que s'encarrega de guardar un teclat en un document .txt en format JSON
    public void save_Teclat(String Usuari, String id, char[][] disposicio, String idDoc) {
        String path = p.path_new_Teclat(Usuari, id);
        JSONArray jc = new JSONArray();
        jc.put(disposicio);
        jc.put(idDoc);
        String content = jc.toString();
        f.c_file(path, content);
    }
    
    //Funcio que s'encarrega de guardar un alfabet en un document .txt en format JSON
    public void save_Alfabet(String Usuari, String id, ArrayList<Character> s, int[][] fr) {
        String path = p.path_new_Alfabet(Usuari, id);
        JSONArray jc = new JSONArray();
        jc.put(s);
        jc.put(fr);
        String content = jc.toString();
        f.c_file(path, content);
    }

    //Funcio que s'encarrega de guardar un text en un document .txt en format JSON
    public void save_Text(String Usuari, String id, String text) {
        String path = p.path_new_Text(Usuari, id);
        JSONArray jc = new JSONArray();
        jc.put(text);
        String content = jc.toString();
        f.c_file(path, content);
    }

    //Funcio que s'encarrega de guardar una llista de paraules en un document .txt en format JSON
    public void save_Llista_Paraules(String Usuari, String id, ArrayList<String> nom_Paraules, ArrayList<Integer> frec_Paraules) {
        String path = p.path_new_Llista_Paraules(Usuari, id);
        JSONArray jc = new JSONArray();
        jc.put(nom_Paraules);
        jc.put(frec_Paraules);
        String content = jc.toString();
        f.c_file(path, content);
    }

    //Funcio que s'encarrega de llegir l'identificador d'un document en la direccio path
    public String read_id(String path) {
        int last_b = 0;
        for (int i = 0; i < path.length(); ++i) if (path.charAt(i) == '/') last_b = i;
        ++last_b;
        return path.substring(last_b);
    }

    //Funcio que s'encarrega de llegir la disposicio del teclat en la direccio path de l'usuari Usuari
    public char[][] read_Teclat_disposicio(String Usuari, String path) {
        String content = f.r_file(path);
        JSONArray jc = new JSONArray(content);
        JSONArray d = jc.getJSONArray(0);
        char[][] disposicio = new char[d.length()][d.getJSONArray(0).length()];
        for (int i = 0; i < d.length(); ++i) {
            JSONArray line = d.getJSONArray(i);
            for (int j = 0; j < line.length(); ++j) disposicio[i][j] = line.getString(j).charAt(0);
        }
        return disposicio;
    }

    //Funcio que s'encarrega de llegir el identificador del document amb el que creem el teclat en la direccio path de l'usuari Usuari
    public String read_Teclat_idDoc(String Usuari, String path) {
        String content = f.r_file(path);
        JSONArray jc = new JSONArray(content);
        String idDoc = jc.getString(1);
        return idDoc;
    }

    //Funcio que s'encarrega de llegir els simbols d'un alfabet en la direccio path de l'usuari Usuari
    public ArrayList<Character> read_Alfabet_simbols(String Usuari, String path) {
        String content = f.r_file(path);
        JSONArray jc = new JSONArray(content);
        JSONArray simbols = jc.getJSONArray(0);
        ArrayList<Character> s = new ArrayList<>();
        for (int i = 0; i < simbols.length(); ++i) s.add(simbols.getString(i).charAt(0));
        return s;
    }

    //Funcio que s'encarrega de llegir la matriu de frequencies d'un alfabet en la direccio path de l'usuari Usuari
    public int[][] read_Alfabet_mat_frec(String Usuari, String path) {
        String content = f.r_file(path);
        JSONArray jc = new JSONArray(content);
        JSONArray f = jc.getJSONArray(1);
        int[][] fr = new int[f.length()][f.getJSONArray(0).length()];
        for (int i = 0; i < f.length(); ++i) {
            JSONArray line = f.getJSONArray(i);
            for (int j = 0; j < line.length(); ++j) fr[i][j] = line.getInt(j);
        }
        return fr;
    }

    //Funcio que s'encarrega de llegir el text d'un text en la direccio path de l'usuari Usuari
    public String read_Text(String Usuari, String path) {
        String content = f.r_file(path);
        JSONArray jc = new JSONArray(content);
        String text = jc.get(0).toString();
        return text;
    }

    //Funcio que s'encarrega de llegir els noms de les paraules de la llista de paraules en la direccio path de l'usuari Usuari
    public ArrayList<String> read_Llista_Paraules_nomsParaules(String Usuari, String path) {
        String content = f.r_file(path);
        JSONArray jc = new JSONArray(content);
        JSONArray paraules = jc.getJSONArray(0);
        ArrayList<String> nom_Paraules = new ArrayList<>();
        for (int i = 0; i < paraules.length(); ++i) nom_Paraules.add(paraules.getString(i));
        return nom_Paraules;
    }

    //Funcio que s'encarrega de llegir les frequencies de les paraules de la llista de paraules en la direccio path de l'usuari Usuari
    public ArrayList<Integer> read_Llista_Paraules_frecsParaules(String Usuari, String path) {
        String content = f.r_file(path);
        JSONArray jc = new JSONArray(content);
        JSONArray frequencies = jc.getJSONArray(1);
        ArrayList<Integer> frec_Paraules = new ArrayList<>();
        for (int i = 0; i < frequencies.length(); ++i) frec_Paraules.add(frequencies.getInt(i));
        return frec_Paraules;
    }

    //Funcio que s'encarrega de llegir el conjunt de identificadors dels arxius que estan dins de la carpeta de l'usuari Usuari
    public ArrayList<String> read_conj_id(String path) {
        String[] names = f.get_File_Names_in_Folder(path);
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < names.length; ++i) {
            names[i] = names[i].substring(0, names[i].length() - 4);
            ids.add(names[i]);
        }
        return ids;
    }

    //Funcio que s'encarrega de llegir els identificadors de tots els teclats de l'usuari Usuari
    public ArrayList<String> read_conjTeclat_id(String Usuari) {
        String path = p.path_Teclats(Usuari);
        return read_conj_id(path);
    }

    //Funcio que s'encarrega de llegir les disposicions de tots els teclats de l'usuari Usuari
    public ArrayList<char[][]> read_conjTeclat_disposicio(String Usuari) {
        String path = p.path_Teclats(Usuari);
        String[] names = f.get_File_Names_in_Folder(path);
        for (int i = 0; i < names.length; ++i) {
            names[i] = path + "/" + names[i];
        }
        ArrayList<char[][]> conj = new ArrayList<>();
        for (int i = 0; i < names.length; ++i) conj.add(read_Teclat_disposicio(Usuari, names[i]));
        return conj;
    }

    //Funcio que s'encarrega de llegir els identificadors amb el que s'han creat tots els teclats de l'usuari Usuari
    public ArrayList<String> read_conjTeclat_idDoc(String Usuari) {
        String path = p.path_Teclats(Usuari);
        String[] names = f.get_File_Names_in_Folder(path);
        for (int i = 0; i < names.length; ++i) {
            names[i] = path + "/" + names[i];
        }
        ArrayList<String> conj = new ArrayList<>();
        for (int i = 0; i < names.length; ++i) conj.add(read_Teclat_idDoc(Usuari,names[i]));
        return conj;
    }

    //Funcio que s'encarrega de llegir els identificadors de tots els teclats de l'usuari Usuari
    public ArrayList<String> read_conjAlfabet_id(String Usuari) {
        String path = p.path_Alfabet(Usuari);
        return read_conj_id(path);
    }

    //Funcio que s'encarrega de llegir els simbols de tots els alfabets de l'usuari Usuari
    public ArrayList<ArrayList<Character>> read_conjAlfabet_simbols(String Usuari) {
        String path = p.path_Alfabet(Usuari);
        String[] names = f.get_File_Names_in_Folder(path);
        for (int i = 0; i < names.length; ++i) {
            names[i] = path + "/" + names[i];
        }
        ArrayList<ArrayList<Character>> conj = new ArrayList<>();
        for (int i = 0; i < names.length; ++i) conj.add(read_Alfabet_simbols(Usuari,names[i]));
        return conj;
    }

    //Funcio que s'encarrega de llegir les matrius de frequencies de tots els alfabets de l'usuari Usuari
    public ArrayList<int[][]> read_conjAlfabet_mat_frec(String Usuari) {
        String path = p.path_Alfabet(Usuari);
        String[] names = f.get_File_Names_in_Folder(path);
        for (int i = 0; i < names.length; ++i) {
            names[i] = path + "/" + names[i];
        }
        ArrayList<int[][]> conj = new ArrayList<>();
        for (int i = 0; i < names.length; ++i) conj.add(read_Alfabet_mat_frec(Usuari, names[i]));
        return conj;
    }

    //Funcio que s'encarrega de llegir els identificadors de tots els textos de l'usuari Usuari
    public ArrayList<String> read_conjText_id(String Usuari) {
        String path = p.path_Text(Usuari);
        return read_conj_id(path);
    }

    //Funcio que s'encarrega de llegir el contingut de tots els textos de l'usuari Usuari
    public ArrayList<String> read_conjText(String Usuari) {
        String path = p.path_Text(Usuari);
        String[] names = f.get_File_Names_in_Folder(path);
        for (int i = 0; i < names.length; ++i) {
            names[i] = path + "/" + names[i];
        }
        ArrayList<String> conj = new ArrayList<>();
        for (int i = 0; i < names.length; ++i) conj.add(read_Text(Usuari, names[i]));
        return conj;
    }

    //Funcio que s'encarrega de llegir els identificadors de totes les llistes de paraules de l'usuari Usuari
    public ArrayList<String> read_conjLlistaParaules_id(String Usuari) {
        String path = p.path_Llista_Paraules(Usuari);
        return read_conj_id(path);
    }

    //Funcio que s'encarrega de llegir els noms de les totes paraules de totes les llistes de paraules de l'usuari Usuari
    public ArrayList<ArrayList<String>> read_conjLlistaParaules_nomsParaules(String Usuari) {
        String path = p.path_Llista_Paraules(Usuari);
        String[] names = f.get_File_Names_in_Folder(path);
        for (int i = 0; i < names.length; ++i) {
            names[i] = path + "/" + names[i];
        }
        ArrayList<ArrayList<String>> conj = new ArrayList<>();
        for (int i = 0; i < names.length; ++i) conj.add(read_Llista_Paraules_nomsParaules(Usuari, names[i]));
        return conj;
    }
    
    //Funcio que s'encarrega de llegir les frequencies de totes les paraules de totes les llistes de paraules de l'usuari Usuari
    public ArrayList<ArrayList<Integer>> read_conjLlistaParaules_frecsParaules(String Usuari) {
        String path = p.path_Llista_Paraules(Usuari);
        String[] names = f.get_File_Names_in_Folder(path);
        for (int i = 0; i < names.length; ++i) {
            names[i] = path + "/" + names[i];
        }
        ArrayList<ArrayList<Integer>> conj = new ArrayList<>();
        for (int i = 0; i < names.length; ++i) conj.add(read_Llista_Paraules_frecsParaules(Usuari, names[i]));
        return conj;
    }
}
