package ru.mai.cipher.polynomial.impl;

import java.math.BigInteger;
import ru.mai.cipher.polynomial.PolynomialAddition;
import ru.mai.cipher.utils.RSAPolynomialFactory;

/**
 * Реализация операции сложения многочленов в RSA Котана и Тэшелеану.
 */
public class RSAPolynomialAddition implements PolynomialAddition {

  private final Integer degree;
  private final BigInteger modulus;

  /**
   * Конструктор.
   *
   * @param degree максимальная степень многочленов
   * @param modulus модуль
   */
  public RSAPolynomialAddition(Integer degree, BigInteger modulus) {
    this.degree = degree;
    this.modulus = modulus;
  }

  /**
   * Метод для сложения многочленов.
   *
   * @param A первый многочлен
   * @param B второй многочлен
   * @return результат сложения
   */
  @Override
  public BigInteger[] add(BigInteger[] A, BigInteger[] B) {
    BigInteger[] result = RSAPolynomialFactory.polynomial(degree);

    for (int i = 0; i < degree; i++) {
      BigInteger a =  i >= A.length ? BigInteger.ZERO : A[i];
      BigInteger b =  i >= B.length ? BigInteger.ZERO : B[i];

      result[i] = (a.add(b)).mod(modulus);
    }

    return result;
  }
}
