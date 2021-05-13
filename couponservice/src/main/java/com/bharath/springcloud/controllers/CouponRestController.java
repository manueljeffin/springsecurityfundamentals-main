package com.bharath.springcloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bharath.springcloud.model.Coupon;
import com.bharath.springcloud.repos.CouponRepo;

@RestController
@RequestMapping("/couponapi")
@CrossOrigin
public class CouponRestController {

	@Autowired
	CouponRepo repo;

	@PostMapping("/coupons")
	public Coupon create(@RequestBody Coupon coupon) {

		return repo.save(coupon);

	}

	@GetMapping("/coupons/{code}")
	//PostAuthorize comes into picture after method is executed, but before results are returned back
	@PostAuthorize("returnObject.discount<60") //basically saying only return coupons with discount less than 60
	public Coupon getCoupon(@PathVariable("code") String code) {
		//Beauty of SpringJPA
		return repo.findByCode(code);

	}
}
