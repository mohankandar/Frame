package com.wynd.vop.framework.security.jks;

import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.shared.sanitize.Sanitizer;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for creating KeyStore objects from PEM format certificates.
 * 
 * @author jluck
 *
 */
public class KeystoreUtils {
    private static final BipLogger LOGGER = BipLoggerFactory.getLogger(KeystoreUtils.class);

	/** PEM Certificate Header */
    private static final String PEM_CERTIFICATE_PREFIX = "-----BEGIN CERTIFICATE-----";
    /** PEM Certificate Footer */
    private static final String PEM_CERTIFICATE_SUFFIX = "-----END CERTIFICATE-----";
    /** PEM Private Key Header */
    private static final String PRIVATE_KEY_PREFIX = "-----BEGIN PRIVATE KEY-----";
    /** PEM Private Key Footer */
    private static final String PRIVATE_KEY_SUFFIX = "-----END PRIVATE KEY-----";
    
    /** System TrustStore Property */
    private static final String SYS_TRUSTSTORE = "javax.net.ssl.trustStore";
    /** System TrustStore Password Property */
    private static final String SYS_TRUSTSTOREPASS = "javax.net.ssl.trustStorePassword";
    /** System TrustStore Type */
    private static final String SYS_TRUSTSTORETYPE = "javax.net.ssl.trustStoreType";
    
    /** Default System TrustStore */
    private static final String SYS_TRUSTSTORE_DEFAULT = System.getProperty("java.home") + "/lib/security/cacerts";
    /** Default System TrustStore Password */
    private static final String SYS_TRUSTSTOREPASS_DEFAULT = "changeit";
    
    /**
	 * Private constructor to prevent instantiation.
	 */
	private KeystoreUtils() {
    }
    
	
    /**
     * Create a KeyStore from the given private/public key pair.
     * @param publicCert provided in PEM format
     * @param privateKey provided in PEM format
     * @param privateKeyPassword used to protect the given private key
     * @param alias used to name the entry in the KeyStore
     * @return KeyStore object containing the give private/public key pair stored under the given alias
     * @throws KeyStoreException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws InvalidKeySpecException
     */
    public static KeyStore createKeyStore(String publicCert, String privateKey, String privateKeyPassword, String alias) throws 
    			KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException {
        //Create Certificate from PEM format string
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        Certificate publicX509 = parseCertificateString(publicCert, certFactory);
        Certificate[] certChain = new Certificate[1];
        certChain[0] = publicX509;

        //Create PrivateKey from PEM format string
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateX509 = parsePrivateKeyString(privateKey, keyFactory);


        KeyStore clientStore = KeyStore.getInstance(KeyStore.getDefaultType());
        clientStore.load(null, null);
        if (privateKeyPassword != null) {
            clientStore.setKeyEntry(alias, privateX509, privateKeyPassword.toCharArray(), certChain);
        } else {
            clientStore.setKeyEntry(alias, privateX509, "".toCharArray(), certChain);
        }
        
        return clientStore;

    }
    
    /**
     * Create a KeyStore for trusted certificates based on the system truststore. The given certificate is added to the keystore as a trusted entry.
     *
     * @param alias for the keystore entry
     * @param certificate to add to the keystore
     * @return the key store
     * @throws KeyStoreException the key store exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws CertificateException the certificate exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static KeyStore createTrustStore(String alias, String certificate) throws 
    			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
    	
    		return createTrustStore(alias, certificate, true);
    }
    
    /**
     * Create a KeyStore for trusted certificates. The given certificate is added to the keystore as a trusted entry.
     *
     * @param alias for the keystore entry
     * @param certificate to add to the keystore
     * @param loadSystemTrustStore should the returned KeyStore contain certificates from the system truststore?
     * @return the key store
     * @throws KeyStoreException the key store exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws CertificateException the certificate exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static KeyStore createTrustStore(String alias, String certificate, boolean loadSystemTrustStore) throws 
    			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
    		
    		KeyStore trustStore = KeyStore.getInstance(System.getProperty(SYS_TRUSTSTORETYPE, KeyStore.getDefaultType()));
    		if (loadSystemTrustStore) {
    		    String trustStorePath = Sanitizer.safePath(System.getProperty(SYS_TRUSTSTORE, SYS_TRUSTSTORE_DEFAULT));
                LOGGER.debug("Creating truststore from {} to add alias {} to.", trustStorePath, alias);
    	 		try(InputStream inputstream = new FileInputStream(trustStorePath);) {
	    			trustStore.load(inputstream, System.getProperty(SYS_TRUSTSTOREPASS, SYS_TRUSTSTOREPASS_DEFAULT).toCharArray()); 
	    		}
    		} else {
                LOGGER.debug("Creating blank truststore to add alias {} to ", alias);
    			trustStore.load(null, null);
    		}

    		addTrustedCert(alias, certificate, trustStore);

    		return trustStore;
    }
    
    /**
     * Add the given certificate to the given KeyStore as a trusted entry.
     * @param alias for the keystore entry
     * @param certificate to add to the keystore
     * @param trustStore to add the certificate to
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws IOException
     */
    public static void addTrustedCert(String alias, String certificate, KeyStore trustStore) throws 
    			CertificateException, KeyStoreException, IOException {
        if (StringUtils.isNotBlank(certificate)) {
            LOGGER.debug("Adding certificate {} alias {}.", "..." + StringUtils.right(certificate, 100), alias);
            //Create Certificate from PEM format string
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            Certificate certX509 = parseCertificateString(certificate, certFactory);
            trustStore.setCertificateEntry(alias, certX509);
        } else {
            LOGGER.warn("No certificate provided for alias {}.", alias);
        }

    }
    

    /**
     * Convert a PEM format private key into a PrivateKey object
     * @param privateKey in PEM format
     * @param keyFactory defining the format of the PrivateKey to generate
     * @return  PrivateKey object
     * @throws InvalidKeySpecException
     */
    protected static PrivateKey parsePrivateKeyString(String privateKey, KeyFactory keyFactory) throws InvalidKeySpecException {
    		// strip off PEM markers, if any, so the certificate is in Base64-encoded DER format
        // (possibly with extraneous whitespace)
        String base64DER = privateKey.replaceAll(PRIVATE_KEY_PREFIX + "|" + PRIVATE_KEY_SUFFIX, "");
        byte[] decodedKey = Base64.getMimeDecoder().decode(base64DER);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);	
        
    }

    
    /**
     * Convert a PEM format certificate into a Certificate object
     * @param certificateString in PEM format
     * @param certFactory defining the format of the Certificate to generate
     * @return Certificate object
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws IOException
     */
    private static Certificate parseCertificateString(String certificateString, CertificateFactory certFactory) 
            throws CertificateException, IOException{
    	
    		// strip off PEM markers, if any, so the certificate is in Base64-encoded DER format
        // (possibly with extraneous whitespace)
        String base64DER = certificateString.replaceAll(PEM_CERTIFICATE_PREFIX + "|" + PEM_CERTIFICATE_SUFFIX, "");
        try (ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getMimeDecoder().decode(base64DER));) {
        		return certFactory.generateCertificate(bis);
        }
    }
}