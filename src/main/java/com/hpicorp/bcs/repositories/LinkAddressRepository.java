package com.hpicorp.bcs.repositories;

import org.springframework.stereotype.Repository;
import com.hpicorp.bcs.entities.LinkAddress;
import org.springframework.data.repository.PagingAndSortingRepository;

@Repository
public interface  LinkAddressRepository extends PagingAndSortingRepository<LinkAddress,Long>{	
	
}
