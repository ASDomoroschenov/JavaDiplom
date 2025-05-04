package ru.mai.cipher.utils;

import java.math.BigInteger;

/**
 * Утилитарный класс для работы в Поле Галуа.
 */
public class GaloisField {

  /**
   * Умножение по модулю неприводимого многочлена в поле Галуа с преобразованием
   * многочлена к приведенному с помощью операции умножения на обратный элемент в поле.
   *
   * @param A первый многочлен
   * @param B второй многочлен
   * @param n максимальная степень многочленов
   * @param a свободный член неприводимого многочлена
   * @param m модуль поля Галуа
   * @return результат умножения
   */
  public static BigInteger[] multiplyWithInverse(
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
  public static BigInteger[] multiply(
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

    return result;
  }
}
