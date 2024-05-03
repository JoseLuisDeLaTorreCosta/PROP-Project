package main.Domain;

import java.util.*;

/**
 * @author Gerard Gispert Carreras (gerard.gispert@estudiantat.upc.edu)
 */
public class Text extends Document {
  private String text;

  public Text(String text, String id) {
    this.text = text;
    this.calcAlfabet();
    this.calcMatMarkov();
    super.setId(id);
  }

  public ArrayList<Character> calcAlfabet() {
    int textSize = this.text.length();

    // Es va afegint els caracters a un text per evitar duplicats
    Set<Character> alfabetSet = new HashSet<Character>();
    char c;
    for (int i = 0; i < textSize; i++) {
      c = text.charAt(i);
      alfabetSet.add(c);
    }

    // Es transforma el Set a una ArrayList i es treuen els espais en blanc
    ArrayList<Character> alfabet = new ArrayList<Character>();
    for (char x : alfabetSet) {
      if (x != ' ' && x != '\n' && x != '\t')
        alfabet.add(x);
    }
    super.setAlfabet(alfabet);
    return alfabet;
  }

  public int[][] calcMatMarkov() {
    // Declaració de variables
    int alfabetSize = super.getAlfabet().size();
    int textSize = this.text.length();
    int[][] matMarkovAux = new int[alfabetSize][alfabetSize];
    if (textSize == 0)
      return matMarkovAux;
    Map<Character, Integer> posLletres = new HashMap<Character, Integer>();
    char cInicial = text.charAt(0);
    char c1 = text.charAt(0);
    posLletres.put(c1, 0);
    char c2;
    int pos1 = 0;
    int pos2 = 0;
    int lastCol = 1;
    for (int i = 1; i < textSize; i++) {
      c2 = text.charAt(i);

      // Evitar que s'afageixin a la matriu els espais en blanc
      if (c2 == ' ' || c2 == '\n' || c2 == '\t')
        continue;

      // Si ja haviem trobat la lletra abans s'agafen les posicions i es suma a la
      // matriu
      else if (posLletres.containsKey(c2)) {
        pos1 = posLletres.get(c1);
        pos2 = posLletres.get(c2);
      }

      // Si és el primer cop que es troba el caràcter se li assigna la següent columna
      // de la matriu
      else {
        pos1 = posLletres.get(c1);
        posLletres.put(c2, lastCol);
        pos2 = lastCol;
        lastCol++;
      }
      matMarkovAux[pos1][pos2]++;
      c1 = c2;
    }
    pos1 = posLletres.get(c1); // Posició últim caràcter
    pos2 = posLletres.get(cInicial); // Posició primer caràcter
    matMarkovAux[pos1][pos2]++;
    super.setMatMarkov(matMarkovAux);
    return matMarkovAux;
  }

  public boolean isText() {
    return true;
  }

  public String getText() {
    return text;
  }
}
