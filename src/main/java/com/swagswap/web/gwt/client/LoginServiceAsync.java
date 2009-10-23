package com.swagswap.web.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.swagswap.web.gwt.client.domain.LoginInfo;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}
