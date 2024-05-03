package main.Domain;


import java.util.ArrayList;

import main.Persistence.Persistence_Controller;

public class CtrlDomini {
    private final Persistence_Controller _ctrlPersistencia;
    private Usuari _usuari;

    //Creadora per defecte
    public CtrlDomini() {
        _ctrlPersistencia = new Persistence_Controller();
        _usuari = new Usuari(null, null);
    }

    //Iniciem sessio i carreguem tots els documents i teclats assosiats a un usuari 
    public boolean iniciar_sessio(String usr, String pssw){
        if(_ctrlPersistencia.iniciar_sessio(usr, pssw)){
            _usuari = new Usuari(usr, pssw);
            //Afegim tots els documents a usuari
                //Afegir alfabets
            ArrayList<ArrayList<Character>> aux_alfabet = _ctrlPersistencia.read_conjAlfabet_simbols(usr); 
            ArrayList<int[][]> aux2_alfabet = _ctrlPersistencia.read_conjAlfabet_mat_frec(usr);
            ArrayList<String> aux3_alfabet = _ctrlPersistencia.read_conjAlfabet_id(usr);
            for (int i = 0; i < aux_alfabet.size(); ++i) {
                Alfabet new_Alfabet = new Alfabet(aux_alfabet.get(i), aux2_alfabet.get(i), aux3_alfabet.get(i)); 
                _usuari.newAlfabet(new_Alfabet);
            }
                //Afegir Llistes de Paraules
            ArrayList<ArrayList<String>> aux_Paraules = _ctrlPersistencia.read_conjLlistaParaules_nomsParaules(usr); 
            ArrayList<ArrayList<Integer>> aux2_Paraules = _ctrlPersistencia.read_conjLlistaParaules_frecsParaules(usr);
            ArrayList<String> aux3_Paraules = _ctrlPersistencia.read_conjLlistaParaules_id(usr);
            for (int i = 0; i < aux_Paraules.size(); ++i) {
                ArrayList<Paraula> paraules = new ArrayList<>();
                for (int j = 0; j < aux_Paraules.get(i).size(); ++j){
                    Paraula newParaula = new Paraula(aux_Paraules.get(i).get(j), aux2_Paraules.get(i).get(j));
                    paraules.add(newParaula);
                }
                Llista_Paraules new_Llista_Paraules = new Llista_Paraules(paraules, aux3_Paraules.get(i)); 
                _usuari.newLlista_Paraules(new_Llista_Paraules);
            }
                //Afegir Texts
            ArrayList<String> aux_Texts = _ctrlPersistencia.read_conjText(usr); 
            ArrayList<String>  aux2_Texts = _ctrlPersistencia.read_conjText_id(usr);
            for (int i = 0; i < aux_Texts.size(); ++i) {
                Text new_Text = new Text(aux_Texts.get(i), aux2_Texts.get(i)); 
                _usuari.newText(new_Text);
            }
            //Afegir Teclats a usuari
            ArrayList<String> aux_Teclats = _ctrlPersistencia.read_conjTeclat_id(usr);
            ArrayList<String> aux2_Teclats = _ctrlPersistencia.read_conjTeclat_idDoc(usr);
            ArrayList<char[][]> aux3_Teclats = _ctrlPersistencia.read_conjTeclat_disposicio(usr);
            for (int i = 0; i < aux_Teclats.size(); ++i) {
                //Cas 1, idDoc pertany a un Alfabet
                if(_usuari.getAlfabet(aux2_Teclats.get(i))!= null){
                    Alfabet doc_teclat = _usuari.getAlfabet(aux2_Teclats.get(i));
                    Teclats New_Teclat = new Teclats(aux_Teclats.get(i), doc_teclat);
                    New_Teclat.SetDisposicio(aux3_Teclats.get(i));
                    _usuari.newKeyboard(New_Teclat);
                }
                //Cas 2, idDoc pertany a una llista de paraules
                if(_usuari.getLlista_Paraules(aux2_Teclats.get(i))!= null){
                    Llista_Paraules doc_teclat = _usuari.getLlista_Paraules(aux2_Teclats.get(i));
                    Teclats New_Teclat = new Teclats(aux_Teclats.get(i), doc_teclat);
                    New_Teclat.SetDisposicio(aux3_Teclats.get(i));
                    _usuari.newKeyboard(New_Teclat);
                }
                //Cas 3, idDoc pertany a un Text
                if(_usuari.getText(aux2_Teclats.get(i))!= null){
                    Text doc_teclat = _usuari.getText(aux2_Teclats.get(i));
                    Teclats New_Teclat = new Teclats(aux_Teclats.get(i), doc_teclat);
                    New_Teclat.SetDisposicio(aux3_Teclats.get(i));
                    _usuari.newKeyboard(New_Teclat);
                }
            }
            return true;
        }
        else return false;
    }

    //Registrem un usuari
    public boolean registrar (String usr, String pssw){
        if ( _ctrlPersistencia.registrar(usr, pssw)){
            _usuari = new Usuari(usr, pssw);
            return true;
        }
        else return false;
    }

