package com.django.data.entity.rating.instance;

import java.math.BigDecimal;

import com.django.data.entity.ChordSignatureEntity;
import com.django.data.entity.IntervalEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class ChordChangeConsonanceRatingInstanceEntity {
	
	private @Id @GeneratedValue Long id;
	private ChordSignatureEntity referenceChordSignature;
	private ChordSignatureEntity targetChordSignature;
	private IntervalEntity intervalBetweenRoots;
	@Min(0)
	@Max(100)
	private BigDecimal rating;
	
	protected ChordChangeConsonanceRatingInstanceEntity() {}
	
	public ChordChangeConsonanceRatingInstanceEntity(ChordSignatureEntity referenceChordSignature,
			ChordSignatureEntity targetChordSignature, IntervalEntity intervalBetweenRoots, BigDecimal rating) {
		super();
		this.id = null;
		this.referenceChordSignature = referenceChordSignature;
		this.targetChordSignature = targetChordSignature;
		this.intervalBetweenRoots = intervalBetweenRoots;
		this.rating = rating;
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

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}
}
