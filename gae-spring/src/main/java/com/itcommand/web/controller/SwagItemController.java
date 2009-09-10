package com.itcommand.web.controller;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itcommand.domain.DataFormat;
import com.itcommand.domain.Protocol;
import com.itcommand.domain.SwagItem;
import com.itcommand.service.SwagItemService;



@Controller
public class SwagItemController {
 
  @Autowired
  private SwagItemService swagItemService;
 
 
  @ModelAttribute("availableDataFormats")
  public DataFormat[] populateDataFormats() {
    return DataFormat.values();
  }
 
  @ModelAttribute("availableProtocols")
  public Protocol[] populateProtocols() {
    return Protocol.values();
  }
 
 
  @RequestMapping(value = "/swagItems", method = RequestMethod.GET)
  public String getAllandler(Model model) {
    model.addAttribute("swagItem", new SwagItem());
    model.addAttribute("swagItems", swagItemService.getAll());
    return "swagItems";
  }
 
  @RequestMapping(value = "/swagItem/add", method = RequestMethod.GET)
  public String addHandler(Model model) {
    model.addAttribute("swagItem", new SwagItem());
    return "swagItems";
  }
 
  @RequestMapping(value = "/swagItem/delete/{key}", method = RequestMethod.GET)
  public String deleteHandler(@PathVariable("key") Long key) {
    swagItemService.delete(key);
    return "redirect:/swag/swagItems";
  }
 
  @RequestMapping(value = "/swagItem/edit/{key}", method = RequestMethod.GET)
  public String editHandler(@PathVariable("key") Long key, Model model) {
    model.addAttribute("swagItem", swagItemService.get(key));
    return "swagItems";
  }
 
  @RequestMapping(value = "/swagItem/save", method = RequestMethod.POST)
  public String saveHandler(@ModelAttribute SwagItem swagItem) {
    swagItemService.save(swagItem);
    return "redirect:/swag/swagItems";
  }
 
  @RequestMapping(value = "/swagItem/search", method = RequestMethod.POST)
  public String searchHandler(Model model,
                                 @ModelAttribute SwagItem swagItem) {
    Collection<SwagItem> swagItems = swagItemService.search(swagItem.getName());
    model.addAttribute("swagItems", swagItems);
    return "swagItems";
  }
 
  @InitBinder
  protected void initBinder(HttpServletRequest request,
                            ServletRequestDataBinder binder) throws Exception {
    binder.registerCustomEditor(String.class,
        new StringTrimmerEditor(false)
    );
 
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class,
        new CustomDateEditor(dateFormat, false)
    );
 
//    binder.registerCustomEditor(com.google.appengine.swagItem.datastore.Key.class,
//        new GoogleDatastoreKeyEditor()
//    );
 
    binder.registerCustomEditor(List.class, new CustomCollectionEditor(List.class));
  }
}