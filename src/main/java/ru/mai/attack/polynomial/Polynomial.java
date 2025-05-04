package ru.mai.attack.polynomial;

import java.math.BigInteger;
import java.util.Arrays;
import ru.mai.attack.monomial.Monomial;

/**
 * Утилитарный класс для работы с полиномами.
 */
public class Polynomial {

  /**
   * Вычисление функции phi.
   *
   * @param n максимальная степень многочленов
   * @param N модуль RSA
   * @return полином-функция phi
   */
  public static BigInteger[] phi(int n, BigInteger N) {
    if (n == 1) {
      return new BigInteger[]{BigInteger.ONE};
    }

    if (n == 2) {
      return new BigInteger[]{BigInteger.ONE, N.add(BigInteger.ONE)};
    }

    BigInteger[] one = new BigInteger[]{BigInteger.ONE, BigInteger.ZERO};
    BigInteger[] result = subtract(multiply(one, phi(n - 1, N)), multiply(N, phi(n - 2, N)));

    result[result.length - 1] = result[result.length - 1].add(N.pow(n - 1).add(BigInteger.ONE));

    return result;
  }

  /**
   * Умножение полинома на число.
   *
   * @param N число
   * @param A полином
   * @return результат умножения
   */
  public static BigInteger[] multiply(BigInteger N, BigInteger[] A) {
    BigInteger[] res = new BigInteger[A.length];

    for (int i = 0; i < A.length; i++) {
      res[i] = A[i].multiply(N);
    }

    return res;
  }

  /**
   * Умножение полиномов.
   *
   * @param A первый полином
   * @param B второй полином
   * @return результат умножения
   */
  public static BigInteger[] multiply(BigInteger[] A, BigInteger[] B) {
    BigInteger[] res = new BigInteger[A.length + B.length - 1];
    Arrays.fill(res, BigInteger.ZERO);

    for (int i = 0; i < A.length; i++) {
      for (int j = 0; j < B.length; j++) {
        res[i + j] = res[i + j].add(A[i].multiply(B[j]));
      }
    }

    return res;
  }

  /**
   * Вычитание полиномов.
   *
   * @param A первый полином
   * @param B второй полином
   * @return результат вычитания
   */
  public static BigInteger[] subtract(BigInteger[] A, BigInteger[] B) {
    int len = Math.max(A.length, B.length);

    BigInteger[] res = new BigInteger[len];
    Arrays.fill(res, BigInteger.ZERO);

    for (int i = 0; i < len; i++) {
      BigInteger a = i < A.length ? A[A.length - 1 - i] : BigInteger.ZERO;
      BigInteger b = i < B.length ? B[B.length - 1 - i] : BigInteger.ZERO;
      res[len - 1 - i] = a.subtract(b);
    }

    return res;
  }

  /**
   * Построение функции F(x,y,z).
   *
   * @param H функция H(y)
   * @param c свободный член
   * @return функция F(x,y,z)
   */
  public static MultivariatePolynomial buildF(BigInteger[] H, BigInteger c) {
    MultivariatePolynomial F = new MultivariatePolynomial();

    F.addTerm(BigInteger.ONE, new Monomial(0, 0, 1));

    for (int i = 1; i < H.length; i++) {
      F.addTerm(H[i], new Monomial(1, H.length - i - 1, 0));
    }

    F.addTerm(c, new Monomial(0, 0, 0));

    return F;
  }

  /**
   * Замена z=xy^r
   *
   * @param poly полином, в котором необходимо произвести замену
   * @param r параметр для замены
   * @return результат замены
   */
  public static MultivariatePolynomial reduceXYrToZ(MultivariatePolynomial poly, int r) {
    MultivariatePolynomial result = new MultivariatePolynomial();

    for (var entry : poly.terms.entrySet()) {
      Monomial m = entry.getKey();
      BigInteger coeff = entry.getValue();

      int q = Math.min(m.x(), m.y() / r);

      int newX = m.x() - q;
      int newY = m.y() - q * r;
      int newZ = m.z() + q;

      Monomial reduced = new Monomial(newX, newY, newZ);
      result.addToCoeff(reduced, coeff);
    }

    return result;
  }
}
