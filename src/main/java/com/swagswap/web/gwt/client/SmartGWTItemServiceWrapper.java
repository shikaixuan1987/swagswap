package com.swagswap.web.gwt.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

@RemoteServiceRelativePath ("smartGWTItemServiceWrapper")
public interface SmartGWTItemServiceWrapper extends RemoteService {

	/**
	 * 
	 * These are for SmartGWT
	 */
	
	//maps to getAll in ItemService
    List<SwagItemGWTDTO> fetch ();

    SwagItemGWTDTO add (SwagItemGWTDTO swagItemGWTDTO);

    SwagItemGWTDTO update (SwagItemGWTDTO swagItemGWTDTO);

    void remove (SwagItemGWTDTO swagItemGWTDTO);
    
    /**
     * These are extra for interfacing with ItemService
     *
     */
    
	void updateRating(Long swagItemKey, int computedRatingDifference, boolean isNew);
	
	void addComment(SwagItemComment swagItemComment);
    
    public static class Util {
		private static SmartGWTItemServiceWrapperAsync instance;
		public static SmartGWTItemServiceWrapperAsync getInstance() {
			return (instance == null) ? (instance = GWT.create(SmartGWTItemServiceWrapper.class)) : instance;
		}
	}

}
