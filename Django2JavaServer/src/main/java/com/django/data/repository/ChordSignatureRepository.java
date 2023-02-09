package com.django.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.django.data.entity.ChordSignatureEntity;

public interface ChordSignatureRepository extends JpaRepository<ChordSignatureEntity, Long> {

}
