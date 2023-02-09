package com.django.data.entity.rating.instance;

import java.math.BigDecimal;

import com.django.data.entity.ChordSignatureEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class ChordConsonanceRatingInstanceEntity {
	
	private @Id @GeneratedValue Long id;
	private ChordSignatureEntity chordSignature;
	@Min(0)
	@Max(100)
	private BigDecimal rating;
	
	protected ChordConsonanceRatingInstanceEntity() {}

	public ChordConsonanceRatingInstanceEntity(ChordSignatureEntity chordSignature, BigDecimal rating) {
		super();
		this.id = null;
		this.chordSignature = chordSignature;
		this.rating = rating;
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

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}
	
}
