package ru.mai.attack;

import java.util.Comparator;

/**
 * Класс для сравнения мономов в порядке, указанном в статье.
 */
public class MonomialComparator implements Comparator<Monomial> {

  /**
   * Сравнение мономов.
   *
   * @param first первый моном
   * @param second второй моном
   * @return результат сравнения
   */
  @Override
  public int compare(Monomial first, Monomial second) {
    if ((first.getZ() < second.getZ())
        || (first.getZ() == second.getZ() && first.getX() < second.getX())
        || (first.getZ() == second.getZ() && first.getX() == second.getX() && first.getY() < second.getY())) {
      return -1;
    }

    if (first.getZ() == second.getZ() && first.getX() == second.getX() && first.getY() == second.getY()) {
      return 0;
    }

    return 1;
  }
}
