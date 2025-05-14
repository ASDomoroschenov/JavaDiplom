package ru.mai;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import ru.mai.factorization.RSALatticeFactorization;
import ru.mai.factorization.dividers.Dividers;

public class Main {

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

    System.out.println("Запущена факторизация со следующими параметрами:");
    System.out.println("Параметры редукции решетки: m=" + m + ", t=" + t);
    System.out.println("Открытая экспонента: e=" + e);
    System.out.println("Модуль RSA: N=" + N);
    System.out.println("Известные младшие биты: d0=" + d0);
    System.out.println("Количество известных младших бит: M=2^s=" + M);
    System.out.println("Приближение d относительно модуля RSA N: delta=" + delta);

    long begin = System.currentTimeMillis();
    Dividers result = RSALatticeFactorization.factorization(n, m, t, N, e, d0, M, delta);
    long end = System.currentTimeMillis();

    System.out.println("Факторизация выполнилась за: " + (((double) (end - begin)) / 1000) + "с");

    if (result == null) {
      System.out.println("Факторизация невозможна, не выполнены условия");
    } else {
      System.out.println("Результаты факторизации: {p=" + result.p() + ", q=" + result.q() + "}");
    }
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
}