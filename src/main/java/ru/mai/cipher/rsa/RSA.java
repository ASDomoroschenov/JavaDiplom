package ru.mai.cipher.rsa;

import java.math.BigInteger;

/**
 * Интерфейс для шифрования RSA.
 */
public interface RSA {

  /**
   * Шифрование многочлена.
   *
   * @param text многочлен
   * @return зашифрованный многочлен
   */
  BigInteger[] encrypt(BigInteger[] text);

  /**
   * Дешифрование многочлена.
   *
   * @param text многочлен
   * @return дешифрованный многочлен
   */
  BigInteger[] decrypt(BigInteger[] text);
}
