package com.django.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.django.data.entity.IntervalEntity;

public interface IntervalRepository extends JpaRepository<IntervalEntity, Long> {

}
