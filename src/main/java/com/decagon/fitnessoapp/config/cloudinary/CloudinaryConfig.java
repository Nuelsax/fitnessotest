package com.decagon.fitnessoapp.config.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    public String createImage(String name) throws IOException {
        Cloudinary cloudinary;
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "fitnesso");
        config.put("api_key", "276229173867457");
        config.put("api_secret", "I0Ljecy2RdYulnkH8IODSJDUYIo");
        cloudinary = new Cloudinary(config);

        File file = new File(name);
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

        return uploadResult.get("url").toString();
    }

}
