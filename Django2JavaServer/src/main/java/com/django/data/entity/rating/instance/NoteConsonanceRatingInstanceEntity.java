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
public class NoteConsonanceRatingInstanceEntity {

	private @Id @GeneratedValue Long id;
	private ChordSignatureEntity referenceChordSignature;
	private IntervalEntity interval;
	@Min(0)
	@Max(100)
	private BigDecimal rating;
	
	protected NoteConsonanceRatingInstanceEntity() {}
	
	public NoteConsonanceRatingInstanceEntity(ChordSignatureEntity referenceChordSignature,
			IntervalEntity interval, BigDecimal rating) {
		super();
		this.id = null;
		this.referenceChordSignature = referenceChordSignature;
		this.interval = interval;
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

	public IntervalEntity getInterval() {
		return interval;
	}

	public void setInterval(IntervalEntity interval) {
		this.interval = interval;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

}
