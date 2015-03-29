package openlauncher.legacyATL;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import openlauncher.DownloadUtils;
import openlauncher.Launch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class ATLPackHelper {


	public List<ATLPack> packs; // Packs in the Launcher


	public void loadPacks() throws IOException {
		Launch.main.println("Loading ATL packs");
		File atlDir = new File(Launch.main.getHome(), "atl");
		if (!atlDir.exists()) {
			atlDir.mkdirs();
		}
		DownloadUtils.downloadFile("http://www.creeperrepo.net/OpenLauncher/launcher/json/packs.json", atlDir, "packs.json");
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
		Launch.main.println("Finished loading ATL packs");
	}
}
