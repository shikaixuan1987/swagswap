package com.swagswap.web.jsf.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "testBean")
@RequestScoped

public class TestBean {
	
	private byte[] uploadedFile;

	public byte[] getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(byte[] uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public void actionUpload() {
		System.out.println("****  UPLOAD");
	}
	

}
