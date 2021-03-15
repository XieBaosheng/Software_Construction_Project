package object.Board;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Iterator.FlightBoardIterator;
import object.Resource.Plane.*;
import interfaces.PlanningEntry.*;
class Time1 implements Runnable{
	private JTextField jLabel = new JTextField();
	private final String locationString;
	 /*
     * Abstraction function:
     * 		Keep the panel updated at any time
     */

    /*
     * Representation invariant:
     * 		jLabel and locationString are not null
     * 		
     */

    /*
     * Safety from rep exposure:
     * 		any function in this class returns nothing
     */
	private void checkRep() {
		assert jLabel != null;
		assert locationString != null;
	}
	/**
	 * constructor for this class and initialize jLabel and locationString
	 * @param label
	 * @param locationString
	 */
	public Time1(JTextField label, String locationString) {
		this.jLabel = label;
		this.locationString = locationString;
		checkRep();
	}
	@Override
	public void run() {
		while (true) {
			DateFormat d1 = DateFormat.getDateTimeInstance();
			Date timeCalendar = new Date();
			jLabel.setText(d1.format(timeCalendar) + " " + locationString);
			try {
				Thread.sleep(1000);
				checkRep();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
}
class Time2 implements Runnable{
	private List<FlightPlanningEntry<Plane>> entries;
	private String locationString;
	private JTable table1;
	private JTable table2;
	
	private final String[] titles1 = {"到达时间", "航班号", "航程", "状态"};
	private final String[] titles2 = {"出发时间", "航班号", "航程", "状态"};
	 /*
     * Abstraction function:
     * 		Keep the panel updated at any time
     */

    /*
     * Representation invariant:
     * 		entries, locationString, table1 and table2 are not null
     * 		
     */

    /*
     * Safety from rep exposure:
     * 		any function in this class returns nothing
     */
	private void checkRep() {
		assert entries != null;
		assert locationString != null;
		assert table1 != null;
		assert table2 != null;
	}
	/**
	 * constructor and initialize all fields in this class
	 * @param table1
	 * @param table2
	 * @param location
	 * @param entries
	 */
	public Time2(JTable table1, JTable table2, String location, List<FlightPlanningEntry<Plane>> entries) {
		this.table1 = table1;
		this.table2 = table2;
		this.entries = entries;
		this.locationString = location;
		checkRep();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("1");
		while (true) {
			Calendar date = Calendar.getInstance();
			String[][] datas1 = arrivalTo(date);
			String[][] datas2 = departureTo(date);
			if (datas1 == null)
				System.out.println("null");
			DefaultTableModel dtm1 = (DefaultTableModel)table1.getModel();
			DefaultTableModel dtm2 = (DefaultTableModel)table2.getModel();
			dtm1.setDataVector(datas1,titles1);
			dtm2.setDataVector(datas2, titles2);
			table1.updateUI();
			table2.updateUI();
			try {
				Thread.sleep(1000*60);
				checkRep();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @param date
	 * @return a two-dimensional array of strings representing what the panel is supposed to display
	 */
	private String[][] departureTo(Calendar date){
		FlightBoardIterator<Plane> entryIterator = new FlightBoardIterator<Plane>(entries, 0, locationString, date);
		String[][] ansStrings = new String[entries.size()][4];
		for (int i = 0; i < entryIterator.getSize(); i++) {
			FlightPlanningEntry<Plane> entry = entryIterator.next();
			ansStrings[i][0] = entry.getBeginTimeStr();
			ansStrings[i][1] = entry.getEntryName();
			ansStrings[i][2] = entry.getDepartureLocation() + "-" + entry.getArrivalLocation();
			ansStrings[i][3] = entry.getEntryState();
		}
		checkRep();
		return ansStrings;
	}
	/**
	 * 
	 * @param date
	 * @return a two-dimensional array of strings representing what the panel is supposed to display
	 */
	private String[][] arrivalTo(Calendar date){
		FlightBoardIterator<Plane> entryIterator = new FlightBoardIterator<Plane>(entries, 1, locationString, date);
		String[][] ansStrings = new String[entryIterator.getSize()][4];
		for (int i = 0; i < entryIterator.getSize(); i++) {
			FlightPlanningEntry<Plane> entry = entryIterator.next();
			ansStrings[i][0] = entry.getEndTimeStr();
			ansStrings[i][1] = entry.getEntryName();
			ansStrings[i][2] = entry.getDepartureLocation() + "-" + entry.getArrivalLocation();
			ansStrings[i][3] = entry.getEntryState();
		}
		checkRep();
		return ansStrings;
	}
	
}
public class FlightBoard{
	private final String locationString;
	private List<FlightPlanningEntry<Plane>> entries;
	 /*
     * Abstraction function:
     * 		represents a board in the airport that presents flight information
     */

    /*
     * Representation invariant:
     * 		locationString and entries are both not null
     * 		
     */

    /*
     * Safety from rep exposure:
     * 		any function in this class returns nothing
     */
	private void checkRep() {
		assert locationString != null;
		assert entries != null;
	}
	/**
	 * constructor for this class
	 * @param location
	 * @param entries
	 */
	public FlightBoard(String location, List<FlightPlanningEntry<Plane>> entries) {
		locationString = location;
		this.entries = entries;
		checkRep();
	}
	/**
	 * show the board
	 */
	public void visualize() {
		
		JFrame frame = new JFrame("GridWeightTest");
		
		JTable table1 = new JTable();
		JTable table2 = new JTable();
		JTextField titleArea = new JTextField();
		JTextField subTitle1 = new JTextField("抵达航班");
		JTextField subTitle2 = new JTextField("出发航班");
		
		Container container = frame.getContentPane();
		
		
        frame.setLocation(0, 0);
        frame.setSize(800,800);
       // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container.setLayout(new BorderLayout());
        
        JPanel centerPane = new JPanel();
        
        container.add(centerPane, BorderLayout.CENTER);
        
        centerPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        new Thread(new Time2(table1, table2, locationString, entries)).start();
        new Thread(new Time1(titleArea, locationString)).start();
        
        titleArea.setFont(new Font("宋体",Font.BOLD,40));
        titleArea.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        centerPane.add(titleArea, gbc);
        
        subTitle1.setFont(new Font("宋体",Font.PLAIN,25));
        subTitle1.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.05;
        centerPane.add(subTitle1, gbc);
        
        
        table1.getTableHeader().setVisible(true); 
        table1.getTableHeader().setBackground(Color.black);
        table1.getTableHeader().setForeground(Color.white);
        table1.getTableHeader().setFont(new Font("宋体",Font.PLAIN,20));
        table1.setFont(new Font("宋体",Font.PLAIN,15));
        table1.setRowHeight(25);
        
        DefaultTableCellRenderer r=new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table1.setDefaultRenderer(Object.class,r);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.4;
        centerPane.add(new JScrollPane(table1), gbc);
        
        subTitle2.setHorizontalAlignment(JTextField.CENTER);
        subTitle2.setFont(new Font("宋体",Font.PLAIN,25));
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.05;
        centerPane.add(subTitle2, gbc);
        
        table2.getTableHeader().setVisible(true); 
        table2.getTableHeader().setBackground(Color.black);
        table2.getTableHeader().setForeground(Color.white);
        table2.getTableHeader().setFont(new Font("宋体",Font.PLAIN,20));
        table2.setFont(new Font("宋体",Font.PLAIN,15));
        table2.setRowHeight(25);
        table2.setDefaultRenderer(Object.class,r);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.4;
        centerPane.add(new JScrollPane(table2), gbc);
        frame.setVisible(true);
        checkRep();
	}
}

