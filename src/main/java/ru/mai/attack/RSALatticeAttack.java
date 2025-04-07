package ru.mai.attack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Класс для атаки на RSA.
 */
public class RSALatticeAttack {

  /**
   * Генерация полиномов G(x,y,z).
   *
   * @param H полином H(y)
   * @param c свободный член уравнения f(x,y,z) = xH(y) + c
   * @param e модуль уравнения f(x,y,z) = xH(y) + c = 0 (mod e)
   * @param m параметр решетки
   * @param t параметр решетки
   * @param r параметр решетки
   * @return сгенерированные полиномы G(x,y,z)
   */
  private static Map<IndexTriple, MultivariatePolynomial> generateG(BigInteger[] H, BigInteger c,
      BigInteger e, int m, int t, int r) {
    Map<IndexTriple, MultivariatePolynomial> result = new HashMap<>();
    MultivariatePolynomial F = Polynomial.buildF(H, c);

    for (int k = 0; k <= m; k++) {
      BigInteger scale = e.pow(m - k);
      MultivariatePolynomial Fk = F.pow(k).multiply(scale);

      for (int j = 0; j <= t; j++) {
        result.put(new IndexTriple(k, 0, j), Polynomial.reduceXYrToZ(Fk.multiplyMonomial(0, j, 0), r));
      }

      for (int i = 1; i <= m - k; i++) {
        for (int j = 0; j <= r - 1; j++) {
          result.put(new IndexTriple(k, i, j), Polynomial.reduceXYrToZ(Fk.multiplyMonomial(i, j, 0), r));
        }
      }
    }

    return result.entrySet()
        .stream()
        .sorted(new IndexTripleEntryComparator())
        .collect(Collectors.toMap(
            Entry::getKey,
            Entry::getValue,
            (a, b) -> a,
            LinkedHashMap::new
        ));
  }

  /**
   * Перевод полиномов в матрицу, включая операцию масштабирования.
   *
   * @param Gs сгенерированные полиномы G(x,y,z)
   * @param basis базис решетки
   * @param X масштабирование по X
   * @param Y масштабирование по Y
   * @param Z масштабирование по Z
   * @return матрица, готовая для LLL
   */
  private static BigInteger[][] polynomialsToMatrix(
      Map<IndexTriple, MultivariatePolynomial> Gs,
      List<Monomial> basis,
      BigInteger X,
      BigInteger Y,
      BigInteger Z) {
    BigInteger[][] matrix = new BigInteger[Gs.size()][basis.size()];
    int i = 0;
    int j = 0;

    for (Entry<IndexTriple, MultivariatePolynomial> entry : Gs.entrySet()) {
      for (Monomial monomial : basis) {
        BigInteger coeff = entry.getValue().terms.getOrDefault(monomial, BigInteger.ZERO);
        matrix[i][j] = coeff
            .multiply(X.pow(monomial.x))
            .multiply(Y.pow(monomial.y))
            .multiply(Z.pow(monomial.z));
        j++;
      }

      i++;
      j = 0;
    }

    return matrix;
  }

