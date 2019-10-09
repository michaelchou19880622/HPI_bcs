package com.hpicorp.bcs.repositories;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpicorp.bcs.entities.UserClick;

public interface UserClickRepository extends CrudRepository<UserClick, Long> {
	
	@Query(value = "select "
				 + "		A.id as id, "
				 + "		A.keyword as keyword, "
				 + "		A.userStatus as status, "
				 + "		A.period as period, "
				 + "		A.creationTime as createtime, "
				 + "		A.modifyUser as user, "
				 + "		count(distinct U) as count "
				 + "	from Autoreply A "
				 + "inner join AutoreplyDetail D on D.autoreplyId = A.id and D.keyword like %:keyword% "
				 + "inner join UserClick U on A.id = U.mappingId and U.type = :type "
				 + "where (A.period = 'FOREVER' or (A.period ='DAY' and A.datetimeBegin >= :period)) "
				 + "group by A.id, A.keyword, A.userStatus, A.period, A.creationTime, A.modifyUser")
	public Page<Map<String, Object>> getByKeywordAndPeriod(@Param("keyword") String keyword, @Param("period") Date period, @Param("type") String type, Pageable pageable);

	@Query(value = "select "
				 + "		A.id as id, "
				 + "		A.keyword as keyword, "
				 + "		A.userStatus as status, "
				 + "		A.period as period, "
				 + "		A.creationTime as createtime, "
				 + "		A.modifyUser as user, "
				 + "		count(distinct U) as count from Autoreply A "
				 + "inner join UserClick U on A.id = U.mappingId and U.type = :type "
				 + "where (A.period = 'FOREVER' or (A.period ='DAY' and A.datetimeBegin >= :period)) "
				 + "group by A.id, A.keyword, A.userStatus, A.period, A.creationTime, A.modifyUser")
	public Page<Map<String, Object>> getByPeriod(@Param("period") Date period, @Param("type") String type, Pageable pageable);

	@Query(value = "select U.id from UserClick U "
				 + "	where U.mappingId = :id "
				 + "and U.type = :type "
				 + "and U.createTime between :since and :untils "
				 + "group by date_format(U.createTime, '%Y/%m/%d'), U.id")
	public List<Long> getRowCountByMappingIdAndTypeAndBetweenDate(@Param("id") Long mappingId, @Param("type") String type, @Param("since") Date since, @Param("untils") Date untils);

	@Query(value = "select "
				 + "		date_format(c.create_time, '%Y/%m/%d') as date, "
				 + "		count(c.lineuser_uid) as count "
				 + "from user_click c "
				 + "where c.mapping_id = :id "
				 + "and c.type = :type "
				 + "and c.create_time between :since and :untils "
				 + "group by date "
				 + "order by date asc limit :page, :pageSize", nativeQuery = true)
	public List<Map<String, String>> getDateAndCountByMappingIdAndType(
			@Param("id") Long mappingId, @Param("type") String type, @Param("since") Date since, 
			@Param("untils") Date untils, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

	@Query(value = "select "
				 + "		date_format(c.create_time, '%Y/%m/%d') as date, "
				 + "		count(distinct c.lineuser_uid) as count "
				 + "from user_click c "
				 + "where c.mapping_id = :id "
				 + "and c.type = :type "
				 + "and c.create_time between :since and :untils "
				 + "group by date "
				 + "order by date asc limit :page, :pageSize", nativeQuery = true)
	public List<Map<String, String>> getDistinctCountAndDateByMappingIdAndType(
			@Param("id") Long mappingId, @Param("type") String type, @Param("since") Date since, 
			@Param("untils") Date untils, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

	@Query(value = "select distinct U.lineUserUid from UserClick U "
				 + "where U.type = :type "
				 + "and U.mappingId = :mappingId "
				 + "and U.createTime between :since "
				 + "and :untils")
	public List<String> getUidByTypeAndMappingIdAndCreateTimeBetweenSinceAndUntils(
			@Param("type") String type, @Param("mappingId") Long mappingId, @Param("since") Date since, @Param("untils") Date untils);

	
	// 2019.10.09 已確認該 SQL 會被使用
	@Query(value = "select date_format(c.create_time, '%Y/%m/%d') as date,  " + 
			"		       count(c.lineuser_uid) as count, " + 
			"		       count(DISTINCT c.lineuser_uid) as userCount " + 
			"         from user_click c  " + 
			"        where c.mapping_id = :id  " + 
			"          and c.type = :type " + 
			"          and c.create_time between :since and :untils " + 
			"        group by date " +
			"      order by date desc ",
		   countQuery = "select count(DISTINCT DATE(create_time)) from user_click where mapping_id = :id and type = :type and create_time between :since and :untils ",
		   nativeQuery = true)
	public Page<Object[]> getAutoreplyDetailByPage(@Param("id") Long id, @Param("type") String type, @Param("since") Date since, @Param("untils") Date untils, Pageable pageable);
	
}
