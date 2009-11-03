package com.swagswap.web.gwt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.tile.TileRecord;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

public interface SmartGWTItemServiceWrapperAsync {

    public abstract void fetch (AsyncCallback<List<SwagItemGWTDTO>> asyncCallback);
    
    public abstract void fetch (Long key, AsyncCallback<SwagItemGWTDTO> asyncCallback);

    public abstract void add (SwagItemGWTDTO swagItemGWTDTO, AsyncCallback<SwagItemGWTDTO> asyncCallback);

    public abstract void update (SwagItemGWTDTO swagItemGWTDTO, AsyncCallback<SwagItemGWTDTO> asyncCallback);

    public abstract void remove (SwagItemGWTDTO swagItemGWTDTO, AsyncCallback<Object> asyncCallback);

	void updateRating(Long swagItemKey, int computedRatingDifference, boolean isNew, AsyncCallback callback);
	
	void addComment(SwagItemComment swagItemComment, AsyncCallback callback);

}
