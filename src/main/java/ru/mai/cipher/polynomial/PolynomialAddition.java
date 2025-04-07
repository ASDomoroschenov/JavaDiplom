package ru.mai.cipher.polynomial;

import java.math.BigInteger;

/**
 * Функциональный интерфейс для сложения многочленов.
 */
public interface PolynomialAddition {

  /**
   * Метод для сложения многочленов.
   *
   * @param A первый многочлен
   * @param B второй многочлен
   * @return результат сложения
   */
  BigInteger[] add(BigInteger[] A, BigInteger[] B);
}
