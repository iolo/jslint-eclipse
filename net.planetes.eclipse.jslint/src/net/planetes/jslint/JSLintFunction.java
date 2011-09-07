package net.planetes.jslint;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

public class JSLintFunction {

	public static final JSLintFunction[] EMPTY_LIST = new JSLintFunction[0];

	private final String name;
	private final int line;
	private final int last;

	public JSLintFunction(NativeObject nobj) {
		name = (String) nobj.get("name", nobj);
		line = ((Number) nobj.get("line", nobj)).intValue();
		// TODO: more properties...
		last = 0;// ((Number) nobj.get("last", nobj)).intValue();
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

	public static JSLintFunction[] fromNativeArray(NativeArray narray) {
		if (narray == null) {
			return EMPTY_LIST;
		}
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
