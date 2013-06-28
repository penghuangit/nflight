package com.abreqadhabra.freelec.java.demo.addressbook.mvc.bin;

import java.awt.Frame;
import java.awt.Panel;
import java.io.Serializable;

import com.abreqadhabra.freelec.java.demo.addressbook.mvc.controller.HashAddressBook;
import com.abreqadhabra.freelec.java.demo.addressbook.mvc.controller.PatternedAddressBook;
import com.abreqadhabra.freelec.java.demo.addressbook.mvc.controller.SimpleAddressDataSelectionModel;
import com.abreqadhabra.freelec.java.demo.addressbook.mvc.controller.SortedAddressBook;
import com.abreqadhabra.freelec.java.demo.addressbook.mvc.model.SimpleAddressData;
import com.abreqadhabra.freelec.java.demo.addressbook.mvc.model.SimpleAddressDataFactory;
/**
 * This sample code is provided "as is" and is
 * intended for demonstration purposes only.
 * 
 * Neither Scott Stanchfield nor IBM shall be
 * held liable for any damages resulting from your
 * use of this code.
 * 
 * Creation date: (1/19/00 1:36:49 AM)
 * @author: Scott Stanchfield
 */
public class PhoneApplication5 extends Frame implements Serializable {
	private AddressBookPatternSelectionListUI ivjAddressBookPatternListUI1 = null;
	private AddressBookSelectionUI ivjAddressBookUI1 = null;
	private Panel ivjContentsPane = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private HashAddressBook ivjhashAddressBook = null;
	private PatternedAddressBook ivjpatternedAddressBook = null;
	private SimpleAddressDataFactory ivjsimpleAddressDataFactory = null;
	private SortedAddressBook ivjsortedAddressBook = null;

class IvjEventHandler implements java.awt.event.WindowListener {
		public void windowActivated(java.awt.event.WindowEvent e) {};
		public void windowClosed(java.awt.event.WindowEvent e) {};
		public void windowClosing(java.awt.event.WindowEvent e) {
			if (e.getSource() == PhoneApplication5.this) 
				connEtoC1(e);
		};
		public void windowDeactivated(java.awt.event.WindowEvent e) {};
		public void windowDeiconified(java.awt.event.WindowEvent e) {};
		public void windowIconified(java.awt.event.WindowEvent e) {};
		public void windowOpened(java.awt.event.WindowEvent e) {};
	};
	private SimpleAddressData ivjinitialSelection = null;
	private SimpleAddressDataSelectionModel ivjsimpleAddressDataSelectionModel = null;

	/**
	 * PhoneApplication3 constructor comment.
	 */
	public PhoneApplication5() {
		super();
		initialize();
	}

