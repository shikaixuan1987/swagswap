package com.swagswap.web.gwt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

/**
 *
 * @author Aleksandras Novikovas
 * @author System Tier
 * @version 1.0
 */
public interface SimpleGwtRPCDSServiceAsync {

    public abstract void fetch (AsyncCallback<List<SwagItemGWTDTO>> asyncCallback);

    public abstract void add (SwagItemGWTDTO swagItemGWTDTO, AsyncCallback<SwagItemGWTDTO> asyncCallback);

    public abstract void update (SwagItemGWTDTO swagItemGWTDTO, AsyncCallback<SwagItemGWTDTO> asyncCallback);

    public abstract void remove (SwagItemGWTDTO swagItemGWTDTO, AsyncCallback<Object> asyncCallback);

}
