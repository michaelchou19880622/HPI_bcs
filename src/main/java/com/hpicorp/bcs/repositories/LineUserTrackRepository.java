package com.hpicorp.bcs.repositories;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpicorp.bcs.entities.LineUserTrack;

public interface LineUserTrackRepository extends CrudRepository<LineUserTrack, Long> {

	public List<LineUserTrack> findBySource(String source);

	public LineUserTrack findByUserIdAndSource(Long userId, String source);

	public Integer countBySourceAndCreateTimeBetween(String source, Date since, Date until);

	@Query(value = "select "
				 + "		T.source as source, "
				 + "		count(T.id) as count , "
				 + "		date_format(T.creation_time, '%Y/%m/%d') as date "
				 + "from lineuser_track T "
				 + "where T.source in :state "
				 + "and date_format(T.creation_time, '%Y/%m/%d') between date_format(:since, '%Y/%m/%d') and date_format(:untils, '%Y/%m/%d')"
				 + "group by T.source, date_format(T.creation_time, '%Y/%m/%d')", nativeQuery = true)
	public List<Map<String, Object>> countBySourceAndCreateTimeBetween(@Param("since") Date since, @Param("untils") Date until, @Param("state") List<String> state);

	@Query(value = "select * from lineuser_track "
				 + "where user_id = :userId "
				 + "and source = 'BINDED' ", nativeQuery = true)
	public List<LineUserTrack> findByUserId(@Param("userId") Long userId);

}
