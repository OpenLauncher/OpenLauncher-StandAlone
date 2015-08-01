package openlauncher.minecraft;


import openlauncher.OpenLauncher;
import openlauncher.util.DownloadUtils;

import java.io.File;
import java.io.IOException;

public class MinecraftVersionInstaller {

	public static void installMc(String version, OpenLauncher openLauncher) {
		openLauncher.logger.info("Downloading minecraft version " + version);
		File mcverDir = new File(openLauncher.getMcDir(), "versions/" + version);
		if (!mcverDir.exists()) {
			mcverDir.mkdirs();
		}
		try {
			DownloadUtils.downloadFile("https://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".jar", mcverDir, version + ".jar", "");//MD5
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			DownloadUtils.downloadFile("https://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".json", mcverDir, version + ".json", "");//MD5
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
