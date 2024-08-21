package com.wynd.vop.framework.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used as authentication properties in projects.
 * The values assigned to members in this class are defaults,
 * and are typically overridden in yml and spring configuration.
 */
@ConfigurationProperties(prefix = "vop.framework.security.jwt")
public class JwtAuthenticationProperties {
	private boolean enabled = true;
	private String header = "Authorization";
	private String secret = "secret";
	private String issuer = "Vets.gov";
	private int expireInSeconds = 900;
	private String[] filterProcessUrls = { "/api/**" };
	private String[] excludeUrls = { "/token", "/*" };
	/** List of inner class {@link JwtKeyPairs} configuration objects */
	private List<JwtKeyPairs> keyPairs = new ArrayList<>();

	@NestedConfigurationProperty
	private JwtValidation validation;

	public static final int AUTH_ORDER = SecurityProperties.BASIC_AUTH_ORDER - 2;
	public static final int NO_AUTH_ORDER = AUTH_ORDER + 1;

	/**
	 * Authentication enabled
	 *
	 * @return boolean
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Authentication enabled
	 *
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Authentication header
	 *
	 * @return String
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Authentication header
	 *
	 * @param header
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Authentication secret
	 *
	 * @return String
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * Authentication issuer
	 *
	 * @return String
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * Authentication secret
	 *
	 * @param secret
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * Authentication issuer
	 *
	 * @param issuer
	 */
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	/**
	 * A wildcard URL that filters with URls to process
	 *
	 * @return String
	 */
	public String[] getFilterProcessUrls() {
		return filterProcessUrls;
	}

	/**
	 * A wildcard URL / path that filters which URls to process.
	 *
	 * @param filterProcessUrls the new filter process urls
	 */
	public void setFilterProcessUrls(String[] filterProcessUrls) {
		this.filterProcessUrls = filterProcessUrls;
	}

	/**
	 * An array of wildcard URLs / paths should be excluded from processing
	 *
	 * @return String[]
	 */
	public String[] getExcludeUrls() {
		return excludeUrls;
	}

	/**
	 * An array of wildcard URLs / paths should be excluded from processing
	 *
	 * @param excludeUrls
	 */
	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	/**
	 * Request expiration time expressed in seconds
	 *
	 * @return int
	 */
	public int getExpireInSeconds() {
		return expireInSeconds;
	}

	/**
	 * Request expiration time expressed in seconds
	 *
	 * @param expireInSeconds
	 */
	public void setExpireInSeconds(int expireInSeconds) {
		this.expireInSeconds = expireInSeconds;
	}
	
	/**
	 * List of inner class {@link JwtKeyPairs} configuration objects.
	 *
	 * @return List of JwtKeyPairs objects
	 */
	public List<JwtKeyPairs> getKeyPairs() {
		return keyPairs;
	}
	
	/**
	 * The inner class {@link JwtKeyPairs} configuration object.
	 *
	 * @param keyPairs the JWT key pairs
	 */
	public void setKeyPairs(final List<JwtKeyPairs> keyPairs) {
		this.keyPairs = keyPairs;
	}

	public JwtValidation getValidation() {
		return validation;
	}

	public void setValidation(JwtValidation validation) {
		this.validation = validation;
	}

	/**
	 * Inner class to hold the JWT validation related metadata
	 * <p>
	 * Any properties under {@code vop.framework.security.validate}.
	 *
	 */
	public static class JwtValidation {
		private String mappedAuthority;

		public String getMappedAuthority() {
			return mappedAuthority;
		}

		public void setMappedAuthority(String mappedAuthority) {
			this.mappedAuthority = mappedAuthority;
		}
	}

	/**
	 * Inner class to hold the secret and issuer pair
	 * <p>
	 * A list of JwtKeyPairs objects is populated from list entries in the application yaml
	 * under {@code vop.framework.security.jwt.keyPairs}.
	 *
	 */
	public static class JwtKeyPairs {
		
		/** The secret */
		private String secret;

		/** The issuer */
		private String issuer;
  
		/** The signature algorithm */
        private SignatureAlgorithm signatureAlgorithm;
        
        /** The public key */
        private String publicKey;

        /** The JWT source */
        private String source;

        /**
		 * Instantiates a new JWT key pair POJO.
		 *
		 */
		public JwtKeyPairs() {
		}
		
		/**
		 * Instantiates a new JWT key pair POJO.
		 *
		 * @param secret the secret
		 * @param issuer the issuer
		 */
		public JwtKeyPairs(String secret, String issuer) {
			setSecret(secret);
			setIssuer(issuer);
		}

        /**
         * Instantiates a new JWT key pair POJO.
         *  @param secret the secret
         * @param publicKey the public key
         * @param issuer the issuer
         * @param signatureAlgorithm the signature algorithm
         */
        public JwtKeyPairs(String secret, String publicKey, String issuer, SignatureAlgorithm signatureAlgorithm) {
            setSecret(secret);
            setPublicKey(publicKey);
            setIssuer(issuer);
            setSignatureAlgorithm(signatureAlgorithm);
        }

		/**
		 * Secret for JWT token.
		 *
		 * @return String
		 */
		public String getSecret() {
			return secret;
		}

		/**
		 * Secret for JWT token.
		 *
		 * @param secret
		 */
		public void setSecret(final String secret) {
			this.secret = secret;
		}

		/**
		 * Issuer name for JWT token.
		 *
		 * @return Long
		 */
		public String getIssuer() {
			return issuer;
		}

		/**
		 * Issuer name for JWT token.
		 *
		 * @param issuer
		 */
		public void setIssuer(final String issuer) {
			this.issuer = issuer;
		}

        /**
         * Signature algorithm for the Jwt Key Pair
         * @return SignaturAlgorithm
         */
        public SignatureAlgorithm getSignatureAlgorithm() {
            return signatureAlgorithm;
        }
        
        /**
         * Sets the SignatureAlgorithm of the JWT Key Pair
         * @param signatureAlgorithm
         */
        public void setSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
            this.signatureAlgorithm = signatureAlgorithm;
        }

		/**
		 * Public Key for the Jwt Key Pair
		 * @return publicKey
		 */
        public String getPublicKey() {
            return publicKey;
        }

		/**
		 * Sets the PublicKey of the JWT Key Pair
		 * @param publicKey
		 */
        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

		/**
		 * Source of the Jwt Key Pair
		 * @return source
		 */
		public String getSource() {
			return source;
		}

		/**
		 * Sets the source of the JWT Key Pair
		 * @param source
		 */
		public void setSource(String source) {
			this.source = source;
		}
	}

}
