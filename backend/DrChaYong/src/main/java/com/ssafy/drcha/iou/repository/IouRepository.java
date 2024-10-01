package com.ssafy.drcha.iou.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.member.entity.Member;

@Repository
public interface IouRepository extends JpaRepository<Iou, Long> {

	@Query("SELECT i FROM Iou i WHERE i.chatRoom = :chatRoom ORDER BY i.contractStartDate DESC")
	Optional<Iou> findLatestByChatRoomId(ChatRoom chatRoom);

	List<Iou> findByChatRoom_ChatRoomIdOrderByContractStartDateDesc(Long chatRoomId);

	List<Iou> findByCreditor(Member creditor);

	List<Iou> findByDebtor(Member debtor);


}
