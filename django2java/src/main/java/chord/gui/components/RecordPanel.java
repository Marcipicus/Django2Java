package chord.gui.components;

import javax.swing.JPanel;

/**
 * JPanel used to display the record being rated.
 * @author DAD
 *
 * @param <RECORD>
 */
public abstract class RecordPanel<RECORD> extends JPanel {
	public RecordPanel(RECORD record) {
		super();
		initializePanel();
		updatePanel(record);
	}
	
	/**
	 * Initialize the panel by creating labels and 
	 * setting the layout of the panel.
	 * 
	 */
	protected abstract void initializePanel();
	
	/**
	 * Update the panel with the given record,
	 * pass null if there are no more ratings to add.
	 * @param record
	 */
	public abstract void updatePanel(RECORD record);
}
