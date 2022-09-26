package chord.gui.components;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * GridBagConstraints class that provides a constructor that only requires 
 * gridx,gridy,width,height, and fill.
 * @author DAD
 *
 */
public class CustomGridBagConstraints extends GridBagConstraints {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight,int fill) {
		super();
		this.gridx = gridx;
		this.gridy = gridy;
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
		this.fill = fill;
	}

}
