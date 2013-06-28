package com.abreqadhabra.freelec.jbpf.examples.lms.bin.client.tcp;

import com.abreqadhabra.freelec.jbpf.examples.lms.user.*;
import com.abreqadhabra.freelec.jbpf.examples.lms.dao.*;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;              //for layout managers and more
import java.awt.event.*;        //for action events


public class TCPClientGUI extends JPanel implements ActionListener, ItemListener {


    protected static final String findNameRadioButtonString = "name";
    protected static final String findSsnRadioButtonString = "ssn";
    protected static final String findUserTextFieldString = "findUserTextField";
    protected static final String findUserButtonString = "findUserButton";
    protected static final String createUserButtonString = "createUserButton";
    protected static final String deleteUserButtonString = "deleteUserButton";
    protected static final String updateUserButtonString = "updateUserButton";
    protected static final String listUserButtonString = "listUserButton";
    protected static final String userNoString = "user_no";
    protected static final String userNameString = "name";
    protected static final String userSsnString = "ssn";
    protected static final String userAgeString = "age";
    protected static final String userAddressString = "address";
    protected static final String userStudentNoString = "studentNo";
    protected static final String userCourseString = "course";
    protected static final String userDepartmentString = "department";
	protected static final String userStudentNoComboBoxString = "studentNo";
    protected static final String userCourseComboBoxString = "course";
    protected static final String userDepartmentComboBoxString = "department";
    protected static final String dataCardString = "������Ÿ��";
	protected static final String studentDataCardString = "�л�";
    protected static final String professorDataCardString = "����";
    protected static final String employeeDataCardString = "������";

	private boolean DEBUG = false;	 //usersTable
	private boolean displaySet;
    private boolean ALLOW_COLUMN_SELECTION = false;	//usersTable
    private boolean ALLOW_ROW_SELECTION = true;	//usersTable
    private boolean isSelectedRow = false;	//usersTable

	protected UserTableDataModel userTDM;
	protected ResultTableDataModel resultTDM;
	protected TCPClient tcpc;

	protected ButtonGroup group;
	protected JComboBox cb ;
	protected JPanel addtionalDataCardPanel;
	protected JTable usersTable;
	protected JTable resultTable;
	protected JTextArea logTextArea;
	protected JTextArea editDisplayTextArea;
	protected JTextField findUserTextField;
	protected JTextField[] defaultDataTextField   = { new JTextField(15), new JTextField(15), new JTextField(15), new JTextField(15), new JTextField(15)};
	protected JTextField[] addtionalDataTextField =  {new JTextField(15), new JTextField(15), new JTextField(15)};
	
	protected String[] defaultDataLabels = {"ȸ����ȣ", "�̸� ","����", "�ֹε�Ϲ�ȣ","�ּ�" };
	protected String[] addtionalDataLabels = {"�й�", "������", "�μ�"};
	protected String comboBoxItems[] = {dataCardString, studentDataCardString, professorDataCardString, employeeDataCardString};
	protected String selectedRowObjectType;
	protected String selectedRowAddtionalData;
	protected String selectedRowSsn;
	protected String originalCategory = null;
	protected String originalSequence = null;

//--------------------------------------------------------------------
//  TCPClientGUI(TCPClient tcpc) ������
//--------------------------------------------------------------------

public TCPClientGUI(TCPClient tcpc) {
//set up Grid Bag Layout
	super(new GridBagLayout());
	this.tcpc = tcpc;
	this.displaySet =false;

	JDialog.setDefaultLookAndFeelDecorated(true);
	String options[] = {"id", "pwd"};
	options[0] = JOptionPane.showInputDialog("���̵� �Է��� �ּ���."); 
	options[1] = JOptionPane.showInputDialog("�н����带 �Է��� �ּ���."); 
	if(!"admin".equals(options[0]) && !"admin".equals(options[0])){
			 JOptionPane.showMessageDialog(this, "�α����� �����Ͽ����ϴ�.\n���̵�� �н����带 ��Ȯ��\nȮ���� ������Ͽ� �ּ���", "�α���", JOptionPane.WARNING_MESSAGE); 
			System.exit(0);
	}
	
//	Lay out the text controls and the labels.
	GridBagLayout gridbag = (GridBagLayout)getLayout();
	GridBagConstraints c = new GridBagConstraints();	

//  User ��ü ������ ���̺�
	 usersTable  = createUserTable();	
//	Create the scroll pane and add the table to it.
	JScrollPane scrollPane = new JScrollPane(usersTable);
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	scrollPane.setBorder(
		BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("ȸ�� ���"),
			BorderFactory.createEmptyBorder(5,5,5,5)
		));
//	Add the scroll pane to this panel.
	c.fill = GridBagConstraints.BOTH; //Fill entire cell.
	c.weighty = 1.0;  //Button area and message area have equal height.
	c.gridwidth = GridBagConstraints.REMAINDER; //end of row
	gridbag.setConstraints(scrollPane, c);
	this.add(scrollPane);
// �ǵ����� ����/�Է�, �ý��� �α�
	JPanel editTabbedPanel = createTabbedPanel();
	c.weighty = 1.0;  //Button area and message area have equal height.
	c.fill = GridBagConstraints.BOTH; //Fill entire cell.
	gridbag.setConstraints(editTabbedPanel, c);
	this.add(editTabbedPanel);
//	Make sure we have nice window decorations.
	JFrame.setDefaultLookAndFeelDecorated(true);
//	Create and set up the window.
	JFrame frame = new JFrame("LMS: Learning Management System Ŭ���̾�Ʈ");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setContentPane(this);
	frame.setJMenuBar(createJMenuBar());
//	Display the window.
	frame.pack();
	frame.setVisible(true);

	}

