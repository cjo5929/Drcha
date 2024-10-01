package com.ssafy.drcha.chat.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.ssafy.drcha.chat.entity.ChatMessage;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {
	private final MongoTemplate mongoTemplate;

	public ChatMessageRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<ChatMessage> findByRoomIdWithPaging(String chatRoomId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

		Query query = new Query().with(pageable);
		query.addCriteria(Criteria.where("chatRoomId").is(chatRoomId));

		List<ChatMessage> filteredChatMessage = mongoTemplate.find(query, ChatMessage.class);

		return PageableExecutionUtils.getPage(
			filteredChatMessage,
			pageable,
			() -> mongoTemplate.count(Query.query(Criteria.where("chatRoomId").is(chatRoomId)), ChatMessage.class)
		);
	}
}
