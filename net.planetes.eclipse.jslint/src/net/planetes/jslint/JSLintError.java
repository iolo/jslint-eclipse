package net.planetes.jslint;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

public class JSLintError {

	public static final JSLintError[] EMPTY_LIST = new JSLintError[0];

	private int line;
	private int character;
	private String reason;
	private String evidence;

	public JSLintError(NativeObject nobj) {
		line = ((Number) nobj.get("line", nobj)).intValue();
		character = ((Number) nobj.get("character", nobj)).intValue();
		reason = (String) nobj.get("reason", nobj);
		evidence = nobj.has("evidence", nobj) ? (String) nobj.get("evidence",
				nobj) : null;
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

	public static JSLintError[] fromNativeArray(NativeArray narray) {
		if (narray == null) {
			return EMPTY_LIST;
		}
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
