package com.itcommand.dao;

import java.io.OutputStream;
import java.util.List;

import com.itcommand.domain.SwagImage;

public interface SwagImageDao {

	List<SwagImage> getAll();
	
	SwagImage get(String key);

	void streamImage(String key, OutputStream os);


}
