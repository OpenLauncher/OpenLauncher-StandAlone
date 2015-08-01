package openlauncher.legacyATL;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import openlauncher.Main;
import openlauncher.util.DownloadUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class ATLPackHelper {


	public List<ATLPack> packs; // Packs in the Launcher


	public void loadPacks() throws IOException {
		Main.openLauncher.logger.debug("Loading ATL packs");
		File atlDir = new File(Main.openLauncher.getHome(), "atl");
		if (!atlDir.exists()) {
			atlDir.mkdirs();
		}
		DownloadUtils.downloadFile("http://www.creeperrepo.net/OpenLauncher/launcher/json/packs.json", atlDir, "packs.json", "");//TODO md5
		try {
			java.lang.reflect.Type type = new TypeToken<List<ATLPack>>() {
			}.getType();
			this.packs = new GsonBuilder().setPrettyPrinting().create().fromJson(new FileReader(new File(atlDir, "packs.json")), type);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Main.openLauncher.logger.debug("Finished loading ATL packs");
	}
}