    //Tanquem sessio i guardem tots els documents i teclats assosiats amb un usuari
    public boolean sign_out (String usr, String pssw){
        ArrayList<Alfabet> Alfabets_usuari = _usuari.getAllAlfabets();
        ArrayList<Llista_Paraules> Llista_Paraules_usuari = _usuari.getAllLlista_Paraules();
        ArrayList<Text> Text_usuari = _usuari.getAllTexts();
        ArrayList<Teclats> Teclats_usuari = _usuari.getAllKeybords();
        
        //Teclats
        ArrayList<String> idTeclats = new ArrayList<>();
        ArrayList<char[][]> disposicioTeclats = new ArrayList<>();
        ArrayList<String> idDocTeclats = new ArrayList<>();
        
        //Alfabets
        ArrayList<String> idAlfabets = new ArrayList<>();
        ArrayList<ArrayList<Character>> sAlfabets = new ArrayList<>();
        ArrayList<int[][]> frAlfabets = new ArrayList<>();
        
        //Llistes_de_Paraules
        ArrayList<String> idLlistaParaules = new ArrayList<>();
        ArrayList<ArrayList<String>> nom_ParaulesLlistaParaules = new ArrayList<>();
        ArrayList<ArrayList<Integer>> frec_ParaulesLlistaParaules = new ArrayList<>();
        
        //Textos
        ArrayList<String> idTexts = new ArrayList<>();
        ArrayList<String> textTexts = new ArrayList<>();
        
            for (int i = 0; i < Alfabets_usuari.size(); ++i){
                idAlfabets.add(Alfabets_usuari.get(i).get_id());
                sAlfabets.add(Alfabets_usuari.get(i).getAlfabet());
                frAlfabets.add(Alfabets_usuari.get(i).calcMatMarkov());
            }
            
            for (int i = 0; i < Llista_Paraules_usuari.size(); ++i){
                idLlistaParaules.add(Llista_Paraules_usuari.get(i).get_id());
                nom_ParaulesLlistaParaules.add(Llista_Paraules_usuari.get(i).get_Paraules());
                frec_ParaulesLlistaParaules.add(Llista_Paraules_usuari.get(i).get_Freq());
            }
            
            for (int i = 0; i < Text_usuari.size(); ++i){
                idTexts.add(Text_usuari.get(i).getId());
                textTexts.add(((Text)Text_usuari.get(i)).getText());
            }
            
            for (int j = 0; j < Teclats_usuari.size(); ++j){
                idTeclats.add(Teclats_usuari.get(j).getId());
                disposicioTeclats.add(Teclats_usuari.get(j).getDisposicio());
                idDocTeclats.add(Teclats_usuari.get(j).getIdDoc());
            }
        
        //Comprovem que tot s'hagi executat correctament
        if ( _ctrlPersistencia.sign_out(usr, pssw, idTeclats, disposicioTeclats, idDocTeclats, idAlfabets, sAlfabets, frAlfabets, idTexts, textTexts, idLlistaParaules, nom_ParaulesLlistaParaules, frec_ParaulesLlistaParaules)){
            return true;
        }
        
        else return false;
    }

    //Obtenim els id de tots els documents assosiats amb un usuari
    public ArrayList<String> get_all_ids_Documents(){
        ArrayList<String> Results = new ArrayList<>();
        ArrayList<Alfabet> Alfabets_usuari = _usuari.getAllAlfabets();
        ArrayList<Llista_Paraules> Llista_Paraules_usuari = _usuari.getAllLlista_Paraules();
        ArrayList<Text> Text_usuari = _usuari.getAllTexts();
        for (int i = 0; i < Alfabets_usuari.size(); ++i) Results.add(Alfabets_usuari.get(i).get_id());
        for (int i = 0; i < Llista_Paraules_usuari.size(); ++i) Results.add(Llista_Paraules_usuari.get(i).get_id());
        for (int i = 0; i < Text_usuari.size(); ++i) Results.add(Text_usuari.get(i).getId());
        return Results;
    }

    //Obtenim els id de tots els teclats assosiats amb un usuari
    public ArrayList<String> get_all_ids_Teclats(){
        ArrayList<String> Results = new ArrayList<>();
        ArrayList<Teclats> aux2 = _usuari.getAllKeybords();
        for (int i = 0; i < aux2.size(); ++i) Results.add(aux2.get(i).getId());
        return Results;
    }

    //Creem un nou Alfabet
    public void newAlfabet(ArrayList<Character> S, int[][] fr, String name){
        Alfabet nou = new Alfabet(S, fr, name);
        _usuari.newAlfabet(nou);
    }

    //Creem una nova llista de paraules
    public void newLlistaParaules(ArrayList<String> aux, ArrayList<Integer> freq, String name){
        ArrayList<Paraula> Result = new ArrayList<>();
        for (int i = 0; i < aux.size(); ++i){
            Paraula news = new Paraula(aux.get(i), freq.get(i));
            Result.add(news);
        }
        Llista_Paraules nou = new Llista_Paraules(Result, name);
        _usuari.newLlista_Paraules(nou);
    }

