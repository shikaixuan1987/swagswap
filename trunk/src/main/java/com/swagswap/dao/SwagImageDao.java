package com.swagswap.dao;

import java.io.OutputStream;
import java.util.List;

import com.swagswap.domain.SwagImage;

public interface SwagImageDao {

	List<SwagImage> getAll();
	
	SwagImage get(String key);

	void streamImage(String key, OutputStream os);


}
