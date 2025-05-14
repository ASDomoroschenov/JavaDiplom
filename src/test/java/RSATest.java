import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import ru.mai.cipher.rsa.RSA;
import ru.mai.cipher.rsa.impl.RSAImpl;
import ru.mai.cipher.rsa.impl.RSAImpl.PrivateKey;
import ru.mai.cipher.rsa.impl.RSAImpl.PublicKey;
import ru.mai.cipher.utils.RSAPolynomialFactory;
import ru.mai.cipher.utils.RSAUtils;

public class RSATest {

  private static final Random rand = new Random();

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
