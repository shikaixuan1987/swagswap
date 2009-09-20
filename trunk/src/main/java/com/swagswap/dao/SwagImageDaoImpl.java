package com.swagswap.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.apache.log4j.Logger;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.swagswap.domain.SwagImage;

/**
 * Inspired by Spring Image Database sample application
 */
public class SwagImageDaoImpl extends JdoDaoSupport implements SwagImageDao {

	private static final Logger log = Logger.getLogger(SwagImageDaoImpl.class);

	
	@SuppressWarnings("unchecked")
	public List<SwagImage> getAll() {
		PersistenceManager pm = getPersistenceManager();
		String query = "select from " + SwagImage.class.getName();
		List<SwagImage> swagImages = (List<SwagImage>) pm.newQuery(query).execute();

		if (log.isInfoEnabled()) {
			log.info("returning " + swagImages.size() + " swag images");
		}
		return swagImages;

	}

	public SwagImage get(String key) {
		SwagImage swagImage = getPersistenceManager().getObjectById(SwagImage.class, key);
		return swagImage;
	}
	
	public void streamImage(final String key, final OutputStream contentStream) {
		byte[] imageBytes = get(key).getImage().getBytes();
		try {
			contentStream.write(imageBytes, 0, imageBytes.length);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}


//	public void storeImage(
//	    final String name, final InputStream contentStream, final int contentLength, final String description)
//	    throws DataAccessException {
//
//		getJdbcTemplate().execute(
//				"INSERT INTO imagedb (image_name, content, description) VALUES (?, ?, ?)",
//				new AbstractLobCreatingPreparedStatementCallback(this.lobHandler) {
//					protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
//						ps.setString(1, name);
//						lobCreator.setBlobAsBinaryStream(ps, 2, contentStream, contentLength);
//						lobCreator.setClobAsString(ps, 3, description);
//					}
//				}
//		);
//	}


}
