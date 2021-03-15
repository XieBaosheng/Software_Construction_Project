package Iterator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import interfaces.PlanningEntry.TrainPlanningEntry;

public class TrainBoardIterator<Carriage> implements Iterator<TrainPlanningEntry<Carriage>>{
	//这个存储得到的entries,这些entries未经处理，需要自己实现排序的功能，还得把那些不符合条件的删除掉
	private int count = 0;//计数器
	private List<TrainPlanningEntry<Carriage>> entries = new ArrayList<TrainPlanningEntry<Carriage>>();
	private final int type;
	private final String location;
	private final Calendar date;
	private int size;
	/*
     * Abstraction function:
     * 		That's the iterator of the TrainBoard
     */

    /*
     * Representation invariant:
     * 		count is less than or equal to the entries's size
     * 		entries, location, date is not null
     * 		size is nonnegative
     * 		type is 0, 1 or 2
     */

    /*
     * Safety from rep exposure:
     * 		all fields are private or final 
     * 		all functions return an immutable type
     * 		the only one to return an entry is needed
     */
	private void checkRep() {
		assert count <= entries.size();
		assert entries != null;
		assert location != null;
		assert size >= 0;
		assert type == 1 || type == 2 || type == 0;
	}
	/**
	 * Constructor for this class
	 * @param entries
	 * @param type
	 * @param location
	 * @param date
	 */
	public TrainBoardIterator(List<TrainPlanningEntry<Carriage>> entries, int type, String location, Calendar date) {
		// TODO Auto-generated constructor stub
		this.entries.addAll(entries);
		this.type = type;
		this.location = location;
		this.date = date;
		pre();
		checkRep();
	}
	/**
	 * Select items that have passed through the specified location from the 
	 * current entries and sort the qualified items
	 */
	private void pre() {
		Map<TrainPlanningEntry<Carriage>, Integer> map = new HashMap<TrainPlanningEntry<Carriage>, Integer>();
		Iterator<TrainPlanningEntry<Carriage>> iterator = entries.iterator();
		switch (type) {//出发
		case 0:
			while (iterator.hasNext()) {
				TrainPlanningEntry<Carriage> planningEntry = iterator.next();
				if (!planningEntry.getDepartureLocation().toString().equals(location) || 
						Math.abs(date.getTimeInMillis() - planningEntry.getBeginTime().getTimeInMillis()) > 60 * 60 * 1000)
					iterator.remove();
			}
			break;
		case 1://到达
			while (iterator.hasNext()) {
				TrainPlanningEntry<Carriage> planningEntry = iterator.next();
				if (!planningEntry.getArrivalLocation().toString().equals(location) || 
						Math.abs(date.getTimeInMillis() - planningEntry.getEndTime().getTimeInMillis()) > 60 * 60 * 1000)
					iterator.remove();
			}
			break;
		case 2:
			while (iterator.hasNext()) {
				TrainPlanningEntry<Carriage> planningEntry = iterator.next();
				for (int i = 1; i < planningEntry.getLocationNumber() - 1; i++) {
					if (!planningEntry.getLocationAtIndex(i).toString().equals(location) ||
							(Math.abs(date.getTimeInMillis() - planningEntry.getEndTimeAtIndex(i - 1).getTimeInMillis()) > 60 * 60 * 1000
					 && Math.abs(date.getTimeInMillis() - planningEntry.getBeginTimeAtIndex(i).getTimeInMillis()) > 60 * 60 * 1000)) {
						iterator.remove();
					}
					else {
						map.put(planningEntry, i);//下标是i的位置是满足条件的
					}
				}	
			}
			break;
		}
		Collections.sort(entries, new Comparator<TrainPlanningEntry<Carriage>>() {

			@Override
			public int compare(TrainPlanningEntry<Carriage> o1, TrainPlanningEntry<Carriage> o2) {
				switch (type) {
				case 0:
					return o1.getBeginTime().before(o2.getBeginTime()) ? -1 : 1;
				case 1:
					return o1.getEndTime().before(o2.getEndTime()) ? -1 : 1;
				case 2:
					if( o1.getEndTimeAtIndex(map.get(o1) - 1).before(o2.getEndTimeAtIndex(map.get(o2) - 1)))
						return -1;
					else
						return o1.getBeginTimeAtIndex(map.get(o1)).before(o2.getBeginTimeAtIndex(map.get(o2))) ? -1 :1;
				}
				return 0;
			}
		});
		size = entries.size();
		checkRep();
	}

	@Override
	public boolean hasNext() {
		return count < entries.size();
	}

	@Override
	public TrainPlanningEntry<Carriage> next() {
		TrainPlanningEntry<Carriage> planningEntry = entries.get(count);
		count++;
		checkRep();
		return planningEntry;
	}
	/**
	 * 
	 * @return the number of elements contained in the iterator
	 */
	public int getSize() {
		return size;
	}
}

