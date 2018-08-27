package ar.com.snd.mongocleaner.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MongoConfig {

    @Value("${mongodb.host}")
    private String host;
    @Value("${mongodb.port}")
    private String port;
    @Value("${mongodb.db_name}")
    private String dbName;
    @Value("${mongodb.user_name}")
    private String username;
    @Value("${mongodb.password}")
    private String password;
    @Value("${mongodb.replicas}")
    private String replicas;
    @Value("${collections}")
    private String collections;



    public String getDbName() {
        return dbName;
    }

    public String getHost() {
        return host;
    }

    public List<String> getCollections() {
        return Arrays.asList(this.collections.split(","));
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getReplicas() {
        return replicas;
    }
}
