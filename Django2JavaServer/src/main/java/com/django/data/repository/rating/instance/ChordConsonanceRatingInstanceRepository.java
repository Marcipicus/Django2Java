package com.django.data.repository.rating.instance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.django.data.entity.rating.instance.ChordConsonanceRatingInstanceEntity;

public interface ChordConsonanceRatingInstanceRepository
		extends JpaRepository<ChordConsonanceRatingInstanceEntity, Long> {

}
