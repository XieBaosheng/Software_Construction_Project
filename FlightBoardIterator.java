package Iterator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import interfaces.PlanningEntry.FlightPlanningEntry;
import object.Resource.Plane.Plane;

public class FlightBoardIterator<Plane> implements Iterator<FlightPlanningEntry<Plane>>{
	//这个存储得到的entries,这些entries未经处理，需要自己实现排序的功能，还得把那些不符合条件的删除掉
	private int count = 0;//计数器
	private List<FlightPlanningEntry<Plane>> entries = new ArrayList<FlightPlanningEntry<Plane>>();
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
     * 		type is 0 or 1
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
		assert type == 1 || type == 0;
	}
	/**
	 * Constructor for this class and initialize all fields
	 * @param entries 
	 * @param type : 0 or 1
	 * @param location
	 * @param date
	 */
	public FlightBoardIterator(List<FlightPlanningEntry<Plane>> entries, int type, String location, Calendar date) {
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
		Map<FlightPlanningEntry<Plane>, Integer> map = new HashMap<FlightPlanningEntry<Plane>, Integer>();
		Iterator<FlightPlanningEntry<Plane>> iterator = entries.iterator();
		switch (type) {//出发
		case 0:
			while (iterator.hasNext()) {
				FlightPlanningEntry<Plane> planningEntry = iterator.next();
				if (!planningEntry.getDepartureLocation().toString().equals(location) || 
						Math.abs(date.getTimeInMillis() - planningEntry.getBeginTime().getTimeInMillis()) > 60 * 60 * 1000)
					iterator.remove();
			}
			break;
		case 1://到达
			while (iterator.hasNext()) {
				FlightPlanningEntry<Plane> planningEntry = iterator.next();
				if (!planningEntry.getArrivalLocation().toString().equals(location) ||
						Math.abs(date.getTimeInMillis() - planningEntry.getEndTime().getTimeInMillis()) > 60 * 60 * 1000)
					iterator.remove();
			}
			break;
		}
		Collections.sort(entries, new Comparator<FlightPlanningEntry<Plane>>() {

			@Override
			public int compare(FlightPlanningEntry<Plane> o1, FlightPlanningEntry<Plane> o2) {
				switch (type) {
				case 0:
					return o1.getBeginTime().before(o2.getBeginTime()) ? -1 : 1;
				case 1:
					return o1.getEndTime().before(o2.getEndTime()) ? -1 : 1;
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
	public FlightPlanningEntry<Plane> next() {
		FlightPlanningEntry<Plane> planningEntry = entries.get(count);
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
