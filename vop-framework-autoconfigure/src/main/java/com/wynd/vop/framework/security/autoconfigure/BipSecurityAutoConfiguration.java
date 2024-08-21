package com.wynd.vop.framework.security.autoconfigure;

import com.wynd.vop.framework.client.rest.template.RestClientTemplate;
import com.wynd.vop.framework.kong.KongProperties;
import com.wynd.vop.framework.log.BipBanner;
import com.wynd.vop.framework.log.BipLogger;
import com.wynd.vop.framework.log.BipLoggerFactory;
import com.wynd.vop.framework.rest.exception.BasicErrorController;
import com.wynd.vop.framework.security.HttpProperties;
import com.wynd.vop.framework.security.handler.JwtAuthenticationEntryPoint;
import com.wynd.vop.framework.security.handler.JwtAuthenticationSuccessHandler;
import com.wynd.vop.framework.security.jwt.JwtAuthenticationFilter;
import com.wynd.vop.framework.security.jwt.JwtAuthenticationProperties;
import com.wynd.vop.framework.security.jwt.JwtAuthenticationProvider;
import com.wynd.vop.framework.security.jwt.JwtParser;
import com.wynd.vop.framework.security.jwt.JwtTokenService;
import com.wynd.vop.framework.security.jwt.TokenResource;
import com.wynd.vop.framework.security.jwt.*;
import com.wynd.vop.framework.security.opa.BipOpaProperties;
import com.wynd.vop.framework.security.opa.voter.BipOpaVoter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

/**
 * AutoConfiguration for various authentication types on the Platform (basic
 * authentication, JWT)
 */
@Configuration
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
@EnableConfigurationProperties({WebEndpointProperties.class, JwtAuthenticationProperties.class, KongProperties.class, BipOpaProperties.class, HttpProperties.class, BipRestClientProperties.class })
public class BipSecurityAutoConfiguration {

	private static final String NOOP_PREFIX = "{noop}";

	/** The Constant LOGGER. */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(BipSecurityAutoConfiguration.class);

	/**
	 * Adapter for JWT
	 */

	@Value("${vop.framework.security.actuator.username:admin}")
	String actuatorUserName;

	@Value("${vop.framework.security.actuator.password:default}")
	String actuatorPassword;

	@Value("${vop.framework.security.actuator.role:ACTUATOR}")
	String actuatorRole;

	@Autowired
	private WebEndpointProperties webEndpointProperties;


	public BipSecurityAutoConfiguration(WebEndpointProperties webEndpointProperties, HttpProperties httpProperties){
		this.webEndpointProperties = webEndpointProperties;
		this.httpProperties = httpProperties;
	}

	@Autowired
	private JwtAuthenticationProperties jwtAuthenticationProperties;

	@Autowired
	private KongProperties kongProperties;

	@Autowired
	private HttpProperties httpProperties;

	@Autowired
	private BipOpaProperties opaProperties;

	@Autowired
	@Lazy
	private RestClientTemplate restClientTemplate;

	@Bean
	@Order(1)
	public SecurityFilterChain basicFilterChain(HttpSecurity http) throws Exception {

		http.antMatcher(webEndpointProperties.getBasePath() + "/**")
				.authorizeRequests().antMatchers(webEndpointProperties.getBasePath() + "/info").permitAll()
				.antMatchers(webEndpointProperties.getBasePath() + "/health").permitAll()
				.antMatchers(webEndpointProperties.getBasePath() + "/**")
				.hasRole(actuatorRole).and().httpBasic().and().csrf()
				.disable()
				.addFilterAfter(headerFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers().contentSecurityPolicy(httpProperties.getContentSecurityPolicy());
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authenticationManager(authenticationManager());
		return http.build();
	}

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.withUsername(actuatorUserName)
				.password(NOOP_PREFIX.concat(actuatorPassword))
				.roles(actuatorRole)
				.build();
		return new InMemoryUserDetailsManager(user);
	}

