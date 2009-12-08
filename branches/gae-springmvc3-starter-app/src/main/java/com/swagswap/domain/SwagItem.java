package com.swagswap.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

/**
 * Represents one piece of swag
 */
@SuppressWarnings("unchecked")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SwagItem {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;

	@Persistent
	private String name;

	//Text and Blob are not part of the default fetch group
	@Persistent
	private Text description;

	/**
	 * Owned One-to-One Relationship (lazy loaded)
	 * see http://code.google.com/appengine/docs/java/datastore/relationships.html
	 */
	@Persistent
	private SwagImage image;
	
	/**
	 * Store this so we can get the images separately with an image servlet
	 * to show them with an image tag but not have to load them twice
	 * (once when retrieving it from SwagItem, and once when looking it up with the servlet)
	 */
	@Persistent
	private String imageKey;
	
	//just used to store imageBytes from the HTML form
	@NotPersistent
	private byte[] imageBytes;

	@Persistent
	private String owner;

	@Persistent
	private Float rating;

	@Persistent
	private Integer numberOfRatings;

	@Persistent
	private Date created;

	@Persistent
	private Date lastUpdated;

	@Persistent(defaultFetchGroup = "true")
	private List<String> tags = new ArrayList<String>();

	@Persistent(defaultFetchGroup = "true")
	private List<String> comments = new ArrayList<String>();

	
	public SwagItem() {
		super();
	}
	
	public SwagItem(String name, String description, SwagImage image,
			String owner, Float rating, Integer numberOfRatings,
			List<String> tags, List<String> comments) {
		super();
		this.name = name;
		this.description = new Text(description);
		this.image = image;
		this.owner = owner;
		this.rating = rating;
		this.numberOfRatings = numberOfRatings;
		this.tags = tags;
		this.comments = comments;
	}
	
	public boolean isNew() {
		return getKey() == null;
	}
	
	// SwagItem has a new image if imageBytes is filled from file upload
	public boolean hasNewImage() {
		return (imageBytes != null && imageBytes.length !=0);
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return (description==null)?"":description.getValue();
	}

	public void setDescription(String description) {
		this.description = new Text(description);
	}

	public SwagImage getImage() {
		return image;
	}

	public void setImage(SwagImage image) {
		this.image = image;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
	
	//Lazily populate this
	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public Integer getNumberOfRatings() {
		return numberOfRatings;
	}

	public void setNumberOfRatings(Integer numberOfRatings) {
		this.numberOfRatings = numberOfRatings;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	//Leave out created field
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + ((imageKey == null) ? 0 : imageKey.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((numberOfRatings == null) ? 0 : numberOfRatings.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		return result;
	}

	/**
	 * Note, Blobs and children are lazy loaded. You can't just compare
	 * description fields for example, you have to actually call the
	 * getDescription() method to have it loaded.  Same with image (since it's a child object).
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SwagItem other = (SwagItem) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!getDescription().equals(other.getDescription()))
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!getImage().equals(other.getImage()))
			return false;
	    //TODO why is imageKey sometimes null from getAll ?
//		if (imageKey == null) {
//			if (other.imageKey != null)
//				return false;
//		} else if (!imageKey.equals(other.imageKey))
//			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (lastUpdated == null) {
			if (other.lastUpdated != null)
				return false;
		} else if (!lastUpdated.equals(other.lastUpdated))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numberOfRatings == null) {
			if (other.numberOfRatings != null)
				return false;
		} else if (!numberOfRatings.equals(other.numberOfRatings))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (rating == null) {
			if (other.rating != null)
				return false;
		} else if (!rating.equals(other.rating))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}


}