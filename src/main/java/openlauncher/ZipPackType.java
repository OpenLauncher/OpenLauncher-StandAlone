package openlauncher;


import java.io.File;
import java.io.IOException;

public class ZipPackType extends PackType {

	@Override
	public void checkMods(ModPackInstance modPackInstance) {
		File workDir = new File(Launch.main.getHome().getAbsoluteFile() + "/", modPackInstance.getInstanceName());
		if (!workDir.exists())
			workDir.mkdirs();

		try {
			DownloadUtils.downloadFile(modPackInstance.typeDownloadURL, workDir, modPackInstance.getInstanceName() + "-" + modPackInstance.version + ".zip");
		} catch (IOException e) {
			e.printStackTrace();
		}

		UnZip.unZip(new File(workDir, modPackInstance.getInstanceName() + "-" + modPackInstance.version + ".zip"), workDir);
	}
}
