package com.django.data.repository.rating;

import org.springframework.data.jpa.repository.JpaRepository;

import com.django.data.entity.rating.NoteConsonanceAveragedRatingEntity;

public interface NoteConsonanceAveragedRatingRepository
		extends JpaRepository<NoteConsonanceAveragedRatingEntity, Long> {

}
