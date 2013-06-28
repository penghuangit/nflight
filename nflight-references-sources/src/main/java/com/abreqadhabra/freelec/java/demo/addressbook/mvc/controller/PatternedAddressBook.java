package com.abreqadhabra.freelec.java.demo.addressbook.mvc.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.abreqadhabra.freelec.java.demo.addressbook.mvc.model.AddressBookModel;
import com.abreqadhabra.freelec.java.demo.addressbook.mvc.model.AddressDataModel;

/**
 * This sample code is provided "as is" and is
 * intended for demonstration purposes only.
 * 
 * Neither Scott Stanchfield nor IBM shall be
 * held liable for any damages resulting from your
 * use of this code.
 * 
 * An AddressBookModel filter.
 * This filter allows specification of a pattern that
 *   restricts which address are returned by the addresses
 *   indexed property
 *
 * Creation date: (1/19/00 1:10:11 AM)
 * @author: Scott Stanchfield
 */
public class PatternedAddressBook
	 implements AddressBookModel, Patterned {
  private AddressBookModel fieldRealAddressBook;
  protected transient PropertyChangeSupport propertyChange;
  private String fieldPattern;

  // pcl is a holder for the PropertyChangeListener that
  //   we add to the real model. We need to hold a reference
  //   to it in case we change the real model later
  private PropertyChangeListener pcl;

	protected PropertyChangeSupport getPropertyChange() {
	  if (propertyChange == null) {
		propertyChange = new PropertyChangeSupport(this);
	  };
	  return propertyChange;
	}

	// Simply delegate the add & find methods
	public void add(AddressDataModel data) {
	  getRealAddressBook().add(data);
	}

	// support methods for the bound properties
	public void addPropertyChangeListener(
			PropertyChangeListener listener) {
	  getPropertyChange().addPropertyChangeListener(listener);
	}

	public AddressDataModel find(String name) {
		return getRealAddressBook().find(name);
	}  

	public void firePropertyChange(String propertyName,
					 Object oldValue,
					 Object newValue) {
	  getPropertyChange().firePropertyChange(propertyName,
						   oldValue,
						   newValue);
	}

	// Define addresses property to filter based on the
	//   pattern property
	public AddressDataModel[] getAddresses() {
	  AddressDataModel[] data = getRealAddressBook().getAddresses();
	  String pattern = getPattern();
	
	  // if no pattern or it's empty, return data
	  if (pattern == null || pattern.equals(""))
		return data;
	
	  // find out how many match the pattern
	  int count = 0;
	  for (int i = 0; i < data.length; i++)
		if (data[i].getName().startsWith(getPattern()))
		  count++;
	
	  // if pattern matches all, return all
	  if (count == data.length)
		return data;
	
	  // Otherwise Make the new data array
	  // Unfortunately, there is no other way to
	  //   do this for an indexed property other
	  //   than copy the String refs to a new
	  //   array
	  AddressDataModel[] patterned = new AddressDataModel[count];
	  for (int i = 0, j = 0; i < data.length; i++)
		if (data[i].getName().startsWith(getPattern()))
		  patterned[j++] = data[i];
	  return patterned;
	}

	public AddressDataModel getAddresses(int index) {
	  return getAddresses()[index];
	}

	// the pattern string property
	public String getPattern() {
	  return fieldPattern;
	}

	// the realAddressBook property, tracking the real model
	//   that we filter
	public AddressBookModel getRealAddressBook() {
	  return fieldRealAddressBook;
	}

	public void removePropertyChangeListener(
			PropertyChangeListener listener) {
	  getPropertyChange().removePropertyChangeListener(listener);
	}

	public void setPattern(String pattern) {
	  String oldValue = fieldPattern;
	  fieldPattern = pattern;
	  firePropertyChange("pattern", oldValue, pattern);
	  firePropertyChange("addresses", null, getAddresses());
	}

	public void setRealAddressBook(AddressBookModel realAddressBook) {
	  AddressBookModel oldValue = fieldRealAddressBook;
	  // if we used to have a value, remove the
	  //   old property change listener
	  if (oldValue != null)
		oldValue.removePropertyChangeListener(pcl);
	
	  // set the property value
	  fieldRealAddressBook = realAddressBook;
	
	  // set up listener to delegate events
	  if (pcl == null)
		pcl = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent e) {
		  firePropertyChange(e.getPropertyName(),
					 e.getOldValue(),
					 e.getNewValue());
		}
	  };
	
	  realAddressBook.addPropertyChangeListener(pcl);
	
	  // report that the property has changed
	  firePropertyChange("realAddressBook", oldValue,
				 realAddressBook);
	}
}
