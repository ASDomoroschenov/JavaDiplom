package ru.mai.cipher.polynomial;

import java.math.BigInteger;

/**
 * Функциональный интерфейс для умножения многочленов
 */
public interface PolynomialMultiplication {

  /**
   * Метод для умножения многочленов.
   *
   * @param A первый многочлен
   * @param B второй многочлен
   * @return результат умножения
   */
  BigInteger[] multiply(BigInteger[] A, BigInteger[] B);
}
