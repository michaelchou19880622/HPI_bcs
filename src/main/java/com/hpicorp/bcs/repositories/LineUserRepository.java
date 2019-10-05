package com.hpicorp.bcs.repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hpicorp.bcs.entities.LineUser;

public interface LineUserRepository extends JpaRepository<LineUser, Long>{
	
	public boolean existsByLineUid(String lineUid);
	
	public List<LineUser> findByIdIn(List<Long> ids);
	
	public Optional<LineUser> findByLineUid(String lineUid);

	public Optional<LineUser> findByPrivateCode(String privateCode);
	
	public Optional<LineUser> findBySPrivateCode(String sPrivateCode);
	
	@Query(value = "select distinct L.id from LineUser L where L.lineUid = :uid ")
	public List<Long> findIdByUid(@Param("uid") String uid, Pageable pageable);
	
	@Query(value = "select * from lineuser where line_uid = :lineUid ", nativeQuery = true)
	public List<LineUser> findByLineUidList(@Param("lineUid") String lineUid);
	
	@Query(value = "select distinct L.id from LineUser L where L.lineUid = :uid ")
	public Long getIdByUid(@Param("uid") String uid);
    
    @Query(value = "select distinct L.lineUid from LineUser L where L.id = :id ")
    public String findUidById(@Param("id") Long id);
	
	@Query(value = "select distinct L.lineUid from LineUser L where L.id in :ids ")
	public List<String> findUidByIdIn(@Param("ids") List<Long> ids);
	
	@Query(value="select L.id as tarKey, L.lineUid as tarValue from LineUser L where L.id in :ids")
	public List<Map<String, Object>> findIdAndUidByIds(@Param("ids") List<Long> ids);

	@Modifying
    @Transactional
	@Query(value = "select * from lineuser as l "
				 + "where l.line_uid in "
				 + "		(select distinct u.uid from uploaduids as u) ", nativeQuery = true)
	public List<LineUser> compareFileAndLineUser();
	
	@Modifying
    @Transactional
	@Query(value = "select l.line_uid from lineuser as l "
				 + "where l.line_uid in "
				 + "		(select distinct u.uid from uploaduids as u) :condition ", nativeQuery = true)
	public List<String> compareFileAndLineUserWithCondition(@Param("condition") String condition);	

    @Query (value="select L.lineUid from LineUser L")
	public List<String> getAllUidList();    
    
    @Query(value = "select line_uid from lineuser "
    				 + "where line_uid in "
    				 + "		(select distinct uid from upload_uid where filename = :filename and original_filename = :originalFilename) ", nativeQuery = true)
    public List<String> compareFileAndLineUser(@Param("filename") String filename, @Param("originalFilename") String originalFilename);
	
	@Query(value = "select * from lineuser l "
				 + "where gid is not null "
				 + "and length(l.line_uid) = 33", nativeQuery = true)
	public List<LineUser> findGid();
	
	@Query(value = "select * from lineuser l "
				 + "where line_uid not in "
				 + "		(select line_uid from activity_share) and l.gid is not null limit 50000", nativeQuery = true)
	public List<LineUser> findBind();
	
	@Query(value = "select * from lineuser "
				 + "where line_uid in "
				 + "		(select line_uid from lineuser where gid is null group by line_uid having count(*) > 1 ) ", nativeQuery = true)
	public List<LineUser> test();
	
}
