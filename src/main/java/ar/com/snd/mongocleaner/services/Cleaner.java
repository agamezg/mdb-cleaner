package ar.com.snd.mongocleaner.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Cleaner {

    @Value("${init_hour}")
    private String initHour;
    @Value("${documents_expiration_time}")
    private String documentsExpiration;

    public String getInitHour() {
        return initHour;
    }

    public String getDocumentsExpiration() {
        return documentsExpiration;
    }
}
