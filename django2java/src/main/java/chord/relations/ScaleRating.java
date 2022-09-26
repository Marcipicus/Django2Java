package chord.relations;

import chord.ConsonanceRating;
import chord.ident.ScaleSignature;

public class ScaleRating {
	private ScaleSignature scaleSig;
	private ConsonanceRating rating;

	public ScaleRating(ScaleSignature scaleSig, ConsonanceRating rating) {
		this.scaleSig = scaleSig;
		this.rating = rating;
	}

	public ScaleRating() {

	}

	public ScaleSignature getScaleSig() {
		return scaleSig;
	}

	public void setScaleSig(ScaleSignature scaleSig) {
		this.scaleSig = scaleSig;
	}

	public ConsonanceRating getRating() {
		return rating;
	}

	public void setRating(ConsonanceRating rating) {
		this.rating = rating;
	}
	
	//TODO: Might have to change this method to look at displayText
	@Override
	public String toString() {
		return(scaleSig.toString()+":"+rating.toString());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ScaleRating))
			return false;
		ScaleRating other = (ScaleRating) o;

		boolean scalesEqual, ratingsEqual;

		scalesEqual = this.scaleSig.equals(other.scaleSig);
		ratingsEqual = this.rating.equals(other.rating);
		return scalesEqual && ratingsEqual;
	}

	@Override
	public int hashCode() {
		final int prime = 17;

		int result = 1;

		result = prime * result + ((scaleSig==null) ? 0 : scaleSig.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());

		return result;
	}
}
