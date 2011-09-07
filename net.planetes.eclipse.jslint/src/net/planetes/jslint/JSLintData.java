package net.planetes.jslint;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

public class JSLintData {

	private final boolean valid;

	private final JSLintError[] errors;

	private final JSLintFunction[] functions;

	public JSLintData(boolean valid, NativeObject nobj) {
		this.valid = valid;
		errors = nobj.has("errors", nobj) ? JSLintError
				.fromNativeArray((NativeArray) nobj.get("errors", nobj))
				: JSLintError.EMPTY_LIST;
		functions = nobj.has("functions", nobj) ? JSLintFunction
				.fromNativeArray((NativeArray) nobj.get("functions", nobj))
				: JSLintFunction.EMPTY_LIST;
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
}
