package main.Persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;


public class Files {
    File f;

    //Creadora per defecte de la classe Files
    public Files() {
        this.f = new File("");
    }

    
    //Post: Es crea un arxiu en el path especificat (arxiu + nom), que contindrà content
    public void c_file(String path, String content) {
        try {
            this.f = new File(path);
            this.f.createNewFile();

            PrintWriter pw = new PrintWriter(f);
            pw.print(content);
            pw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Post: Crea una carpeta en el path (arxiu + nom) Path
    public void c_folder(String path) {
        this.f = new File(path);
        f.mkdir();
    }


    //Retorna en string el contingut JSON del arxiu anteriorment acces
    public String r_file() {
        try {
            BufferedReader r = new BufferedReader(new FileReader(f.getPath()));
            String content = r.readLine();
            r.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Retorna en string el contingut JSON del arxiu, que està en path
    public String r_file(String path) {
        this.f = new File(path);
        return r_file();
    }

    //Elimina l'arxiu que s'ha acces anteriorment
    public void e_archive() {
        this.f.delete();
    }


    //Elimina l'arxiu que s'ha acces anteriorment
    public void e_archive(String path) {
        this.f = new File(path);
        e_archive();
    }

    //Retorna el path de l'arxiu
    public String getPath() {
        return f.getPath();
    }

    //Diu si existeix l'arxiu amb el path passat per paramentre
    public boolean existFile(String path) {
        File x = new File(path);
        return x.exists();
    }

    //Retorna el nom de tots els fitxers que estan en la carpeta amb la direccio passada per parametre
    public String[] get_File_Names_in_Folder(String path) {
        this.f = new File(path);
        return f.list();
    }


    public static void main(String[] a) {
        Files f = new Files();
        Scanner input = new Scanner(System.in);

        System.out.println("Introdueix el nom del path");
        String path = input.nextLine();

        System.out.println("Introdueix el nom del nou fitxer");
        String name = input.nextLine() + ".txt";

        path += name;

        System.out.println("Introdueix el JSON String que hi haura en el document");
        String content = input.nextLine();
        input.close();

        System.out.println("Creant fitxer");
        f.c_file(path, content);

        String contenido = f.r_file();
        System.out.println(contenido);
        System.out.println(f.getPath());
        /*
        Scanner input = new Scanner(System.in);
        System.out.println("Introdueix el nom del nou fitxer");
        String name = input.nextLine();
        f.c_folder(name);
        input.close();
        */
    }
}