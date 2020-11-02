package utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class ResUtil {
	/**
	 * language 资源
	 */
	private static ResourceBundle resource= ResourceBundle.getBundle("language.str", Locale.CHINESE);

	private static ResourceBundle resource0 = ResourceBundle.getBundle("language.str", Locale.CHINESE);
	private static ResourceBundle resource1 = ResourceBundle.getBundle("language.str", Locale.ENGLISH);
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
	public static String gs(String key) {
		try {
			String res = getResource().getString(key);
			return res;
		} catch (Exception e) {
			return "--";
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
	 * Gets a string from the ResourceBundles. <br>
	 * Convenience method to save casting.
	 *
	 * @param language 0:chinese 1:english
	 * @param key      the key of the properties.
	 * @return the value of the property. Return the key if the value is not found.
	 */
	public static String gs(int language, String key) {
		try {
			String res = "";
			if (language == 1) {
				// 英文
				res = resource1.getString(key);
			} else {
				res = resource0.getString(key);
			}
			return res;
		} catch (Exception e) {
			return "--";
		}
	}

	/**
	 * Gets a string from the ResourceBundles. <br>
	 * Convenience method to save casting.
	 *
	 * @param language 0:chinese 1:english
	 * @param key      the key of the properties.
	 * @return the value of the property. Return the key if the value is not found.
	 */
	public static String gs(int language, String key, Object... strs) {
		try {
			String res = "";
			if (language == 1) {
				// 英文
				res = String.format(resource1.getString(key), strs);
			} else {
				res = String.format(resource0.getString(key), strs);
			}
			return res;
		} catch (Exception e) {
			return "--";
		}
	}

	/**
	 * 获取中英文拼接
	 * @param key
	 * @return
	 */
	public static String gsAll(String key) {
		try {
			String res1 = resource1.getString(key);
			String res0 = resource0.getString(key);
			return res0+"!@#"+res1;
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