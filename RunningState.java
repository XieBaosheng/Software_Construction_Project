package State;

import java.util.Calendar;
import java.util.List;

import object.time.*;
public class RunningState extends State{
	
	@Override
	public boolean end() {
		super.getContext().setState(Context.endedState);
		return true;
	}
	@Override
	public boolean block(Calendar date, List<Timeslot> timeslots) {
		if (timeslots == null)
			return false;
		int length = timeslots.size();
		for (int i = 0; i < length - 1; i++) {
			if (timeslots.get(i).getEndDate().before(date) &&timeslots.get(i +1).getBeginDate().after(date)) {
				super.getContext().setState(Context.blockedState);
				return true;
			}
		}
		return false;
	}
	@Override
	public String toString() {
		return "RunningState";
	}
}
