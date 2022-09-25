package chord.gui.components;

import javax.swing.JLabel;

import chord.ident.ChordSignature;

public class ChordLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String labelPrefix;
	private ChordSignature chordSignature;
	
	public ChordLabel(String labelPrefix,ChordSignature chordSignature) {
		super();
		if(labelPrefix == null) {
			throw new NullPointerException("Label prefix may not be null");
		}
		this.labelPrefix = labelPrefix;
		setChordSignature(chordSignature);
	}
	
	
	/**
	 * Creates a chord label with a default prefix of "Chord" and
	 * the value of the given chord Signature.
	 * @param chordSignature
	 */
	public ChordLabel(ChordSignature chordSignature) {
		this("Chord", chordSignature);
	}
	public void setChordSignature(ChordSignature chordSignature) {
		if(chordSignature == null) {
			throw new NullPointerException("Chord Signature may not be null.");
		}
		this.chordSignature = chordSignature;
		setText(labelPrefix+":" + chordSignature.toString());
	}

	public ChordSignature getChordSignature() {
		return chordSignature;
	}
}
