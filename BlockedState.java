package State;

import java.util.Calendar;
import java.util.List;
import object.time.*;
public class BlockedState extends State{
	@Override 
	public boolean cancel() {
		super.getContext().setState(Context.cancalledState);
		return true;
	}
	//不知道这个参数怎么设置，实现什么功能
	@Override
	public boolean reRun(Calendar date, List<Timeslot> timeslots) {
		if (timeslots == null)
			return false;
		int length = timeslots.size();
		
		super.getContext().setState(Context.runningState);
		return false;
		
	}
	@Override
	public String toString() {
		return "BlockedState";
	}
}
