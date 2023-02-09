package com.django.data.repository.rating;

import org.springframework.data.jpa.repository.JpaRepository;

import com.django.data.entity.rating.ScaleConsonanceIsolatedAveragedRatingEntity;

public interface ScaleConsonanceIsolatedAveragedRatingRepository
		extends JpaRepository<ScaleConsonanceIsolatedAveragedRatingEntity, Long> {

}
