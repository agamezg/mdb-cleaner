package ar.com.snd.mongocleaner;

import ar.com.snd.mongocleaner.configuration.MongoConfig;
import ar.com.snd.mongocleaner.services.Cleaner;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoCleanerApplicationTests {

	@Autowired
	Cleaner cleaner;
	@Autowired
	MongoConfig mongoConfig;
	@Autowired
	MongoTemplate mongoTemplate;

	private MongoCollection<Document> mensajes;
	private MongoCollection<Document> llamados;

	@Test
	public void contextLoads() {
	}

	@Test
	public void loadConfigurations(){
		String mongoHost = mongoConfig.getHost();
		String mongoPort = mongoConfig.getPort();
		String mongoDBName = mongoConfig.getDbName();
		String mongoUserName = mongoConfig.getUsername();
		String mongoPassword = mongoConfig.getPassword();
		String mongoReplicas = mongoConfig.getReplicas();
		List<String> collections = mongoConfig.getCollections();
		String documentsExpiration = cleaner.getDocumentsExpiration();
		String initHour = cleaner.getInitHour();

		assertTrue(mongoHost.equalsIgnoreCase("localhost"));
		assertTrue(mongoPort.equalsIgnoreCase("27017"));
		assertTrue(mongoDBName.equalsIgnoreCase("cmp"));
		assertTrue(mongoUserName.equalsIgnoreCase("mongo_cleaner_username"));
		assertTrue(mongoPassword.equalsIgnoreCase("mongo_cleaner_password"));
		assertTrue(mongoReplicas.equalsIgnoreCase("127.0.0.2,127.0.0.3"));
		assertTrue(collections.containsAll(Arrays.asList("Mensajes","Llamados")));
		assertTrue(documentsExpiration.equalsIgnoreCase("3"));
		assertTrue(initHour.equalsIgnoreCase("23"));
	}

	@Test
	public void fakeMongoInjection(){
		assertNotNull(mongoTemplate);
		assertTrue(mongoTemplate.getDb().getName().equalsIgnoreCase("cmp"));
	}

	@Test
	public void existCollections(){
		fillLlamados();
		fillMensajes();
		assertEquals(this.mensajes.count(), 2);
		assertEquals(this.llamados.count(), 1);
	}

	@Test
	public void cleanSomething(){

	}

	private void fillLlamados() {
		this.llamados = this.mongoTemplate.createCollection("Llamados");
		this.llamados.insertOne(new Document());
	}

	private void fillMensajes() {
		this.mensajes = this.mongoTemplate.createCollection("Mensajes");
		this.mensajes.insertOne(new Document());
		this.mensajes.insertOne(new Document());
	}


}
