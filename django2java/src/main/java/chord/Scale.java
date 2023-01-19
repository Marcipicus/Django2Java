package chord;

import chord.ident.ScaleSignature;

public interface Scale extends ToneCollection{
	NoteName getRootNote();
	
	ScaleSignature getScaleSignature();
}
