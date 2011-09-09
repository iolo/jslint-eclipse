package net.planetes.jslint;

import net.planetes.rhino.utils.RhinoUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class JSLintOptionsTest {

	private JSLintOptions opts;

	@Before
	public void setUp() {
		opts = new JSLintOptions();
		opts.setOption("sloppy", true);
	}

	@Test
	public void test_hasOption() {
		System.err.println(opts);
		Assert.assertTrue(opts.hasOption("sloppy"));
		Assert.assertFalse(opts.hasOption("evil"));
	}

	@Test
	public void test_tJSObject() {
		Context context = Context.enter();
		try {
			Scriptable scope = context.initStandardObjects();

			Scriptable nobj = opts.toJSObject(context, scope);
			System.err.println(RhinoUtils.toString(nobj, true));

			Assert.assertNotNull(nobj);
			Assert.assertTrue(nobj.has("sloppy", nobj));
			Assert.assertFalse(nobj.has("evil", nobj));
			Assert.assertEquals(true, nobj.get("sloppy", nobj));
		} finally {
			Context.exit();
		}
	}

}
