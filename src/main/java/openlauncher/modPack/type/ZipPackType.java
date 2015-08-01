package openlauncher.modPack.type;


import openlauncher.Main;
import openlauncher.modPack.ModPackInstance;
import openlauncher.modPack.PackType;
import openlauncher.util.DownloadUtils;
import openlauncher.util.UnZip;

import java.io.File;
import java.io.IOException;

public class ZipPackType extends PackType {

	@Override
	public void checkMods(ModPackInstance modPackInstance) {
		File workDir = new File(Main.openLauncher.getHome().getAbsoluteFile() + "/instances/", modPackInstance.getInstanceName());
		if (!workDir.exists())
			workDir.mkdirs();

		try {
			DownloadUtils.downloadFile(modPackInstance.getTypeDownloadURL(), workDir, modPackInstance.getInstanceName() + "-" + modPackInstance.getVersion() + ".zip", "");//md5
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			UnZip.unZip(new File(workDir, modPackInstance.getInstanceName() + "-" + modPackInstance.getVersion() + ".zip"), workDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
