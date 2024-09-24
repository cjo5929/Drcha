package com.ssafy.drcha.iou.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.drcha.iou.entity.Iou;

public interface IouRepository extends JpaRepository<Iou, Long> {
}
