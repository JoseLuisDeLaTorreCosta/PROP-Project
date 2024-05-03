package main.Domain;

import java.util.ArrayList;

/**
 * @author Gerard Gispert Carreras (gerard.gispert@estudiantat.upc.edu)
 */
public abstract class Document {
  protected String id;
  private int[][] matMarkov;
  protected ArrayList<Character> alfabet;

  abstract int[][] calcMatMarkov();

  public String getId() {
    return this.id;
  }

  public boolean isLlistaParaules() {
    return false;
  }

  public boolean isAlfabet() {
    return false;
  }

  public boolean isText() {
    return false;
  }

  public ArrayList<Character> getAlfabet() {
    return this.alfabet;
  }

  public ArrayList<Character> setAlfabet(ArrayList<Character> alfabet) {
    this.alfabet = alfabet;
    return this.alfabet;
  }

  public int[][] getMatMarkov() {
    return this.matMarkov;
  }

  public String setId(String newId) {
    this.id = newId;
    return this.id;
  }

  public int[][] setMatMarkov(int[][] newMatMarkov) {
    this.matMarkov = newMatMarkov;
    return this.matMarkov;
  }
}