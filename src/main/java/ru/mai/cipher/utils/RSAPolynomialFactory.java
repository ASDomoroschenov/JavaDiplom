package ru.mai.cipher.utils;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Утилитарный класс для работы с многочленами RSA
 */
public class RSAPolynomialFactory {

  /**
   * Единичный многочлен заданной степени.
   *
   * @param degree степень многочлена
   * @return единичный многочлен
   */
  public static BigInteger[] unitPolynomial(int degree) {
    BigInteger[] result = polynomial(degree);
    result[0] = BigInteger.ONE;
    return result;
  }

  /**
   * Генерация пустого многочлена заданной степени.
   *
   * @param degree степень многочлена
   * @return пустой многочлен заданной степени
   */
  public static BigInteger[] polynomial(int degree) {
    return allocate(degree);
  }

  /**
   * Создание пустого многочлена заданной длины с нулевыми коэффициентами.
   *
   * @param degree степень многочлена
   * @return пустой многочлен заданной степени
   */
  private static BigInteger[] allocate(int degree) {
    BigInteger[] result = new BigInteger[degree];
    Arrays.fill(result, BigInteger.ZERO);
    return result;
  }

  /**
   * Генерация неприводимого многочлена вида x^n - a.
   *
   * @param p параметр RSA
   * @param q параметр RSA
   * @param N параметр RSA
   * @param n параметр RSA
   * @return свободный член неприводимого многочлена
   */
  public static BigInteger generateIrreducible(BigInteger p, BigInteger q, BigInteger N, int n) {
    for (BigInteger a = BigInteger.ONE; a.compareTo(N) < 0; a = a.add(BigInteger.ONE)) {
      if (isIrreducibleMod(a, p, n) && isIrreducibleMod(a, q, n) && isIrreducibleMod(a, N, n)) {
        return a;
      }
    }

    return null;
  }

  /**
   * Проверка неприводимости многочлена.
   *
   * @param a свободный член многочлена
   * @param modulus модуль
   * @param n степень многочлена
   * @return результат неприводимости
   */
  private static boolean isIrreducibleMod(BigInteger a, BigInteger modulus, int n) {
    for (BigInteger x = BigInteger.ZERO; x.compareTo(modulus) < 0; x = x.add(BigInteger.ONE)) {
      BigInteger xn = x.modPow(BigInteger.valueOf(n), modulus);

      if (xn.equals(a.mod(modulus))) {
        return false;
      }
    }

    return true;
  }
}
