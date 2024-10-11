package com.ssafy.drcha.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.entity.ChatRoomMember;
import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.member.entity.Member;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

	@EntityGraph(attributePaths = {"chatRoom"})
	List<ChatRoomMember> findByMemberAndMemberRole(Member member, MemberRole role);

	Optional<ChatRoomMember> findByChatRoom_ChatRoomIdAndMember_IdNot(Long chatRoomId, Long memberId);

	Optional<ChatRoomMember> findByChatRoomAndMember(ChatRoom chatRoom, Member member);


}
