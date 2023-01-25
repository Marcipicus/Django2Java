package chord;

import chord.ident.ScaleSignature;

public interface Scale extends ToneCollection{
	/**
	 * Get the root note.
	 * @return root note
	 */
	NoteName getRootNote();
	
	/**
	 * Get the scale signature.
	 * @return scale signature
	 */
	ScaleSignature getScaleSignature();
}
