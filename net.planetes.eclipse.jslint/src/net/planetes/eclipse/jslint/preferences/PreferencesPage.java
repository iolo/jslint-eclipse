package net.planetes.eclipse.jslint.preferences;

import net.planetes.eclipse.jslint.Activator;
import net.planetes.jslint.JSLintOption;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencesPage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public PreferencesPage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("JSLint Validator Plugin for Eclipse");
	}

	@Override
	public void createFieldEditors() {
		addField(new FileFieldEditor(PreferenceConstants.P_JSLINT_FILE,
				"&JSLint File", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.P_SHOW_PROBLEMS,
				"Show &Problems", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.P_SHOW_FUNCTIONS,
				"Show &Functions", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.P_SHOW_GLOBALS,
				"Show &Globals", getFieldEditorParent()));

		// FIXME: separator and/or grouping
		for (JSLintOption option : JSLintOption.loadOptions()) {
			String name = option.getName();
			String description = option.getDescription() + "(" + name + ")";
			FieldEditor fieldEditor;
			JSLintOption.Type type = option.getType();
			if (type == JSLintOption.Type.STRING) {
				fieldEditor = new StringFieldEditor(name, description,
						getFieldEditorParent());
			} else if (type == JSLintOption.Type.INT) {
				fieldEditor = new IntegerFieldEditor(name, description,
						getFieldEditorParent());
			} else {
				fieldEditor = new BooleanFieldEditor(name, description,
						getFieldEditorParent());
			}
			addField(fieldEditor);
		}
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}