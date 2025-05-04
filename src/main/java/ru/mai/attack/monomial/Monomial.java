package ru.mai.attack.monomial;

/**
 * Класс для работы с мономами.
 */
public record Monomial(int x, int y, int z) {

  public Monomial add(Monomial other) {
    return new Monomial(this.x + other.x, this.y + other.y, this.z + other.z);
  }

  public boolean equals(Object o) {
    if (!(o instanceof Monomial m)) {
      return false;
    }
    return x == m.x && y == m.y && z == m.z;
  }

  public String toString() {
    return "Monomial{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
  }
}