package com.wynd.vop.framework.test.util;

import com.wynd.vop.framework.test.exception.BipTestLibRuntimeException;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Utilities for handling properties.
 *
 * @author aburkholder
 */
public class PropertiesUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

	/**
	 * Empty private constructor that should not initialized.
	 */
	private PropertiesUtil() {
		throw new UnsupportedOperationException("PropertiesUtil is a static class. Do not instantiate it.");
	}

	/**
	 * Read a properties file. If the fileUrl cannot be found, the returned
	 * properties may be {@code null} or empty.
	 * <p>
	 * Simple variable substitution is done when reading the properties.<br/>
	 * For example:
	 *
	 * <pre>
	 * Properties file:
	 * ----------------
	 * some.value=this is a test
	 * some.substitute.me=Substituted variable is: ${some.value}
	 *
	 * Resulting properties:
	 * ---------------------
	 * some.value=this is a test
	 * some.substitute.me=Substituted variable is: this is a test
	 * </pre>
	 *
	 * @param fileUrl
	 *            the properties file to read
	 * @return the properties
	 */
	public static Properties readFile(final URL fileUrl) {
		Properties properties = null;
		
		if (fileUrl == null) {
			LOGGER.warn("Property file resource URL is null. No properties file to load.");
		} else {
			URI fileUri = null;
			try {
				fileUri = fileUrl.toURI();
			} catch (URISyntaxException e) {
				throw new BipTestLibRuntimeException("Could not return a java.net.URI equivalent to this URL: " + fileUrl, e);
			}
			
			final File file = new File(fileUri);
			try (InputStream input = new FileInputStream(file)) {
				properties = new Properties();
				properties.load(input);
				substitutePlaceholders(properties);
			} catch (Exception e) {
				throw new BipTestLibRuntimeException("Could not load the properties file with URL : " + fileUrl, e);
			}
		}
		return properties;
	}

	/**
	 * Perform simple placeholder substitutions.
	 *
	 * @param properties
	 */
	private static void substitutePlaceholders(final Properties properties) {
		final Map<String, String> valuesMap = new HashMap<>();
		final Map<String, String> templateMap = new HashMap<>();
		for (final Object key : properties.keySet()) {
			final String value = properties.getProperty((String) key);
			if (value.contains("${")) {
				templateMap.put((String) key, value);
			} else {
				valuesMap.put((String) key, value);
			}
		}

		final StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
		for (final Entry<String, String> entry : templateMap.entrySet()) {
			final String tmplt = entry.getKey();
			final String subbed = substitutor.replace(tmplt);
			properties.put(entry.getKey(), subbed);
		}
	}

}