package ru.mai.cipher.polynomial.impl;

import java.math.BigInteger;
import ru.mai.cipher.polynomial.PolynomialMultiplication;
import ru.mai.cipher.utils.GaloisField;

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
    return GaloisField.multiply(A, B, n, a, N);
  }
}
