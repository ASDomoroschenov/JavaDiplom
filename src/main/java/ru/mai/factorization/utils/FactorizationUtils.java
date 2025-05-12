package ru.mai.factorization.utils;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class FactorizationUtils {

  public static final MathContext MATH_CONTEXT = new MathContext(100);

  public static BigDecimal log(BigDecimal base, BigDecimal number) {
    BigDecimal logBase = BigDecimalMath.log(base, MATH_CONTEXT);
    BigDecimal logNumber = BigDecimalMath.log(number, MATH_CONTEXT);

    return logNumber.divide(logBase, MATH_CONTEXT);
  }

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
                BigDecimal.valueOf(6).multiply(BigDecimal.valueOf(n - 1)).multiply(alpha.add(mu)).add(
                    BigDecimal.valueOf(n - 1).multiply(BigDecimal.valueOf(n - 1))
                ).sqrt(MATH_CONTEXT)
            )
        )
    ) < 0;
  }

  public static BigInteger scale(BigInteger base, BigDecimal scale) {
    return BigDecimalMath.pow(new BigDecimal(base), scale, MATH_CONTEXT).toBigInteger();
  }
}
