package com.itcommand.domain;

import java.io.Serializable;

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
 * to size contraints 
 * @author BDBRODS
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SwagImageHold implements Serializable {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
	private Blob image;
    @Persistent
    private String filename;
    @Persistent
    private String mimeType;
    
    public SwagImageHold() {
    }
    
	public SwagImageHold(byte[] image) {
		this.image = new Blob(image);
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
    
    

}
