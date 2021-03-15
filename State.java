package State;

import java.util.Calendar;
import java.util.List;

import object.time.*;

public abstract class State {
	private Context context;
	
	//不知道这个有没有表示泄露
	public boolean setContext(Context context) {
		this.context = context;
		return true;
	}
	protected Context getContext() {
		return this.context;
	}
	public boolean setResource() {
		return false;
	}
	public boolean run() {
		return false;
	}
	public boolean end() {
		return false;
	}
	public boolean block(Calendar date, List<Timeslot> timeslots) {
		return false;
	}
	public boolean reRun(Calendar date, List<Timeslot> timeslots) {
		return false;
	}
	public boolean cancel() {
		return false;
	}
	@Override
	public String toString() {
		return null;
	}
}

