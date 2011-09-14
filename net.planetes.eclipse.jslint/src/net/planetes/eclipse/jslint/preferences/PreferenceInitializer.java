package net.planetes.eclipse.jslint.preferences;

import net.planetes.eclipse.jslint.Activator;
import net.planetes.jslint.JSLintOption;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault(PreferenceConstants.P_JSLINT_FILE, "");
		store.setDefault(PreferenceConstants.P_SHOW_PROBLEMS, true);
		store.setDefault(PreferenceConstants.P_SHOW_FUNCTIONS, true);
		store.setDefault(PreferenceConstants.P_SHOW_GLOBALS, true);

		for (JSLintOption option : JSLintOption.loadOptions()) {
			String name = option.getName();
			JSLintOption.Type type = option.getType();
			if (type == JSLintOption.Type.STRING) {
				store.setDefault(name, option.getString());
			} else if (type == JSLintOption.Type.INT) {
				store.setDefault(name, option.getInt());
			} else {
				store.setDefault(name, option.getBoolean());
			}
		}
	}

}
