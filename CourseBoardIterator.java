package Iterator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import interfaces.PlanningEntry.CoursePlanningEntry;
import object.Resource.Teacher.Teacher;

public class CourseBoardIterator<Teacher> implements Iterator<CoursePlanningEntry<Teacher>>{
	private int count = 0;//计数器
	private List<CoursePlanningEntry<Teacher>> entries = new ArrayList<CoursePlanningEntry<Teacher>>();
	private final String location;
	private final Calendar date;
	private int size;
	/*
     * Abstraction function:
     * 		That's the iterator of the CourseBoard
     */

    /*
     * Representation invariant:
     * 		count is less than or equal to the entries's size
     * 		entries, location, date is not null
     * 		size is nonnegative
     */

    /*
     * Safety from rep exposure:
     * 		all fields are private or final 
     * 		all functions return an immutable type
     * 		the only one to return an entry is needed
     */
	public void checkRep() {
		assert count <= entries.size();
		assert entries != null;
		assert location != null;
		assert date != null;
		assert size >= 0;
	}
	/**
	 * Constructor for this class and initialize all fields
	 * @param entries : which to select from to show
	 * @param location : represents the location to show
	 * @param date : the current time
	 */
	//这个计划项是所有的计划项，这个type是表示出发、到达、还是经停0, 1, 2
	public CourseBoardIterator(List<CoursePlanningEntry<Teacher>> entries, String location, Calendar date) {
		// TODO Auto-generated constructor stub
		this.entries.addAll(entries);
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
		Map<CoursePlanningEntry<Teacher>, Integer> map = new HashMap<CoursePlanningEntry<Teacher>, Integer>();
		Iterator<CoursePlanningEntry<Teacher>> iterator = entries.iterator();
		while (iterator.hasNext()) {
			CoursePlanningEntry<Teacher> planningEntry = iterator.next();
			if (!planningEntry.getDepartureLocation().toString().equals(location) || !(date.get(Calendar.YEAR) 
					== planningEntry.getBeginTime().get(Calendar.YEAR) && 
					date.get(Calendar.MONTH) == planningEntry.getBeginTime().get(Calendar.MONTH) && 
					date.get(Calendar.DATE) == planningEntry.getBeginTime().get(Calendar.DATE)))
				iterator.remove();
		}
		Collections.sort(entries, new Comparator<CoursePlanningEntry<Teacher>>() {

			@Override
			public int compare(CoursePlanningEntry<Teacher> o1, CoursePlanningEntry<Teacher> o2) {
					if (o1.getBeginTime().before(o2.getBeginTime()))
						return -1;
					else return o1.getEndTime().before(o2.getEndTime()) ? -1 : 1;
			}	
		});
		size = entries.size();
		checkRep();
	}

	@Override
	public boolean hasNext() {
		boolean ans = count < entries.size();
		checkRep(); 
		return ans;
	}

	@Override
	public CoursePlanningEntry<Teacher> next() {
		CoursePlanningEntry<Teacher> planningEntry = entries.get(count);
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
