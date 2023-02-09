package com.django.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class IntervalEntity {
	
	private @Id @GeneratedValue Long id;
	private String name;
	private Short numSemitones;
	private IntervalEntity inverseInterval;
	
	protected IntervalEntity() {}
	
	public IntervalEntity(String name, Short numSemitones, IntervalEntity inverseInterval) {
		super();
		this.id = null;
		this.name = name;
		this.numSemitones = numSemitones;
		this.inverseInterval = inverseInterval;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Short getNumSemitones() {
		return numSemitones;
	}


	public void setNumSemitones(Short numSemitones) {
		this.numSemitones = numSemitones;
	}


	public IntervalEntity getInverseInterval() {
		return inverseInterval;
	}


	public void setInverseInterval(IntervalEntity inverseInterval) {
		this.inverseInterval = inverseInterval;
	}
}