	@Bean
	public Filter headerFilter(){
		return new RemoveHeaderFilter();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager() {
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setPasswordEncoder(passwordEncoder());
	    provider.setUserDetailsService(userDetailsService());
	    return new ProviderManager(provider);
	}


	@Bean
	@Order(2)
	@ConditionalOnProperty(prefix = "vop.framework.security.jwt", name = "enabled", matchIfMissing = true)
	public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(jwtAuthenticationProvider());
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http
				.authorizeRequests();

		boolean isOpaEnabled = false;

		if (opaProperties.isEnabled()) {
			if (opaProperties.getUrls() != null && opaProperties.getUrls().length > 0
					&& !opaProperties.getUrls()[0].isEmpty()) {
				LOGGER.info(
						"Setting AccessDecisionManager to use Open Policy Agent (OPA) with BipOpaVoter (AccessDecisionVoter)");
				urlRegistry.antMatchers(jwtAuthenticationProperties.getFilterProcessUrls()).authenticated()
						.accessDecisionManager(setAccessDecisionManager());
				isOpaEnabled = true;
			} else {
				LOGGER.warn(BipBanner.newBanner("Open Policy Agent Missing Configuration", Level.WARN),
						"Property to enable OPA set to true, however Urls property is missing");
			}
		} else {
			LOGGER.info("Starting without Open Policy Agent (OPA) enabled");
		}

		if (!isOpaEnabled) {
			urlRegistry.antMatchers(jwtAuthenticationProperties.getFilterProcessUrls()).authenticated();
		}

		urlRegistry.and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers().cacheControl();
		http.headers().contentSecurityPolicy(httpProperties.getContentSecurityPolicy());
		if (httpProperties.getCors() != null && httpProperties.getCors().isEnabled()) {
			http.cors();
		}
		return http.build();
	}
	private AccessDecisionManager setAccessDecisionManager() {
		final String[] opaUrls = opaProperties.getUrls();
		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
		for (String opaUrl : opaUrls) {
			if (StringUtils.isNotBlank(opaUrl)) {
				LOGGER.info("OPA Url {}", opaUrl);
				decisionVoters.add(new BipOpaVoter(opaUrl, restClientTemplate));
			}
		}
		if (opaProperties.isAllVotersAbstainGrantAccess()) {
			return new UnanimousBased(decisionVoters);
		} else {
			return new AffirmativeBased(decisionVoters);
		}
	}

	/**
	 * Authentication entry point.
	 *
	 * @return the authentication entry point
	 */
	@Bean
	protected AuthenticationEntryPoint authenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}

	/**
	 * Jwt authentication provider.
	 *
	 * @return the authentication provider
	 */
	@Bean
	protected AuthenticationProvider jwtAuthenticationProvider() {
		return new JwtAuthenticationProvider(new JwtParser(jwtAuthenticationProperties));
	}

	/**
	 * Jwt authentication success handler.
	 *
	 * @return the authentication success handler
	 */
	@Bean
	protected AuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
		return new JwtAuthenticationSuccessHandler();
	}

	/**
	 * Jwt authentication filter.
	 *
	 * @return the jwt authentication filter
	 */
	protected JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtAuthenticationProperties, kongProperties, jwtAuthenticationSuccessHandler(),
				jwtAuthenticationProvider(), authenticationEntryPoint());
	}

	/**
	 * Open Policy Agent properties used for policy engine authorization.
	 *
	 * @return BipOpaProperties the properties
	 */
	@Bean
	@ConditionalOnMissingBean
	protected BipOpaProperties opaProperties() {
		return new BipOpaProperties();
	}

	/**
	 * The Rest Client Template
	 *
	 * @return RestClientTemplate the rest client template
	 */
	@Bean
	@ConditionalOnMissingBean
	protected RestClientTemplate restClientTemplate() {
		return new RestClientTemplate();
	}


	/**
	 * Adapter that only processes URLs specified in the filter
	 */
	@Configuration
	@ConditionalOnProperty(prefix = "vop.framework.security.jwt", name = "enabled", havingValue = "false")
	@Order(JwtAuthenticationProperties.AUTH_ORDER)
	protected static class JwtNoWebSecurityConfigurerAdapter{

		@Autowired
		private JwtAuthenticationProperties jwtAuthenticationProperties;

		@Bean
		public WebSecurityCustomizer webSecurityCustomizer() {
			return (web) -> web.ignoring().antMatchers(jwtAuthenticationProperties.getFilterProcessUrls());
		}
	}

	/**
	 * Adapter that only excludes specified URLs
	 */
	@Bean
	@Order(JwtAuthenticationProperties.NO_AUTH_ORDER)
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers(jwtAuthenticationProperties.getExcludeUrls());
	}

	/**
	 * Security properties used for both JWT and Basic Auth authentication. Spring
	 * configuration (yml / properties, etc) provides values to this object.
	 *
	 * @return JwtAuthenticationProperties the properties
	 */
	@Bean
	@ConditionalOnMissingBean
	public JwtAuthenticationProperties jwtAuthenticationProperties() {
		return new JwtAuthenticationProperties();
	}

	/**
	 * Security Kong properties used for both JWT authentication. Spring
	 * configuration (yml / properties, etc) provides values to this object.
	 *
	 * @return KongProperties the properties
	 */
	@Bean
	@ConditionalOnMissingBean
	public KongProperties kongProperties() {
		return new KongProperties();
	}


	/**
	 * The service component for processing JWT
	 *
	 * @return JwtTokenService the service component
	 */
	@Bean
	@ConditionalOnMissingBean
	public JwtTokenService jwtTokenService() {
		return new JwtTokenService();
	}

	@Bean
	@ConditionalOnMissingBean
	@Primary
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public BasicErrorController basicErrorController() {
		return new BasicErrorController();
	}

	/**
	 * The REST Controller that creates a "valid" JWT token that can be used for
	 * testing.
	 *
	 * @return TokenResource the rest controller
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnExpression("${vop.framework.security.jwt.enabled:true} && ${vop.framework.security.jwt.generate.enabled:true}")
	public TokenResource tokenResource() {
		return new TokenResource();
	}
}

