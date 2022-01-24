package com.decagon.fitnessoapp.Email;

import lombok.Data;

@Data
public class API {

    public static String API_KEY = System.getenv("API_KEY");
    public static String API_SECRET = System.getenv("API_SECRET");


}
