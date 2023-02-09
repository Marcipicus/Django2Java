package com.django.data.entity.rating;

import java.math.BigDecimal;

import com.django.data.entity.ChordSignatureEntity;
import com.django.data.entity.IntervalEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class ChordChangeConsonanceAveragedRatingEntity {

	private @Id @GeneratedValue Long id;
	private ChordSignatureEntity referenceChordSignature;
	private ChordSignatureEntity targetChordSignature;
	private IntervalEntity intervalBetweenRoots;
	private Long numRatings;
	@Min(0)
	@Max(100)
	private BigDecimal averagedRating;
	
	protected ChordChangeConsonanceAveragedRatingEntity() {}
	
	public ChordChangeConsonanceAveragedRatingEntity(ChordSignatureEntity referenceChordSignature,
			ChordSignatureEntity targetChordSignature, IntervalEntity intervalBetweenRoots, Long numRatings,
			BigDecimal averagedRating) {
		super();
		this.id = null;
		this.referenceChordSignature = referenceChordSignature;
		this.targetChordSignature = targetChordSignature;
		this.intervalBetweenRoots = intervalBetweenRoots;
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

	public ChordSignatureEntity getTargetChordSignature() {
		return targetChordSignature;
	}

	public void setTargetChordSignature(ChordSignatureEntity targetChordSignature) {
		this.targetChordSignature = targetChordSignature;
	}

	public IntervalEntity getIntervalBetweenRoots() {
		return intervalBetweenRoots;
	}

	public void setIntervalBetweenRoots(IntervalEntity intervalBetweenRoots) {
		this.intervalBetweenRoots = intervalBetweenRoots;
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
