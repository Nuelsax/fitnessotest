package com.decagon.fitnessoapp.utils;


import com.decagon.fitnessoapp.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class Utility {
    private static PersonRepository personRepository;

    public String getSiteURL(HttpServletRequest request){
        String siteURL = request.getRequestURI().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
