package ru.mai.factorization.comparator;

import java.util.Comparator;
import java.util.Map.Entry;
import ru.mai.factorization.utils.IndexTriple;
import ru.mai.factorization.polynomial.MultivariatePolynomial;

/**
 * Класс для сравнения объектов G(x,y,z) в порядке, указанном в статье.
 */
public class IndexTripleEntryComparator implements Comparator<Entry<IndexTriple, MultivariatePolynomial>> {

  /**
   * Сравнение объектов G(x,y,z).
   *
   * @param first первый объект
   * @param second второй объект
   * @return результат сравнения
   */
  @Override
  public int compare(
      Entry<IndexTriple, MultivariatePolynomial> first,
      Entry<IndexTriple, MultivariatePolynomial> second) {
    IndexTriple a = first.getKey();
    IndexTriple b = second.getKey();

    if ((a.k() < b.k())
        || (a.k() == b.k() && a.i() < b.i())
        || (a.k() == b.k() && a.i() == b.i() && a.j() < b.j())) {
      return -1;
    }

    if (a.k() == b.k() && a.i() == b.i() && a.j() == b.j()) {
      return 0;
    }

    return 1;
  }
}
