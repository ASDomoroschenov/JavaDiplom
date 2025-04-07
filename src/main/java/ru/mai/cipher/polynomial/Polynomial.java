package ru.mai.cipher.polynomial;

import java.math.BigInteger;

/**
 * Интерфейс для работы с многочленами в RSA
 */
public interface Polynomial {

  /**
   * Получение массива коэффициентов многочлена.
   *
   * @return массив коэффициентов
   */
  BigInteger[] getCoefficients();

  /**
   * Сложение многочленов.
   *
   * @param polynomial многочлен для сложения
   * @return результат сложения
   */
  Polynomial add(Polynomial polynomial);

  /**
   * Умножение многочленов.
   *
   * @param polynomial многочлен для умножения
   * @return результат умножения
   */
  Polynomial multiply(Polynomial polynomial);

  /**
   * Возведение в степень многочлена.
   *
   * @param n степень
   * @return результат возведения в степень
   */
  Polynomial pow(BigInteger n);
}