//--------------------------------------------------------------------
//  User ��ü ������ ���̺�
//--------------------------------------------------------------------

private JTable createUserTable(){
//Create a User Ojbect Table
	userTDM = new UserTableDataModel(new ArrayList());
	final JTable  table =  new JTable(userTDM);
	table.setGridColor(Color.DARK_GRAY );
	table.setPreferredScrollableViewportSize(new Dimension(600, 100));

//	���� ���� ������	
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//	�ο� ������
	if (ALLOW_ROW_SELECTION) { // true by default
		ListSelectionModel rowSM = table.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()){
					return;
				}
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (lsm.isSelectionEmpty()) {
//					System.out.println("No rows are selected.");
				} else {
					int selectedRow = lsm.getMinSelectionIndex();
					//System.out.println("Row " + selectedRow + " is now selected.");
//	���̺� ����/ �ش� �ο��� ���� ���翩�� �˻�
					String selectedSsn = (String) table.getModel().getValueAt(selectedRow , 2);
					findUser(findSsnRadioButtonString,  selectedSsn);
					defaultDataTextField[3].setEditable(false);
				}
			}
		});
	} else {
		table.setRowSelectionAllowed(false);
	}
//	 �÷� ������
	if (ALLOW_COLUMN_SELECTION) { // false by default
		if (ALLOW_ROW_SELECTION) {
			//We allow both row and column selection, which
			//implies that we *really* want to allow individual
			//cell selection.
			table.setCellSelectionEnabled(true);
		}
		table.setColumnSelectionAllowed(true);
		ListSelectionModel colSM = table.getColumnModel().getSelectionModel();
		colSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()){
					return;
				}
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (lsm.isSelectionEmpty()) {
					System.out.println("No columns are selected.");
				}else {
					int selectedCol = lsm.getMinSelectionIndex();
					System.out.println("Column " + selectedCol + " is now selected.");
				}
			}
		});
	}
//���̺� ������ �����
	if (DEBUG) {
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				printDebugData(table);
			}
		});
	}
		return table;
}

//--------------------------------------------------------------------
//  User ��ü ������ ���̺� ����� ��ƿ �޽��
//--------------------------------------------------------------------

