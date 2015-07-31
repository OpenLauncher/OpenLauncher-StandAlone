package openlauncher;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ModPack {
	String instanceName;
	String text;
	String jsonLocation;
    String logoLocation;
    Texture texture;

	public ModPack(String instanceName, String text, String jsonLocation, String logoLocation, Texture texture) {
		this.instanceName = instanceName;
		this.text = text;
		this.jsonLocation = jsonLocation;
		this.logoLocation = logoLocation;
		this.texture = texture;
	}

	public ModPack() {
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

    public Texture getTexture() {
        if(getLogoLocation() == null || PackLoader.oflineDev){
            return null;
        }
        if(texture == null){
            File logoFolder = new File(Launch.main.getHome(), "img");
            File logoFile = new File(logoFolder,getInstanceName() + ".png");
            if(!logoFolder.exists()){
                logoFolder.mkdirs();
            }
            if(!logoFile.exists()){
                try {
                    DownloadUtils.downloadFile(getLogoLocation(), logoFolder, getInstanceName() + ".png");
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            try {
                texture = TextureLoader.getTexture("PNG", new FileInputStream(logoFile));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public String getLogoLocation() {
        return logoLocation;
    }

    public void setLogoLocation(String logoLocation) {
        this.logoLocation = logoLocation;
    }
}
