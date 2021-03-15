package object.Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Calendar;
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

import Iterator.CourseBoardIterator;
import object.Resource.Teacher.Teacher;
import interfaces.PlanningEntry.*;

class Time4 implements Runnable{
	private List<CoursePlanningEntry<Teacher>> entries;
	private String locationString;
	private JTable table1;
	
	private final String[] titles1 = {"起止时间", "课程名称", "教师", "状态"};
	 /*
     * Abstraction function:
     * 		Keep the panel updated at any time
     */

    /*
     * Representation invariant:
     * 		entries, locationString, table1 is not null
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
	}
	/**
	 * this class's constructor, and initialize table1, location and entries
	 * @param table1
	 * @param location
	 * @param entries
	 */
	public Time4(JTable table1, String location, List<CoursePlanningEntry<Teacher>> entries) {
		this.table1 = table1;
		this.entries = entries;
		this.locationString = location;
		checkRep();
	}
	@Override
	public void run() {
		while (true) {
			Calendar date = Calendar.getInstance();
			String[][] datas1 = getEntries(date);
			DefaultTableModel dtm1 = (DefaultTableModel)table1.getModel();
			dtm1.setDataVector(datas1,titles1);
			table1.updateUI();
			try {
				Thread.sleep(1000*60);
				checkRep();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Select qualified entries from entries
	 * @param date current time
	 * @return
	 */
	private String[][] getEntries(Calendar date){
		
		CourseBoardIterator<Teacher> boardIterator = new CourseBoardIterator<Teacher>(entries, locationString, date);
		String[][] ansStrings = new String[boardIterator.getSize()][4];
		for (int i = 0; i < boardIterator.getSize(); i++) {
			CoursePlanningEntry<Teacher> entry = boardIterator.next();
			ansStrings[i][0] = entry.getBeginTimeStr() + " - " + entry.getEndTimeStr();
			ansStrings[i][1] = entry.getEntryName();
			ansStrings[i][2] = entry.getResourceName();
			ansStrings[i][3] = entry.getEntryState();
		}
		checkRep();
		return ansStrings;
	}
}
public class CourseBoard{
	private final String locationString;
	private List<CoursePlanningEntry<Teacher>> entries;
	
	 /*
     * Abstraction function:
     * 		represents a board that shows class in this location
     */

    /*
     * Representation invariant:
     * 		locationString isn't null
     * 		entries isn't null
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
	 * this class's constructor
	 * @param location 
	 * @param entries
	 */
	public CourseBoard (String location, List<CoursePlanningEntry<Teacher>> entries) {
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
		JTextField titleArea = new JTextField();
		JTextField subTitle1 = new JTextField("课程表");
		
		Container container = frame.getContentPane();
		
		
        frame.setLocation(0, 0);
        frame.setSize(1600,800);
        container.setLayout(new BorderLayout());
        
        JPanel centerPane = new JPanel();
        
        container.add(centerPane, BorderLayout.CENTER);
        
        centerPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        new Thread(new Time4(table1, locationString, entries)).start();
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
        gbc.weighty = 0.75;
        centerPane.add(new JScrollPane(table1), gbc);
        frame.setVisible(true);
        checkRep();
	}
}

