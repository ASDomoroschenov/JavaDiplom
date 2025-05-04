package ru.mai.factorization.utils;

/**
 * Класс для работы с тройками индексов.
 *
 * @param k первый индекс
 * @param i второй индекс
 * @param j третий индекс
 */
public record IndexTriple(int k, int i, int j) implements Comparable<IndexTriple> {

  @Override
  public int k() {
    return k;
  }

  @Override
  public int i() {
    return i;
  }

  @Override
  public int j() {
    return j;
  }

  @Override
  public int compareTo(IndexTriple o) {
    if (this.k != o.k) {
      return Integer.compare(this.k, o.k);
    }
    if (this.i != o.i) {
      return Integer.compare(this.i, o.i);
    }
    return Integer.compare(this.j, o.j);
  }

  @Override
  public String toString() {
    return "G_" + k + "," + i + "," + j;
  }
}