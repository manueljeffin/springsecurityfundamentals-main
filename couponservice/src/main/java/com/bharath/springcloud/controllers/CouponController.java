package com.bharath.springcloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bharath.springcloud.model.Coupon;
import com.bharath.springcloud.repos.CouponRepo;

//Not a REST Controller, just a regular MVC controller
//For returned string thymleaf will append ".html" and will render that page
@Controller
public class CouponController {

	@Autowired
	private CouponRepo repo;

//	@GetMapping("/")
//	public String index() {
//		return "index";
//	}

	@GetMapping("/showCreateCoupon")
	@PreAuthorize("hasRole('ADMIN')") //Comes into picture before method execution
	public String showCreateCoupon() {
		return "createCoupon";
	}

	@PostMapping("/saveCoupon")
	public String save(Coupon coupon) {
		repo.save(coupon);
		return "createResponse";
	}

	@GetMapping("/showGetCoupon")
	public String showGetCoupon() {
		return "getCoupon";
	}

	@PostMapping("/getCoupon")
	public ModelAndView getCoupon(String code) {
		ModelAndView mav = new ModelAndView("couponDetails");
		mav.addObject(repo.findByCode(code));
		return mav;
	}

}
