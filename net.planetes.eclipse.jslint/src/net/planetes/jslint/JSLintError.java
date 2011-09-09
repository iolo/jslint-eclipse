package net.planetes.jslint;

import java.util.ArrayList;
import java.util.List;

import net.planetes.rhino.utils.RhinoUtils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

public class JSLintError {

	public static final JSLintError[] EMPTY_LIST = new JSLintError[0];

	private int line;
	private int character;
	private String reason;
	private String evidence;

	public JSLintError(NativeObject nobj) {
		line = RhinoUtils.getIntProperty(nobj, "line", 1);
		character = RhinoUtils.getIntProperty(nobj, "character", -1);
		reason = RhinoUtils.getStringProperty(nobj, "reason", "<unknown>");
		evidence = RhinoUtils.getStringProperty(nobj, "evidence", "<unknown>");
	}

	public int getLine() {
		return line;
	}

	public int getCharacter() {
		return character;
	}

	public String getReason() {
		return reason;
	}

	public String getEvidence() {
		return evidence;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * convert a javascript array object to an array of JSLintError objects.
	 * 
	 * @param obj
	 *            a javascript array object or {@literal null}.
	 * @return an array of JSLintError objects
	 */
	public static JSLintError[] fromJSObject(Object obj) {
		if (obj == null || !(obj instanceof NativeArray)) {
			return EMPTY_LIST;
		}
		NativeArray narray = (NativeArray) obj;
		int length = (int) narray.getLength();
		List<JSLintError> result = new ArrayList<JSLintError>(length);
		for (int i = 0; i < length; i++) {
			NativeObject nobj = (NativeObject) narray.get(i, narray);
			if (nobj != null) {
				result.add(new JSLintError(nobj));
			}
		}
		return result.toArray(EMPTY_LIST);
	}

}
