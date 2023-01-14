package chord;

import chord.ident.ScaleSignature;

public interface Scale {
	NoteName getRootNote();
	
	ScaleSignature getScaleSignature();
}
