package main.Domain.QAP;

import java.util.*;

/**
 * @author Gerard Gispert Carreras (gerard.gispert@estudiantat.upc.edu)
 */
public class MinimalLines {
  private int matSize;
  private int[][] costMatrix;
  private boolean[] rowsAssigned; // If i row is assigned
  private boolean[] colsAssigned; // If i column is assigned
  private int[] rowsStarred; // Starred row and column
  private int[] colsStarred; // Starred row and column
  private int[] rowsPrimed; // Primed row and column
  private int[] colsPrimed; // Primed row and column

  public MinimalLines(int[][] costMatrix, boolean[] rowsAssigned, boolean[] colsAssigned, int[] rowsStarred) {
    this.matSize = costMatrix.length;
    this.costMatrix = costMatrix;
    this.rowsAssigned = rowsAssigned;
    this.colsAssigned = colsAssigned;
    this.rowsStarred = rowsStarred;
    this.colsStarred = new int[this.matSize];
    this.rowsPrimed = new int[this.matSize];
    this.colsPrimed = new int[this.matSize];
    this.initArrays();
  }

  public int calcMinimalLines() {
    this.initArrays();
    this.initStarZeros();
    Coord zero;
    boolean minimumLinesFound = false;
    while (!minimumLinesFound) { // While all zeros not covered
      zero = this.findNonCoveredZero();
      if (zero.getX() != -1) { // Non-covered zero found
        this.prime(new Coord(zero.getX(), zero.getY())); // Prime the non-covered zero
        if (this.isStarredRow(zero.getX())) { // If there's a starred zero on the row
          this.assignRow(zero.getX()); // Assign row
          this.unassignColumn(this.rowsStarred[zero.getX()]); // Assign column
        } else {
          boolean pathEnded = false;
          Coord currentZero = new Coord(zero.getX(), zero.getY());
          while (!pathEnded) { // While starred zero on the column of the primed zero
            Coord starredZero = this.getStarredByColumn(currentZero.getY());
            if (starredZero.getX() != -1) { // If starred zero found
              // Star primes and unstar starreds
              this.unstar(new Coord(starredZero.getX(), starredZero.getY())); 
              this.unprime(currentZero);
              this.star(new Coord(currentZero.getX(), currentZero.getY()));
              currentZero.setX(starredZero.getX());
              currentZero.setY(this.rowsPrimed[starredZero.getX()]);
            } else { // Starred zero not found
              this.unprime(currentZero);
              this.star(new Coord(currentZero.getX(), currentZero.getY()));
              pathEnded = true;
            }
          }
          this.resetCover();
        }
      } else {
        minimumLinesFound = true;
      }
    }
    return calcStarredZeros();
  }

  private void initArrays() {
    initArrayFalse(this.rowsAssigned);
    initArrayFalse(this.colsAssigned);
    initArrayNeg(this.rowsStarred);
    initArrayNeg(this.colsStarred);
    initArrayNeg(this.rowsPrimed);
    initArrayNeg(this.colsPrimed);
  }

  private void initArrayFalse(boolean[] array) {
    Arrays.fill(array, false);
  }

  private void initArrayNeg(int[] array) {
    Arrays.fill(array, -1);
  }

  private void initStarZeros() {
    for (int i = 0; i < this.matSize; ++i) {
      for (int j = 0; j < this.matSize; ++j) {
        if (this.costMatrix[i][j] == 0 && !this.isColumnAssigned(j)) {
          Coord zero = new Coord(i, j);
          this.star(zero);
          this.assignColumn(j);
          break;
        }
      }
    }
  }

  private Coord findNonCoveredZero() {
    for (int j = 0; j < this.matSize; ++j) {
      for (int i = 0; i < this.matSize && !colsAssigned[j]; ++i) {
        if (!isRowAssigned(i) && this.costMatrix[i][j] == 0) {
          return new Coord(i, j);
        }
      }
    }
    return new Coord(-1, -1);
  }

