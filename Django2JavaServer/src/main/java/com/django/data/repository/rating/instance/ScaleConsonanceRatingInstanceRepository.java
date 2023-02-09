package com.django.data.repository.rating.instance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.django.data.entity.rating.instance.ScaleConsonanceRatingInstanceEntity;

public interface ScaleConsonanceRatingInstanceRepository
		extends JpaRepository<ScaleConsonanceRatingInstanceEntity, Long> {

}
