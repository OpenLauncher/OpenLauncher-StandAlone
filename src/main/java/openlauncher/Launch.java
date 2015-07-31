package openlauncher;

import openlauncher.gui.LauncherForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Launch {

	//TODO http://www.techrepublic.com/article/work-with-java-web-start-beyond-the-sandbox/
	//TODO https://docs.oracle.com/javase/tutorial/deployment/webstart/

	public static Main main = new Main();
	public static LauncherForm form;

	public static final Map<String, ModPack> packMap = new HashMap<String, ModPack>();
	public static ArrayList<ModPack> modPacks = new ArrayList<ModPack>();

	public static void main(String[] args) {

		form = new LauncherForm();

		form.openGui();

		main.start(form);
	}
}
