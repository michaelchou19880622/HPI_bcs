package com.hpicorp.bcs.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.SendMessage;
import com.hpicorp.bcs.repositories.LineUserGroupRepository;
import com.hpicorp.bcs.repositories.SendMessageRepository;
import com.hpicorp.bcs.repositories.SendMessageUsersRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendMessageService {

	@Autowired
	private SendMessageRepository sendMessageRepository;

	@Autowired
	private SendMessageUsersRepository sendMessageUsersRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	LineUserGroupRepository lineUserGroupRepository;

	public Page<SendMessage> getAllSendMessage(Pageable pageable) {
		return sendMessageRepository.findAll(pageable);
	}

	public Page<SendMessage> getAllSendMessageByMode(Pageable pageable, List<String> keys) {
		return sendMessageRepository.getSendMessageByMode(pageable, keys);
	}

	public Page<SendMessage> getAllSendMessageByStatus(Pageable pageable, int status) {
		return sendMessageRepository.getSendMessageByStatus(pageable, status);
	}

	public void insert(SendMessage sendMessage) {
		sendMessageRepository.save(sendMessage);
	}

	public void save(SendMessage sendMessage) {
		sendMessageRepository.save(sendMessage);
	}

	public Optional<SendMessage> findById(Long id) {
		return sendMessageRepository.findById(id);
	}

	public void deleteById(Long id) {
		sendMessageUsersRepository.deleteBySendID(id);
		sendMessageRepository.deleteById(id);
	}

	@SuppressWarnings("unchecked")
	public List<BigInteger> getLineUserGroupListById(Long lineUserGroupId) {
		List<BigInteger> result = new ArrayList<>();

		String getUserSql = lineUserGroupRepository.findgetUsersById(lineUserGroupId);
		getUserSql = getUserSql.replaceAll("select line_uid from lineuser", "select id from lineuser");
		try {
			Query query = em.createNativeQuery(getUserSql);
			result = query.getResultList();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return result;
	}

	@Transactional
	public int insSendMessageUser(String sqlString) {
		int result = -1;
		try {
			Query query = em.createNativeQuery(sqlString);
			result = query.executeUpdate();
		} catch (Exception e) {
			log.error("Exception : ", e);
		}
		return result;
	}

}
