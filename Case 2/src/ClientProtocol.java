import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.security.cert.CertificateEncodingException;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Represents the client protocol
 */
public class ClientProtocol {

	private final static String ALGORITHM = "RSA";

	public static void process(BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut) throws
	IOException, CertificateEncodingException, NoSuchAlgorithmException, OperatorCreationException, CertificateException {

		Security.addProvider(new BouncyCastleProvider());

		String fromServer = "";
		// reads the input
		System.out.print("Message to send: ");
		String fromUser = stdIn.readLine();

		// send through the network
		pOut.println(fromUser);			

		// read what arrives from the network
		// if the server response isn't null, display response
		if((fromServer = pIn.readLine()) != null) {
			System.out.println("Response from Server: " +  fromServer);
			// reads the input
			System.out.print("Message to send: ");
			fromUser = stdIn.readLine();

			// send through the network
			pOut.println(fromUser);			

			// read what arrives from the network
			// if the server response isn't null, display response
			if((fromServer = pIn.readLine()) != null) {
				System.out.println("Response from Server: " +  fromServer);
				if (fromServer.equals("OK")) {

					// generates the asymmetric keys
					KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
					generator.initialize(1024);
					KeyPair keyPair = generator.generateKeyPair();

					// generates the certificate
					java.security.cert.X509Certificate cert = generateCertificate(keyPair);
					byte[] certBytes = cert.getEncoded();
					String certString = DatatypeConverter.printBase64Binary(certBytes);
					fromUser = certString;
					System.out.println("Message to send: " + fromUser);

					// send through the network
					pOut.println(fromUser);
					if ((fromServer = pIn.readLine()) != null) {
						System.out.println("Response from Server: " +  fromServer);
						fromServer = pIn.readLine();
						System.out.println("Response from Server: " +  fromServer);
					}
					System.out.println("I've finished for the moment :)");
				}
			}
		}

	}

	private static java.security.cert.X509Certificate generateCertificate(KeyPair keyPair) throws OperatorCreationException, CertificateException {

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.add(Calendar.YEAR, 10);
		X509v3CertificateBuilder x509v3CertificateBuilder = new X509v3CertificateBuilder(
				new X500Name("CN=localhost"),
				BigInteger.valueOf(1),
				Calendar.getInstance().getTime(),
				endCalendar.getTime(),
				new X500Name("CN=localhost"),
				SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded()));
		ContentSigner contentSigner = new JcaContentSignerBuilder("SHA1withRSA").build(keyPair.getPrivate());
		X509CertificateHolder x509CertificateHolder = x509v3CertificateBuilder.build(contentSigner);
		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(x509CertificateHolder);

	}
}