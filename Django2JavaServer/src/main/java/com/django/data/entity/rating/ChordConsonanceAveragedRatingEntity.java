package com.django.data.entity.rating;

import java.math.BigDecimal;

import com.django.data.entity.ChordSignatureEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class ChordConsonanceAveragedRatingEntity {

	private @Id @GeneratedValue Long id;
	private ChordSignatureEntity chordSignature;
	private Long numRatings;
	@Min(0)
	@Max(100)
	private BigDecimal averagedRating;
	
	protected ChordConsonanceAveragedRatingEntity() {}
	
	public ChordConsonanceAveragedRatingEntity(ChordSignatureEntity chordSignature, Long numRatings,
			BigDecimal averagedRating) {
		super();
		this.id = null;
		this.chordSignature = chordSignature;
		this.numRatings = numRatings;
		this.averagedRating = averagedRating;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ChordSignatureEntity getChordSignature() {
		return chordSignature;
	}

	public void setChordSignature(ChordSignatureEntity chordSignature) {
		this.chordSignature = chordSignature;
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
