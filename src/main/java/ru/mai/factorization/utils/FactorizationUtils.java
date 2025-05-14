package ru.mai.factorization.utils;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * Утилитарный класс для факторизации.
 */
public class FactorizationUtils {

  public static final MathContext MATH_CONTEXT = new MathContext(100);

  /**
   * Взятие логарифма по основанию.
   *
   * @param base   основание логарифма
   * @param number логарифмируемое число
   * @return результат логарифма по основанию
   */
  public static BigDecimal log(BigDecimal base, BigDecimal number) {
    BigDecimal logBase = BigDecimalMath.log(base, MATH_CONTEXT);
    BigDecimal logNumber = BigDecimalMath.log(number, MATH_CONTEXT);
    return logNumber.divide(logBase, MATH_CONTEXT);
  }

  /**
   * Проверка возможности факторизации.
   *
   * @param n     параметр RSA
   * @param alpha приближение e относительно N
   * @param mu    приближение M относительно N
   * @param delta приближение d относительно N
   * @return true/false - можно или нельзя факторизовать
   */
  public static boolean canFactorize(
      int n,
      BigDecimal alpha,
      BigDecimal mu,
      BigDecimal delta) {
    return delta.compareTo(
        mu.add(
            BigDecimal.valueOf(7.0 / 6.0).multiply(BigDecimal.valueOf(n).subtract(BigDecimal.ONE))
        ).subtract(
            BigDecimal.valueOf(1.0 / 3.0).multiply(
                BigDecimal.valueOf(6).multiply(BigDecimal.valueOf(n - 1)).multiply(alpha.add(mu))
                    .add(
                        BigDecimal.valueOf(n - 1).multiply(BigDecimal.valueOf(n - 1))
                    ).sqrt(MATH_CONTEXT)
            )
        )
    ) < 0;
  }

  /**
   * Возведение числа в рациональную степень.
   *
   * @param base  основание степени
   * @param scale степень
   * @return результат возведения в степень рационального числа
   */
  public static BigInteger scale(BigInteger base, BigDecimal scale) {
    return BigDecimalMath.pow(new BigDecimal(base), scale, MATH_CONTEXT).toBigInteger();
  }
}