private void printDebugData(JTable table) {
	int numRows = table.getRowCount();
    int numCols = table.getColumnCount();
    javax.swing.table.TableModel model = table.getModel();  
		System.out.println("Value of data: ");
		for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

//--------------------------------------------------------------------
// �ǵ����� ���� (�޴��� �Է�/������, �ý��� �α�)
//--------------------------------------------------------------------

private  JPanel createTabbedPanel(){
	JPanel panel = new JPanel(new GridLayout(1, 1));
	JTabbedPane tabbedPane = new JTabbedPane();     
	JComponent editPanel = createEditPanel();
	JComponent logPanel = createLogPanel();
	tabbedPane.addTab("�޴�", null, editPanel,  "�Է� �� ����") ;
	tabbedPane.addTab("�ý��۷α�", null, logPanel,  "�Է� �� ����") ;
	panel.add(tabbedPane);
	return panel;
}

//--------------------------------------------------------------------
// �޴� ���� (�˻�/��ϰ���)
//--------------------------------------------------------------------

private JPanel createUserMenu(){
//�˻� ���� ��ư	
	final int numButtons = 2;
	JRadioButton[] findUserRadioButtons = new JRadioButton[numButtons];
	group = new ButtonGroup();
	findUserRadioButtons[0] = new JRadioButton("�ֹε�Ϲ�ȣ");
	findUserRadioButtons[0].setActionCommand(findSsnRadioButtonString);
	findUserRadioButtons[1] = new JRadioButton("�̸�");
	findUserRadioButtons[1].setActionCommand(findNameRadioButtonString);
	for (int i = 0; i < numButtons; i++) {
		group.add(findUserRadioButtons[i]);
	}
	findUserRadioButtons[1].setSelected(true);
// �˻� �ؽ�Ʈ�ʵ�
	findUserTextField = new JTextField(15); 
//	findUserTextField.setPreferredSize(new Dimension(30, 10));
	findUserTextField.setActionCommand(findUserTextFieldString);
	findUserTextField.addActionListener(this);
// �˻� ��ư
	JButton findUserButton = new  JButton("�˻�");
//	findUserButton.setPreferredSize(new Dimension(40, 10));
	findUserButton.setVerticalTextPosition(AbstractButton.CENTER);
	//findUserButton.setMnemonic(KeyEvent.VK_P); //����Ű
	findUserButton.setToolTipText("�˻�� �Է��ϰ� �˻� ��ư�� Ŭ���ϼ���.");
	findUserButton.setActionCommand(findUserButtonString);
	findUserButton.addActionListener(this);
//  ��� ���Ź�ư
	JButton listUserButton = new  JButton("��� ����");
//    listUserButton.setPreferredSize(new Dimension(80, 20));
	listUserButton.setVerticalTextPosition(AbstractButton.CENTER);
	listUserButton.setToolTipText("ȸ�� ����� �ֽ� �����ͷ� �����Ϸ��� ��� ���� ��ư�� Ŭ���ϼ���.");
	listUserButton.setActionCommand(listUserButtonString);
	listUserButton.addActionListener(this);
//  �˻� �ǳ�(�׸��� ���̾ƿ�)
	JPanel panel = new JPanel(new GridLayout(3, 1));
	JPanel radioButtonPanel = new JPanel(new SpringLayout());
	for (int i = 0; i < numButtons; i++) {
		radioButtonPanel.add(findUserRadioButtons[i]);
	}	
//  Spring Layout Utilities  class
	SpringUtilities.makeCompactGrid(radioButtonPanel
																		,1, 2 	//rows, cols
																		,2, 2		//initX, initY
																		,2, 2);  //xPad, yPad
	JPanel findPanel = new JPanel(new SpringLayout());
	findPanel.add(findUserTextField);
	findPanel.add(findUserButton);
	findPanel.add(listUserButton);
//  Spring Layout Utilities  class
	SpringUtilities.makeCompactGrid(findPanel
																		,1, 3 	//rows, cols
																		,2, 2		//initX, initY
																		,2, 2);  //xPad, yPad
	panel.add(radioButtonPanel );
	panel.add(findPanel);
	//panel.add(new JSeparator(SwingConstants.HORIZONTAL));
	panel.add(createMenuPanel());
	return panel;
}

//--------------------------------------------------------------------
// �޴� ���� (����/����/�Է�)
//--------------------------------------------------------------------

private  JPanel createMenuPanel(){
//	���� �� ���¹�ư
	cb = new JComboBox(comboBoxItems);
	cb.setEditable(false);
	cb.setPreferredSize(new Dimension(60, 20));
	cb.addItemListener(this);

//	�Է�
	JButton createUserButton = new  JButton("�Է�");
	createUserButton.setPreferredSize(new Dimension(60, 20));
	createUserButton.setVerticalTextPosition(AbstractButton.CENTER);
	createUserButton.setToolTipText("���ο� �����͸� �Է��Ϸ��� �Է� ��ư�� Ŭ���ϼ���.");
	createUserButton.setActionCommand(createUserButtonString);
	createUserButton.addActionListener(this);

//	����
	JButton updateUserButton = new  JButton("����");
	updateUserButton.setPreferredSize(new Dimension(60, 20));
	updateUserButton.setVerticalTextPosition(AbstractButton.CENTER);
	updateUserButton.setToolTipText("���� �����͸� �����Ϸ��� ���� ��ư�� Ŭ���ϼ���.");
	updateUserButton.setActionCommand(updateUserButtonString);
	updateUserButton.addActionListener(this);

	//����
	JButton deleteUserButton = new  JButton("����");
	deleteUserButton.setPreferredSize(new Dimension(60, 20));
	deleteUserButton.setVerticalTextPosition(AbstractButton.CENTER);
	deleteUserButton.setToolTipText("���� �����͸� �����Ϸ��� ���� ��ư�� Ŭ���ϼ���.");
	deleteUserButton.setActionCommand(deleteUserButtonString);
	deleteUserButton.addActionListener(this);

//	�޴� �ǳ�(�׸��� ���̾ƿ�)
	JPanel panel = new JPanel(new SpringLayout());
	 panel.add(cb);
	 panel.add(createUserButton);
	 panel.add(updateUserButton);
	 panel.add(deleteUserButton);

//  Spring Layout Utilities  class
	SpringUtilities.makeCompactGrid(panel  
																		,1, 4 	//rows, cols
																		,2, 2		//initX, initY
																		,2, 2);  //xPad, yPad
	return panel;
}

//--------------------------------------------------------------------
// User ������ ����/�Է� 
//--------------------------------------------------------------------

protected JComponent createEditPanel() {
	JPanel editUserPanel = new JPanel(new GridLayout(1, 2, 2, 2));
//	Lay out the text controls and the labels.
	editUserPanel.setBorder(
								BorderFactory.createCompoundBorder(
									BorderFactory.createTitledBorder("�Է�/���� ����"),
									BorderFactory.createEmptyBorder(5,5,5,5)));	
	JPanel defaultDataPanel = new JPanel(new SpringLayout());
	String[] defaultDataActionCommand = {userNoString, userNameString, userSsnString, userAgeString, userAddressString};

	int defaultDataNumPairs = defaultDataLabels.length;
	for (int i = 0; i < defaultDataNumPairs; i++) {
		JPanel  panel = new JPanel(new GridLayout(1, 2, 2, 2));
		JLabel label = new JLabel(defaultDataLabels[i] + ": ", JLabel.TRAILING);
		label.setPreferredSize(new Dimension(50, 20));
		panel.add(label);
		defaultDataTextField[i].setActionCommand(defaultDataActionCommand[i]);
		defaultDataTextField[i].addActionListener(this);
		label.setLabelFor(defaultDataTextField[i]);
		panel.add(defaultDataTextField[i]);
		defaultDataPanel.add(panel);
	}
		defaultDataTextField[0].setEditable(false);
//	 �л�/����/������ ������ �Է� ī�巹�̾ƿ�
	addtionalDataCardPanel = new JPanel(new CardLayout());	
	String[] addtionalDataActionCommand = {userStudentNoString , userCourseString, userDepartmentString};
	String[] addtionalConstraint = {studentDataCardString, professorDataCardString, employeeDataCardString};
	int addtionalDataNumPairs = addtionalDataLabels.length;
	for (int i = 0; i < addtionalDataNumPairs; i++) {
		JPanel  panel = new JPanel(new GridLayout(1, 2, 2, 2));
		JLabel label = new JLabel(addtionalDataLabels[i] + ": ", JLabel.TRAILING);
		//label.setPreferredSize(new Dimension(50, 20));
//	Create the "cards".
		panel.add(label);
		addtionalDataTextField[i].setActionCommand(addtionalDataActionCommand[i]);
		addtionalDataTextField[i].addActionListener(this);
		label.setLabelFor(addtionalDataTextField[i]);
		panel.add(addtionalDataTextField[i]);
	   addtionalDataCardPanel.add(panel, addtionalConstraint[i]);
	}
	defaultDataPanel.add(addtionalDataCardPanel);
//	Spring Layout Utilities  class
	SpringUtilities.makeCompactGrid(defaultDataPanel  
																					,6, 1		//rows, cols
																					,2, 2		//initX, initY																					
																					,2, 2);  //xPad, yPad
	editUserPanel.add(defaultDataPanel);
   JPanel panel = new JPanel();     
   	panel.setBorder(
									BorderFactory.createCompoundBorder(
									BorderFactory.createTitledBorder("�޴�"),
									BorderFactory.createEmptyBorder(5,5,5,5)));
//	Create a User Ojbect Table
	resultTDM = new ResultTableDataModel(new ArrayList());
	resultTable =  new JTable(resultTDM);
	resultTable.setGridColor(Color.DARK_GRAY );
	resultTable.setPreferredScrollableViewportSize(new Dimension(500, 50));
//	Create the scroll pane and add the table to it.
	JScrollPane scrollPane = new JScrollPane(resultTable);
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	scrollPane.setBorder(
		BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("ȸ����� ���ð��"),
			BorderFactory.createEmptyBorder(5,5,5,5)
		));
	JPanel editPanel = new JPanel(new BorderLayout());
	editPanel.add(scrollPane,  BorderLayout.NORTH);
	editPanel.add(editUserPanel, BorderLayout.CENTER);
//	�˻� �޴�(�̸�/�ֹε�Ϲ�ȣ)
	panel.add(createUserMenu());
	editPanel.add(panel,  BorderLayout.EAST);
	return editPanel;
}

