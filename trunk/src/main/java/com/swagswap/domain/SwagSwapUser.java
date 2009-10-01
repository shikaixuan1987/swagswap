package com.swagswap.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SwagSwapUser {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;
	
	//Never displayed. Used as identifier and to communicate with the user
	@Persistent
	private String email;
	
	//This is the google accounts nickName now.  Maybe make this changeable for swagswap
	//It is the name displayed as swagitem owner
	@Persistent
	private String nickName; 
	
	@Persistent
	private Set<SwagItemRating> swagItemRatings;// = new HashSet<SwagItemRating>();
	
	@Persistent
	private Date joined;
	
	public SwagSwapUser() {
	}
	
	public SwagSwapUser(String email, String nickName) {
		this.email=email;
		this.nickName=nickName;
	}
	
	public SwagSwapUser(String email) {
		this.email=email;
	}

	public boolean isNew() {
		return joined==null;
	}
	
	/**
	 * 
	 * @param swagItemKey
	 * @return SwagItem by key or null if not found
	 */
	public SwagItemRating getSwagItemRating(Long swagItemKey) {
		for (SwagItemRating rating: swagItemRatings) {
			if (swagItemKey==rating.getSwagItemKey()) {
				return rating;
			}
		}
		return null;
	}
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Set<SwagItemRating> getSwagItemRatings() {
		return swagItemRatings;
	}

	public void setSwagItemRatings(Set<SwagItemRating> ratedSwagItems) {
		this.swagItemRatings = ratedSwagItems;
	}

	public Date getJoined() {
		return joined;
	}
	public void setJoined(Date joined) {
		this.joined = joined;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((joined == null) ? 0 : joined.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((swagItemRatings == null) ? 0 : swagItemRatings.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SwagSwapUser other = (SwagSwapUser) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (joined == null) {
			if (other.joined != null)
				return false;
		} else if (!joined.equals(other.joined))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (swagItemRatings == null) {
			if (other.swagItemRatings != null)
				return false;
		} else if (!swagItemRatings.equals(other.swagItemRatings))
			return false;
		return true;
	}
	
	
	
}
