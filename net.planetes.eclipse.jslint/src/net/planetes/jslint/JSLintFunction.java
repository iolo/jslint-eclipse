package net.planetes.jslint;

import java.util.ArrayList;
import java.util.List;

import net.planetes.rhino.utils.RhinoUtils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

public class JSLintFunction {

	public static final JSLintFunction[] EMPTY_LIST = new JSLintFunction[0];

	private final String name;
	private final int line;
	private final int last;

	public JSLintFunction(NativeObject nobj) {
		name = RhinoUtils.getStringProperty(nobj, "name", "<unknown>");
		line = RhinoUtils.getIntProperty(nobj, "line", 1);
		last = RhinoUtils.getIntProperty(nobj, "line", -1);
		// TODO: more properties...
	}

	public String getName() {
		return name;
	}

	public int getLine() {
		return line;
	}

	public int getLast() {
		return last;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * convert a javascript array object to an array of JSLintFunction objects.
	 * 
	 * @param obj
	 *            a javascript array object or {@literal null}.
	 * @return an array of JSLintFunction objects
	 */
	public static JSLintFunction[] fromJSObject(Object obj) {
		if (obj == null || !(obj instanceof NativeArray)) {
			return EMPTY_LIST;
		}
		NativeArray narray = (NativeArray) obj;
		int length = (int) narray.getLength();
		List<JSLintFunction> result = new ArrayList<JSLintFunction>(length);
		for (int i = 0; i < length; i++) {
			NativeObject nobj = (NativeObject) narray.get(i, narray);
			if (nobj != null) {
				result.add(new JSLintFunction(nobj));
			}
		}
		return result.toArray(EMPTY_LIST);
	}

}