    //Creem un nou text
    public void newText(String Text, String id){
        Text nou = new Text(Text, id);
        _usuari.newText(nou);
    }

    //Retornem totes les paraules de llista de paraules name
     public ArrayList<String> getLlista_Paraules(String name){
        Llista_Paraules nou = _usuari.getLlista_Paraules(name);
        return nou.get_Paraules();
    }

    //Retornem totes les frequencies de llista de paraules name
    public ArrayList<Integer> get_Freq_Llista_Paraules(String name){
        Llista_Paraules nou = _usuari.getLlista_Paraules(name);
        return nou.get_Freq();
    }

    //Retornem el text name
    public String getText(String name){
        Text nou = _usuari.getText(name);
        return nou.getText();
    }

    //Retornem la disposició de taclat name
    public char[][] getTeclats(String name){
        return _usuari.getKeyboard(name).getDisposicio();
    }

    //Eliminem el document name
    public boolean deleteDocument(String name){
        if(_usuari.getAlfabet(name)!= null){
                    _usuari.deleteAlfabet(name);
                    return true;
                }
                if(_usuari.getLlista_Paraules(name)!= null){
                    _usuari.deleteLlista_Paraules(name);
                    return true;
                }
                if(_usuari.getText(name)!= null){
                    _usuari.deleteText(name);
                    return true;
        }
       return false;
    }

    //Eliminem el teclat name
    public boolean deleteTeclat(String name){
       if(_usuari.deleteKeyboard(name)) return true;
       else return false;
    }

    public void modTeclat(String name, char[][] disp){
       _usuari.modKeyboard(name, disp);
    }

    //Obtenim la matriu de markov del document id
    public int[][] getMatMarkov(String id){
        if(_usuari.getAlfabet(id)!= null){
                    return _usuari.getAlfabet(id).calcMatMarkov();
                }
                if(_usuari.getLlista_Paraules(id)!= null){
                    return _usuari.getLlista_Paraules(id).calcMatMarkov();
                }
                if(_usuari.getText(id)!= null){
                    return _usuari.getText(id).calcMatMarkov();
        }
        return null;
    }

    //Obtenim l'alfabet del document id
    public ArrayList<Character> getAlfabet(String id){
        if(_usuari.getAlfabet(id)!= null){
                    return _usuari.getAlfabet(id).get_alfabet();
                }
                if(_usuari.getLlista_Paraules(id)!= null){
                    return _usuari.getLlista_Paraules(id).get_alfabet();
                }
                if(_usuari.getText(id)!= null){
                    return _usuari.getText(id).getAlfabet();
        }
        return null;
    }

    //Comprovem si el document id es un alfabet
    public boolean is_Alfabet(String id){
        if(_usuari.getAlfabet(id)!= null)return true;
        else return false;
    }

    //Comprovem si el document id es una llista de paraules
    public boolean is_Llista_paraules(String id){
        if(_usuari.getLlista_Paraules(id)!= null)return true;
        else return false;
    }

    //Comprovem si el document id es un text
    public boolean is_Text(String id){
        if(_usuari.getText(id)!= null)return true;
        else return false;
    }
    //Comprovem si el teclat id existeix
    public boolean Teclat_exists(String id){
        if(_usuari.getKeyboard(id)!= null)return true;
        else return false;
    }

    //Obtenim el cost del teclat id
    public double get_cost(String id){
        return _usuari.getKeyboard(id).GetCost();
    }

    //Esta funcion genera nuevos teclados a partir de el nombre del teclado nuevo, 
    //el algoritmo que se quiere utilizar y el nombre del documento
    //Devolvemos la disposición de teclado
    public char[][] newTeclat(String id, int tipus_algorisme, String Documento){
            //Comprovamos si Documento es un alfabeto, si es asi, creamos un teclado con alfabeto 
            if(_usuari.getAlfabet(Documento)!= null){
                Alfabet nou = _usuari.getAlfabet(Documento);
                Teclats Results = new Teclats(id, tipus_algorisme, nou.get_alfabet(), nou.calcMatMarkov(), nou.get_id());
                _usuari.newKeyboard(Results);
                return Results.getDisposicio();
            }
            //Comprovamos si Documento es una lista de palabras, si es asi, creamos un teclado con una lista de palabras
            else if(_usuari.getLlista_Paraules(Documento)!= null){
                Llista_Paraules nou = _usuari.getLlista_Paraules(Documento);
                Teclats Results = new Teclats(id, tipus_algorisme, nou.getList(), nou.get_id());
                _usuari.newKeyboard(Results);
                return Results.getDisposicio();
            }
            //Si documento es un texto, creamos el teclado con el texto
            else {
                Text nou = _usuari.getText(Documento);
                Teclats Results = new Teclats(id, tipus_algorisme, nou.getAlfabet(), nou.calcMatMarkov(), nou.getId());
                _usuari.newKeyboard(Results);
                return Results.getDisposicio();
            }
    }
}
