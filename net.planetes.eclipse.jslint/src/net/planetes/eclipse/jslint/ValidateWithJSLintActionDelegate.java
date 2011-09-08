package net.planetes.eclipse.jslint;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import net.planetes.eclipse.utils.ConsoleUtils;
import net.planetes.eclipse.utils.GenericFileLinkPatternMatchListener;
import net.planetes.jslint.JSLint;
import net.planetes.jslint.JSLintData;
import net.planetes.jslint.JSLintError;
import net.planetes.jslint.JSLintFunction;
import net.planetes.jslint.JSLintOptions;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
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

	private static final Pattern FILE_LINK_LINE_QUALIFIER_PATTERN = Pattern
			.compile("-->");

	private final MessageConsole console;

	private final JSLint jslint;

	private IFile sourceFile;

	public ValidateWithJSLintActionDelegate() throws IOException {
		console = ConsoleUtils.getConsole(CONSOLE_NAME);
		jslint = new JSLint();

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
		// run(); //syncExec
	}

	//
	// implements Runnable
	//

	@Override
	public void run() {
		console.activate();

		GenericFileLinkPatternMatchListener patternMatchListener = new GenericFileLinkPatternMatchListener(
				sourceFile.getProject().getLocation(), FILE_LINK_PATTERN,
				FILE_LINK_LINE_QUALIFIER_PATTERN, 1, 2);

		console.addPatternMatchListener(patternMatchListener);

		MessageConsoleStream consoleStream = console.newMessageStream();

		consoleStream.print("Validate with JSLint... ");

		String source;
		InputStream in = null;
		try {
			in = sourceFile.getContents();
			source = IOUtils.toString(in, sourceFile.getCharset());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			IOUtils.closeQuietly(in);
		}

		JSLintOptions options = new JSLintOptions();
		options.setOption("browser", true);
		options.setOption("white", true);
		options.setOption("vars", true);
		
		JSLintData data = jslint.validate(source, options);

		if (data.isValid()) {
			consoleStream.println("OK!");
		} else {
			consoleStream.println("FAIL!");
		}

		if (data.hasError()) {
			consoleStream.println("Problems:");
			for (JSLintError error : data.getErrors()) {
				StringBuilder sb = new StringBuilder();
				sb.append("--> (").append(sourceFile.getProjectRelativePath())
						.append(":").append(error.getLine()).append(":")
						.append(error.getCharacter()).append(")\t")
						.append(error.getReason());
				consoleStream.println(sb.toString());
				// if(error.getEvidence() != null) {
				// consoleStream.println(error.getEvidence());
				// }
			}
		} else {
			consoleStream.println("No Problems.");
		}

		if (data.hasFunction()) {
			consoleStream.println("Functions:");
			for (JSLintFunction func : data.getFunctions()) {
				StringBuilder sb = new StringBuilder();
				sb.append("--> (").append(sourceFile.getProjectRelativePath())
						.append(":").append(func.getLine()).append(":")
						.append(func.getLast()).append(")\t")
						.append(func.getName());
				consoleStream.println(sb.toString());
			}
		} else {
			consoleStream.println("No Functions.");
		}

		consoleStream.println();

		// console.removePatternMatchListener(patternMatchListener);
	}
}
