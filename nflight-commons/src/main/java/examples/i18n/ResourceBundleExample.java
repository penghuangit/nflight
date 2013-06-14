package examples.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.abreqadhabra.nflight.commons.i18n.XMLResourceBundleControl;

public class ResourceBundleExample {

	private static ResourceBundle resourceBundle;
	private static String resourceBundleName = "examples.resources.i18n.LoggingMessages";

	// Singleton Pattern
	static {
		resourceBundle = ResourceBundle.getBundle(resourceBundleName,
				Locale.getDefault());
	}

	/**
	 * Locale을 설정한다.
	 */
	public static void setLocale(Locale targetLocale) {
		resourceBundle = ResourceBundle.getBundle(resourceBundleName,
				targetLocale);
	}

	/**
	 * Message를 출력한다.
	 */
	public static String getString(String msgKey) {
		String message = "";
		try {
			message = resourceBundle.getString(msgKey);
		} catch (MissingResourceException mre) {
			mre.printStackTrace();
		}
		return message;
	}

	/**
	 * 메시지에 파라미터를 대입하여 메시지를 출력한다. 파라미터가 String type 일경우에 msgKey로 인식하여 찾는다.
	 */
	public static String getString(String msgKey, Object[] arguments) {
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = getString((String) arguments[i]);
		}
		return MessageFormat.format(getString(msgKey), arguments);
	}

	public static void main(String[] args) {

		System.out.println("resourceBundleName: " + resourceBundleName);

		System.out.println("-------- .properties --------");

		Locale targetLocale = resourceBundle.getLocale();

		String msgKey01 = ResourceBundleExample.getString("MSG_KEY_01");

		System.out.println(resourceBundle.getLocale() + ":: MSG_KEY_01: "
				+ msgKey01);

		msgKey01 = ResourceBundleExample.getString("MSG_KEY_01",
				new String[] { "MSG_KEY_01_01" });

		System.out.println(resourceBundle.getLocale() + ":: MSG_KEY_01: "
				+ msgKey01);

		targetLocale = Locale.ENGLISH;

		ResourceBundleExample.setLocale(targetLocale);

		msgKey01 = ResourceBundleExample.getString("MSG_KEY_01");

		System.out.println(resourceBundle.getLocale() + ":: MSG_KEY_01: "
				+ msgKey01);

		msgKey01 = ResourceBundleExample.getString("MSG_KEY_01",
				new String[] { "MSG_KEY_01_01" });

		System.out.println(resourceBundle.getLocale() + ":: MSG_KEY_01: "
				+ msgKey01);

		ResourceBundle.clearCache();

		System.out.println("-------- .xml --------");

		resourceBundle = ResourceBundle.getBundle(resourceBundleName,
				targetLocale, new XMLResourceBundleControl());

		msgKey01 = ResourceBundleExample.getString("MSG_KEY_01");

		System.out.println(resourceBundle.getLocale() + ":: MSG_KEY_01: "
				+ msgKey01);

		msgKey01 = ResourceBundleExample.getString("MSG_KEY_01",
				new String[] { "MSG_KEY_01_01" });

		System.out.println(resourceBundle.getLocale() + ":: MSG_KEY_01: "
				+ msgKey01);

		targetLocale = Locale.KOREAN;

		ResourceBundleExample.setLocale(targetLocale);

		msgKey01 = ResourceBundleExample.getString("MSG_KEY_01");

		System.out.println(resourceBundle.getLocale() + ":: MSG_KEY_01: "
				+ msgKey01);

		msgKey01 = ResourceBundleExample.getString("MSG_KEY_01",
				new String[] { "MSG_KEY_01_01" });

		System.out.println(resourceBundle.getLocale() + ":: MSG_KEY_01: "
				+ msgKey01);

	}
}
