package com.mshop.restapi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mshop.entity.ProductRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mshop.entity.Statistical;
import com.mshop.repository.OrderRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/statistical/api")
public class StatisticalRestApi {
	
	@Autowired
	OrderRepository repo;	
	
	@GetMapping("/{year}")
	public ResponseEntity<List<Statistical>> getStatisticalYear(@PathVariable("year") int year)  {
		List<Object[]> list = repo.getStatisticalMonthYear(year);
		List<Statistical> listSta = new ArrayList<>();
		List<Statistical> listReal = new ArrayList<>();
		for (Object[] objects : list) {
			Statistical sta = new Statistical((int) objects[1], null, (Double) objects[0], BigInteger.valueOf(0));
			listSta.add(sta);
		}
		for(int i = 1; i < 13; i++) {
			Statistical sta = new Statistical(i, null, 0.0, BigInteger.valueOf(0));
			for (Statistical statistical : listSta) {
				if (statistical.getMonth() == i) {
					listReal.remove(sta);
					listReal.add(statistical);
					break;
				} else {
					listReal.remove(sta);
					listReal.add(sta);
				}
			}
		}
		return ResponseEntity.ok(listReal);
	}
	
	@GetMapping("/month")
	public ResponseEntity<List<Statistical>> getStatisticalYears() {
		List<Object[]> list = repo.getStatisticalMonth();
		List<Statistical> listSta = new ArrayList<>();
		for (Object[] objects : list) {
			Statistical sta = new Statistical(0, (Date) objects[2], (Double) objects[0], (BigInteger) objects[3]);
			listSta.add(sta);
		}
		return ResponseEntity.ok(listSta);
	}
	
	@GetMapping("/date")
	public ResponseEntity<List<Statistical>> getStatisticalAllDate() {
		List<Object[]> list = repo.getStatisticalDate();
		List<Statistical> listSta = new ArrayList<>();
		for (Object[] objects : list) {
			Statistical sta = new Statistical(0, (Date) objects[1], (Double) objects[2], (BigInteger) objects[3]);
			listSta.add(sta);
		}
		return ResponseEntity.ok(listSta);
	}
	
	@GetMapping("/years")
	public ResponseEntity<List<Statistical>> getStatisticalAllYear() {
		List<Object[]> list = repo.getStatisticalYear();
		List<Statistical> listSta = new ArrayList<>();
		for (Object[] objects : list) {
			Statistical sta = new Statistical(0, (Date) objects[1], (Double) objects[2], (BigInteger) objects[3]);
			listSta.add(sta);
		}
		return ResponseEntity.ok(listSta);
	}
	

	@GetMapping("/year")
	public ResponseEntity<List<Integer>> getYears() {
		return ResponseEntity.ok(repo.getYears());
	}

	@GetMapping("/product-rating")
	public ResponseEntity<List<ProductRating>> getProductRating() {
		List<Object[]> listProductRating = repo.getProductRating();
		List<ProductRating> list = listProductRating.stream().map(objects ->
				new ProductRating((BigInteger) objects[0], (String) objects[1], (BigInteger) objects[2])).toList();
		return ResponseEntity.ok(list);
	}
}
