package com.mshop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mshop.models.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long>{
	@Query(value = "select * from rates where product_id = ?", nativeQuery = true)
	List<Rate> findByProduct(Long id);
	
	@Query(value = "select AVG(star) from rates where product_id = ?", nativeQuery = true)
	Double getAvgByProduct(Long id);
}
