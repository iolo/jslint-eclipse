package net.planetes.jslint;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JSLint {

	private static final String JSLINT_JS_RESOURCE_NAME = "jslint.js";

	private String jslintSource;

	public JSLint() throws IOException {
		// load jslint.js from classpath resource
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream(JSLINT_JS_RESOURCE_NAME);
			jslintSource = IOUtils.toString(in, "UTF-8");
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public JSLintData validate(String source, JSLintOptions options) {
		Context ctx = Context.enter();
		try {
			Scriptable scope = ctx.initStandardObjects();

			ctx.evaluateString(scope, jslintSource, "<jslint>", 1, null);
			
			ScriptableObject.putProperty(scope, "_source", source);
			ScriptableObject.putProperty(scope, "_options", options.toJSObject(ctx, scope));

			// TODO: support jslint options...
			String script = "JSLINT(_source, _options);\n";

			boolean valid = (Boolean) ctx.evaluateString(scope, script,
					"<jslint>", 1, null);

			return new JSLintData(valid, (NativeObject) ctx.evaluateString(
					scope, "JSLINT.data()", "<jslint>", 1, null));
		} finally {
			Context.exit();
		}
	}

}
