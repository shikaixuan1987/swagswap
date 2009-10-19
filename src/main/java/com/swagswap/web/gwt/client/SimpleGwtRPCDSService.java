package com.swagswap.web.gwt.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

@RemoteServiceRelativePath ("SimpleGwtRPCDSService") //TODO lowercase this
public interface SimpleGwtRPCDSService extends RemoteService {

    List<SwagItemGWTDTO> fetch ();

//    SwagItemGWTDTO add (SwagItemGWTDTO swagItemGWTDTO);
    void add (SwagItemGWTDTO swagItemGWTDTO);

    void update (SwagItemGWTDTO swagItemGWTDTO);

    void remove (SwagItemGWTDTO swagItemGWTDTO);
    
    public static class Util {
		private static SimpleGwtRPCDSServiceAsync instance;
		public static SimpleGwtRPCDSServiceAsync getInstance() {
			return (instance == null) ? (instance = GWT.create(SimpleGwtRPCDSService.class)) : instance;
		}
	}

}
