package com.hpicorp.bcs.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpicorp.bcs.entities.LinkAddressTrack;

@Repository
public interface LinkAddressTrackRepository extends JpaRepository<LinkAddressTrack, Long> {

	@Query(value = "select linked from lineuser_link where user_id= :userid ", nativeQuery = true)
	public String getLinkedByUserID(@Param("userid") long userid);

	// 2019.10.6 已確認該 SQL 會被使用
	@Query(value = "select " 
				 + "		count(distinct lineuser_id) as cnt, " 
				 + "		ll.title as title, " 
				 + "		ll.url as url, " 
				 + "		ll.id as linkAddressListId, " 
				 + "		count(linkaddresslist_id) as tot " 
				 + "from linkaddress_track join linkaddresslist ll on linkaddresslist_id = ll.id " 
				 + "group by linkaddresslist_id ", 
				 countQuery = "select count(linkaddresslist_id) from linkaddress_track group by linkaddresslist_id",
				 nativeQuery = true)
	public Page<Object[]> getLinkAddressTrackByPage(Pageable pageable);
	
	// 2019.10.6 已確認該 SQL 會被使用
	/**
	 * 撈取該追蹤連結所有點擊數據(分頁)
	 */
	@Query(value = "select "
				 + "		t.DateOnly as date, "
				 + "		sum(cnt) as tot, "
				 + "		count(lineuser_id) as cnt "
				 + "from  "
				 + "(select DATE(create_time) DateOnly, lineuser_id, count(id) as cnt from linkaddress_track where linkaddresslist_id = :id group by DateOnly, lineuser_id) t "
				 + "group by t.DateOnly "
				 + "order by t.DateOnly desc ",
		   countQuery = "select count(distinct DATE(create_time)) from linkaddress_track where linkaddresslist_id = :id",
		   nativeQuery = true)
	public Page<Object[]> getLinkAddressTrackDetailByPage(@Param("id") Long id, Pageable pageable);
	
	/**
	 * 撈取該追蹤連結所有點擊數據(不分頁)
	 * @param linkaddress_list_id
	 */
	@Query(value = "select t.DateOnly as date, "
			 + "		   sum(cnt) as tot, "
			 + "		   count(lineuser_id) as cnt "
			 + "      from (select DATE(create_time) DateOnly, "
			 + "                   lineuser_id, "
			 + "                   count(id) as cnt"
			 + "              from linkaddress_track "
			 + "             where linkaddresslist_id = :id "
			 + "             group by DateOnly, lineuser_id ) t "
			 + "      group by t.DateOnly "
			 + "      order by t.DateOnly desc ",
	   nativeQuery = true)
	public List<Object[]> getLinkAddressTrackDetailByList(@Param("id") Long id);
 
	@Query (value="select l.title," + 
    		"       l.url," + 
    		"	   linkaddresslist_id," + 
    		"       sum(cnt) as tot," + 
    		"       count(lineuser_id) as cnt" + 
    		"  from (select linkaddresslist_id," + 
    		"			   lineuser_id," + 
    		"               count(id) as cnt" + 
    		"		  from linkaddress_track" + 
    		"		 group by linkaddresslist_id," + 
    		"				  lineuser_id) t ," + 
    		"	    linkaddresslist l," + 
    		"        linkaddress ld" + 
    		"  where ld.id = l.link_id" + 
    		"    and t.linkaddresslist_id = l.id" + 
    		"    and l.title like %:name% " + 
    		"  group by linkaddresslist_id", nativeQuery = true)
	public List<Object[]>  getLinkAddressTrackByName(@Param("name") String name);

	@Query(value = "select line_uid " + 
			"         from lineuser " + 
			"        where id in ( select distinct lineuser_id " + 
			"                        from linkaddress_track " + 
			"                       where linkaddresslist_id = :id" + 
			"                       group by lineuser_id )", nativeQuery = true)
	public List<String> getTrackDetailUidBylinkaddresslistId(@Param("id") Long id);

}
