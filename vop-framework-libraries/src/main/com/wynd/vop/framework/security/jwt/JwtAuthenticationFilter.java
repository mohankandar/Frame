package com.wynd.vop.framework.security.jwt;

import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.messages.MessageKey;
import com.wynd.vop.framework.messages.MessageKeys;
import com.wynd.vop.framework.messages.MessageSeverity;
import com.wynd.vop.framework.audit.AuditEventData;
import com.wynd.vop.framework.audit.AuditEvents;
import com.wynd.vop.framework.audit.AuditLogger;
import com.wynd.vop.framework.kong.KongProperties;
import com.wynd.vop.framework.kong.KongTokenService;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Configure springboot starter for the platform. Similar to
 * {@code UsernamePasswordAuthenticationFilter}
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final BipLogger LOG = BipLoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private AuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private JwtAuthenticationProperties jwtAuthenticationProperties;

	private static final String TOKEN_TAMPERED = "Tampered Token";
	private static final String TOKEN_MALFORMED = "Malformed Token";

	/**
	 * Create the filter.
	 *
	 * @param jwtAuthenticationProperties
	 * @param jwtAuthenticationSuccessHandler
	 * @param jwtAuthenticationProvider
	 */
	public JwtAuthenticationFilter(JwtAuthenticationProperties jwtAuthenticationProperties,
			KongProperties kongProperties,
			AuthenticationSuccessHandler jwtAuthenticationSuccessHandler,
			AuthenticationProvider jwtAuthenticationProvider,
            AuthenticationEntryPoint jwtAuthenticationEntryPoint) {
		super(new AuthenticationRequestMatcher(jwtAuthenticationProperties.getFilterProcessUrls()));
		this.jwtAuthenticationProperties = jwtAuthenticationProperties;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;

		if (kongProperties != null && kongProperties.getEnabled()) {
			// Grab All Kong Consumer Tokens and add them to VOP JWT Key Pairs for authentication
			KongTokenService kongTokenService = new KongTokenService(jwtAuthenticationProperties,kongProperties);

			List<JwtAuthenticationProperties.JwtKeyPairs> jwtKeyPairsList = kongTokenService.getKeyPairs(true);
			jwtAuthenticationProperties.setKeyPairs(jwtKeyPairsList);

			for (JwtAuthenticationProperties.JwtKeyPairs keypair : jwtKeyPairsList) {
				LOG.debug("JWT Key: {}, Secret:{}", keypair.getIssuer(), keypair.getSecret());
			}
		}

    	setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler);
		setAuthenticationManager(new ProviderManager(new ArrayList<>(Arrays.asList(jwtAuthenticationProvider))));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String token = request.getHeader(jwtAuthenticationProperties.getHeader());
		if (token == null || !token.startsWith("Bearer ")) {
			MessageKeys key = MessageKeys.VOP_SECURITY_TOKEN_BLANK;
			LOG.error(key.getMessage());
			JwtAuthenticationException authException = new JwtAuthenticationException(key, MessageSeverity.ERROR,
					HttpStatus.BAD_REQUEST);
			try {
				jwtAuthenticationEntryPoint.commence(request, response, authException);
			} catch (Exception ce1) {
				LOG.error(ce1.getMessage());
			}
			return null;
		}

		token = token.substring(7);

		try {
			return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
		} catch (SignatureException se) {
			MessageKey key = MessageKeys.VOP_SECURITY_TOKEN_BROKEN;
			String[] params = new String[] { TOKEN_TAMPERED, token, se.getClass().getSimpleName(), se.getMessage() };
			writeAuditForJwtTokenErrors(key.getMessage(params), se);
			JwtAuthenticationException authException = new JwtAuthenticationException(key, MessageSeverity.ERROR,
					HttpStatus.BAD_REQUEST, se, params);
			try {
				jwtAuthenticationEntryPoint.commence(request, response, authException);
			} catch (Exception ce2) {
				LOG.error(ce2.getMessage());
			}
			return null;
		} catch (MalformedJwtException ex) {
			MessageKey key = MessageKeys.VOP_SECURITY_TOKEN_BROKEN;
			String[] params = new String[] { TOKEN_MALFORMED, token, ex.getClass().getSimpleName(), ex.getMessage() };
			writeAuditForJwtTokenErrors(key.getMessage(params), ex);
			JwtAuthenticationException authException = new JwtAuthenticationException(key, MessageSeverity.ERROR,
					HttpStatus.BAD_REQUEST, ex, params);
			try {
				jwtAuthenticationEntryPoint.commence(request, response, authException);
			} catch (Exception ce3) {
				LOG.error(ce3.getMessage());
			}
			return null;
		}
	}

	/**
	 * Audit any errors.
	 *
	 * @param cause
	 *            - cause
	 * @param request
	 *            - original request
	 */
	private void writeAuditForJwtTokenErrors(final String cause, final Throwable t) {

		AuditEventData auditData = new AuditEventData(AuditEvents.SECURITY, "attemptAuthentication",
				JwtAuthenticationFilter.class.getName());
		AuditLogger.error(auditData, cause, t);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.web.authentication.
	 * AbstractAuthenticationProcessingFilter#successfulAuthentication(javax.
	 * servlet. http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * javax.servlet.FilterChain,
	 * org.springframework.security.core.Authentication)
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);

		chain.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.web.authentication.
	 * AbstractAuthenticationProcessingFilter#unsuccessfulAuthentication(javax.
	 * servlet. http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getLocalizedMessage());
	}
}

/**
 * Set rules for what requests should be ignored.
 */
class AuthenticationRequestMatcher implements RequestMatcher {
	private RequestMatcher baselineMatches;

	/**
	 * Requests to ignore based on ant path configurations.
	 *
	 * @param baselineMatches
	 * @param ignoreUrls
	 */
	public AuthenticationRequestMatcher(String[] baselineMatches) {
		this.baselineMatches = authMatchers(baselineMatches);
	}

	/**
	 * Requests to ignore based on ant path configurations.
	 *
	 * @param baselineMatches
	 * @param ignoreMatches
	 */
	public AuthenticationRequestMatcher(RequestMatcher baselineMatches) {
		this.baselineMatches = baselineMatches;
	}

	/**
	 * Add exclusion URLs to the list.
	 *
	 * @param exclusionUrls
	 * @return RequestMatcher
	 */
	private RequestMatcher authMatchers(String[] authUrls) {
		LinkedList<RequestMatcher> matcherList = new LinkedList<>();
		for (String url : authUrls) {
			matcherList.add(new AntPathRequestMatcher(url));
		}
		return new OrRequestMatcher(matcherList);
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		return baselineMatches.matches(request);
	}
}