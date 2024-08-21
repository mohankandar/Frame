package com.wynd.vop.framework.kong;

import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.security.jwt.JwtAuthenticationProperties;
import com.wynd.vop.framework.security.jwt.JwtSources;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class KongTokenService.
 */
public class KongTokenService {

	private static final BipLogger LOG = BipLoggerFactory.getLogger(KongTokenService.class);

	/** The spring configurable properties used for authentication */
	private JwtAuthenticationProperties jwtAuthenticationProperties;
	private KongProperties kongProperties;

	public KongTokenService(JwtAuthenticationProperties jwtAuthenticationProperties, KongProperties kongProperties) {
		this.jwtAuthenticationProperties = jwtAuthenticationProperties;
		this.kongProperties = kongProperties;
	}

	/**
	 * Get a list of JWT key pairs from VOP config and Kong consumers
	 * 
	 * @param aggregateBipTokens Boolean - if true aggregates VOP JWT Tokens with
	 *                           Kong Consumer JWT Tokens
	 * @return the JwtKeyPairs
	 */
	public List<JwtAuthenticationProperties.JwtKeyPairs> getKeyPairs(Boolean aggregateBipTokens) {

		List<JwtAuthenticationProperties.JwtKeyPairs> keyPairList = new ArrayList<>();
		int kongJwtCount = 0;

		if (Boolean.TRUE.equals(aggregateBipTokens)) {
			keyPairList = jwtAuthenticationProperties.getKeyPairs();
		}

		if (kongProperties != null && kongProperties.getEnabled()) {

			for (String kongConsumer : kongProperties.getConsumers()) {

				try {
					KongConsumerService kongConsumerService = new KongConsumerService();

					kongConsumerService.getKongConsumerJwtData(
							kongProperties.getUrl(),
							kongProperties.getConsumerJwtResourcePath(),
							kongConsumer,
							kongProperties.getAdminConsumerKey());

					keyPairList.addAll(kongConsumerService.getKongJwtKeyPairs());
					kongJwtCount++;
				} catch (Exception ex) {
					LOG.error("error getting kong jwt info: {}", ex.getMessage());
				}
			}

			if (kongJwtCount == 0){
				LOG.warn("Kong Enabled but no JWT Tokens found!");
			}
		}

		return keyPairList;
	}


	/**
	 * Get Only Kong KeyPairs from existing list of Key Pairs
	 * 
	 * @return the JwtKeyPairs
	 */
	public List<JwtAuthenticationProperties.JwtKeyPairs> getKongKeyPairs() {

		List<JwtAuthenticationProperties.JwtKeyPairs> filteredKeyPairs = null;

		List<JwtAuthenticationProperties.JwtKeyPairs> jwtKeyPairs = jwtAuthenticationProperties.getKeyPairs();

		if (jwtKeyPairs != null && !jwtKeyPairs.isEmpty()) {

			filteredKeyPairs = jwtKeyPairs.stream().filter(jwtKeyPair -> (jwtKeyPair != null
					&& (jwtKeyPair.getSignatureAlgorithm() == null
							|| jwtKeyPair.getSignatureAlgorithm() == SignatureAlgorithm.HS256)
					&& jwtKeyPair.getSecret() != null)
					&& (jwtKeyPair.getSource() != null && jwtKeyPair.getSource().equals(JwtSources.KONG.value())))
					.collect(Collectors.toList());
		}

		return filteredKeyPairs;
	}

}