//--------------------------------------------------------------------
// Ŭ���̾�Ʈ ��ũ �ǳ� ���� 
//--------------------------------------------------------------------

protected JComponent createLogPanel() {
	JPanel panel = new JPanel(new GridLayout(1, 1));     
	logTextArea = new JTextArea();
	logTextArea.setLineWrap(true);
	logTextArea.setRows(5) ;
	JScrollPane logScrollPane = new JScrollPane(logTextArea);
	logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	panel.add(logScrollPane);
	return panel;
}

//--------------------------------------------------------------------
// �޴��� ����
//--------------------------------------------------------------------

protected JMenuBar createJMenuBar(){
	JMenuItem connect = new JMenuItem("�����ϱ�(C)"); 
	connect.setMnemonic('c'); 
	connect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK)); 
	connect.addActionListener( new ActionListener() {
																		public void actionPerformed(ActionEvent e) {
																			tcpc.connect(null, 0);
																			tcpc.socketListening();
																		}});
	JMenuItem disconnect = new JMenuItem("���Ӳ���(D)"); 
	disconnect.setMnemonic('d'); // Ű���� ����Ű �Ҵ�
	disconnect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK)); // Ű���� ���������� ����
	disconnect.addActionListener(new ActionListener() {
																		public void actionPerformed(ActionEvent e) {
																			tcpc.close();
																		}});
	JMenuItem exit = new JMenuItem("����(X)"); // �޴� ������ �߰�
	exit.setMnemonic('x'); // Ű���� ����Ű �Ҵ�
	exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK)); // Ű���� ���������� ����
	exit.addActionListener(new ActionListener() {
																		public void actionPerformed(ActionEvent e) {
																			System.exit(0);
																		}});
	final JMenuBar menuBar = new JMenuBar();
	JMenu mFile = new JMenu("File");
	mFile.setMnemonic('f');
	mFile.add(connect);
	mFile.add(disconnect);
	mFile.addSeparator(); // �޴� ���м� ����
	mFile.add(exit);
	menuBar.add(mFile);
	return menuBar;
}

