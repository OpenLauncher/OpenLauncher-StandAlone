package openlauncher.modPack;

public class ModPack {
	String instanceName;
	String text;
	String jsonLocation;

	public ModPack(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getJsonLocation() {
		return jsonLocation;
	}

	public void setJsonLocation(String jsonLocation) {
		this.jsonLocation = jsonLocation;
	}
}
