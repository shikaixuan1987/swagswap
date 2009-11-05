/*package com.swagswap.web.gwt.server;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.smartgwt.client.widgets.form.fields.FileItem;

*//**
 * Uploaded image ontvangen en sturen naar flamingo via RESTful API
 * 
 *//*
public class ImageUploadServlet extends HttpServlet {

    public static final Logger LOGGER = Logger.getLogger(ImageUploadServlet.class);

    public static final Random RANDOM = new Random();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        // NOTE: taken from the example on the commons-file upload page
        // http://commons.apache.org/fileupload/using.html

        // Create a factory for disk-based file items
        FileItemFactory factory = new DiskFileItemFactory();

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            List  FileItem items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    // ignore, we are only expecting a file upload in this form
                } else {
                    try {
                        String uniqueFileName = generateUniqueFileName(item.getName());
                        String naam = removeDirInfo(item.getName()); // just take original filename
                        File uploadedFile = new File(System.getProperty("java.io.tmpdir"), uniqueFileName);
                        LOGGER.debug("save file to " + uploadedFile.getAbsolutePath());
                        item.write(uploadedFile);

                        processUploadedFile(uploadedFile, naam);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (FileUploadException e) {
            throw new RuntimeException(e);
        }
    }

    *//**
     * Maak lezebaar maar wel unqiue naam voor file
     * 
     * @param fileName
     * @return
     *//*
    private String generateUniqueFileName(String fileName) {
        String label = removeDirInfo(fileName);
        String uniqueFileName = System.currentTimeMillis() + "-" + RANDOM.nextInt(1000) + "-" + label;
        LOGGER.debug("creating file: " + uniqueFileName);
        return uniqueFileName;
    }

    private String removeDirInfo(String fileName) {
        int lastSlashIndex = fileName.lastIndexOf("/");
        int lastBackSlashIndex = fileName.lastIndexOf("\\");
        if (lastSlashIndex != -1) {
            return fileName.substring(lastSlashIndex + 1, fileName.length());
        } else if (lastBackSlashIndex != -1) {
            return fileName.substring(lastBackSlashIndex + 1, fileName.length());
        } else {
            return fileName;
        }
    }

    *//**
     * make REST call to geoserver
     * 
     * @param uploadedZipFile
     *            shapefile bundle
     * @param omschrijving
     * @param naam
     *//*
    protected void processUploadedFile(File imageFile, String naam) throws Exception {
        FlamingoRESTClient client = new FlamingoRESTClient();
        String responseString = client.putFlamingoImageFile(imageFile, naam);
        LOGGER.debug("Response from geoserver is " + responseString);
    }

}
*/