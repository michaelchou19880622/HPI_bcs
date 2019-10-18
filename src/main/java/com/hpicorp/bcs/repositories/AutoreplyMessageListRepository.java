package com.hpicorp.bcs.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.hpicorp.bcs.entities.AutoreplyMessageList;

@Repository
public interface AutoreplyMessageListRepository extends JpaRepository<AutoreplyMessageList, Long> {

    @Query(value = "select A from AutoreplyMessageList A where autoreplyId = :autoreply_id order by orderNum ")
	public List<AutoreplyMessageList> getAutoreplyMessageListByAutoreplyID(@Param("autoreply_id") Long autoreplyIid);
	
	@Query(value = "select distinct L from AutoreplyMessageList L "
				 + "inner join Autoreply a on a.id = L.autoreplyId "
				 + "		and a.status = 'ACTIVE' "
				 + "		and a.userStatus = 'ALL' "
				 + "		and (a.period = 'FOREVER' or (a.period = 'DAY' and a.datetimeBegin <= :date and a.datetimeEnd >= :date )) "
				 + "inner join AutoreplyDetail d on d.autoreplyId = L.autoreplyId "
				 + "		and d.keyword = :keyword order by L.orderNum")
	public Page<AutoreplyMessageList> getListByKeyWord(@Param("keyword") String keyword, @Param("date") Date date, Pageable pageable);

	@Query(value = "select distinct L from AutoreplyMessageList L "
				 + "inner join Autoreply a on a.id = L.autoreplyId "
				 + "		and a.status = 'ACTIVE' "
				 + "		and a.userStatus in :status "
				 + "		and (a.period = 'FOREVER' or (a.period = 'DAY' and a.datetimeBegin <= :date and a.datetimeEnd >= :date )) "
				 + "inner join AutoreplyDetail d on d.autoreplyId = L.autoreplyId "
				 + "		and d.keyword = :keyword order by L.orderNum ")
	public Page<AutoreplyMessageList> getListByKeyWordAndDateAndStatus(@Param("keyword") String keyword, @Param("date") Date date, @Param("status") List<String> status, Pageable pageable);
	
	@Modifying
    @Transactional
    @Query(value = "delete from AutoreplyMessageList A where A.autoreplyId = :autoreply_id ")
	public void deleteByAutoreplyID(@Param("autoreply_id") Long autoreplyIid);
	
}
