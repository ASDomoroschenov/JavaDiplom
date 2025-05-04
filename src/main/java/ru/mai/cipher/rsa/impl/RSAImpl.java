package ru.mai.cipher.rsa.impl;

import java.math.BigInteger;
import java.util.Random;
import ru.mai.cipher.polynomial.Polynomial;
import ru.mai.cipher.polynomial.PolynomialAddition;
import ru.mai.cipher.polynomial.PolynomialMultiplication;
import ru.mai.cipher.polynomial.impl.RSAPolynomial;
import ru.mai.cipher.polynomial.impl.RSAPolynomialAddition;
import ru.mai.cipher.polynomial.impl.RSADecipherMultiplication;
import ru.mai.cipher.polynomial.impl.RSACipherMultiplication;
import ru.mai.cipher.rsa.RSA;
import ru.mai.cipher.utils.RSAPolynomialFactory;
import ru.mai.cipher.utils.RSARandomGenerator;

/**
 * Реализация RSA Котана и Тэшелеану.
 */
public class RSAImpl implements RSA {

  private final PublicKey publicKey;
  private final PrivateKey privateKey;
  private final PolynomialAddition rsaAddition;
  private final PolynomialMultiplication rsaCipherMultiplication;
  private final PolynomialMultiplication rsaDecipherMultiplication;

  /**
   * Метод для вызова дефолтного конструктора как статического метода.
   *
   * @return Объект RSAImpl
   */
  public static RSAImpl Default() {
    return new RSAImpl();
  }

  /**
   * Дефолтный конструктор.
   */
  public RSAImpl() {
    RSAPair keys = RSAKeyGenerator.generate(3, 5);
    this.publicKey = keys.publicKey;
    this.privateKey = keys.privateKey;
    this.rsaAddition = new RSAPolynomialAddition(
        publicKey.degree(),
        publicKey.N()
    );
    this.rsaCipherMultiplication = new RSACipherMultiplication(
        publicKey.N(),
        publicKey.a(),
        publicKey.degree()
    );
    this.rsaDecipherMultiplication = new RSADecipherMultiplication(
        privateKey.p(),
        privateKey.q(),
        privateKey.a(),
        privateKey.degree()
    );
  }

  /**
   * Конструктор.
   *
   * @param n максимальная степень многочленов (параметр RSA)
   * @param bitLength битовая длина p и q
   */
  public RSAImpl(int n, int bitLength) {
    RSAPair keys = RSAKeyGenerator.generate(n, bitLength);
    this.publicKey = keys.publicKey;
    this.privateKey = keys.privateKey;
    this.rsaAddition = new RSAPolynomialAddition(
        publicKey.degree(),
        publicKey.N()
    );
    this.rsaCipherMultiplication = new RSACipherMultiplication(
        publicKey.N(),
        publicKey.a(),
        publicKey.degree()
    );
    this.rsaDecipherMultiplication = new RSADecipherMultiplication(
        privateKey.p(),
        privateKey.q(),
        privateKey.a(),
        privateKey.degree()
    );
  }

  /**
   * Конструктор.
   *
   * @param publicKey публичный ключ
   * @param privateKey приватный ключ
   */
  public RSAImpl(PublicKey publicKey, PrivateKey privateKey) {
    this.publicKey = publicKey;
    this.privateKey = privateKey;
    this.rsaAddition = new RSAPolynomialAddition(
        publicKey.degree(),
        publicKey.N()
    );
    this.rsaCipherMultiplication = new RSACipherMultiplication(
        publicKey.N(),
        publicKey.a(),
        publicKey.degree()
    );
    this.rsaDecipherMultiplication = new RSADecipherMultiplication(
        privateKey.p(),
        privateKey.q(),
        privateKey.a(),
        privateKey.degree()
    );
  }

  /**
   * Шифрование многочлена.
   *
   * @param text многочлен
   * @return зашифрованный многочлен
   */
  @Override
  public BigInteger[] encrypt(BigInteger[] text) {
    Polynomial polynomial = new RSAPolynomial(
        publicKey.degree(),
        text,
        rsaAddition,
        rsaCipherMultiplication
    );
    return polynomial.pow(publicKey.e()).getCoefficients();
  }

  /**
   * Дешифрование многочлена.
   *
   * @param text многочлен
   * @return дешифрованный многочлен
   */
  @Override
  public BigInteger[] decrypt(BigInteger[] text) {
    Polynomial polynomial = new RSAPolynomial(
        publicKey.degree(),
        text,
        rsaAddition,
        rsaDecipherMultiplication
    );
    return polynomial.pow(privateKey.d()).getCoefficients();
  }

  /**
   * Класс для генерации ключей RSA.
   */
  public static class RSAKeyGenerator {

    private static final RSARandomGenerator random = new RSARandomGenerator();

    /**
     * Генерация ключей RSA.
     *
     * @param n      максимальная степень многочленов и параметр функции phi
     * @param length длина битов p и q
     * @return сгенерированная пара ключей - публичный и приватный
     */
    static RSAPair generate(int n, int length) {
      BigInteger a;
      BigInteger p;
      BigInteger q;
      BigInteger N;
      BigInteger phi;

      do {
        p = BigInteger.probablePrime(length, new Random());
        q = random.generatePrime(p, length);
        N = p.multiply(q);
        phi = phi(p, q, n);
        a = RSAPolynomialFactory.generateIrreducible(p, q, N, n);
      } while (a == null);

      BigInteger e = random.generateRelativelyPrime(phi);
      BigInteger d = e.modInverse(phi);

      return new RSAPair(
          new PublicKey(p.multiply(q), n, a, e),
          new PrivateKey(p, q, n, a, d)
      );
    }

    /**
     * Вычисление функции phi.
     *
     * @param p параметр RSA
     * @param q параметр RSA
     * @param n параметр RSA
     * @return значение функции phi
     */
    private static BigInteger phi(BigInteger p, BigInteger q, int n) {
      return (p.pow(n).subtract(BigInteger.ONE))
          .multiply(q.pow(n).subtract(BigInteger.ONE))
          .divide((p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE)));
    }
  }

  /**
   * Приватный ключ.
   *
   * @param a свободный член неприводимого многочлена
   * @param d приватная константа
   */
  public record PrivateKey(BigInteger p, BigInteger q, Integer degree, BigInteger a, BigInteger d) {

  }

  /**
   * Публичный ключ.
   *
   * @param N параметр RSA
   * @param degree максимальная степень многочленов
   * @param a свободный член неприводимого многочлена
   * @param e параметр RSA
   */
  public record PublicKey(BigInteger N, Integer degree, BigInteger a, BigInteger e) {

  }

  /**
   * Пара ключей RSA.
   *
   * @param publicKey публичный ключ
   * @param privateKey приватный ключ
   */
  record RSAPair(PublicKey publicKey, PrivateKey privateKey) {

  }
}
