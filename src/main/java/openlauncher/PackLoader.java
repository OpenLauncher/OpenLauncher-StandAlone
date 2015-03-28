package openlauncher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import openlauncher.gui.LauncherForm;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PackLoader {

	private final JsonParser parser = new JsonParser();
	private final Gson gson;
	private final File jsonFile;
	private Main main;

	public PackLoader(Main main) {
		this.jsonFile = new File(main.getHome(), "packs.json");
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		this.gson = builder.create();
		this.main = main;
	}

	public void loadPacks(LauncherForm form) throws IOException {
		JsonObject object = this.parser.parse(FileUtils.readFileToString(this.jsonFile)).getAsJsonObject();
		Object packs = new HashMap<String, ModPack>();
		Type stringStringMap = new TypeToken<Map<String, ModPack>>() {
		}.getType();
		packs = gson.fromJson(object.get("packs"), stringStringMap);
		Launch.packMap.putAll((Map) packs);
		System.out.println(((Map) packs).keySet());
		Iterator it = Launch.packMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			LauncherForm.packListString.addElement(pair.getKey());
			it.remove(); // avoids a ConcurrentModificationException
		}
		form.packList.repaint();
	}
}

