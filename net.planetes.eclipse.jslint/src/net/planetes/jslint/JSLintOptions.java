package net.planetes.jslint;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class JSLintOptions {

	// adsafe true, if ADsafe rules should be enforced
	// bitwise true, if bitwise operators should be allowed
	// browser true, if the standard browser globals should be predefined
	// cap true, if upper case HTML should be allowed
	// confusion true, if types can be used inconsistently
	// 'continue' true, if the continuation statement should be tolerated
	// css true, if CSS workarounds should be tolerated
	// debug true, if debugger statements should be allowed
	// devel true, if logging should be allowed (console, alert, etc.)
	// eqeq true, if == should be allowed
	// es5 true, if ES5 syntax should be allowed
	// evil true, if eval should be allowed
	// forin true, if for in statements need not filter
	// fragment true, if HTML fragments should be allowed
	// indent the indentation factor
	// maxerr the maximum number of errors to allow
	// maxlen the maximum length of a source line
	// newcap true, if constructor names capitalization is ignored
	// node true, if Node.js globals should be predefined
	// nomen true, if names may have dangling _
	// on true, if HTML event handlers should be allowed
	// passfail true, if the scan should stop on first error
	// plusplus true, if increment/decrement should be allowed
	// properties true, if all property names must be declared with
	// /*properties*/
	// regexp true, if the . should be allowed in regexp literals
	// rhino true, if the Rhino environment globals should be predefined
	// undef true, if variables can be declared out of order
	// unparam true, if unused parameters should be tolerated
	// safe true, if use of some browser features should be restricted
	// sloppy true, if the 'use strict'; pragma is optional
	// sub true, if all forms of subscript notation are tolerated
	// vars true, if multiple var statements per function should be allowed
	// white true, if sloppy whitespace is tolerated
	// widget true if the Yahoo Widgets globals should be predefined
	// windows true, if MS Windows-specific globals should be predefined

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
