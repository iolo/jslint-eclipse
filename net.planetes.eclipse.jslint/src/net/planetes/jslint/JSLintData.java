package net.planetes.jslint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.mozilla.javascript.NativeObject;

public class JSLintData {

	private final boolean valid;

	private final JSLintError[] errors;

	private final JSLintFunction[] functions;

	public JSLintData(boolean valid, NativeObject nobj) {
		this.valid = valid;
		errors = JSLintError.fromJSObject(nobj.get("errors", nobj));
		functions = JSLintFunction.fromJSObject(nobj.get("functions", nobj));
		// TODO: more properties...
		// globals
		// member
		// urls
		// json
	}

	public boolean isValid() {
		return valid;
	}

	public boolean hasError() {
		return errors != null & errors.length > 0;
	}

	public JSLintError[] getErrors() {
		return errors;
	}

	public boolean hasFunction() {
		return functions != null & functions.length > 0;
	}

	public JSLintFunction[] getFunctions() {
		return functions;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
