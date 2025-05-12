package ru.mai.cipher.utils;

import java.math.BigInteger;

public class RSAUtils {

  public static BigInteger phi(BigInteger p, BigInteger q, int n) {
    return (p.pow(n).subtract(BigInteger.ONE))
        .multiply(q.pow(n).subtract(BigInteger.ONE))
        .divide((p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE)));
  }
}
