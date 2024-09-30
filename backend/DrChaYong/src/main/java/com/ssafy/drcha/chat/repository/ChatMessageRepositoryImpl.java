package com.ssafy.drcha.chat.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ssafy.drcha.chat.entity.ChatMessage;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {
	private final MongoTemplate mongoTemplate;

	public ChatMessageRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<ChatMessage> getChatMessagesAllByChatRoomAndTopId(String chatRoomId, Long top) {
		Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId));
		if (top != null) {
			query.addCriteria(Criteria.where("_id").lt(top));
		}
		query.with(Sort.by(Sort.Direction.DESC, "_id"));
		query.limit(20);
		return mongoTemplate.find(query, ChatMessage.class);
	}
}