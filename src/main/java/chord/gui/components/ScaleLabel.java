package chord.gui.components;

import javax.swing.JLabel;

import chord.ident.ScaleSignature;

public class ScaleLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ScaleSignature scaleSignature;
	
	public ScaleLabel(ScaleSignature scaleSignature) {
		super();
		setScaleSignature(scaleSignature);
	}

	public ScaleSignature getScaleSignature() {
		return scaleSignature;
	}

	public void setScaleSignature(ScaleSignature scaleSignature) {
		if(scaleSignature == null) {
			throw new NullPointerException("Scale signature may not be null.");
		}
		this.scaleSignature = scaleSignature;
		setText("Scale:" + scaleSignature.toString());
	}
}
