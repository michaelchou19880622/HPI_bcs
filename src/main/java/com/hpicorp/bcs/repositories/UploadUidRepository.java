package com.hpicorp.bcs.repositories;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.hpicorp.bcs.entities.UploadUid;

public interface UploadUidRepository extends JpaRepository<UploadUid, Long>{
	
	public boolean existsByFilename(String filename);
	
	public List<UploadUid> findByGroupId(Long groupId);
	
	public List<UploadUid> findByGroupIdAndFilename(Long groupId, String filename);
	
	public List<UploadUid> findByFilename(String filename);

	@Query(value = "select count(*) from upload_uid where length(uid) <> 33 or uid not like 'U%' ", nativeQuery = true)
	public Integer checkUidValid();
	
	@Modifying
    @Transactional
    @Query(value = "delete from upload_uid where filaname = :filename ;", nativeQuery = true)
	public void deleteByFilename(@Param("filename") String filename);
	
	@Modifying
    @Transactional
	@Query(value = "update upload_uid set group_id = :groupId where filename = :filename ", nativeQuery = true)
	public void updateByFilename(@Param("filename") String filename, @Param("groupId") Long groupId);
	
	@Query(value = "select distinct filename, original_filename from upload_uid where group_id = :groupId ;", nativeQuery = true)
	public List<Map<String, String>> getFilenameAndOriginalFilenameByGroupId(@Param("groupId") Long groupId);
	
}
