package chord.relations.request;

import chord.ident.ScaleSignature;

public class ScaleConsonanceRecordRequest extends AbstractRecordRequest{
	
	private ScaleRequest scaleRequest;
	
	
	public ScaleConsonanceRecordRequest() {
		super();
		scaleRequest = new ScaleRequest();
	}
	
	public void addScaleRequest(ScaleRequest request) throws RequestInitializationException{
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if( !request.isInitialized()) {
			throw new IllegalArgumentException("request has not been initialized.");
		}
		this.scaleRequest = request;
	}

	/**
	 * Test to see if the request is looking for the interval.
	 * @param interval interval being checked to see if requested.
	 * @return true if the request is looking for the interval.
	 */
	public boolean contains(ScaleSignature scaleSignature) {
		//we know that there are no nulls so we don't have to check the
		//method parameter for it.
		return this.contains(scaleSignature);
	}

	@Override
	protected boolean allAditionalParametersInitialized() {
		if(scaleRequest == null) {
			return false;
		}
		return this.scaleRequest.isInitialized();
	}
	
	
}
