package com.hpicorp.bcs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpicorp.bcs.entities.RichMenu;

public interface RichMenuRepository extends JpaRepository<RichMenu, Long> {
	
	public Optional<RichMenu> findByGroupId(Long string);
	
	public List<RichMenu> findByChatBarText(String chatBarText);
	
	@Query(value = "select * from richmenu_new where level = '1' and start_date > now()", nativeQuery = true)
	public List<RichMenu> findRichMenuOnSchedule();
	
	@Query(value = "select * from richmenu_new where group_id = :groupId", nativeQuery = true)
	public List<RichMenu> findOldRichMenu(@Param("groupId") Long groupId);
	
	@Query(value = "select * from richmenu_new where name like :name ", nativeQuery = true)
	public List<RichMenu> findOldRichMenuList(@Param("name") String name);
	
	@Query(value = "select * from richmenu_new where level = '1' ", nativeQuery = true)
	public List<RichMenu> findByLevel();

}
