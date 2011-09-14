package net.planetes.jslint;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class JSLintOption implements Comparable<JSLintOption> {

	private static final JSLintOption[] EMPTY_OPTIONS = new JSLintOption[0];

	private static final String JSLINT_OPTIONS_BUNDLE_BASE_NAME = "net.planetes.jslint.jslint-options";

	private static final String DEF_TYPE = "bool";

	private static final String DEF_VALUE = "false";

	public static enum Type {
		BOOL, INT, STRING;

		public static Type parseType(String type) {
			try {
				return Type.valueOf(StringUtils.upperCase(type));
			} catch (Exception e) {
				return Type.BOOL;
			}
		}
	};

	private String name;
	private String description;
	private Type type;
	private String value;

	public JSLintOption(String name, String description, String type,
			String value) {
		this.name = name;
		this.description = description;
		this.type = Type.parseType(type);
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Type getType() {
		return type;
	}

	public String getString() {
		return value;
	}

	public boolean getBoolean() {
		return BooleanUtils.toBoolean((String) value);
	}

	public int getInt() {
		return NumberUtils.toInt(value);
	}

	//
	// implements Comparable<JSLintOption>
	//

	@Override
	public int compareTo(JSLintOption o) {
		return name.compareTo(o.name);
	}

	//
	//
	//

	public static JSLintOption[] loadOptions() {
		try {
			ResourceBundle bundle = ResourceBundle
					.getBundle(JSLINT_OPTIONS_BUNDLE_BASE_NAME);
			List<JSLintOption> options = new LinkedList<JSLintOption>();
			for (String key : bundle.keySet()) {
				if (key.indexOf('.') >= 0) {
					continue;
				}
				String description = bundle.getString(key);
				String type;
				try {
					type = bundle.getString(key + ".type");
				} catch (MissingResourceException e) {
					type = DEF_TYPE;
				}
				String value;
				try {
					value = bundle.getString(key + ".value");
				} catch (MissingResourceException e) {
					value = DEF_VALUE;
				}
				options.add(new JSLintOption(key, description, type, value));
			}
			Collections.sort(options);
			return options.toArray(EMPTY_OPTIONS);
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return EMPTY_OPTIONS;
		}
	}

}