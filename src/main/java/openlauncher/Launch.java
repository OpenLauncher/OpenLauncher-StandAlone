package openlauncher;

import openlauncher.gui.LauncherForm;

import java.util.HashMap;
import java.util.Map;

public class Launch {

	public static Main main = new Main();
	public static LauncherForm form;

	public static Map<String, ModPack> packMap = new HashMap<String, ModPack>();

	public static void main(String[] args) {
		form = new LauncherForm();

		form.openGui();

		main.start(form);
	}
}
