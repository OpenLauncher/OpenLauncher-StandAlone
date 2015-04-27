package openlauncher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import openlauncher.gui.VersionSelection;
import openlauncher.jsonType.JsonType;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
		packFolder = new File(main.getHome(), "instances/" + pack.getInstanceName());
		if (!packFolder.exists()) {
			packFolder.mkdirs();
		}
		if (pack.getInstanceName().equals("ATLPACK")) {
			//TODO convert to the new pack json format - this will be done with a converter, mabey
		} else {
			DownloadUtils.downloadFile(pack.getJsonLocation(), packFolder, pack.getInstanceName() + ".json");
		}
		instances = getInstances(new File(packFolder, pack.getInstanceName() + ".json"));
		VersionSelection.versionList.clear();
		for (ModPackInstance instance : instances) {
			VersionSelection.versionList.addElement(instance.getVersion());
		}
		if (!isInstalled()) {
			VersionSelection.main(this);
		} else {
			if (!isNewest()) {
				int o = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "An update is available! Would you like to update?","Update!", o);
				if(dialogResult == JOptionPane.YES_OPTION){
					if(new File(packFolder, "mods").exists()){
						new File(packFolder, "mods").delete();
					}
					if(new File(packFolder, "config").exists()){
						new File(packFolder, "config").delete();
					}
					if(new File(packFolder, "instance.json").exists()){
						new File(packFolder, "instance.json").delete();
					}
					VersionSelection.main(this);
					//TODO look at this and see if it is working right.
				} else {
					continueInstall(getInstalledInstance());
				}
				//TODO update packs
			} else {
				continueInstall(getInstalledInstance());
			}
		}

	}

	public void continueInstall(String version) {
		for (ModPackInstance packInstance : instances) {
			if (packInstance.version.equals(version)) {
				continueInstall(packInstance);
			}
		}
	}

	public void continueInstall(ModPackInstance instance) {
		System.out.println(instance.version);
		if (!isInstalled()) {
			if (instance.type.equals("zip")) {
				new ZipPackType().checkMods(instance);
			} else if (instance.type.equals("legacy")) {
				new LegacyType().checkMods(instance);
			} else if (instance.type.equals("json")) {
				new JsonType().checkMods(instance);
			}
			ModPackInstance installedInstance = instance;
			Gson gson = new Gson();
			String json = gson.toJson(installedInstance);
			try {
				FileWriter writer = new FileWriter(new File(packFolder, "instance.json"));
				writer.write(json);
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Launch.main.launch(instance.getInstanceName(), instance.getForgeVersion(), instance.getMinecraftVersion());
	}

	public boolean isInstalled() {
		return new File(packFolder, "instance.json").exists();
	}

	public boolean isNewest() throws FileNotFoundException {
		if (!new File(packFolder, "instance.json").exists()) {
			return false;
		}
		if (instances.size() == 1) {
			return true;
		}
		Gson gson = new Gson();
		BufferedReader br = new BufferedReader(new FileReader(new File(packFolder, "instance.json")));
		ModPackInstance installedInstance = gson.fromJson(br, ModPackInstance.class);
		if (!installedInstance.getVersion().equals(instances.get(0).getVersion())) {
			return false;
		}
		return true;
	}

	public ModPackInstance getInstalledInstance() throws FileNotFoundException {
		if (!new File(packFolder, "instance.json").exists()) {
			return null;
		}
		Gson gson = new Gson();
		BufferedReader br = new BufferedReader(new FileReader(new File(packFolder, "instance.json")));
		ModPackInstance installedInstance = gson.fromJson(br, ModPackInstance.class);
		return installedInstance;
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
		packMap.putAll((Map) packs);
		ArrayList<ModPackInstance> instances = new ArrayList<ModPackInstance>();
		Iterator it = packMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			instances.add((ModPackInstance) pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}

		return instances;
	}
}
