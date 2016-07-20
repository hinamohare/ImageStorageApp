/**
 * This class implements the getters and setters for  the attributes of an image like photo, photoName, photoID
 * */
package executors;

public class PhotoInfo {

	public String photoName = "";
	public Integer photoID = -1;

	public PhotoInfo()
	{
		
	}
	public PhotoInfo(Integer _photoID, String _photoName) {
		this.photoID = _photoID;
		this.photoName = _photoName;
		
	}
}
