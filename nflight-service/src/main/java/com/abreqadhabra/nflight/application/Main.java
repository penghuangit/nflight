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

import com.abreqadhabra.nflight.application.launcher.Launcher;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class Main {
	private static Class<Main> THIS_CLAZZ = Main.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static ClassLoader cl;

	static {
		try {
			cl = getClassLoaderFromPath(Globals.CODE_BASE_PATH, Thread
					.currentThread().getContextClassLoader());
			Thread.currentThread().setContextClassLoader(cl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Returns a ClassLoader that for the provided path.
	private static ClassLoader getClassLoaderFromPath(Path path,
			ClassLoader parent) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		path = path.normalize().getParent();
		LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "codebase path : " + path
				.toString());

		// get jar files from jarPath
		ArrayList<URL> classpathList = new ArrayList<URL>();

		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path,
				"*.{jar}")) {

			for (Path classpath : ds) {
				classpathList.add(classpath.toUri().toURL());
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, classpath.toString());
			}
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.SEVERE, current[0].getClassName(),
					current[0].getMethodName(), "Exception is instance of "
							+ e.getClass().getCanonicalName(), e);
			// e.printStackTrace();
		}

		// toArray copies content into other array
		URL urls[] = new URL[classpathList.size()];
		classpathList.toArray(urls);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Jar file's urls : " + Arrays.toString(urls));

		return new URLClassLoader(urls, parent);
	}

	public static void main(String[] args) throws Exception {
		Launcher launcher = Launcher.class.cast(Class.forName(
				Globals.LAUNCHER_CLASS, true, cl).newInstance());

		launcher.launch(args);
	}

}