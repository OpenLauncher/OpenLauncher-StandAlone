package openlauncher;

import gui.OpenLauncherGui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Launch {

	//TODO http://www.techrepublic.com/article/work-with-java-web-start-beyond-the-sandbox/
	//TODO https://docs.oracle.com/javase/tutorial/deployment/webstart/

	public static Main main = new Main();
	public static OpenLauncherGui form;

	public static final Map<String, ModPack2> packMap = new HashMap<String, ModPack2>();
	public static ArrayList<ModPack2> modPacks = new ArrayList<ModPack2>();

	public static void main(String[] args) {

        if(new File(Main.getHome(), "jar").exists()){
            for(File file: new File(Main.getHome(), "jar").listFiles() ){
                main.addToClasspath(file);
            }
            System.setProperty("org.lwjgl.librarypath", new File(Main.getHome(), "natives").getAbsolutePath());
        }



		form = new OpenLauncherGui();
		main.start(form);

        form.start();
	}
}
