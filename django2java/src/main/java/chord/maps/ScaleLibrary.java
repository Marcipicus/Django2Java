package chord.maps;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import chord.NoteName;
import chord.Scale;
import chord.ident.ScaleSignature;

public class ScaleLibrary {
	
	private static ScaleLibrary instance;
	
	public static ScaleLibrary getInstance() {
		if(instance == null) {
			instance = new ScaleLibrary();
		}
		return instance;
	}
	
	private Map<ScaleSignature,Map<NoteName,Scale>> scaleMap;

	private ScaleLibrary() {
		scaleMap = new EnumMap<>(ScaleSignature.class);
		
		for(ScaleSignature scaleSig : ScaleSignature.values()) {

			Map<NoteName,Scale> noteToConcreteScaleMap = new EnumMap<>(NoteName.class);
			for(NoteName noteName : NoteName.values()) {
				Scale concreteScale = new Scale(noteName,scaleSig);
				
				noteToConcreteScaleMap.put(noteName, concreteScale);
			}
			
			//prevent modification of data structure
			scaleMap.put(scaleSig, Collections.unmodifiableMap(noteToConcreteScaleMap));
		}
		
		//prevent modification to the data structure
		scaleMap = Collections.unmodifiableMap(scaleMap);
	}
	
	public Scale getScale(NoteName rootNote, ScaleSignature scaleSig) {
		return this.scaleMap.get(scaleSig).get(rootNote);
	}

}
