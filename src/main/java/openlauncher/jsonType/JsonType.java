package openlauncher.jsonType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import openlauncher.DownloadUtils;
import openlauncher.Launch;
import openlauncher.ModPackInstance;
import openlauncher.PackType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonType extends PackType {

	private final JsonParser parser = new JsonParser();
	private final Gson gson;

	public JsonType() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		this.gson = builder.create();
	}

	@Override
	public void checkMods(ModPackInstance modPackInstance) {
		File workDir = new File(Launch.main.getHome().getAbsoluteFile() + "/Instances/", modPackInstance.getInstanceName());
		if (!workDir.exists())
			workDir.mkdirs();

		File json = new File(workDir, "mods.json");
		ArrayList<DownloadableMod> mods = new ArrayList<DownloadableMod>();

		try {
			DownloadUtils.downloadFile(modPackInstance.getTypeDownloadURL(), workDir, json.getName());

			JsonObject object = this.parser.parse(FileUtils.readFileToString(json)).getAsJsonObject();
			Object modList = new HashMap<String, DownloadableMod>();
			Type stringStringMap = new TypeToken<Map<String, DownloadableMod>>() {
			}.getType();
			modList = gson.fromJson(object.get("mods"), stringStringMap);

			Map<String, DownloadableMod> modMap = new HashMap<String, DownloadableMod>();
			modMap.putAll((Map)modList);

			Iterator it = modMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				mods.add((DownloadableMod) pair.getValue());
			}



		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		for(DownloadableMod downloadableMod : mods){
			//Do checks on the mod to check to see if it sould be installed.
			if(downloadableMod.getDownload().equals("server")){
				mods.remove(downloadableMod);
			}
		}

		for(DownloadableMod downloadableMod : mods){
			//TODO download all of the mods in one go, so the user can see 1 progress bar for all mods
			try {
				DownloadUtils.downloadFile(downloadableMod.getUrl(), new File(workDir, "mods"), downloadableMod.getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//TODO check mod's md5
	}
}
