package ru.mai.attack;

import java.util.Objects;

/**
 * Класс для работы с мономами.
 */
public class Monomial {

  final int x;
  final int y;
  final int z;

  public Monomial(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Monomial add(Monomial other) {
    return new Monomial(this.x + other.x, this.y + other.y, this.z + other.z);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  public boolean equals(Object o) {
    if (!(o instanceof Monomial m)) {
      return false;
    }
    return x == m.x && y == m.y && z == m.z;
  }

  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  public String toString() {
    return "Monomial{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
  }
}