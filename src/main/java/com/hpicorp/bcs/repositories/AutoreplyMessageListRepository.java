package com.hpicorp.bcs.repositories;

import java.util.List;
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
	
	@Modifying
    @Transactional
    @Query(value = "delete from AutoreplyMessageList A where A.autoreplyId = :autoreply_id ")
	public void deleteByAutoreplyID(@Param("autoreply_id") Long autoreplyIid);
	
}
