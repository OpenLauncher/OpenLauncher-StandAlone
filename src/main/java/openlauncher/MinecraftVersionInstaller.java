package openlauncher;


import java.io.File;
import java.io.IOException;

public class MinecraftVersionInstaller {

	public static void installMc(String version, Main main) {
		main.print("Downloading" + version);
		File mcverDir = new File(main.mcDir, "versions/" + version);
		if (!mcverDir.exists()) {
			mcverDir.mkdirs();
		}
		try {
			DownloadUtils.downloadFile("https://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".jar", mcverDir, version + ".jar");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			DownloadUtils.downloadFile("https://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".json", mcverDir, version + ".json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
