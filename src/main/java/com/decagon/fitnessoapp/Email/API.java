package com.decagon.fitnessoapp.Email;

import lombok.Data;

@Data
public class API {

    public static String API_KEY = System.getenv("API_KEY");
    public static String API_SECRET = System.getenv("API_SECRET");
    public static String API_KEY_PAYMENT = System.getenv("API_KEY_PAYMENT");
}
