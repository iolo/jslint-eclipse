package net.planetes.jslint;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class JSLintOptions {

	private final Map<String, Object> options;

	public JSLintOptions() {
		options = new HashMap<String, Object>();
	}

	public void setOption(String name, Object value) {
		options.put(name, value);
	}

	public Object getOption(String name) {
		return options.get(name);
	}

	public boolean hasOption(String name) {
		return options.containsKey(name);
	}

	public String toJSONString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		boolean first = true;
		for (Map.Entry<String, Object> option : options.entrySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(',');
			}
			sb.append('\"').append(option.getKey()).append('\"').append(':')
					.append(option.getValue());
		}
		return sb.append('}').toString();
	}

	public Scriptable toJSObject(Context context, Scriptable scope) {
		Scriptable nobj = context.newObject(scope);
		for (Map.Entry<String, Object> entry : options.entrySet()) {
			nobj.put(entry.getKey(), nobj, entry.getValue());
		}
		return nobj;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(toJSONString()).toString();
	}
}
