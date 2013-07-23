package com.abreqadhabra.nflight.common.i18n;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class XMLResourceBundleControl extends Control {

	private static String XML = "xml";

	@Override
	public List<String> getFormats(final String baseName) {
		return Collections.singletonList(XML);
	}

	@Override
	public ResourceBundle newBundle(final String baseName, final Locale locale,
			final String format, final ClassLoader loader, final boolean reload)
			throws IOException {

		if ((baseName == null) || (locale == null) || (format == null)
				|| (loader == null)) {
			throw new NullPointerException();
		}
		ResourceBundle bundle = null;
		if (!format.equals(XML)) {
			return null;
		}

		final String bundleName = this.toBundleName(baseName, locale);
		final String resourceName = this.toResourceName(bundleName, format);
		final URL url = loader.getResource(resourceName);
		if (url == null) {
			return null;
		}
		final URLConnection connection = url.openConnection();
		if (connection == null) {
			return null;
		}
		if (reload) {
			connection.setUseCaches(false);
		}
		final InputStream stream = connection.getInputStream();
		if (stream == null) {
			return null;
		}
		final BufferedInputStream bis = new BufferedInputStream(stream);
		bundle = new XMLResourceBundle(bis);
		bis.close();

		return bundle;
	}

}
