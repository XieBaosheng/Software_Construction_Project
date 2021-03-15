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

import Iterator.TrainBoardIterator;
import object.Resource.Carriage.Carriage;
import interfaces.PlanningEntry.*;

class Time3 implements Runnable {
	private List<TrainPlanningEntry<Carriage>> entries;
	private String locationString;
	private JTable table1;
	private JTable table2;
	private JTable table3;

	private final String[] titles1 = { "到达时间", "车次号", "旅程", "状态" };
	private final String[] titles2 = { "出发时间", "车次号", "旅程", "状态" };
	private final String[] titles3 = { "经停时间", "车次号", "旅程", "状态" };
	 /*
     * Abstraction function:
     * 		To achieve a timed refresh function
     */

    /*
     * Representation invariant:
     * 		entries, locationString, table1, table2, table3 are all not null
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
		assert table3 != null;
	}
	/**
	 * Constructor for this class and initialize all fields
	 * @param table1: the first JTable
	 * @param table2: the second JTable
	 * @param table3: the third JTable
	 * @param location:a string that represents the location to show
	 * @param entries: which  selects from to show
	 */
	public Time3(JTable table1, JTable table2, JTable table3, String location,
			List<TrainPlanningEntry<Carriage>> entries) {
		this.table1 = table1;
		this.table2 = table2;
		this.table3 = table3;
		this.entries = entries;
		this.locationString = location;
		checkRep();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			Calendar date = Calendar.getInstance();
			String[][] datas1 = arrivalTo(date);
			String[][] datas2 = departureTo(date);
			String[][] datas3 = pass(date);
			DefaultTableModel dtm1 = (DefaultTableModel) table1.getModel();
			DefaultTableModel dtm2 = (DefaultTableModel) table2.getModel();
			DefaultTableModel dtm3 = (DefaultTableModel) table3.getModel();
			dtm1.setDataVector(datas1, titles1);
			dtm2.setDataVector(datas2, titles2);
			dtm3.setDataVector(datas3, titles3);
			table1.updateUI();
			table2.updateUI();
			table3.updateUI();
			try {
				Thread.sleep(1000 * 60);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Filter out the eligible items and return an array of two-dimensional strings 
	 * representing this information
	 * @param date
	 * @return :the information to show
	 */
	private String[][] departureTo(Calendar date) {

		TrainBoardIterator<Carriage> entryIterator = new TrainBoardIterator<Carriage>(entries, 0, locationString, date);
		String[][] ansStrings = new String[entryIterator.getSize()][4];
		for (int i = 0; i < entryIterator.getSize(); i++) {
			TrainPlanningEntry<Carriage> entry = entryIterator.next();
			ansStrings[i][0] = entry.getBeginTimeStrAtIndex(0);
			ansStrings[i][1] = entry.getEntryName();
			ansStrings[i][2] = entry.getDepartureLocation() + "-" + entry.getArrivalLocation();
			ansStrings[i][3] = entry.getEntryState();
		}
		checkRep();
		return ansStrings;
	}
	/**
	 * Filter out the eligible items and return an array of two-dimensional strings 
	 * representing this information
	 * @param date
	 * @return the information to show
	 */
	private String[][] arrivalTo(Calendar date) {
		TrainBoardIterator<Carriage> entryIterator = new TrainBoardIterator<Carriage>(entries, 1, locationString, date);
		String[][] ansStrings = new String[entryIterator.getSize()][4];
		for (int i = 0; i < entryIterator.getSize(); i++) {
			TrainPlanningEntry<Carriage> entry = entryIterator.next();
			ansStrings[i][0] = entry.getBeginTimeStrAtIndex(entry.getTimesLotNumber() - 1);
			ansStrings[i][1] = entry.getEntryName();
			ansStrings[i][2] = entry.getDepartureLocation() + "-" + entry.getArrivalLocation();
			ansStrings[i][3] = entry.getEntryState();
		}
		checkRep();
		return ansStrings;
	}

	/**
	 * Filter out the eligible items and return an array of two-dimensional strings 
	 * representing this information
	 * @param date
	 * @return the information to show
	 */
	private String[][] pass(Calendar date) {
		TrainBoardIterator<Carriage> entryIterator = new TrainBoardIterator<Carriage>(entries, 2, locationString, date);
		String[][] ansStrings = new String[entryIterator.getSize()][4];
		for (int i = 0; i < entryIterator.getSize(); i++) {
			TrainPlanningEntry<Carriage> entry = entryIterator.next();
			for (int j = 1; j < entry.getLocationNumber() - 1; j++) {
				if (entry.getLocationAtIndex(j).toString().equals(locationString)) {
					ansStrings[i][0] = entry.getEndTimeStrAtIndex(j - 1) + " - " + entry.getBeginTimeStrAtIndex(j);
					ansStrings[i][1] = entry.getEntryName();
					ansStrings[i][2] = entry.getDepartureLocation() + "-" + entry.getArrivalLocation();
					ansStrings[i][3] = entry.getEntryState();
				}
			}
		}
		checkRep();
		return ansStrings;
	}
}
	public class TrainBoard {
		private final String locationString;
		private List<TrainPlanningEntry<Carriage>> entries;
		
		 /*
	     * Abstraction function:
	     * 		represents a board in the train station that show the trains's information
	     */

	    /*
	     * Representation invariant:
	     * 		locationString and entries are both not null
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
		 * Constructor for this class and initialize locationString and entries
		 * @param location : the location to show
		 * @param entries : the entries to select from
		 */
		public TrainBoard(String location, List<TrainPlanningEntry<Carriage>> entries) {
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
			JTable table3 = new JTable();
			JTextField titleArea = new JTextField();
			JTextField subTitle1 = new JTextField("抵达高铁");
			JTextField subTitle2 = new JTextField("出发高铁");
			JTextField subTitle3 = new JTextField("经停高铁");
			Container container = frame.getContentPane();

			frame.setLocation(0, 0);
			frame.setSize(800, 800);

			container.setLayout(new BorderLayout());

			JPanel centerPane = new JPanel();

			container.add(centerPane, BorderLayout.CENTER);

			centerPane.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			new Thread(new Time3(table1, table2, table3, locationString, entries)).start();
			new Thread(new Time1(titleArea, locationString)).start();

			titleArea.setFont(new Font("宋体", Font.BOLD, 40));
			titleArea.setHorizontalAlignment(JTextField.CENTER);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.weightx = 1;
			gbc.weighty = 0.1;
			centerPane.add(titleArea, gbc);

			subTitle1.setFont(new Font("宋体", Font.PLAIN, 25));
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
			table1.getTableHeader().setFont(new Font("宋体", Font.PLAIN, 20));
			table1.setFont(new Font("宋体", Font.PLAIN, 15));
			table1.setRowHeight(25);

			DefaultTableCellRenderer r = new DefaultTableCellRenderer();
			r.setHorizontalAlignment(JLabel.CENTER);
			table1.setDefaultRenderer(Object.class, r);

			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridwidth = 3;
			gbc.gridheight = 1;
			gbc.weightx = 1;
			gbc.weighty = 0.25;
			centerPane.add(new JScrollPane(table1), gbc);

			subTitle2.setHorizontalAlignment(JTextField.CENTER);
			subTitle2.setFont(new Font("宋体", Font.PLAIN, 25));
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
			table2.getTableHeader().setFont(new Font("宋体", Font.PLAIN, 20));
			table2.setFont(new Font("宋体", Font.PLAIN, 15));
			table2.setRowHeight(25);
			table2.setDefaultRenderer(Object.class, r);
			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.gridwidth = 3;
			gbc.gridheight = 1;
			gbc.weightx = 1;
			gbc.weighty = 0.25;
			centerPane.add(new JScrollPane(table2), gbc);

			subTitle3.setHorizontalAlignment(JTextField.CENTER);
			subTitle3.setFont(new Font("宋体", Font.PLAIN, 25));
			gbc.gridx = 0;
			gbc.gridy = 5;
			gbc.gridwidth = 3;
			gbc.gridheight = 1;
			gbc.weightx = 1;
			gbc.weighty = 0.05;
			centerPane.add(subTitle3, gbc);

			table3.getTableHeader().setVisible(true);
			table3.getTableHeader().setBackground(Color.black);
			table3.getTableHeader().setForeground(Color.white);
			table3.getTableHeader().setFont(new Font("宋体", Font.PLAIN, 20));
			table3.setFont(new Font("宋体", Font.PLAIN, 15));
			table3.setRowHeight(25);
			table3.setDefaultRenderer(Object.class, r);
			gbc.gridx = 0;
			gbc.gridy = 6;
			gbc.gridwidth = 3;
			gbc.gridheight = 1;
			gbc.weightx = 1;
			gbc.weighty = 0.25;
			centerPane.add(new JScrollPane(table3), gbc);

			frame.setVisible(true);
			checkRep();
		}

}
