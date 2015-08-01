package openlauncher.modPack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import openlauncher.Main;
import openlauncher.OpenLauncher;
import openlauncher.gui.LauncherForm;
import openlauncher.legacyATL.ATLPack;
import openlauncher.legacyATL.ATLPackHelper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PackLoader {

	private final JsonParser parser = new JsonParser();
	private final Gson gson;
	private final File jsonFile;
	private OpenLauncher openLauncher;

	public PackLoader(OpenLauncher openLauncher) {
		this.jsonFile = new File(openLauncher.getHome(), "packs.json");
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		this.gson = builder.create();
		this.openLauncher = openLauncher;
	}

	public void loadPacks(LauncherForm form) throws IOException {
		if (!Main.offlineDev) {
			JsonObject object = this.parser.parse(FileUtils.readFileToString(this.jsonFile)).getAsJsonObject();
			Object packs = new HashMap<String, ModPack>();
			Type stringStringMap = new TypeToken<Map<String, ModPack>>() {
			}.getType();
			packs = gson.fromJson(object.get("packs"), stringStringMap);
			Main.modPacks.addAll((ArrayList) packs);
		} else {
			for (int i = 0; i < 10; i++) {
				ModPack pack = new ModPack("Pack" + i);
				pack.setText("Pack " + i);
				Main.modPacks.add(pack);
			}
		}

		for (ModPack pack : Main.modPacks) {
			LauncherForm.packListString.addElement(pack.getInstanceName());
		}
		if (Main.offlineDev) {
			return;
		}
		ATLPackHelper atlPackHelper = new ATLPackHelper();
		atlPackHelper.loadPacks();
		if (atlPackHelper.packs != null) {
			for (ATLPack atlPack : atlPackHelper.packs) {
				ModPack modPack = new ModPack(atlPack.getName());
				modPack.setInstanceName(atlPack.getName());
				modPack.setJsonLocation("ATLPACK");
				modPack.setText(atlPack.getDescription());
				LauncherForm.packListString.addElement(modPack.getInstanceName());
				Main.modPacks.add(modPack);
			}
		}

//		form.packList.repaint();
	}
}

