package com.django.data.repository.rating;

import org.springframework.data.jpa.repository.JpaRepository;

import com.django.data.entity.rating.ChordChangeConsonanceAveragedRatingEntity;

public interface ChordChangeConsonanceAveragedRatingRepository
		extends JpaRepository<ChordChangeConsonanceAveragedRatingEntity, Long> {

}
