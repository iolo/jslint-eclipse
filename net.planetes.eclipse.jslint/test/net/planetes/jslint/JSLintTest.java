package net.planetes.jslint;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class JSLintTest {

	@Test
	public void testBuiltinJSLint() throws IOException {
		JSLint jslint = new JSLint();
		JSLintOptions options = new JSLintOptions();
		String source = "(function () {\"use strict\"; function hello() { } }());";
		JSLintData data = jslint.validate(source, options);
		Assert.assertNotNull(data);
		System.err.println(data);
		Assert.assertTrue(data.isValid());
	}

	@Test
	public void testBuiltinJSLint_sloppy() throws IOException {
		JSLint jslint = new JSLint();
		JSLintOptions options = new JSLintOptions();
		options.setOption("sloppy", true);
		String source = "function hello() { }";
		JSLintData data = jslint.validate(source, options);
		Assert.assertNotNull(data);
		System.err.println(data);
		Assert.assertTrue(data.isValid());
	}

}
