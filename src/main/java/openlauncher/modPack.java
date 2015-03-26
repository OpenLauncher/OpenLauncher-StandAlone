package openlauncher;

public class ModPack {

	String instanceName;
	String forgeVersion;
	String minecraftVersion;
	String text;

	public ModPack(String instanceName, String forgeVersion, String minecraftVersion, String info) {
		this.instanceName = instanceName;
		this.forgeVersion = forgeVersion;
		this.minecraftVersion = minecraftVersion;
		this.text = info;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getForgeVersion() {
		return forgeVersion;
	}

	public void setForgeVersion(String forgeVersion) {
		this.forgeVersion = forgeVersion;
	}

	public String getMinecraftVersion() {
		return minecraftVersion;
	}

	public void setMinecraftVersion(String minecraftVersion) {
		this.minecraftVersion = minecraftVersion;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
