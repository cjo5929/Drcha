package com.ssafy.drcha.iou.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.drcha.iou.entity.Iou;

@Repository
public interface IouRepository extends JpaRepository<Iou, Long> {

	@Query("SELECT i FROM Iou i WHERE i.chatRoomId = :chatRoomId ORDER BY i.contractStartDate DESC")
	Optional<Iou> findLatestByChatRoomId(@Param("chatRoomId") Long chatRoomId);

}