  /**
   * Запись матрицы во входной файл для LLL.
   *
   * @param matrix матрица
   * @param file входной файл
   * @throws IOException исключение при работе с файлом
   */
  private static void writeMatrixForFplll(BigInteger[][] matrix, File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(file)) {
      writer.println("[");

      for (int i = 0; i < matrix.length; i++) {
        String row = Arrays.stream(matrix[i])
            .map(BigInteger::toString)
            .collect(Collectors.joining(" "));
        writer.print("[" + row + "]");
        writer.println(i < matrix.length - 1 ? "" : "]]");
      }
    }
  }

  /**
   * Запуск LLL.
   *
   * @param inputFile файл с входными данными
   * @param outputFile файл для вывода
   * @throws IOException исключение при работе с файлом
   * @throws InterruptedException исключение при работе с утилитой fplll
   */
  private static void runFplll(File inputFile, File outputFile) throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder(
        "fplll",
        inputFile.getAbsolutePath(),
        "-a",
        "lll"
    );

    pb.redirectOutput(outputFile);
    pb.redirectError(ProcessBuilder.Redirect.INHERIT);

    Process process = pb.start();
    int exitCode = process.waitFor();

    if (exitCode != 0) {
      throw new RuntimeException("fplll завершился с ошибкой, код: " + exitCode);
    }
  }

  /**
   * Чтение файла-результата применения LLL.
   *
   * @param file файл с результатами LLL
   * @return список полиномов
   * @throws IOException исключение при работе с файлами
   */
  private static List<BigInteger[]> readFplllOutput(File file) throws IOException {
    List<BigInteger[]> rows = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;

      while ((line = reader.readLine()) != null) {
        line = line.trim();

        if (line.startsWith("[[")) {
          line = line.substring(2).trim();
        } else if (line.startsWith("[")) {
          line = line.substring(1).trim();
        }

        if (line.endsWith("]]")) {
          line = line.substring(0, line.length() - 2).trim();
        } else if (line.endsWith("]")) {
          line = line.substring(0, line.length() - 1).trim();
        }

        if (line.isEmpty()) {
          continue;
        }

        String[] tokens = line.split("\\s+");
        BigInteger[] row = Arrays.stream(tokens)
            .map(BigInteger::new)
            .toArray(BigInteger[]::new);
        rows.add(row);
      }
    }
    return rows;
  }

  /**
   * Вычисление Евклидовой нормы.
   *
   * @param poly полиномы
   * @return Евклидова норма
   */
  private static BigInteger computeEuclideanNormSquared(MultivariatePolynomial poly) {
    BigInteger normSquared = BigInteger.ZERO;

    for (BigInteger coeff : poly.getTerms().values()) {
      normSquared = normSquared.add(coeff.pow(2));
    }

    return normSquared.sqrt();
  }

  /**
   * Экспортирование полиномов в файл sage для решения методом Гребнера.
   *
   * @param polys полиномы
   * @param file файл для вывода
   * @throws IOException исключение при работе с файлом
   */
  private static void exportPolynomialsToSage(List<MultivariatePolynomial> polys, File file)
      throws IOException {
    try (PrintWriter writer = new PrintWriter(file)) {
      writer.println("R.<x, y, z> = PolynomialRing(QQ, 3)");
      writer.println();

      List<MultivariatePolynomial> sorted = new ArrayList<>(polys);
      sorted.sort(Comparator.comparing(RSALatticeAttack::computeEuclideanNormSquared));

      for (int i = 0; i < Math.min(45, sorted.size()); i++) {
        MultivariatePolynomial poly = sorted.get(i);
        StringBuilder sb = new StringBuilder("f" + (i + 1) + " = ");
        List<String> terms = getTerms(poly);
        sb.append(String.join(" + ", terms));
        writer.println(sb);
      }

      writer.println();
      writer.println("I = ideal(f1, f2, f3)");
      writer.println("print('Groebner basis:')");
      writer.println("print(I.groebner_basis())");
      writer.println("print('Solutions:')");
      writer.println("print(I.variety())");
    }
  }

  /**
   * Вывод полинома для файла sage.
   *
   * @param poly полином
   * @return список мономов
   */
  private static List<String> getTerms(MultivariatePolynomial poly) {
    List<String> terms = new ArrayList<>();

    for (var entry : poly.terms.entrySet()) {
      BigInteger coeff = entry.getValue();
      Monomial m = entry.getKey();

      if (coeff.equals(BigInteger.ZERO)) {
        continue;
      }

      String term = coeff.toString();
      if (m.x > 0) {
        term += "*x" + (m.x > 1 ? "^" + m.x : "");
      }
      if (m.y > 0) {
        term += "*y" + (m.y > 1 ? "^" + m.y : "");
      }
      if (m.z > 0) {
        term += "*z" + (m.z > 1 ? "^" + m.z : "");
      }

      terms.add(term);
    }

    if (terms.isEmpty()) {
      terms.add("0");
    }

    return terms;
  }

  /**
   * Генерация базиса для решетки.
   *
   * @param Gs сгенерированные полиномы G(Xx, Yy, Zz)
   * @return базис решетки
   */
  private static List<Monomial> generateCustomMonomialBasis(
      Map<IndexTriple, MultivariatePolynomial> Gs) {
    return Gs.values().stream()
        .flatMap(p -> p.getTerms().keySet().stream())
        .distinct()
        .sorted(new MonomialComparator())
        .toList();
  }

  /**
   * Перевод векторов в полиномы с размасштабированием.
   *
   * @param vector вектор
   * @param basis базис
   * @param X масштаб по X
   * @param Y масштаб по Y
   * @param Z масштаб по Z
   * @return полином
   */
  public static MultivariatePolynomial vectorToPolynomialUnscale(
      BigInteger[] vector,
      List<Monomial> basis,
      BigInteger X,
      BigInteger Y,
      BigInteger Z) {
    MultivariatePolynomial poly = new MultivariatePolynomial();

    for (int i = 0; i < vector.length; i++) {
      BigInteger rawCoeff = vector[i];

      if (rawCoeff.equals(BigInteger.ZERO)) {
        continue;
      }

      Monomial m = basis.get(i);
      BigInteger scale = X.pow(m.x).multiply(Y.pow(m.y)).multiply(Z.pow(m.z));

      BigInteger coeff = rawCoeff.divide(scale);

      poly.addTerm(coeff, m);
    }

    return poly;
  }

  /**
   * Запускает SageMath на скрипте (файле .sage) и возвращает строки вывода.
   *
   * @param sageFile файл скрипта Sage, который нужно запустить
   * @return список строк, которые скрипт Sage напечатал в stdout
   * @throws IOException          при ошибках ввода-вывода
   * @throws InterruptedException при сбоях выполнения процесса
   */
  private static List<String> runSage(File sageFile) throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder("sage", sageFile.getAbsolutePath());
    pb.redirectError(ProcessBuilder.Redirect.INHERIT);

    Process process = pb.start();

    List<String> outputLines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;

      while ((line = reader.readLine()) != null) {
        outputLines.add(line);
      }
    }

    int exitCode = process.waitFor();
    if (exitCode != 0) {
      throw new RuntimeException("Sage завершился с ошибкой, код: " + exitCode);
    }

    return outputLines;
  }

  /**
   * Атака на RSA.
   *
   * @param N модуль RSA
   * @param c свободный член уравнения f(x,y) = xH(y) + c
   * @param e модуль уравнения f(x,y) = xH(y) + c = 0 (mod e)
   * @param n произвольно выбранная константа (размер полиномов) из публичного ключа
   * @param m параметр решетки. максимальная степень базового многочлена F(x, y, z)
   * @param t параметр решетки. максимальная степень дополнительной переменной y
   * @param X масштабирование по X. определяется экспериментально.
   * @param Y масштабирование по Y. определяется экспериментально.
   * @param Z масштабирование по Z. определяется экспериментально.
   * @throws IOException исключение при работе с файлами
   * @throws InterruptedException исключение при работе с утилитой fplll
   */
  public static void attack(
      BigInteger N,
      BigInteger c,
      BigInteger e,
      int n,
      int m,
      int t,
      BigInteger X,
      BigInteger Y,
      BigInteger Z) throws IOException, InterruptedException {
    int r = n - 1;
    BigInteger[] H = Polynomial.phi(n, N);

    Map<IndexTriple, MultivariatePolynomial> Gs = generateG(H, c, e, m, t, r);
    List<Monomial> basis = generateCustomMonomialBasis(Gs);
    BigInteger[][] matrix = polynomialsToMatrix(Gs, basis, X, Y, Z);

    File latticeFile = new File("lattice.input");
    File reducedFile = new File("reduced.output");

    writeMatrixForFplll(matrix, latticeFile);
    runFplll(latticeFile, reducedFile);

    List<BigInteger[]> vectors = readFplllOutput(reducedFile);
    List<MultivariatePolynomial> polys = new ArrayList<>();

    for (BigInteger[] vector : vectors) {
      polys.add(vectorToPolynomialUnscale(vector, basis, X, Y, Z));
    }

    exportPolynomialsToSage(polys, new File("solve.sage"));

    File sageScript = new File("solve.sage");
    List<String> sageOutput = runSage(sageScript);

    for (String line : sageOutput) {
      System.out.println(line);
    }
  }

  /**
   * Проверка полиномов после выполнения LLL. Исключительно для числового примера
   * из статьи.
   *
   * @param polys полиномы после алгоритма LLL
   */
  private static void check(List<MultivariatePolynomial> polys) {
    BigInteger x0 = new BigInteger("16165734257585");
    BigInteger y0 = new BigInteger("1360935721901674");
    BigInteger z0 = new BigInteger("40748185648950035910680304028872647558518309799826755032040");

    for (MultivariatePolynomial poly : polys) {
      Map<Monomial, BigInteger> terms = poly.getTerms();
      BigInteger val = BigInteger.ZERO;

      for (Entry<Monomial, BigInteger> entry : terms.entrySet()) {
        BigInteger coeff = entry.getValue();
        Monomial monomial = entry.getKey();

        val = val.add(
            coeff.multiply(x0.pow(monomial.x))
                .multiply(y0.pow(monomial.y))
                .multiply(z0.pow(monomial.z))
        );
      }

      System.out.println(val);
    }
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    BigInteger N = new BigInteger("463028995904606051817018641173");
    BigInteger c = new BigInteger("89508787964537769839958980218674109695435455229928587492654228177046463498977617360027022");
    BigInteger e = new BigInteger("17245940996311682203024873234841963839090492688579713115090719406582906246851863033916922");

    int n = 4;
    int m = 4;
    int t = 2;

    BigInteger X = new BigInteger("680462339813605");
    BigInteger Y = new BigInteger("2041387019440815");
    BigInteger Z = new BigInteger("5788687978307547385658367719917397827407255326981854011616875");

    attack(N, c, e, n, m, t, X, Y, Z);
  }
}