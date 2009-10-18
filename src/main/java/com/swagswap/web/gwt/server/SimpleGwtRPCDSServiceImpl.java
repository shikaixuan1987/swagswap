package com.swagswap.web.gwt.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.swagswap.service.ItemService;
import com.swagswap.web.gwt.client.SimpleGwtRPCDSRecord;
import com.swagswap.web.gwt.client.SimpleGwtRPCDSService;

/**
 *
 * @author Aleksandras Novikovas
 * @author System Tier
 * @version 1.0
 */
public class SimpleGwtRPCDSServiceImpl extends AutoinjectingRemoteServiceServlet implements SimpleGwtRPCDSService {
	private static final long serialVersionUID = 1L;

	private ItemService itemService;
	
	static List<SimpleGwtRPCDSRecord> list;

    static int id;
    static {
        id = 1;
        list = new ArrayList<SimpleGwtRPCDSRecord> ();
        SimpleGwtRPCDSRecord record;
        record = new SimpleGwtRPCDSRecord ();
        record.setId (id++);
        record.setPicture("picture" + id);
        record.setName ("First");
        record.setDate (new Date ());
        list.add (record);
        record = new SimpleGwtRPCDSRecord ();
        record.setId (id++);
        record.setPicture("picture" + id);
        record.setName ("Second");
        record.setDate (new Date ());
        list.add (record);
        record = new SimpleGwtRPCDSRecord ();
        record.setId (id++);
        record.setPicture("picture" + id);
        record.setName ("Third");
        record.setDate (new Date ());
        list.add (record);
    }

	@Autowired
	@Required
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}
	
    public List<SimpleGwtRPCDSRecord> fetch () {
        return list;
    }

    public SimpleGwtRPCDSRecord add (SimpleGwtRPCDSRecord record) {
        record.setId (id++);
        list.add (record);
        return record;
    }

    public SimpleGwtRPCDSRecord update (SimpleGwtRPCDSRecord record) {
        Integer recordId = record.getId ();
        if (recordId != null) {
            int index = -1;
            for (int i = 0; i < list.size (); i++) {
                if (recordId.equals (list.get (i).getId ())) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                list.set (index, record);
                return record;
            }
        }
        return null;
    }

    public void remove (SimpleGwtRPCDSRecord record) {
        Integer recordId = record.getId ();
        if (recordId != null) {
            int index = -1;
            for (int i = 0; i < list.size (); i++) {
                if (recordId.equals (list.get (i).getId ())) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                list.remove (index);
            }
        }
    }

}
