package ru.mai.cipher.polynomial.impl;

import java.math.BigInteger;
import ru.mai.cipher.polynomial.PolynomialMultiplication;
import ru.mai.cipher.utils.GaloisField;
import ru.mai.cipher.utils.RSAPolynomialFactory;

/**
 * Класс, реализующий умножения полиномов для дешифрующей стороны.
 */
public class RSADecipherMultiplication implements PolynomialMultiplication {

  private final BigInteger p;
  private final BigInteger q;
  private final BigInteger a;
  private final int n;

  /**
   * Конструктор.
   *
   * @param p параметр RSA
   * @param q параметр RSA
   * @param a параметр RSA
   * @param n параметр RSA
   */
  public RSADecipherMultiplication(BigInteger p, BigInteger q, BigInteger a, int n) {
    this.p = p;
    this.q = q;
    this.a = a;
    this.n = n;
  }

  /**
   * Метод для умножения многочленов.
   *
   * @param A первый многочлен
   * @param B второй многочлен
   * @return результат умножения
   */
  @Override
  public BigInteger[] multiply(BigInteger[] A, BigInteger[] B) {
    return multiplyInner(A, B);
  }

  /**
   * Внутренний метод умножения.
   *
   * @param A первый многочлен
   * @param B второй многочлен
   * @return результат умножения
   */
  private BigInteger[] multiplyInner(BigInteger[] A, BigInteger[] B) {
    BigInteger[] Ap = RSAPolynomialFactory.polynomial(A.length);
    BigInteger[] Aq = RSAPolynomialFactory.polynomial(A.length);

    BigInteger[] Bp = RSAPolynomialFactory.polynomial(B.length);
    BigInteger[] Bq = RSAPolynomialFactory.polynomial(B.length);

    for (int i = 0; i < n; i++) {
      Ap[i] = A[i].mod(p);
      Bp[i] = B[i].mod(p);

      Aq[i] = A[i].mod(q);
      Bq[i] = B[i].mod(q);
    }

    BigInteger[] multiplyModP = GaloisField.multiplyWithInverse(Ap, Bp, n, a, p);
    BigInteger[] multiplyModQ = GaloisField.multiplyWithInverse(Aq, Bq, n, a, q);

    return restoreCTR(multiplyModP, multiplyModQ, p, q, n);
  }

  /**
   * Восстановление коэффициентов по системе из КТО.
   *
   * @param A первый многочлен
   * @param B второй многочлен
   * @param p модуль выполнения первого умножения
   * @param q модуль выполнения первого умножения
   * @param n максимальная степень многочленов
   * @return восстановленные коэффициенты из системы КТО
   */
  private static BigInteger[] restoreCTR(
      BigInteger[] A,
      BigInteger[] B,
      BigInteger p,
      BigInteger q,
      int n) {
    BigInteger[] result = RSAPolynomialFactory.polynomial(n);
    BigInteger N = p.multiply(q);
    BigInteger Np = N.divide(p);
    BigInteger Nq = N.divide(q);

    BigInteger invNp = Np.modInverse(p);
    BigInteger invNq = Nq.modInverse(q);

    BigInteger[] A1 = multiplyByNumber(multiplyByNumber(A, Np, N), invNp, N);
    BigInteger[] B1 = multiplyByNumber(multiplyByNumber(B, Nq, N), invNq, N);

    for (int i = 0; i < A1.length; i++) {
      result[i] = (A1[i].add(B1[i])).mod(N);
    }

    return result;
  }

  /**
   * Умножение многочлена на число по модулю.
   *
   * @param A многочлен
   * @param B число
   * @param m модуль
   * @return результат умножения
   */
  private static BigInteger[] multiplyByNumber(BigInteger[] A, BigInteger B, BigInteger m) {
    BigInteger[] result = RSAPolynomialFactory.polynomial(A.length);

    for (int i = 0; i < A.length; i++) {
      result[i] = (A[i].multiply(B)).mod(m);
    }

    return result;
  }

  /**
   * Умножение по модулю неприводимого многочлена в поле Галуа.
   *
   * @param A первый многочлен
   * @param B второй многочлен
   * @param n максимальная степень многочленов
   * @param a свободный член неприводимого многочлена
   * @param m модуль поля Галуа
   * @return результат умножения
   */
  private static BigInteger[] multiplyInGaloisField(
      BigInteger[] A,
      BigInteger[] B,
      int n,
      BigInteger a,
      BigInteger m) {
    BigInteger[] product = RSAPolynomialFactory.polynomial(2 * n - 1);
    BigInteger[] result = RSAPolynomialFactory.polynomial(n);

    for (int i = 0; i < A.length; i++) {
      for (int j = 0; j < B.length; j++) {
        product[i + j] = (product[i + j].add(A[i].multiply(B[j]))).mod(m);
      }
    }

    for (int i = n; i < 2 * n - 1; i++) {
      product[i - n] = (product[i - n].add(a.multiply(product[i]))).mod(m);
    }

    System.arraycopy(product, 0, result, 0, n);

    if (result[result.length - 1].equals(BigInteger.ZERO)) {
      return result;
    }

    BigInteger reverse = result[n - 1].modInverse(m);

    for (int i = 0; i < result.length; i++) {
      result[i] = (result[i].multiply(reverse)).mod(m);
    }

    return result;
  }
}
