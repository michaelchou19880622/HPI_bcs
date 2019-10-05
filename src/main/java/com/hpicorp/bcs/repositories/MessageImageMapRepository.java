package com.hpicorp.bcs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.MessageImageMap;

@Repository
public interface MessageImageMapRepository extends PagingAndSortingRepository<MessageImageMap, Integer> {

	@Modifying
	@Transactional
	@Query(value = "select * from message_imagemap "
				 + "where type = :type "
				 + "order by id desc ", nativeQuery = true)
	public List<MessageImageMap> getMessageImageMapByType(@Param("type") String type);

}
