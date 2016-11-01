package com.zhan.app.service;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.zhan.app.common.User;

@Service
public class DeviceService {
	@Resource
	protected MongoTemplate mongoTemplate;

	public void removeToken(String errorToken,String collectionName) {
		Query query = new Query();
		Criteria criteria = Criteria.where("token").is(errorToken);
		query.addCriteria(criteria);
		mongoTemplate.remove(query, User.class,collectionName);
	}
	public void removeToken(String errorToken) {
		Query query = new Query();
		Criteria criteria = Criteria.where("token").is(errorToken);
		query.addCriteria(criteria);
		mongoTemplate.remove(query, User.class);
	}
}
