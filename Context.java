package State;

import java.util.Calendar;
import java.util.List;

import object.time.*;
public class Context {
	
	protected final static WaitingState waitingState = new WaitingState();
	protected final static AllocatedState allocatedState = new AllocatedState();
	protected final static RunningState runningState = new RunningState();
	protected final static EndedState endedState = new EndedState();
	protected final static BlockedState blockedState = new BlockedState();
	protected final static CancelledState cancalledState = new CancelledState();
	
	private State state;
	
	public static Context inintState() {
		Context context = new Context();
		context.setState(waitingState);
		return context;
	}
	//表示泄露怎么处理
	public State getState() {
		return this.state;
	}
	//设置状态
	public boolean setState(State state) {
		this.state = state;
		//把当前的环境通知到各个实现类中
		return this.state.setContext(this);
		
	}
	
	public boolean setResource() {
		return this.state.setResource();
	}
	
	public boolean run() {
		return this.state.run();
	}
	
	public boolean end() {
		return this.state.end();
	}
	
	public boolean block(Calendar date, List<Timeslot> timeslots) {
		return this.state.block(date, timeslots);
	}
	
	public boolean reRun(Calendar date, List<Timeslot> timeslots) {
		return this.state.reRun(date, timeslots);
	}
	public boolean cancel() {
		return this.state.cancel();
	}
	
	@Override
	public String toString() {
		return this.state.toString();
	}
}

