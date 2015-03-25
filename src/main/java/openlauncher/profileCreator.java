package openlauncher;

import net.minecraft.launcher.Launcher;
import net.minecraft.launcher.profile.Profile;
import net.minecraft.launcher.profile.ProfileManager;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class profileCreator {

	public static void createProfile(File mcDir, String version, String name) throws FileNotFoundException, UnsupportedEncodingException {
		File json = new File(mcDir, "launcher_profiles.json");
		if (json.exists()) {
			JFrame frame = new JFrame();
			List<String> list = new ArrayList<String>();
		//TODO calling this starts the gui for the launcher
			Launcher launcher = new FakeMCLauncher(frame, Main.workDir, Proxy.NO_PROXY, (PasswordAuthentication) null, (String[]) list.toArray(new String[list.size()]), Integer.valueOf(100));
			ProfileManager manager = launcher.getProfileManager();
			try {
				manager.loadProfiles();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (manager.getProfiles().containsKey(name)) {
				System.out.println("This profile is there!");
			} else {
				Profile profile = new Profile();
				profile.setName(name);
				profile.setGameDir(mcDir);
				profile.setLastVersionId(version);
				manager.getProfiles().put(name, profile);
				System.out.println("added a new profile!");
			}

			manager.setSelectedProfile(name);

			try {
				System.out.println("Saving profiles");
				manager.saveProfiles();
			} catch (IOException e) {
				e.printStackTrace();
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
