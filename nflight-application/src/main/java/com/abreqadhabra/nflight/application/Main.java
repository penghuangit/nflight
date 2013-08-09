package com.abreqadhabra.nflight.application;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.Launcher;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class Main {
	private static final Class<Main> THIS_CLAZZ = Main.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static ClassLoader cl;

	static {
		try {
			cl = getClassLoaderFromPath(Configure.CODE_BASE_PATH, Thread
					.currentThread().getContextClassLoader());
			Thread.currentThread().setContextClassLoader(cl);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	// Returns a ClassLoader that for the provided path.
	private static ClassLoader getClassLoaderFromPath(Path path,
			final ClassLoader parent) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		path = path.normalize().getParent();
		LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"codebase path : " + path.toString());

		// get jar files from jarPath
		final ArrayList<URL> classpathList = new ArrayList<URL>();

		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path,
				"*.{jar}")) {

			for (final Path classpath : ds) {
				classpathList.add(classpath.toUri().toURL());
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, classpath.toString());
			}
		} catch (final Exception e) {
			final StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.SEVERE, current[0].getClassName(),
					current[0].getMethodName(), "Exception is instance of "
							+ e.getClass().getCanonicalName(), e);
			// e.printStackTrace();
		}

		// toArray copies content into other array
		final URL urls[] = new URL[classpathList.size()];
		classpathList.toArray(urls);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Jar file's urls : " + Arrays.toString(urls));

		return new URLClassLoader(urls, parent);
	}

	public static void main(final String[] args) throws Exception {
		final Launcher launcher = Launcher.class.cast(Class.forName(
				Configuration.LAUNCHER_CLASS, true, cl).newInstance());

		launcher.launch(args);
	}

}