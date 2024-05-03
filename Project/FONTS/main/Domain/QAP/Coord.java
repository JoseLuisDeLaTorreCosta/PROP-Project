package main.Domain.QAP;

/**
 * @author Gerard Gispert Carreras (gerard.gispert@estudiantat.upc.edu)
 */
public class Coord {
  private int x;
  private int y;

  public Coord(int x, int y) {
    this.setX(x);
    this.setY(y);
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int setX(int x) {
    if (x >= 0) {
      this.x = x;
    } else {
      this.x = -1;
    }
    return this.x;
  }

  public int setY(int y) {
    if (y >= 0) {
      this.y = y;
    } else {
      this.y = -1;
    }
    return this.y;
  }
}