//--------------------------------------------------------------------
// �˻� �޽��(�ֹε�Ϲ�ȣ, �̸�)
//--------------------------------------------------------------------

public void findUser(String cmd, String value){
	if (cmd.equals("name")){
		if (value.length() ==0) {
			JOptionPane.showMessageDialog(this, "�̸��� ��Ȯ�� �Է��Ͽ� �ּ���", "�̸����� �˻�", JOptionPane.WARNING_MESSAGE); 
		}else{
			tcpc.readUser(cmd, value);
		}
	}else if(cmd.equals("ssn")){
		if(checkSsn(value) && value != null){
			tcpc.readUser(cmd, value);
		}
	}
}

//--------------------------------------------------------------------
// ���� üũ ��ƿ �޽��
//--------------------------------------------------------------------

public boolean checkAge(String a){
		boolean validAge = false; 
			try{
				if((Integer.parseInt(a) <= 0) || (Integer.parseInt(a) >120) ){
					 JOptionPane.showMessageDialog(this, "���̸� ��Ȯ�� �Է��Ͽ� �ּ���(1~120�� ����)", "���� �˻�", JOptionPane.WARNING_MESSAGE); 
				}else{
					return true;
				} // if
			}catch(NumberFormatException nfe){
				 JOptionPane.showMessageDialog(this, "���̸� ���ڷ� ��Ȯ�� �Է��Ͽ� �ּ���(1~120�� ����)", "���� �˻�", JOptionPane.WARNING_MESSAGE); 
			}			return false;
	} // checkAge(String a)

//--------------------------------------------------------------------
// �ֹε�Ϲ�ȣ ����Ʈ üũ ��ƿ �޽��
//--------------------------------------------------------------------

public boolean checkSsn(String ssn){
	boolean check = false;
	/*
	int total = 0;
	int chk = 0;
	int [] temp = new int[13];
	try{
		for(int i=0; i<ssn.length(); i++){
				temp[i] = Integer.parseInt(ssn.substring(i, i+1));//s.charAt(i);
		}//for
		for(int i=0;i<temp.length-1;i++){
			if(i<=7){
				total+=temp[i]*(i+2);
			}else{
				total+=temp[i]*(i-6);
			}//if
		}//for
		chk=(11-(total%11))%10;
		if(chk==temp[12]){
			check=true;
		} //if

		if(!check){
			 JOptionPane.showMessageDialog(this, "�ֹε�Ϲ�ȣ�� ��Ȯ�� �Է��Ͽ� �ּ���", "�ֹε�Ϲ�ȣ�� �˻�", JOptionPane.WARNING_MESSAGE); 
		}
	}catch(NumberFormatException nfe){}
	*/

		//��� üũ

		if(ssn.length() !=12){
			check=true;
		}

		if(!check){
			 JOptionPane.showMessageDialog(this, "�ֹε�Ϲ�ȣ�� ��Ȯ�� �Է��Ͽ� �ּ���", "�ֹε�Ϲ�ȣ�� �˻�", JOptionPane.WARNING_MESSAGE); 
		}

	return check;
}

//--------------------------------------------------------------------
// ���� ���̺� ������ �Է� �޽��
//--------------------------------------------------------------------

