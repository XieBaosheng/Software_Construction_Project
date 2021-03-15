package State;

public class AllocatedState extends State{
	
	@Override
	public boolean run() {
		super.getContext().setState(Context.runningState);
		return true;
	}
	
	@Override 
	public boolean cancel() {
		super.getContext().setState(Context.cancalledState);
		return true;
	}
	@Override
	public String toString() {
		return "AllocatedState";
	}
}
