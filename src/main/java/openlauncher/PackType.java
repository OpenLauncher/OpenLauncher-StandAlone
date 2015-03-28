package openlauncher;

//This is a master class for the subclasses
//
//ZipType -- this is a simple pack type where all of the mods and configs are in one big file
//				This one will be used mainly when people add 3rd party packs using the website


//Recommended
//JsonType -- this is a type where all of the mods are listed out and the launcher downloads them one by one

//LegacyType -- this is for downloading and launching old ATL based xml mod packs. --No new packs will be made in this style.
//TODO look into converting the old LegacyType into the JsonType

//CustomPack -- this is downloading a mod pack from a small code, this code will be generated on the website
public class PackType {

	public void checkMods(ModPackInstance modPackInstance){
		//TODO extend this class to allow the mods to be downloaded.
	}

	public static PackType getPackTypeFromString(String name){
		if(name.equals("zip")){
			return new ZipPackType();
		}
		return null;
	}
}
