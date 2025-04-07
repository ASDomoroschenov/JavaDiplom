package ru.mai.cipher.polynomial.impl;

import java.math.BigInteger;
import java.util.Arrays;
import ru.mai.cipher.polynomial.Polynomial;
import ru.mai.cipher.polynomial.PolynomialAddition;
import ru.mai.cipher.polynomial.PolynomialMultiplication;
import ru.mai.cipher.utils.RSAPolynomialFactory;

/**
 * Реализация интерфейса для работы с многочленами в RSA Котана и Тэшелеану.
 */
public class RSAPolynomial implements Polynomial {

  private final int degree;
  private final BigInteger[] coefficients;
  private final PolynomialAddition addition;
  private final PolynomialMultiplication multiplication;

  /**
   * Конструктор.
   *
   * @param degree максимальная степень многочлена
   * @param coefficients коэффициенты многочлена
   * @param addition класс, реализующий операцию сложения
   * @param multiplication класс, реализующий операцию умножения
   */
  public RSAPolynomial(
      int degree,
      BigInteger[] coefficients,
      PolynomialAddition addition,
      PolynomialMultiplication multiplication) {
    this.degree = degree;
    this.coefficients = coefficients;
    this.addition = addition;
    this.multiplication = multiplication;
  }

  /**
   * Получение массива коэффициентов многочлена.
   *
   * @return массив коэффициентов
   */
  @Override
  public BigInteger[] getCoefficients() {
    return coefficients;
  }

  /**
   * Сложение многочленов.
   *
   * @param polynomial многочлен для сложения
   * @return результат сложения
   */
  @Override
  public Polynomial add(Polynomial polynomial) {
    return new RSAPolynomial(
        degree,
        addition.add(this.coefficients, ((RSAPolynomial) polynomial).coefficients),
        this.addition,
        this.multiplication
    );
  }

  /**
   * Умножение многочленов.
   *
   * @param polynomial многочлен для умножения
   * @return результат умножения
   */
  @Override
  public Polynomial multiply(Polynomial polynomial) {
    return new RSAPolynomial(
        degree,
        multiplication.multiply(this.coefficients, ((RSAPolynomial) polynomial).coefficients),
        this.addition,
        this.multiplication
    );
  }

  /**
   * Возведение в степень многочлена. Быстрое возведение в степень.
   *
   * @param n степень
   * @return результат возведения в степень
   */
  @Override
  public Polynomial pow(BigInteger n) {
    Polynomial result = new RSAPolynomial(
        degree,
        RSAPolynomialFactory.unitPolynomial(degree),
        this.addition,
        this.multiplication
    );
    Polynomial base = this;

    while (!n.equals(BigInteger.ZERO)) {
      if (n.testBit(0)) {
        result = result.multiply(base);
      }

      base = base.multiply(base);
      n = n.shiftRight(1);
    }

    return result;
  }

  /**
   * Переопределенный метод преобразования объекта в строку.
   *
   * @return массив коэффициентов многочлена
   */
  @Override
  public String toString() {
    return Arrays.toString(coefficients);
  }
}
