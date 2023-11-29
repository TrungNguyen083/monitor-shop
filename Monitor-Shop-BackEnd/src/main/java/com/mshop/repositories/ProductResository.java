package com.mshop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mshop.models.Product;

@Repository
public interface ProductResository extends JpaRepository<Product, Long>{
	@Query(value = "SELECT * FROM products WHERE category_id = ? and status = 1", nativeQuery = true)
	List<Product> findAllProductByCategoryId(Long id);
	
	@Query(value = "select * from products where status = 1", nativeQuery = true)
	List<Product> findAllStatusTrue();
	
	@Query(value = "select * from products where status = 1 and product_id = ?", nativeQuery = true)
	Product findByIdAndStatusTrue(Long id);

	@Query(value = "SELECT p.* FROM products p " +
			"JOIN (SELECT product_id, COUNT(*) AS soluong " +
			"FROM `monitor-shop`.order_details " +
			"GROUP BY product_id " +
			"HAVING soluong = (SELECT MAX(product_count) FROM (SELECT COUNT(*) AS product_count FROM `monitor-shop`.order_details GROUP BY product_id) AS counts)) " +
			"AS max_counts ON p.product_id = max_counts.product_id", nativeQuery = true)
	List<Product> getBestSeller();

}
