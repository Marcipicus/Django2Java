package com.django.data.entity.rating.instance;

import java.math.BigDecimal;

import com.django.data.entity.ScaleSignatureEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class ScaleConsonanceIsolatedInstanceEntity {
	
	private @Id @GeneratedValue Long id;
	private ScaleSignatureEntity scaleSignature;
	@Min(0)
	@Max(100)
	private BigDecimal rating;

	protected ScaleConsonanceIsolatedInstanceEntity() {}

	public ScaleConsonanceIsolatedInstanceEntity(ScaleSignatureEntity scaleSignature, BigDecimal rating) {
		super();
		this.id = null;
		this.scaleSignature = scaleSignature;
		this.rating = rating;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ScaleSignatureEntity getScaleSignature() {
		return scaleSignature;
	}

	public void setScaleSignature(ScaleSignatureEntity scaleSignature) {
		this.scaleSignature = scaleSignature;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

}
