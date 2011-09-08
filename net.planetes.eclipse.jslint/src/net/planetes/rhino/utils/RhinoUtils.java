package net.planetes.rhino.utils;

import org.mozilla.javascript.Scriptable;

public class RhinoUtils {

	public static String getStringProperty(Scriptable nobj, String name) {
		return (String) nobj.get(name, nobj);
	}

	public static String getStringProperty(Scriptable nobj, String name,
			String defaultValue) {
		try {
			return getStringProperty(nobj, name);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int getIntProperty(Scriptable nobj, String name) {
		return ((Number) nobj.get(name, nobj)).intValue();
	}

	public static int getIntProperty(Scriptable nobj, String name,
			int defaultValue) {
		try {
			return getIntProperty(nobj, name);
		} catch (Exception e) {
			return defaultValue;
		}
	}

}
