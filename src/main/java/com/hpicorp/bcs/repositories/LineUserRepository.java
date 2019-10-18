package com.hpicorp.bcs.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.hpicorp.bcs.entities.LineUser;

public interface LineUserRepository extends JpaRepository<LineUser, Long>{
	
	public Optional<LineUser> findByLineUid(String lineUid);

	@Query(value = "select distinct L.id from LineUser L where L.lineUid = :uid ")
	public Long getIdByUid(@Param("uid") String uid);
    
	
}
