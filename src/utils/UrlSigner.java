package utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64; // JDK 1.8 only - older versions may need to use Apache Commons or similar.

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class UrlSigner {
	private static final String GOOGLE_MAP_KEY = "AIzaSyBoJnKpeNZTVek-fJ1BGNF2-P4IgQcD6t8";

	private static final String PRIVATE_KEY = "oKoBJtk6KSyMqN0xi-Z4olxRQ88=";

	private static byte[] key;

	public static String sign(String urlString) {
		try {
			URL url = new URL(urlString + "&key=" + GOOGLE_MAP_KEY);
			urlSigner(PRIVATE_KEY);
			String request = signRequest(url.getPath(), url.getQuery());
			System.out.println("Signed URL :" + url.getProtocol() + "://" + url.getHost() + request);
			return url.getProtocol()+"://"+url.getHost()+request;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static void urlSigner(String keyString) throws IOException {
		// Convert the key from 'web safe' base 64 to binary
		keyString = keyString.replace('-', '+');
		keyString = keyString.replace('_', '/');
		System.out.println("Key: " + keyString);
		// Base64 is JDK 1.8 only - older versions may need to use Apache Commons or
		// similar.
		key = Base64.getDecoder().decode(keyString);
	}

	private static String signRequest(String path, String query)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, URISyntaxException {

		// Retrieve the proper URL components to sign
		String resource = path + '?' + query;

		// Get an HMAC-SHA1 signing key from the raw key bytes
		SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

		// Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(sha1Key);

		// compute the binary signature for the request
		byte[] sigBytes = mac.doFinal(resource.getBytes());

		// base 64 encode the binary signature
		// Base64 is JDK 1.8 only - older versions may need to use Apache Commons or
		// similar.
		String signature = Base64.getEncoder().encodeToString(sigBytes);

		// convert the signature to 'web safe' base 64
		signature = signature.replace('+', '-');
		signature = signature.replace('/', '_');

		return resource + "&signature=" + signature;
	}

}
