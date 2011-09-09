package net.planetes.rhino.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class RhinoUtilsTest {

	private Context context;
	private Scriptable scope;
	private Scriptable nobj;

	@Before
	public void setUp() {
		context = Context.enter();
		scope = context.initStandardObjects();
		nobj = context.newObject(scope);
		nobj.put("int", nobj, 123);
		nobj.put("str", nobj, "abc");
	}

	@After
	public void tearOff() {
		Context.exit();
	}

	@Test
	public void test_getIntProperty() {
		Assert.assertEquals(123, RhinoUtils.getIntProperty(nobj, "int"));
		Assert.assertEquals(456, RhinoUtils.getIntProperty(nobj, "str", 456));
		Assert.assertEquals(456,
				RhinoUtils.getIntProperty(nobj, "not_found", 456));
	}

	@Test(expected = ClassCastException.class)
	public void test_getIntProperty_typeError() {
		RhinoUtils.getIntProperty(nobj, "str");
	}

	@Test(expected = ClassCastException.class)
	public void test_getIntProperty_notFoundError() {
		RhinoUtils.getIntProperty(nobj, "not_found");
	}

	@Test
	public void test_getStringProperty() {
		Assert.assertEquals("abc", RhinoUtils.getStringProperty(nobj, "str"));
		Assert.assertEquals("xyz",
				RhinoUtils.getStringProperty(nobj, "not_found", "xyz"));
	}

	@Test(expected = ClassCastException.class)
	public void test_getStringProperty_typeError() {
		RhinoUtils.getStringProperty(nobj, "int");
	}

	@Test(expected = ClassCastException.class)
	public void test_getStringProperty_notFoundError() {
		RhinoUtils.getStringProperty(nobj, "not_found");
	}

	@Test
	public void test_toString() {
		System.err.println(RhinoUtils.toString(null, true));
		System.err.println(RhinoUtils.toString(Scriptable.NOT_FOUND, true));
		System.err.println(RhinoUtils.toString(nobj, true));
		System.err.println(RhinoUtils.toString(nobj, false));
	}

}
