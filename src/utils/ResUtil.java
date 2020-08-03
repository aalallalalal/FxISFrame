package utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class ResUtil {
	/**
	 * language 资源
	 */
	private static ResourceBundle resource = ResourceBundle.getBundle("language.str", Locale.CHINESE);
	private static int language = 0;

	public static void initLanguage(int l) {
		language = l;
		if (l == 0) {
			resource = ResourceBundle.getBundle("language.str", Locale.CHINESE);
		} else {
			resource = ResourceBundle.getBundle("language.str", Locale.ENGLISH);
		}
	}

	/**
	 * Gets a string from the ResourceBundles. <br>
	 * Convenience method to save casting.
	 *
	 * @param key the key of the properties.
	 * @return the value of the property. Return the key if the value is not found.
	 */
	public static String gs(String key, Object... strs) {
		try {
			String res = String.format(getResource().getString(key), strs);
			return res;
		} catch (Exception e) {
			return "--";
		}
	}

	/**
	 * Gets the integer from the properties.
	 *
	 * @param key the key of the property.
	 *
	 * @return the value of the key. return -1 if the value is not found.
	 */
	public static Integer gi(String key) {
		try {
			return Integer.valueOf(getResource().getString(key));
		} catch (MissingResourceException e) {
			return new Integer(-1);
		}
	}

	public static ResourceBundle getResource() {
		return resource;
	}

	public static int getLanguage() {
		return language;
	}

}