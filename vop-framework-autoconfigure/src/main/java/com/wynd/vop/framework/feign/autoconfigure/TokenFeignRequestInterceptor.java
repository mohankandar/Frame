package com.wynd.vop.framework.feign.autoconfigure;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.security.jwt.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * An implementation of {@link RequestInterceptor} that adds the JWT token from
 * the originating request, and adds it to the outgoing request. No changes are
 * made to the response.
 * <p>
 * Use this class when making feign assisted (e.g. {@code @EnableFeignClients})
 * inter-=service REST calls that require PersonTraits.
 */
public class TokenFeignRequestInterceptor implements RequestInterceptor {

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(TokenFeignRequestInterceptor.class);

	@Autowired
	private JwtTokenService tokenService;

	public TokenFeignRequestInterceptor() {
		LOGGER.debug("TokenFeignRequestInterceptor constructor invoked");
		if (tokenService == null) {
			tokenService = new JwtTokenService();
		}
	}

	/**
	 * Add token header from the originating request to the outgoing request. No
	 * changes made to the response.
	 *
	 * @see feign.RequestInterceptor#apply(feign.RequestTemplate)
	 */
	@Override
	public void apply(RequestTemplate template) {
		Map<String, String> tokenMap = tokenService.getTokenFromRequest();
		for (Map.Entry<String, String> token : tokenMap.entrySet()) {
			LOGGER.info("Adding Token Header {} {}", token.getKey(), token.getValue());
			template.header(token.getKey(), token.getValue());
		}
	}
}