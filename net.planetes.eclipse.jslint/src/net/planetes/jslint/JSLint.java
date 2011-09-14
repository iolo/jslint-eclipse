package net.planetes.jslint;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JSLint {

	private static final String JSLINT_JS_RESOURCE_NAME = "jslint.js";
	private static final String JSLINT_JS_ENCODING = "UTF-8";

	private final String jslintSource;

	public JSLint() throws IOException {
		this(JSLint.loadJSLint());
	}

	public JSLint(String jslintSource) {
		this.jslintSource = jslintSource;
	}

	public JSLint(InputStream in) throws IOException {
		this(IOUtils.toString(in, JSLINT_JS_ENCODING));
	}

	public JSLint(File file) throws IOException {
		this(FileUtils.readFileToString(file, JSLINT_JS_ENCODING));
	}

	public JSLintData validate(String source, JSLintOptions options) {
		Context context = Context.enter();
		try {
			Scriptable scope = context.initStandardObjects();

			context.evaluateString(scope, jslintSource, "<jslint>", 1, null);

			ScriptableObject.putProperty(scope, "_source", source);
			ScriptableObject.putProperty(scope, "_options",
					options.toJSObject(context, scope));

			String script = "JSLINT(_source, _options);\n";

			boolean valid = (Boolean) context.evaluateString(scope, script,
					"<jslint>", 1, null);

			return new JSLintData(valid, (NativeObject) context.evaluateString(
					scope, "JSLINT.data()", "<jslint>", 1, null));
		} finally {
			Context.exit();
		}
	}

	/**
	 * load jslint.js from classpath resource
	 * 
	 * @return
	 * @throws IOException
	 */
	private static final String loadJSLint() throws IOException {
		InputStream in = null;
		try {
			in = JSLint.class.getResourceAsStream(JSLINT_JS_RESOURCE_NAME);
			return loadJSLint(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	private static final String loadJSLint(InputStream in) throws IOException {
		return IOUtils.toString(in, JSLINT_JS_ENCODING);
	}

}
