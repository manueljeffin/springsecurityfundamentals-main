package com.bharath.springcloud.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bharath.springcloud.dto.Coupon;
import com.bharath.springcloud.model.Product;
import com.bharath.springcloud.repos.ProductRepo;

import java.util.Optional;

@RestController
@RequestMapping("/productapi")
public class ProductRestController {

    @Autowired
    private ProductRepo repo;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${couponService.url}")
    private String couponServiceURL;

    @RequestMapping(value = "/products",
                    method = RequestMethod.POST)
    public Product create(@RequestBody Product product) {

        //Prep for Basic auth
        String plainCreds = "doug@bailey.com:doug";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        //Just a GET REST call to coupons service
        HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<Coupon> response = restTemplate.exchange(couponServiceURL + product.getCouponCode(),
																HttpMethod.GET,
																request,
																Coupon.class);
        product.setPrice(product.getPrice().subtract(response.getBody().getDiscount()));

        return repo.save(product);

    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") Long productId) {
        //Beauty of SpringJPA
        Optional<Product> product = repo.findById(productId);
        return product.orElse(null);

    }

}