public void  setResultTable(ArrayList ar){
	resultTDM  = new ResultTableDataModel(ar);
	resultTable.setModel(resultTDM);
}

//--------------------------------------------------------------------
// ��ü ��� ���̺� ������ �Է� �޽��
//--------------------------------------------------------------------

public void  setUsersTable(ArrayList ar){
	userTDM  = new UserTableDataModel(ar);
	usersTable.setModel(userTDM);
}

//--------------------------------------------------------------------
// ���� ���� ������  ���� �޽��
//--------------------------------------------------------------------

public  void setResultObject(String cmd, Object resultObject){


if( (cmd.equals("name")) |  (cmd.equals("ssn"))) {
		if (!(resultObject instanceof  UserImpl) && (cmd.equals("name"))){
			JOptionPane.showMessageDialog(this,"�̸��� ���� �˻������ �����ϴ�.", "�̸����� �˻�",  JOptionPane.INFORMATION_MESSAGE); 
		}else if (!(resultObject instanceof  UserImpl) && (cmd.equals("ssn"))){
			JOptionPane.showMessageDialog(this, "�ֹε�Ϲ�ȣ�� ���� �˻������ �����ϴ�.", "�ֹε�Ϲ�ȣ�� �˻�",  JOptionPane.INFORMATION_MESSAGE); 
		}else if((resultObject instanceof  UserImpl) && ((cmd.equals("ssn")) | (cmd.equals("name")))){
			setInputForm(resultObject);
		}
	} else if ("create".equals(cmd)) {
		JOptionPane.showMessageDialog(this, (String) resultObject, "�Է�",  JOptionPane.INFORMATION_MESSAGE); 
		tcpc.listUser("list");		
	} else if ("update".equals(cmd)) {
		JOptionPane.showMessageDialog(this, (String) resultObject, "����",  JOptionPane.INFORMATION_MESSAGE); 
		tcpc.listUser("list");		
	} //if
}

//--------------------------------------------------------------------
// ����� ���� �޽��
//--------------------------------------------------------------------

public void resetOutPutData(boolean userTab, boolean resultTab){

	for(int i =0;i < defaultDataTextField.length; i++){
		defaultDataTextField[i].setText("");	
	}
	defaultDataTextField[3].setEditable(true);
	for(int i =0;i < addtionalDataTextField.length; i++){
		addtionalDataTextField[i].setText("");	
	}
	selectedRowObjectType = "";
	originalCategory = null;
	originalSequence = null;
	isSelectedRow = false;
	usersTable.clearSelection();
	resultTable.clearSelection();
	if (userTab){
		this.setUsersTable(new ArrayList());
	}
	if(resultTab){
		this.setResultTable(new ArrayList());
	}
}

//--------------------------------------------------------------------
// ������Ÿ�� �޺��ڽ� ������ (ī�� ���̾ƿ� �̺�Ʈ)
//--------------------------------------------------------------------

public void itemStateChanged(ItemEvent evt) {
	CardLayout cl = (CardLayout)(addtionalDataCardPanel.getLayout());
	cl.show(addtionalDataCardPanel, (String)evt.getItem());
	if(resultTable.getModel().getRowCount()!= 0){
		originalCategory = (String) resultTable.getModel().getValueAt(0, 8);
		originalSequence = (String) resultTable.getModel().getValueAt(0, 0);
	}else{
		originalCategory ="��";
	}
	if(evt.getStateChange()==1){ 
		String option = (String)cb.getSelectedItem();
		String typeName = null;	
		if (option.equals("�л�")){
			typeName = "lms_student";
		} else if (option.equals("����")){
			typeName = "lms_professor";
		} else if (option.equals("������")){
			typeName = "lms_employee";
		}
		if(typeName != null){
			if(originalCategory.equals((String)cb.getSelectedItem())){
				defaultDataTextField[0].setText(originalSequence);	
			}else{
				tcpc.createSequence("seq", typeName);
			}
		}

	}

}

//--------------------------------------------------------------------
// �Է��� üũ �޽��
//--------------------------------------------------------------------

