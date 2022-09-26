package chord.gui.components;

import javax.swing.JLabel;

import chord.Interval;

public class IntervalLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Interval interval;
	
	public IntervalLabel(Interval interval) {
		super();
		setInterval(interval);
	}

	public Interval getInterval() {
		return interval;
	}

	public void setInterval(Interval interval) {
		if(interval == null) {
			throw new NullPointerException("interval may not be null.");
		}
		this.interval = interval;
		setText("Interval:"+ interval.toString());
	}
	
	

}
