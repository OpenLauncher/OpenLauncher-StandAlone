package openlauncher.legacyATL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 29/03/15.
 */
public class ATLPack {

	private int id;
	private int position;
	private String name;
	private ATLPackType type;
	private String code;
	private List<ATLPackVersion> versions;
	private List<ATLPackVersion> devVersions;
	private boolean createServer;
	private boolean leaderboards;
	private boolean logging;
	private String description;
	private String supportURL;
	private String websiteURL;
	private List<String> testers = new ArrayList<String>();
	private List<String> allowedPlayers = new ArrayList<String>();
	private String xml; // The xml for a version of the pack
	private String xmlVersion; // The version the XML above is for
	private String json; // The JSON for a version of the pack
	private String jsonVersion; // The version the JSON above is for

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ATLPackType getType() {
		return type;
	}

	public void setType(ATLPackType type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ATLPackVersion> getVersions() {
		return versions;
	}

	public void setVersions(List<ATLPackVersion> versions) {
		this.versions = versions;
	}

	public List<ATLPackVersion> getDevVersions() {
		return devVersions;
	}

	public void setDevVersions(List<ATLPackVersion> devVersions) {
		this.devVersions = devVersions;
	}

	public boolean isCreateServer() {
		return createServer;
	}

	public void setCreateServer(boolean createServer) {
		this.createServer = createServer;
	}

	public boolean isLeaderboards() {
		return leaderboards;
	}

	public void setLeaderboards(boolean leaderboards) {
		this.leaderboards = leaderboards;
	}

	public boolean isLogging() {
		return logging;
	}

	public void setLogging(boolean logging) {
		this.logging = logging;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSupportURL() {
		return supportURL;
	}

	public void setSupportURL(String supportURL) {
		this.supportURL = supportURL;
	}

	public String getWebsiteURL() {
		return websiteURL;
	}

	public void setWebsiteURL(String websiteURL) {
		this.websiteURL = websiteURL;
	}

	public List<String> getTesters() {
		return testers;
	}

	public void setTesters(List<String> testers) {
		this.testers = testers;
	}

	public List<String> getAllowedPlayers() {
		return allowedPlayers;
	}

	public void setAllowedPlayers(List<String> allowedPlayers) {
		this.allowedPlayers = allowedPlayers;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getXmlVersion() {
		return xmlVersion;
	}

	public void setXmlVersion(String xmlVersion) {
		this.xmlVersion = xmlVersion;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getJsonVersion() {
		return jsonVersion;
	}

	public void setJsonVersion(String jsonVersion) {
		this.jsonVersion = jsonVersion;
	}
}