private boolean checkInpuForm(String option){
		String empty="";		
			if(empty.equals(defaultDataTextField[0].getText())){
				JOptionPane.showMessageDialog(this,  "������Ÿ���� �����Ͽ� �ּ���", "�Է�", JOptionPane.WARNING_MESSAGE); 		
				return false;
			} 
			if (empty.equals(defaultDataTextField[1].getText())){
				JOptionPane.showMessageDialog(this,  defaultDataLabels [1] + "�� �Է��Ͽ� �ּ���", "�Է�", JOptionPane.WARNING_MESSAGE); 		
				return false;
			} 
			if (empty.equals(defaultDataTextField[2].getText())){
				JOptionPane.showMessageDialog(this,  defaultDataLabels [2] + "�� �Է��Ͽ� �ּ���", "�Է�", JOptionPane.WARNING_MESSAGE); 		
				return false;
			}
			if (empty.equals(defaultDataTextField[3].getText())){
				JOptionPane.showMessageDialog(this,  defaultDataLabels [3] + "�� �Է��Ͽ� �ּ���", "�Է�", JOptionPane.WARNING_MESSAGE); 		
				return false;
			}
			if (empty.equals(defaultDataTextField[4].getText())){
				JOptionPane.showMessageDialog(this,  defaultDataLabels [4] + "�� �Է��Ͽ� �ּ���", "�Է�", JOptionPane.WARNING_MESSAGE); 		
				return false;
			}
		
			if  (option.equals(studentDataCardString) & empty.equals(addtionalDataTextField[0].getText()) ) {
				JOptionPane.showMessageDialog(this,  addtionalDataLabels [0] + "�� �Է��Ͽ� �ּ���", "�Է�", JOptionPane.WARNING_MESSAGE); 		
				return false;
			} 
			 if (option.equals(professorDataCardString) & empty.equals(addtionalDataTextField[1].getText()) ) {
						JOptionPane.showMessageDialog(this,  addtionalDataLabels [1] + "�� �Է��Ͽ� �ּ���", "�Է�", JOptionPane.WARNING_MESSAGE); 		
				return false;
			}
			if (option.equals(employeeDataCardString) & empty.equals(addtionalDataTextField[2].getText())) {
						JOptionPane.showMessageDialog(this,  addtionalDataLabels [2] + "�� �Է��Ͽ� �ּ���", "�Է�", JOptionPane.WARNING_MESSAGE); 		
				return false;
			}

			return true;
}

//--------------------------------------------------------------------
// �Է��� �� �޽��
//--------------------------------------------------------------------

public void  setInputForm(Object resultObject ){

			CardLayout cl = (CardLayout)(addtionalDataCardPanel.getLayout());
			isSelectedRow = true;
			if(resultObject instanceof  UserImpl){
					UserImpl user = (UserImpl) resultObject;
					defaultDataTextField[0].setText(user.getUserNo());	
					defaultDataTextField[1].setText(user.getName());	
					defaultDataTextField[2].setText(user.getAge());			
					defaultDataTextField[3].setText(user.getSsn());			
					defaultDataTextField[4].setText(user.getAddress());			
					selectedRowSsn = user.getSsn();
					if (user instanceof  Student){
						selectedRowObjectType = studentDataCardString;
						Student stud = (Student ) resultObject;
						addtionalDataTextField[0].setText(stud.getStudentNo());
						selectedRowAddtionalData = stud.getStudentNo();
						cl.show(addtionalDataCardPanel, studentDataCardString);
					}else if (user instanceof  Professor){
						selectedRowObjectType = professorDataCardString;
						Professor prof =  (Professor) resultObject;
						addtionalDataTextField[1].setText(prof.getCourse());
						selectedRowAddtionalData = prof.getCourse();
						cl.show(addtionalDataCardPanel, professorDataCardString);
					}else if (user instanceof  Employee){
						selectedRowObjectType = employeeDataCardString;
						Employee emp = (Employee) resultObject;
						addtionalDataTextField[2].setText(emp.getDepartment());
						selectedRowAddtionalData = emp.getDepartment();
						cl.show(addtionalDataCardPanel, employeeDataCardString);
					}
			}
}

//--------------------------------------------------------------------
// ������ �� �޽��
//--------------------------------------------------------------------

	public void setSequence(String seq){
		defaultDataTextField[0].setText(seq);
	}
//--------------------------------------------------------------------
// actionPerformed
//--------------------------------------------------------------------

