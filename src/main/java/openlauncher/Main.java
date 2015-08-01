package openlauncher;

import openlauncher.gui.LauncherForm;
import openlauncher.modPack.ModPack;

import java.util.ArrayList;

public class Main {

	//TODO http://www.techrepublic.com/article/work-with-java-web-start-beyond-the-sandbox/
	//TODO https://docs.oracle.com/javase/tutorial/deployment/webstart/

	public static OpenLauncher openLauncher = new OpenLauncher();
	public static LauncherForm form;

	public static ArrayList<ModPack> modPacks = new ArrayList<ModPack>();

	public static boolean offlineDev = false;
	public static boolean dev = false;

	public static void main(String[] args) {

		for (String arg : args) {
			if (arg.equals("--offline")) {
				offlineDev = true;
			}
			if (arg.equals("--dev")) {
				dev = true;
			}
		}

		form = new LauncherForm();

		form.openGui();

		openLauncher.start(form);
	}
}
