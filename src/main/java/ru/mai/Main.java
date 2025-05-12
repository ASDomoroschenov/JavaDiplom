package ru.mai;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import ru.mai.cipher.rsa.RSA;
import ru.mai.cipher.rsa.impl.RSAImpl;
import ru.mai.cipher.rsa.impl.RSAImpl.PrivateKey;
import ru.mai.cipher.rsa.impl.RSAImpl.PublicKey;
import ru.mai.cipher.utils.RSAPolynomialFactory;
import ru.mai.cipher.utils.RSAUtils;
import ru.mai.factorization.RSALatticeFactorization;

public class Main {

  private static final Random rand = new Random();

  public static void main(String[] args) throws IOException, InterruptedException {
    Map<String, String> params = parseArgs(args);

    int n = Integer.parseInt(params.get("n"));
    int m = Integer.parseInt(params.get("m"));
    int t = Integer.parseInt(params.get("t"));
    BigInteger e = new BigInteger(params.get("e"));
    BigInteger N = new BigInteger(params.get("N"));
    BigInteger d0 = new BigInteger(params.get("d0"));
    BigInteger M = new BigInteger(params.get("M"));
    BigDecimal delta = new BigDecimal(params.get("delta"));

    RSALatticeFactorization.factorization(n, m, t, N, e, d0, M, delta);
  }

  private static Map<String, String> parseArgs(String[] args) {
    Map<String, String> map = new HashMap<>();

    for (String arg : args) {
      if (arg.startsWith("--")) {
        String[] parts = arg.substring(2).split("=", 2);
        if (parts.length == 2) {
          map.put(parts[0], parts[1]);
        }
      }
    }

    return map;
  }

  private static void test1() {
    int degree = 3;
    BigInteger p = BigInteger.valueOf(7);
    BigInteger q = BigInteger.valueOf(13);
    BigInteger e = BigInteger.valueOf(5);
    BigInteger N = p.multiply(q);
    BigInteger a = RSAPolynomialFactory.generateIrreducible(p, q, N, degree);
    BigInteger phi = RSAUtils.phi(p, q, degree);
    BigInteger d = e.modInverse(phi);

    PublicKey publicKey = new PublicKey(N, degree, a, e);
    PrivateKey privateKey = new PrivateKey(p, q, degree, a, d);
    RSA rsa = new RSAImpl(publicKey, privateKey);

    testRsa(rsa);
  }

  private static void test2() {
    RSA rsa = new RSAImpl(3, 5);
    testRsa(rsa);
  }

  private static void test3() {
    RSA rsa = RSAImpl.Default();
    testRsa(rsa);
  }

  private static void testRsa(RSA rsa) {
    for (int i = 0; i < 100; i++) {
      BigInteger[] text = {BigInteger.valueOf(rand.nextInt(70)), BigInteger.valueOf(rand.nextInt(70)), BigInteger.ONE};

      BigInteger[] encrypt = rsa.encrypt(text);
      BigInteger[] decrypt = rsa.decrypt(encrypt);

      if (!Arrays.equals(text, decrypt)) {
        System.out.println(Arrays.toString(text));
        System.out.println(Arrays.toString(decrypt));
      }
    }
  }
}