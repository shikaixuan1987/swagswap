package com.swagswap.domain;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

/**
 * Used as a vessel to hold the actual image data so that appengine
 * can save it to a Blob (just saving the byte[] is not allowed due 
 * to size constraints 
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SwagImage implements Serializable {

	private static final long serialVersionUID = 1L;

	// Have to use type Key (in this case a String key, see next comment)
	// here (not Long) or SwagImage can't be a child of SwagItem.
	// The key here has to be able to include the parent's key
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    //This makes it so we can use a String key instead of a non-portable Google one
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String encodedKey;
	
    @Persistent
	private Blob image;
    @Persistent
    private String filename;
    @Persistent
    private String mimeType;
    
    public SwagImage() {
    }
    
	public SwagImage(byte[] image) {
		this.image = new Blob(image);
	}
  
	public String getEncodedKey() {
		return encodedKey;
	}

	public void setEncodedKey(String encodedKey) {
		this.encodedKey = encodedKey;
	}

	public Blob getImage() {
		return image;
	}
	public void setImage(Blob image) {
		this.image = image;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((encodedKey == null) ? 0 : encodedKey.hashCode());
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
		return result;
	}

	// Here we had to use the getImage() method to force image loading
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SwagImage other = (SwagImage) obj;
		//TODO For some reason encodedKey is null when you fetch a list of SwagItems
//		if (encodedKey == null) {
//			if (other.encodedKey != null)
//				return false;
//		} else if (!encodedKey.equals(other.encodedKey))
//			return false;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!getImage().equals(other.getImage()))
			return false;
		if (mimeType == null) {
			if (other.mimeType != null)
				return false;
		} else if (!mimeType.equals(other.mimeType))
			return false;
		return true;
	}
	
	
    
    

}