  private void resetCover() {
    for (int j = 0; j < this.matSize; ++j) {
      if (isPrimedColumn(j))
        this.unprime(this.getPrimedByColumn(j));
      if (this.isStarredColumn(j))
        this.assignColumn(j);
      else
        this.unassignColumn(j);
    }
    for (int i = 0; i < this.matSize; ++i) {
      this.unassignRow(i);
    }
  }

  private int calcStarredZeros() {
    int starredZeros = 0;
    boolean ended = true;
    // Unassign all columns and assign non starred rows
    for (int i = 0; i < this.matSize; ++i) {
      this.unassignColumn(i);
      if (this.isStarredRow(i)) {
        starredZeros += 1;
        this.unassignRow(i);
      } else {
        ended = false;
        this.assignRow(i);
      }
    }
    while (!ended) { // While still finding zeros
      ended = true;
      // If new starred columns in assigned rows with 0 assign the column
      for (int i = 0; i < this.matSize; ++i) {
        if (this.isRowAssigned(i)) {
          for (int j = 0; j < this.matSize; ++j) {
            if (this.costMatrix[i][j] == 0 && !this.isColumnAssigned(j)) {
              ended = false;
              this.assignColumn(j);
            }
          }
        }
      }
      if (!ended) {
        ended = true;
        // Assign rows of the starred zeros from new assigned columns
        for (int i = 0; i < this.matSize; ++i) {
          if (!this.isRowAssigned(i)) {
            Coord star = getStarredByRow(i);
            if (this.isColumnAssigned(star.getY())) {
              ended = false;
              this.assignRow(i);
            }
          }
        }
      }
    }
    // Inverse assigned rows
    for (int i = 0; i < this.matSize; ++i) {
      if (this.isRowAssigned(i)) {
        this.unassignRow(i);
      } else {
        this.assignRow(i);
      }
    }
    return starredZeros;
  }

  private void star(Coord zero) {
    this.rowsStarred[zero.getX()] = zero.getY();
    this.colsStarred[zero.getY()] = zero.getX();
  }

  private void unstar(Coord zero) {
    this.rowsStarred[zero.getX()] = -1;
    this.colsStarred[zero.getY()] = -1;
  }

  private void prime(Coord zero) {
    this.rowsPrimed[zero.getX()] = zero.getY();
    this.colsPrimed[zero.getY()] = zero.getX();
  }

  private void unprime(Coord zero) {
    this.rowsPrimed[zero.getX()] = -1;
    this.colsPrimed[zero.getY()] = -1;
  }

  private void assignRow(int r) {
    this.rowsAssigned[r] = true;
  }

  private void unassignRow(int r) {
    this.rowsAssigned[r] = false;
  }

  private boolean isRowAssigned(int r) {
    return this.rowsAssigned[r];
  }

  private void assignColumn(int c) {
    this.colsAssigned[c] = true;
  }

  private void unassignColumn(int c) {
    this.colsAssigned[c] = false;
  }

  private boolean isColumnAssigned(int c) {
    return this.colsAssigned[c];
  }

  private boolean isStarredRow(int r) {
    return this.rowsStarred[r] != -1;
  }

  private boolean isStarredColumn(int c) {
    return this.colsStarred[c] != -1;
  }

  private boolean isPrimedRow(int r) {
    return this.rowsPrimed[r] != -1;
  }

  private boolean isPrimedColumn(int c) {
    return this.colsPrimed[c] != -1;
  }

  private Coord getStarredByRow(int r) {
    return new Coord(r, this.rowsStarred[r]);
  }

  private Coord getStarredByColumn(int c) {
    return new Coord(this.colsStarred[c], c);

  }

  private Coord getPrimedByRow(int r) {
    return new Coord(r, this.rowsPrimed[r]);
  }

  private Coord getPrimedByColumn(int c) {
    return new Coord(this.colsPrimed[c], c);
  }

}
