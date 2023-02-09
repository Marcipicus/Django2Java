package com.django.data.entity.rating;

import java.math.BigDecimal;

import com.django.data.entity.ChordSignatureEntity;
import com.django.data.entity.ScaleSignatureEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class ScaleConsonanceAveragedRatingEntity {

	private @Id @GeneratedValue Long id;
	private ChordSignatureEntity referenceChordSignature;
	private ScaleSignatureEntity scaleSignature;
	private Long numRatings;
	@Min(0)
	@Max(100)
	private BigDecimal averagedRating;
	
	protected ScaleConsonanceAveragedRatingEntity() {}
	
	public ScaleConsonanceAveragedRatingEntity(ChordSignatureEntity referenceChordSignature,
			ScaleSignatureEntity scaleSignature, Long numRatings, BigDecimal averagedRating) {
		super();
		this.id = null;
		this.referenceChordSignature = referenceChordSignature;
		this.scaleSignature = scaleSignature;
		this.numRatings = numRatings;
		this.averagedRating = averagedRating;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ChordSignatureEntity getReferenceChordSignature() {
		return referenceChordSignature;
	}

	public void setReferenceChordSignature(ChordSignatureEntity referenceChordSignature) {
		this.referenceChordSignature = referenceChordSignature;
	}

	public ScaleSignatureEntity getScaleSignature() {
		return scaleSignature;
	}

	public void setScaleSignature(ScaleSignatureEntity scaleSignature) {
		this.scaleSignature = scaleSignature;
	}

	public Long getNumRatings() {
		return numRatings;
	}

	public void setNumRatings(Long numRatings) {
		this.numRatings = numRatings;
	}

	public BigDecimal getAveragedRating() {
		return averagedRating;
	}

	public void setAveragedRating(BigDecimal averagedRating) {
		this.averagedRating = averagedRating;
	}
}
