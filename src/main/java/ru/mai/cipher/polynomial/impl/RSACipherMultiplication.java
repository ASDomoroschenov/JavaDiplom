package ru.mai.cipher.polynomial.impl;

import java.math.BigInteger;
import ru.mai.cipher.polynomial.PolynomialMultiplication;
import ru.mai.cipher.utils.RSAPolynomialFactory;

/**
 * Класс, реализующий умножения полиномов для шифрующей стороны.
 */
public class RSACipherMultiplication implements PolynomialMultiplication {

  private final BigInteger N;
  private final BigInteger a;
  private final int n;

  /**
   * Конструктор.
   *
   * @param N параметр RSA
   * @param a параметр RSA
   * @param n параметр RSA
   */
  public RSACipherMultiplication(BigInteger N, BigInteger a, int n) {
    this.N = N;
    this.a = a;
    this.n = n;
  }

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
    BigInteger[] product = RSAPolynomialFactory.polynomial(2 * n - 1);
    BigInteger[] result = RSAPolynomialFactory.polynomial(n);

    for (int i = 0; i < A.length; i++) {
      for (int j = 0; j < B.length; j++) {
        product[i + j] = (product[i + j].add(A[i].multiply(B[j]))).mod(N);
      }
    }

    for (int i = n; i < 2 * n - 1; i++) {
      product[i - n] = (product[i - n].add(a.multiply(product[i]))).mod(N);
    }

    System.arraycopy(product, 0, result, 0, n);

    return result;
  }
}