	/**
	 * PhoneApplication3 constructor comment.
	 * @param title java.lang.String
	 */
	public PhoneApplication5(String title) {
		super(title);
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			PhoneApplication5 aPhoneApplication5;
			aPhoneApplication5 = new PhoneApplication5();
			aPhoneApplication5.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			aPhoneApplication5.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of java.awt.Frame");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * Return the AddressBookPatternListUI1 property value.
	 * @return AddressBookPatternSelectionListUI
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private AddressBookPatternSelectionListUI getAddressBookPatternListUI1() {
		if (ivjAddressBookPatternListUI1 == null) {
			try {
				ivjAddressBookPatternListUI1 = new AddressBookPatternSelectionListUI();
				ivjAddressBookPatternListUI1.setName("AddressBookPatternListUI1");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjAddressBookPatternListUI1;
	}

	/**
	 * Return the AddressBookUI1 property value.
	 * @return AddressBookSelectionUI
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private AddressBookSelectionUI getAddressBookUI1() {
		if (ivjAddressBookUI1 == null) {
			try {
				ivjAddressBookUI1 = new AddressBookSelectionUI();
				ivjAddressBookUI1.setName("AddressBookUI1");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjAddressBookUI1;
	}

	/**
	 * Return the ContentsPane property value.
	 * @return java.awt.Panel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private java.awt.Panel getContentsPane() {
		if (ivjContentsPane == null) {
			try {
				ivjContentsPane = new java.awt.Panel();
				ivjContentsPane.setName("ContentsPane");
				ivjContentsPane.setLayout(new java.awt.GridLayout());
				getContentsPane().add(getAddressBookPatternListUI1(), getAddressBookPatternListUI1().getName());
				getContentsPane().add(getAddressBookUI1(), getAddressBookUI1().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjContentsPane;
	}

	/**
	 * Return the hashAddressBook property value.
	 * @return HashAddressBook
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private HashAddressBook gethashAddressBook() {
		if (ivjhashAddressBook == null) {
			try {
				ivjhashAddressBook = new HashAddressBook();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjhashAddressBook;
	}

	/**
	 * Return the simpleAddressData property value.
	 * @return SimpleAddressData
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private SimpleAddressData getinitialSelection() {
		if (ivjinitialSelection == null) {
			try {
				ivjinitialSelection = new SimpleAddressData();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjinitialSelection;
	}

	/**
	 * Return the patternedAddressBook property value.
	 * @return PatternedAddressBook
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private PatternedAddressBook getpatternedAddressBook() {
		if (ivjpatternedAddressBook == null) {
			try {
				ivjpatternedAddressBook = new PatternedAddressBook();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjpatternedAddressBook;
	}

	/**
	 * Return the simpleAddressDataFactory property value.
	 * @return SimpleAddressDataFactory
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private SimpleAddressDataFactory getsimpleAddressDataFactory() {
		if (ivjsimpleAddressDataFactory == null) {
			try {
				ivjsimpleAddressDataFactory = new SimpleAddressDataFactory();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjsimpleAddressDataFactory;
	}

	/**
	 * Return the simpleAddressDataSelectionModel property value.
	 * @return SimpleAddressDataSelectionModel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private SimpleAddressDataSelectionModel getsimpleAddressDataSelectionModel() {
		if (ivjsimpleAddressDataSelectionModel == null) {
			try {
				ivjsimpleAddressDataSelectionModel = new SimpleAddressDataSelectionModel();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjsimpleAddressDataSelectionModel;
	}

	/**
	 * Return the sortedAddressBook property value.
	 * @return SortedAddressBook
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private SortedAddressBook getsortedAddressBook() {
		if (ivjsortedAddressBook == null) {
			try {
				ivjsortedAddressBook = new SortedAddressBook();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjsortedAddressBook;
	}

	/**
	 * connEtoC1:  (PhoneApplication3.window.windowClosing(java.awt.event.WindowEvent) --> PhoneApplication3.dispose()V)
	 * @param arg1 java.awt.event.WindowEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.WindowEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.dispose();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connPtoP1SetTarget:  (patternedAddressBook.this <--> AddressBookUI1.addressBookModel)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP1SetTarget() {
		/* Set the target from the source */
		try {
			getAddressBookUI1().setAddressBookModel(getpatternedAddressBook());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connPtoP2SetTarget:  (patternedAddressBook.this <--> AddressBookPatternListUI1.addressBookModel)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP2SetTarget() {
		/* Set the target from the source */
		try {
			getAddressBookPatternListUI1().setAddressBookModel(getsortedAddressBook());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connPtoP3SetTarget:  (patternedAddressBook.this <--> AddressBookPatternListUI1.patternedThing)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP3SetTarget() {
		/* Set the target from the source */
		try {
			getAddressBookPatternListUI1().setPatternedThing(getpatternedAddressBook());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connPtoP4SetTarget:  (hashAddressBook.this <--> patternedAddressBook.realAddressBook)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP4SetTarget() {
		/* Set the target from the source */
		try {
			getpatternedAddressBook().setRealAddressBook(gethashAddressBook());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connPtoP5SetTarget:  (simpleAddressData.this <--> AddressBookUI1.addressDataModel)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP5SetTarget() {
		/* Set the target from the source */
		try {
			getsimpleAddressDataSelectionModel().setSelection(getinitialSelection());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connPtoP6SetTarget:  (simpleAddressDataFactory.this <--> hashAddressBook.addressDataFactory)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP6SetTarget() {
		/* Set the target from the source */
		try {
			gethashAddressBook().setAddressDataFactory(getsimpleAddressDataFactory());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connPtoP7SetTarget:  (patternedAddressBook.this <--> sortedAddressBook.realAddressBook)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP7SetTarget() {
		/* Set the target from the source */
		try {
			getsortedAddressBook().setRealAddressBook(getpatternedAddressBook());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connPtoP8SetTarget:  (simpleAddressDataSelectionModel.this <--> AddressBookUI1.addressDataSelectionModel)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP8SetTarget() {
		/* Set the target from the source */
		try {
			getAddressBookUI1().setAddressDataSelectionModel(getsimpleAddressDataSelectionModel());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connPtoP9SetTarget:  (simpleAddressDataSelectionModel.this <--> AddressBookPatternListUI1.addressDataSelectionModel)
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connPtoP9SetTarget() {
		/* Set the target from the source */
		try {
			getAddressBookPatternListUI1().setAddressDataSelectionModel(getsimpleAddressDataSelectionModel());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
	
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
	}

	/**
	 * Initializes connections
	 * @exception java.lang.Exception The exception description.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		this.addWindowListener(ivjEventHandler);
		connPtoP4SetTarget();
		connPtoP6SetTarget();
		connPtoP7SetTarget();
		connPtoP1SetTarget();
		connPtoP3SetTarget();
		connPtoP2SetTarget();
		connPtoP5SetTarget();
		connPtoP8SetTarget();
		connPtoP9SetTarget();
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("PhoneApplication3");
			setLayout(new java.awt.BorderLayout());
			setSize(426, 240);
			add(getContentsPane(), "Center");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}
}
