package openlauncher;

import openlauncher.gui.LauncherForm;

import java.util.HashMap;
import java.util.Map;

public class Launch {

	public static Main main = new Main();
	public static LauncherForm form;

	public static Map<String, ModPack> packMap = new HashMap<String, ModPack>();

	public static void main(String[] args) {

		ModPack newPack = new ModPack("test", "10.13.2.1342", "1.7.10", "This is a testing mod pack");
		packMap.put("test", newPack);

		//main.start();
		form = new LauncherForm();

		form.openGui();
	}
}
