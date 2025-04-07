package ru.mai.cipher.utils;

import java.math.BigInteger;
import java.util.Random;

/**
 * Класс для генерации случайных параметров RSA.
 */
public class RSARandomGenerator {

  private final Random random;

  /**
   * Конструктор без параметров.
   */
  public RSARandomGenerator() {
    random = new Random();
  }

  /**
   * Генерация случайного числа в заданных границах.
   *
   * @param lowerBound нижняя граница
   * @param upperBound верхняя граница
   * @return случайное число в заданных границах
   */
  public BigInteger generateInBounds(BigInteger lowerBound, BigInteger upperBound) {
    int randomNumBits = random.nextInt(upperBound.bitLength() - lowerBound.bitLength() + 1) + lowerBound.bitLength();
    BigInteger randomBigInteger;

    do {
      randomBigInteger = new BigInteger(randomNumBits, random);
    } while (randomBigInteger.compareTo(lowerBound) < 0 ||
        randomBigInteger.compareTo(upperBound) > 0);

    return randomBigInteger;
  }

  /**
   * Генерация простого числа заданной битовой длины, которое не должно
   * совпадать с заданных числом
   *
   * @param number число, с которым результат не должен совпадать
   * @param bitLength битовая длина числа
   * @return случайное число
   */
  public BigInteger generatePrime(BigInteger number, int bitLength) {
    BigInteger result;

    do {
      result = BigInteger.probablePrime(bitLength, random);
    } while (result.equals(number));

    return result;
  }

  /**
   * Генерация простого числа, взаимно простого с заданным параметром.
   *
   * @param number число, с которым должно быть взаимно просто
   * @return случайное простое число
   */
  public BigInteger generateRelativelyPrime(BigInteger number) {
    BigInteger relativelyPrime;

    do {
      relativelyPrime = generateInBounds(BigInteger.TWO, number.subtract(BigInteger.ONE));
    } while (!number.gcd(relativelyPrime).equals(BigInteger.ONE));

    return relativelyPrime;
  }
}
