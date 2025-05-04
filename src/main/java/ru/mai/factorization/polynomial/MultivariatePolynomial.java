package ru.mai.factorization.polynomial;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import ru.mai.factorization.monomial.Monomial;

/**
 * Класс для работы с полиномами.
 */
public class MultivariatePolynomial {

  public final Map<Monomial, BigInteger> terms = new HashMap<>();

  /**
   * Добавление нового монома к полиному.
   *
   * @param coeff коэффициент монома
   * @param m моном
   */
  public void addTerm(BigInteger coeff, Monomial m) {
    terms.merge(m, coeff, BigInteger::add);
  }

  /**
   * Сложение коэффициентов при подобном мономе.
   *
   * @param m моном
   * @param coeff коэффициент
   */
  public void addToCoeff(Monomial m, BigInteger coeff) {
    this.terms.merge(m, coeff, BigInteger::add);

    if (this.terms.get(m).equals(BigInteger.ZERO)) {
      this.terms.remove(m);
    }
  }

  /**
   * Умножение полиномов.
   *
   * @param other полином
   * @return результат умножения
   */
  public MultivariatePolynomial multiply(MultivariatePolynomial other) {
    MultivariatePolynomial res = new MultivariatePolynomial();

    for (var a : terms.entrySet()) {
      for (var b : other.terms.entrySet()) {
        res.addTerm(a.getValue().multiply(b.getValue()), a.getKey().add(b.getKey()));
      }
    }

    return res;
  }

  /**
   * Умножение на моном.
   *
   * @param dx степень при x
   * @param dy степень при y
   * @param dz степень при z
   * @return результат умножения
   */
  public MultivariatePolynomial multiplyMonomial(int dx, int dy, int dz) {
    MultivariatePolynomial res = new MultivariatePolynomial();

    for (var entry : this.terms.entrySet()) {
      Monomial m = entry.getKey();
      BigInteger coeff = entry.getValue();
      res.addTerm(coeff, new Monomial(m.x() + dx, m.y() + dy, m.z() + dz));
    }

    return res;
  }

  /**
   * Умножение монома на число
   *
   * @param scalar число
   * @return результат умножения
   */
  public MultivariatePolynomial multiply(BigInteger scalar) {
    MultivariatePolynomial res = new MultivariatePolynomial();

    for (var e : terms.entrySet()) {
      res.addTerm(e.getValue().multiply(scalar), e.getKey());
    }

    return res;
  }

  /**
   * Возведение монома в степень с использованием быстрого возведения в степень.
   *
   * @param exp степень
   * @return результат возведения в степень
   */
  public MultivariatePolynomial pow(int exp) {
    if (exp < 0) {
      throw new IllegalArgumentException("Отрицательная степень не поддерживается.");
    }

    MultivariatePolynomial result = new MultivariatePolynomial();
    result.addTerm(BigInteger.ONE, new Monomial(0, 0, 0));

    MultivariatePolynomial base = new MultivariatePolynomial();
    base.terms.putAll(this.terms);

    while (exp > 0) {
      if ((exp & 1) == 1) {
        result = result.multiply(base);
      }
      base = base.multiply(base);
      exp >>= 1;
    }

    return result;
  }

  public Map<Monomial, BigInteger> getTerms() {
    return terms;
  }

  @Override
  public String toString() {
    return "MultivariatePolynomial{" +
        "terms=" + terms +
        '}';
  }
}