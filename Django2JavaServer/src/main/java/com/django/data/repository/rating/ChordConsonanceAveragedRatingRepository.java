package com.django.data.repository.rating;

import org.springframework.data.jpa.repository.JpaRepository;

import com.django.data.entity.rating.ChordConsonanceAveragedRatingEntity;

public interface ChordConsonanceAveragedRatingRepository
		extends JpaRepository<ChordConsonanceAveragedRatingEntity, Long> {

}
