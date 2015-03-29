package openlauncher;

public class ModPackInstance {

	String instanceName;
	String forgeVersion;
	String minecraftVersion;
	String version;
	String type;
	String typeDownloadURL;

	public ModPackInstance(String instanceName, String forgeVersion, String minecraftVersion, String version, String type, String typeDownloadURL) {
		this.instanceName = instanceName;
		this.forgeVersion = forgeVersion;
		this.minecraftVersion = minecraftVersion;
		this.version = version;
		this.type = type;
		this.typeDownloadURL = typeDownloadURL;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeDownloadURL() {
		return typeDownloadURL;
	}

	public void setTypeDownloadURL(String typeDownloadURL) {
		this.typeDownloadURL = typeDownloadURL;
	}
}
