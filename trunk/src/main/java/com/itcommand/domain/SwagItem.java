package com.itcommand.domain;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;

/**
 * Represents one piece of swag
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SwagItem {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;

	@Persistent
	private String name;

	@Persistent
	private String description;

	@Persistent
	@Embedded
	private SwagImage image;
	
	@PersistenceCapable
	@EmbeddedOnly
	public static class SwagImage {
		public Blob image;
		public String filename;
		public String mimeType;

		public SwagImage(byte[] image) {
			this.image = new Blob(image);
		}
	}
	
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
	private List<String> tags;

	@Persistent(defaultFetchGroup = "true")
	private List<String> comments;

	
	
	
	public boolean isNew() {
		return getKey() == null;
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
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

}