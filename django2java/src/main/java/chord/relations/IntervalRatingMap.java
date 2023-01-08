package chord.relations;

import java.util.EnumMap;

import chord.ConsonanceRating;
import chord.Interval;

public class IntervalRatingMap extends EnumMap<Interval, ConsonanceRating> {
	
	public IntervalRatingMap() {
		super(Interval.class);
	}
	
	public ConsonanceRating put(Interval interval,ConsonanceRating rating) {
		if(interval == null) {
			throw new NullPointerException("interval may not be null");
		}
		if(rating == null) {
			throw new NullPointerException("rating may not be null");
		}
		//Make sure that we aren't adding ratings that are
		//already covered in the first octave.
		if( !interval.inFirstOctave()) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7 inclusive");
		}
		
		return super.put(interval, rating);
	}

	public ConsonanceRating get(Interval interval) {
		if(interval == null) {
			throw new NullPointerException("interval may not be null.");
		}
		//Make sure that no one is looking for invalid intervals.
		//The notename in the second octave duplicates those in the
		//first
		if( !interval.inFirstOctave() ) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7");
		}
		
		return super.get(interval);
	}
}
