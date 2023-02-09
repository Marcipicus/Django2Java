package com.django.data.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class ChordSignatureEntity {
	
	private @Id @GeneratedValue Long id;
	private String name;
	private String displayText;
	private List<IntervalEntity> intervals;
	
	protected ChordSignatureEntity() {}
	
	public ChordSignatureEntity(String name, String displayText, List<IntervalEntity> intervals) {
		super();
		this.id = null;
		this.name = name;
		this.displayText = displayText;
		this.intervals = intervals;
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


	public String getDisplayText() {
		return displayText;
	}


	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}


	public List<IntervalEntity> getIntervals() {
		return intervals;
	}


	public void setIntervals(List<IntervalEntity> intervals) {
		this.intervals = intervals;
	}
	
	
}
