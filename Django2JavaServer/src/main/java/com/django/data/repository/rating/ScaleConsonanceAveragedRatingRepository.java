package com.django.data.repository.rating;

import org.springframework.data.jpa.repository.JpaRepository;

import com.django.data.entity.rating.ScaleConsonanceAveragedRatingEntity;

public interface ScaleConsonanceAveragedRatingRepository
		extends JpaRepository<ScaleConsonanceAveragedRatingEntity, Long> {

}
