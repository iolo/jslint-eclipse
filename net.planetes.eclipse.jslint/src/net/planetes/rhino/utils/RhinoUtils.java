package net.planetes.rhino.utils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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

	public static String toString(Object obj, boolean recursive) {
		ToStringBuilder tsb = new ToStringBuilder(obj,
				ToStringStyle.MULTI_LINE_STYLE);
		if (obj == null || !(obj instanceof Scriptable)) {
			return tsb.toString();
		}
		Scriptable nobj = (Scriptable) obj;
		tsb.append("className", nobj.getClassName());
		if (recursive) {
			tsb.append("prototype", toString(nobj.getPrototype(), true));
		} else {
			tsb.append("prototype", nobj.getPrototype());
		}
		for (Object id : nobj.getIds()) {
			Object value;
			if (id instanceof Integer) {
				value = nobj.get((Integer) id, nobj);
			} else if (id instanceof String) {
				value = nobj.get((String) id, nobj);
			} else {
				continue;
			}
			if (value instanceof Scriptable) {
				tsb.append((String) id, toString((Scriptable) value, recursive));
			} else {
				tsb.append((String) id, value);
			}
		}
		return tsb.toString();
	}
}
