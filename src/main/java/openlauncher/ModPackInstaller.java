package openlauncher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import openlauncher.gui.VersionSelection;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ModPackInstaller {

	static File packFolder;
	ModPack pack;

	ArrayList<ModPackInstance> instances;

	//This will check to see if anything needs downloading or updating, then it will load the pack
	public void playPack(ModPack pack) throws IOException {
		this.pack = pack;
		Main main = Launch.main;
		System.out.println(main);
		Launch.main.println("Starting " + pack.getInstanceName());
		packFolder = new File(main.getHome(), "packs/" + pack.getInstanceName());
		if(!packFolder.exists()){
			packFolder.mkdirs();
		}
		DownloadUtils.downloadFile(pack.getJsonLocation(), packFolder, pack.getInstanceName() + ".json");
		instances = getInstances(new File(packFolder, pack.getInstanceName() + ".json"));
		VersionSelection.versionList.clear();
		for(ModPackInstance instance : instances){
			VersionSelection.versionList.addElement(instance.getVersion());
		}
		if(!isInstalled()){
			VersionSelection.main(this);
		} else {
			//TODO get the installed version of the pack here.
			continueInstall(instances.get(0));
		}

	}

	public void continueInstall(String version){
		for(ModPackInstance packInstance : instances){
			if(packInstance.version.equals(version)){
				continueInstall(packInstance);
			}
		}
	}

	public void continueInstall(ModPackInstance instance){
		System.out.println(instance.version);
		if(!isInstalled()){
			//TODO install the pack here
			//TODO create a json file with info about the installed version
		}

		Launch.main.launch(instance.getInstanceName(), instance.getForgeVersion(), instance.getMinecraftVersion());
	}

	public boolean isInstalled(){
		//TODO check to see if the json file is here and then see if it is the newest
		return true;
	}


	public static ArrayList<ModPackInstance> getInstances(File json) throws IOException {
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		JsonObject object = parser.parse(FileUtils.readFileToString(json)).getAsJsonObject();
		Object packs = new HashMap<String, ModPackInstance>();
		Map<String, ModPack> packMap = new HashMap<String, ModPack>();
		Type stringStringMap = new TypeToken<Map<String, ModPackInstance>>() {
		}.getType();
		packs = gson.fromJson(object.get("versions"), stringStringMap);
		packMap.putAll((Map)packs);
		ArrayList<ModPackInstance> instances = new ArrayList<ModPackInstance>();
		Iterator it = packMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			instances.add((ModPackInstance) pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}

		return instances;
	}
}
