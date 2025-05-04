package ru.mai.attack.comparator;

import java.util.Comparator;
import ru.mai.attack.monomial.Monomial;

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
    if ((first.z() < second.z())
        || (first.z() == second.z() && first.x() < second.x())
        || (first.z() == second.z() && first.x() == second.x() && first.y() < second.y())) {
      return -1;
    }

    if (first.z() == second.z() && first.x() == second.x() && first.y() == second.y()) {
      return 0;
    }

    return 1;
  }
}
