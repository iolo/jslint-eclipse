package net.planetes.eclipse.utils;

import org.apache.commons.io.IOUtils;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class ConsoleUtils {

	public static MessageConsole getConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager consoleManager = plugin.getConsoleManager();
		for (IConsole existingConsole : consoleManager.getConsoles()) {
			if (name.equals(existingConsole.getName())) {
				return (MessageConsole) existingConsole;
			}
		}
		MessageConsole newConsole = new MessageConsole(name, null);
		consoleManager.addConsoles(new IConsole[] { newConsole });
		consoleManager.showConsoleView(newConsole);
		return newConsole;
	}

	public static void println(MessageConsole console, String line, Color color) {
		MessageConsoleStream out = console.newMessageStream();
		try {
			if (color != null) {
				out.setColor(color);
			}
			out.println(line);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static void println(MessageConsole console, String line) {
		MessageConsoleStream out = console.newMessageStream();
		try {
			out.println(line);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

}
