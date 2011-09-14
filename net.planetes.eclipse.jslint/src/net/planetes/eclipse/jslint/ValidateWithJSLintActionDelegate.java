package net.planetes.eclipse.jslint;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import net.planetes.eclipse.jslint.preferences.PreferenceConstants;
import net.planetes.eclipse.utils.ConsoleUtils;
import net.planetes.eclipse.utils.GenericFileLinkPatternMatchListener;
import net.planetes.jslint.JSLint;
import net.planetes.jslint.JSLintData;
import net.planetes.jslint.JSLintError;
import net.planetes.jslint.JSLintFunction;
import net.planetes.jslint.JSLintOption;
import net.planetes.jslint.JSLintOptions;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class ValidateWithJSLintActionDelegate extends ActionDelegate implements
		Runnable {

	private static final String CONSOLE_NAME = "JSLint";

	private static final Pattern FILE_LINK_PATTERN = Pattern
			.compile("\\(([a-zA-Z0-9.-/]+):([0-9]+):([0-9]+)\\)");

	private static final int FILE_LINK_FILE_GROUP_INDEX = 1;

	private static final int FILE_LINK_LINE_GROUP_INDEX = 2;

	private static final Pattern FILE_LINK_LINE_QUALIFIER_PATTERN = Pattern
			.compile("-->");

	private IFile sourceFile;

	public ValidateWithJSLintActionDelegate() {
	}

	//
	// extends ActionDelegate
	//

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection.isEmpty() || !(selection instanceof IStructuredSelection)) {
			return;
		}
		sourceFile = (IFile) ((IStructuredSelection) selection)
				.getFirstElement();
	}

	@Override
	public void run(IAction action) {
		Display.getCurrent().asyncExec(this);
	}

	//
	// implements Runnable
	//

	@Override
	public void run() {
		MessageConsole console = ConsoleUtils.getConsole(CONSOLE_NAME);

		console.activate();

		MessageConsoleStream consoleStream = console.newMessageStream();

		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();

		File jslintFile = new File(
				prefs.getString(PreferenceConstants.P_JSLINT_FILE));
		boolean showProblems = prefs
				.getBoolean(PreferenceConstants.P_SHOW_PROBLEMS);
		boolean showFunctions = prefs
				.getBoolean(PreferenceConstants.P_SHOW_FUNCTIONS);
		boolean showGlobals = prefs
				.getBoolean(PreferenceConstants.P_SHOW_GLOBALS);

		JSLint jslint;
		try {
			if (jslintFile.exists() && jslintFile.isFile()
					&& jslintFile.canRead()) {
				jslint = new JSLint(jslintFile);
			} else {
				jslint = new JSLint();
			}
		} catch (IOException e) {
			consoleStream.println("Failed to read jslint source!");
			e.printStackTrace();
			return;
		}

		JSLintOptions options = toJSLintOptions(prefs);

		String source;
		try {
			source = readSource(sourceFile);
		} catch (Exception e) {
			consoleStream.println("Failed to read javascript source!");
			e.printStackTrace();
			return;
		}

		consoleStream.print("Validate with JSLint... ");

		JSLintData data = jslint.validate(source, options);

		if (data.isValid()) {
			consoleStream.println("OK!");
		} else {
			consoleStream.println("FAIL!");
		}

		consoleStream.println();
		consoleStream.println("Options:");
		consoleStream.println(options.toJSONString());

		// FIXME:
		GenericFileLinkPatternMatchListener patternMatchListener = new GenericFileLinkPatternMatchListener(
				sourceFile.getProject().getLocation(), FILE_LINK_PATTERN,
				FILE_LINK_LINE_QUALIFIER_PATTERN, FILE_LINK_FILE_GROUP_INDEX,
				FILE_LINK_LINE_GROUP_INDEX);
		console.addPatternMatchListener(patternMatchListener);

		String location = sourceFile.getProjectRelativePath().toString();

		if (showProblems) {
			consoleStream.println();
			if (data.hasError()) {
				consoleStream.println("Problems:");
				printProblems(location, data.getErrors(), consoleStream);
			} else {
				consoleStream.println("No Problems.");
			}
		}

		if (showFunctions) {
			consoleStream.println();
			if (data.hasFunction()) {
				consoleStream.println("Functions:");
				printFunctions(location, data.getFunctions(), consoleStream);
			} else {
				consoleStream.println("No Functions.");
			}
		}

		if (showGlobals) {
			consoleStream.println();
			// .. printGlobals();
		}

		consoleStream.println("That's all folks!");
		consoleStream.println();

		// FIXME:
		// console.removePatternMatchListener(patternMatchListener);
	}

	//
	//
	//

	private static JSLintOptions toJSLintOptions(IPreferenceStore store) {
		JSLintOptions options = new JSLintOptions();
		for (JSLintOption option : JSLintOption.loadOptions()) {
			String name = option.getName();
			JSLintOption.Type type = option.getType();
			if (type == JSLintOption.Type.BOOL) {
				options.setOption(name, store.getBoolean(name));
			} else if (type == JSLintOption.Type.INT) {
				options.setOption(name, store.getInt(name));
			} else if (type == JSLintOption.Type.STRING) {
				options.setOption(name, store.getString(name));
			}

		}
		return options;
	}

	private static String readSource(IFile sourceFile) throws CoreException,
			IOException {
		InputStream in = null;
		try {
			in = sourceFile.getContents();
			return IOUtils.toString(in, sourceFile.getCharset());
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	private static void printProblems(String location, JSLintError[] errors,
			MessageConsoleStream consoleStream) {
		for (JSLintError error : errors) {
			StringBuilder sb = new StringBuilder();
			sb.append("--> (").append(location).append(":")
					.append(error.getLine()).append(":")
					.append(error.getCharacter()).append(")\t")
					.append(error.getReason());
			consoleStream.println(sb.toString());
			// if(error.getEvidence() != null) {
			// consoleStream.println(error.getEvidence());
			// }
		}
	}

	private static void printFunctions(String location,
			JSLintFunction[] functions, MessageConsoleStream consoleStream) {
		for (JSLintFunction func : functions) {
			StringBuilder sb = new StringBuilder();
			sb.append("--> (").append(location).append(":")
					.append(func.getLine()).append(":").append(func.getLast())
					.append(")\t").append(func.getName());
			consoleStream.println(sb.toString());
		}
	}

}