public void actionPerformed(ActionEvent e){     // �������̵�
	if (findUserButtonString.equals(e.getActionCommand()) | findUserTextFieldString.equals(e.getActionCommand()) ) {
		String cmd = group.getSelection().getActionCommand();
		String value =  findUserTextField.getText();
		this.resetOutPutData(false, true);
		this.findUser(cmd, value);			
	}else if(listUserButtonString.equals(e.getActionCommand())){
		this.resetOutPutData(true, true);
		tcpc.listUser("list");		
	}else if(createUserButtonString.equals(e.getActionCommand())){
		String option = (String)cb.getSelectedItem();
		if  (option.equals(dataCardString)) {
			JOptionPane.showMessageDialog(this, "������Ÿ���� �����Ͽ� �ּ���", "�Է�", JOptionPane.WARNING_MESSAGE); 
		}else{
			String type = (String)cb.getSelectedItem();		
			String [] insertData =  new String [7];
			insertData[0] = defaultDataTextField[0].getText();	
			insertData[1] = defaultDataTextField[1].getText();	
			insertData[2] = defaultDataTextField[2].getText();		
			insertData[3] = defaultDataTextField[3].getText();	
			insertData[4] = defaultDataTextField[4].getText();		
			if (type.equals("�л�")){
				insertData[5] = addtionalDataTextField[0].getText();
				insertData[6] = "lms_student";
			} else if (type.equals("����")){
				insertData[5] = addtionalDataTextField[1].getText();
				insertData[6] = "lms_professor";
			} else if (type.equals("������")){
				insertData[5] = addtionalDataTextField[2].getText();				
				insertData[6] = "lms_employee";
			}
			if (checkInpuForm(option) && checkSsn(insertData[3]) && checkAge(insertData[2])){		
				this.resetOutPutData(true, true);
				tcpc.createUser("create", type, insertData);
			}								
		}
	}else if(updateUserButtonString.equals(e.getActionCommand())){
		String option = (String)cb.getSelectedItem();
		if (isSelectedRow == false	){
			JOptionPane.showMessageDialog(this, "ȸ�� ��Ͽ��� ������ �����͸� �����Ͽ� �ּ���", "����", JOptionPane.WARNING_MESSAGE); 
		}else{
			String type = (String)cb.getSelectedItem();		
			String [] insertData =  new String [9];
			if (!dataCardString.equals(type)){
				if (type.equals("�л�")){
					insertData[5] = addtionalDataTextField[0].getText();
					insertData[7] = "lms_student";
				} else if (type.equals("����")){
					insertData[5] = addtionalDataTextField[1].getText();
					insertData[7] = "lms_professor";
				} else if (type.equals("������")){
					insertData[5] = addtionalDataTextField[2].getText();				
					insertData[7] = "lms_employee";
				}
			}else{
				type = selectedRowObjectType;
					if (type.equals("�л�")){
						insertData[5] = addtionalDataTextField[0].getText();
						insertData[7] = "lms_student";
					} else if (type.equals("����")){
						insertData[5] = addtionalDataTextField[1].getText();
						insertData[7] = "lms_professor";
					} else if (type.equals("������")){
						insertData[5] = addtionalDataTextField[2].getText();				
						insertData[7] = "lms_employee";
					}
			}	
			if (checkInpuForm(selectedRowObjectType) && checkSsn(selectedRowSsn) && checkAge(defaultDataTextField[2].getText())){		
				String options[] = {"��", "�ƴϿ�"};
				int dialogValue = JOptionPane.showOptionDialog(this,  selectedRowSsn + " �����Ͻðڽ��ϱ�?"," ����", JOptionPane.YES_NO_OPTION, /* Need something  */  	JOptionPane.QUESTION_MESSAGE, null, /* Use default icon for message type*/ options,options[1]);
				if ( dialogValue == JOptionPane.CLOSED_OPTION) {
					System.out.println("Closed window");
				} else if(options[dialogValue].equals("��")){
					insertData[0] = defaultDataTextField[0].getText();	
					insertData[1] = defaultDataTextField[1].getText();	
					insertData[2] = defaultDataTextField[2].getText();		
					insertData[3] = defaultDataTextField[3].getText();	
					insertData[4] = defaultDataTextField[4].getText();		
					if(originalCategory == null){
						originalCategory = type;
					}
					if (originalCategory.equals("�л�")){
						originalCategory = "lms_student";
					} else if (originalCategory.equals("����")){
						originalCategory = "lms_professor";
					} else if (originalCategory.equals("������")){
						originalCategory = "lms_employee";
						insertData[8] = null;
					}
 					if(originalCategory.equals(insertData[7])){
						insertData[0] = originalSequence;
					}else{
						insertData[8] = insertData[0];
					}
					insertData[6] = originalCategory;
					tcpc.createUser("update", originalCategory, insertData);
					selectedRowObjectType = "";
					isSelectedRow = false;
					this.resetOutPutData(true, true);
				}
			}	
		}
	}else if(deleteUserButtonString.equals(e.getActionCommand())){
		if (isSelectedRow == false	){
			JOptionPane.showMessageDialog(this, "ȸ�� ��Ͽ��� ������ �����͸� �����Ͽ� �ּ���", "����", JOptionPane.WARNING_MESSAGE); 
		}else{
			if ((isSelectedRow == true )){			
				String options[] = {"��", "�ƴϿ�"};
				int dialogValue = JOptionPane.showOptionDialog(
						this,  selectedRowSsn + " �����Ͻðڽ��ϱ�?"," ����", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, options,options[1]);
				if ( dialogValue == JOptionPane.CLOSED_OPTION) {
					System.out.println("Closed window");
				} else if(options[dialogValue].equals("��")){
					this.resetOutPutData(true, true);
					tcpc.deleteUser("delete", selectedRowSsn);
				}
			}
		}
	}	
}

}//class TCPClientGUI
