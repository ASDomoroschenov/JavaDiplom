import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import ru.mai.cipher.utils.RSAUtils;
import ru.mai.factorization.RSALatticeFactorization;

public class FactorizationTest {

  /**
   * Воспроизведение примера из статьи.
   *
   * @throws IOException исключение при работе с файлами
   * @throws InterruptedException исключение при запуске процесса
   */
  public static void test1() throws IOException, InterruptedException {
    BigInteger x0 = new BigInteger("1616573608644257585");
    BigInteger y0 = new BigInteger("1360935721901674");
    BigInteger z0 = new BigInteger("40748185648950035910680304028872647558518309799826755032040");

    BigInteger N = new BigInteger("463028995904606051817018641173");
    BigInteger c = new BigInteger("89508787964537769839958980218674109695435455229928587492654228177046463498977617360027022");
    BigInteger e = new BigInteger("17245940996311682203024873234841963839090492688579713115090719406582906246851863033916922");

    int n = 4;
    int m = 4;
    int t = 2;

    BigInteger X = new BigInteger("680462339813605");
    BigInteger Y = new BigInteger("2041387019440815");
    BigInteger Z = new BigInteger("5788687978307547385658367719917397827407255326981854011616875");

    RSALatticeFactorization.factorization(
        N,
        c,
        e,
        n,
        m,
        t,
        X,
        Y,
        Z
    );
  }

  /**
   * Адаптация примера из статьи под реальные условия RSA.
   *
   * @throws IOException исключение при работе с файлами
   * @throws InterruptedException исключение при запуске процесса
   */
  public static void test2() throws IOException, InterruptedException {
    BigInteger x0 = new BigInteger("60864");
    BigInteger y0 = new BigInteger("1360935721901674");
    BigInteger z0 = new BigInteger("153416945486038003758230020755607195112116718900736");

    int n = 4;
    int m = 4;
    int t = 2;
    BigInteger p = new BigInteger("683209007134751");
    BigInteger q = new BigInteger("677726714766923");
    BigInteger e = new BigInteger("65537");
    BigInteger N = p.multiply(q);
    BigInteger phi = RSAUtils.phi(p, q, n);
    BigInteger d = e.modInverse(phi);
    BigInteger d1 = d.shiftRight(210);
    BigInteger d0 = d.subtract(d1.multiply(BigInteger.TWO.pow(210)));
    BigInteger M = BigInteger.TWO.pow(210);
    BigDecimal delta = new BigDecimal("3");

    RSALatticeFactorization.factorization(
        n,
        m,
        t,
        N,
        e,
        d0,
        M,
        delta
    );
  }
}
