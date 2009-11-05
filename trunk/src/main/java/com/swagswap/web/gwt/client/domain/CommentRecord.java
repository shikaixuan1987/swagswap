package com.swagswap.web.gwt.client.domain;

import java.util.Date;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.swagswap.domain.SwagItemComment;

/**
 * adaptor domain object to show comments in a SmartGWT ListGrid
 *
 */
public class CommentRecord extends ListGridRecord {
		
    public CommentRecord() {
    }

    public CommentRecord(SwagItemCommentGWTDTO swagItemComment) {
    	setSwagSwapUserID(swagItemComment.getSwagSwapUserID());
    	setSwagSwapUserNickname(swagItemComment.getSwagSwapUserNickname());
    	setCommentText(swagItemComment.getCommentText());
    	setCreated(swagItemComment.getCreated());
    }

    public String getSwagSwapUserNickname() {
    	return getAttributeAsString("swagSwapUserNickname");
	}

	public void setSwagSwapUserNickname(String swagSwapUserNickname) {
		setAttribute("swagSwapUserNickname", swagSwapUserNickname);
	}
	
	public String getSwagSwapUserID() {
		return getAttributeAsString("swagSwapUserID");
	}
	
	public void setSwagSwapUserID(String swagSwapUserID) {
		setAttribute("swagSwapUserID", swagSwapUserID);
	}

	public String getCommentText() {
		return getAttributeAsString("commentText");
	}

	public void setCommentText(String commentText) {
		setAttribute("commentText", commentText);
	}

	public Date getCreated() {
		return getAttributeAsDate("created");
	}

	public void setCreated(Date created) {
		 setAttribute("created", created);
	}

	public String getFieldValue(String field) {
        return getAttributeAsString(field);
    }

    public boolean getMemberG8() {
        return getAttributeAsBoolean("member_g8");
    }
}