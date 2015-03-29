package openlauncher;

import javax.json.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class profileCreator {

	public static void createProfile(File mcDir, String version, String name, File instanceDir) throws FileNotFoundException, UnsupportedEncodingException {
		File json = new File(mcDir, "launcher_profiles.json");
		//TODO rewrite this again
		//Read the file, then rewrite it with the extra data
		if (json.exists()) {
			JsonReader reader = Json.createReader(new FileReader(json));
			JsonObject file = reader.readObject();
			reader.close();
			JsonObject profiles = file.getJsonObject("profiles");
			if(!profiles.containsKey(name)){
				JsonObject newProfile = Json.createObjectBuilder()
						.add("name", name)
						.add("gameDir", instanceDir.toString())
						.add("lastVersionId", version)
						.build();
				JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
						.add(name, newProfile);

				for (java.util.Map.Entry<String, JsonValue> entry : profiles.entrySet())
					objectBuilder.add(entry.getKey(), entry.getValue());

				objectBuilder.add("selectedProfile", name);
				if(file.getJsonString("clientToken") != null){
					objectBuilder.add("clientToken", file.getJsonString("clientToken"));
				}
				if(!file.getJsonObject("authenticationDatabase").isEmpty()){
					objectBuilder.add("authenticationDatabase", file.getJsonObject("authenticationDatabase"));
				}
				if(file.getJsonString("selectedUser") != null){
					objectBuilder.add("selectedUser", file.getJsonString("selectedUser"));
				}

				JsonObject newProfiles = objectBuilder.build();

				try {
					JsonWriter writer = null;
					writer = Json.createWriter(new FileWriter(json));
					writer.writeObject(newProfile);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			System.out.println("Saved");
		} else {
			PrintWriter writer = new PrintWriter(json, "UTF-8");

			writer.println("{");
			writer.println("  \"profiles\": {");
			writer.println("      \"" + name + "\": {");
			writer.println("      \"name\": \"" + name + "\",");
			writer.println("      \"gameDir\": \"/Users/mark/Documents/GitHub/OpenLauncher-StandAlone/OpenLauncher/Instances/" + name + "\",");
			writer.println("      \"lastVersionId\": \"" + version + "\",");
			writer.println("      \"javaArgs\": \"-Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M\",");
			writer.println("      \"resolution\": {");
			writer.println("        \"width\": 854,");
			writer.println("        \"height\": 480");
			writer.println("      },");
			writer.println("      \"useHopperCrashService\": false");
			writer.println("    }");
			writer.println("  },");
			writer.println("  \"selectedProfile\": \"" + name + "\"");
			writer.println("}");


			writer.close();
			System.out.println("Created a new profile!");
		}

	}
}
