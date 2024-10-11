package com.ssafy.drcha.iou.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.member.entity.Member;

@Repository
public interface IouRepository extends JpaRepository<Iou, Long> {

	@Query("SELECT i FROM Iou i WHERE i.chatRoom.chatRoomId = :chatRoomId ORDER BY i.contractStartDate DESC")
	List<Iou> findByChatRoomIdOrderByContractStartDateDesc(@Param("chatRoomId") Long chatRoomId, Pageable pageable);

	default Optional<Iou> findLatestByChatRoomId(Long chatRoomId) {
		return findByChatRoomIdOrderByContractStartDateDesc(chatRoomId, PageRequest.of(0, 1))
			.stream()
			.findFirst();
	}

	List<Iou> findByChatRoom_ChatRoomIdOrderByContractStartDateDesc(Long chatRoomId);

	List<Iou> findByCreditor(Member creditor);

	List<Iou> findByDebtor(Member debtor);

	List<Iou> findAllByContractStatus(ContractStatus contractStatus);

	Optional<Iou> findByChatRoomAndContractStatus(ChatRoom chatRoom, ContractStatus contractStatus);
}
