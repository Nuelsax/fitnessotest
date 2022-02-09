package com.decagon.fitnessoapp.utils;


public class Api_Uri {

    public static String [] PUBLIC_URIs = {
            "/v2/api-docs", "/configuration/**", "/swagger*/**","/swagger-ui/**", "/webjars/**",
            "/address/**", "/person/login", "/person/register", "/person/admin/register", "/product/**", "/order/**", "/articles/blogposts/**"
            , "/product/search",  "/checkout/**", "/carts/**", "/transaction/**", "https://api.paystack.co/**", "api.paystack.co/**",
            "/test-carts/**"

            /*
            {
  "category": "Equipments",
  "description": "For running drills",
  "durationInDays": 0,
  "durationInHoursPerDay": 0,
  "image": "Machine",
  "price": 45700,
  "productName": "Threadmill",
  "productType": "PRODUCT",
  "quantity": 3
}
             */
    };
}
