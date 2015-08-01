package openlauncher.minecraft;

import cpw.mods.fml.installer.ClientInstall;

import java.io.File;

public class ForgeInstaller {

	public static void installForge(File mcdir) {
		ClientInstall install = new ClientInstall();
		install.run(mcdir);
	}
}
