package com.swagswap.domain;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SwagItemComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    //This makes it so we can use a String key instead of a non-portable Google key
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String encodedKey;
	
    @Persistent
	private Long swagItemKey;
	
	@Persistent
	private String swagSwapUserNickname;
	
	@Persistent
	private String commentText;
	
	@Persistent
	private Date created;
	
	public SwagItemComment() {
	}
	
	public SwagItemComment(Long swagItemKey) {
		this.swagItemKey=swagItemKey;
	}

	public SwagItemComment(Long key, String nickName, String commentText) {
		this(key);
		this.swagSwapUserNickname = nickName;
		this.commentText=commentText;
		
	}

	public Long getSwagItemKey() {
		return swagItemKey;
	}

	public void setSwagItemKey(Long swagItemKey) {
		this.swagItemKey = swagItemKey;
	}

	public String getSwagSwapUserNickname() {
		return swagSwapUserNickname;
	}

	public void setSwagSwapUserNickname(String swagSwapUserNickname) {
		this.swagSwapUserNickname = swagSwapUserNickname;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "SwagItemComment [encodedKey=" + encodedKey + ", swagItemKey="
				+ swagItemKey + ", swagSwapUserNickname=" + swagSwapUserNickname + "]";
	}
	
	
}
