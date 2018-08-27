package ar.com.snd.mongocleaner.services;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class Cleaner {

    @Value("${init_hour}")
    private String initHour;
    @Value("${documents_expiration_time}")
    private String documentsExpiration;
    @Autowired
    MongoTemplate mongoTemplate;

    public void clean(MongoCollection<Document> collection){

    }

    public String getInitHour() {
        return initHour;
    }

    public String getDocumentsExpiration() {
        return documentsExpiration;
    }
}
