package net.planetes.eclipse.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.ui.console.FileLink;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.console.IPatternMatchListener;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

public class GenericFileLinkPatternMatchListener implements
		IPatternMatchListener {

	private static final Pattern DEF_PATTERN = Pattern
			.compile("\\(([a-zA-Z0-9.-/]+):([0-9]+)\\)");

	private static final Pattern DEF_LINE_QUALIFIER_PATTERN = null;

	private final IPath basePath;

	private final Pattern pattern;

	private final Pattern lineQualifierPattern;

	private final int fileGroupIndex;

	private final int lineGroupIndex;

	private TextConsole console;

	public GenericFileLinkPatternMatchListener(IPath basePath, Pattern pattern,
			Pattern lineQualifierPattern, int fileGroupIndex, int lineGroupIndex) {
		this.basePath = basePath.addTrailingSeparator();
		this.pattern = pattern;
		this.lineQualifierPattern = lineQualifierPattern;
		this.fileGroupIndex = fileGroupIndex;
		this.lineGroupIndex = lineGroupIndex;
	}

	public GenericFileLinkPatternMatchListener(IPath basePath) {
		this(basePath, DEF_PATTERN, DEF_LINE_QUALIFIER_PATTERN, 1, 2);
	}

	@Override
	public void connect(TextConsole console) {
		this.console = console;
	}

	@Override
	public void disconnect() {
		this.console = null;
	}

	@Override
	public void matchFound(PatternMatchEvent event) {
		int offset = event.getOffset();
		int length = event.getLength();
		String text;
		try {
			text = console.getDocument().get(offset, length);
		} catch (BadLocationException ignore) {
			return;
		}

		Matcher matcher = pattern.matcher(text);
		if (!matcher.matches() || matcher.groupCount() < fileGroupIndex
				|| matcher.groupCount() < lineGroupIndex) {
			return;
		}

		IPath path = basePath.append(matcher.group(fileGroupIndex));
		IFile file = ResourcesPlugin.getWorkspace().getRoot()
				.getFileForLocation(path);
		if (file == null) {
			return;
		}
		int line = NumberUtils.toInt(matcher.group(lineGroupIndex), -1);
		FileLink fileLink = new FileLink(file, null, -1, -1, line);
		try {
			console.addHyperlink(fileLink, offset, length);
		} catch (BadLocationException ignore) {
			return;
		}
	}

	@Override
	public String getPattern() {
		return pattern.pattern();
	}

	@Override
	public int getCompilerFlags() {
		return pattern.flags();
	}

	@Override
	public String getLineQualifier() {
		return (lineQualifierPattern == null) ? null : lineQualifierPattern
				.pattern();
	}

}
