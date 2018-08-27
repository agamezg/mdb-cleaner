package ar.com.snd.mongocleaner;

import ar.com.snd.mongocleaner.configuration.MongoConfig;
import ar.com.snd.mongocleaner.services.Cleaner;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

	@Before
	public void setUp(){
		this.mensajes = this.mongoTemplate.createCollection("Mensajes");
		fillMensajes();
		this.llamados = this.mongoTemplate.createCollection("Llamados");
		fillLlamados();
	}

	@After
	public void tearDown(){
		this.mongoTemplate.dropCollection("Mensajes");
		this.mongoTemplate.dropCollection("Llamados");
	}

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
		assertTrue(collections.containsAll(Arrays.asList("Mensajes->fechaIngreso","Llamados->fechaLlamado")));
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

		assertEquals(this.mensajes.count(), 4);
		assertEquals(this.llamados.count(), 1);
	}

	@Test
	public void cleanSomething(){
		int mensajesAmountBefore = (int) mensajes.count();
		cleaner.clean(mensajes);
		int mensajesAmountAfter = (int) mensajes.count();
		assertEquals(mensajesAmountAfter, mensajesAmountBefore - 2);

	}

	private void fillLlamados() {
		this.llamados.insertOne(new Document());
	}

	private void fillMensajes() {
		Document mensaje1 = new Document(getMensajeHashMap("5b7ec43fbb32524eb9765563", "2018-08-23T18:21:44Z"));
		Document mensaje2 = new Document(getMensajeHashMap("5b7ec43fbb32524eb9765564", "2018-08-23T18:21:44Z"));
		Document mensaje3 = new Document(getMensajeHashMap("5b7ec43fbb32524eb9765565", "2018-05-23T18:21:44Z"));
		Document mensaje4 = new Document(getMensajeHashMap("5b7ec43fbb32524eb9765566", "2018-04-23T18:21:44Z"));
		this.mensajes.insertOne(mensaje1);
		this.mensajes.insertOne(mensaje2);
		this.mensajes.insertOne(mensaje3);
		this.mensajes.insertOne(mensaje4);
	}

	private HashMap<String, Object> getMensajeHashMap(String _id, String date) {
		HashMap<String,Object> mensajeJson = new HashMap<>();
		mensajeJson.put("_id", new ObjectId(_id));
		mensajeJson.put("canal", "movistar");
		mensajeJson.put("cola", "movistar-cmp");
		mensajeJson.put("origen", "2020");
		mensajeJson.put("destino", "112345678917");
		mensajeJson.put("mensaje","test_1 test_1 test_1 test_1 test_1 test_1 test_1 test_1 test_1 " +
				                         "test_1 test_1 test_1 test_1 test_1 test_1 test_1 test_1 test_1");
		mensajeJson.put("consultaEstado", "3");
		mensajeJson.put("esmeId", "0");
		mensajeJson.put("idUsuario", "19");
		mensajeJson.put("idNumero", "1");
		mensajeJson.put("data_coding", "0");
		mensajeJson.put("ultimoEstado", 2);
		mensajeJson.put("descripcionEstado", "Entregado a telco");
		mensajeJson.put("messageParts", Arrays.asList(getMessagePartsOnePart()));
		mensajeJson.put("nroDePartes", 1);
		mensajeJson.put("origenOriginal", "2020");
		mensajeJson.put("messageId", "3");
		mensajeJson.put("fechaIngreso", getISODate(date));
		return mensajeJson;
	}

	private HashMap<String, Object> getMessagePartsOnePart() {
		HashMap<String,Object> part1 = new HashMap<>();
		part1.put("np", 1);
		part1.put("length", 125);
		part1.put("seqNumber", 6);
		part1.put("estado", 6);
		part1.put("telcoId", "3");
		return part1;
	}

	private Date getISODate(String dateString) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		Date fechaIngreso = null;
		try {
			fechaIngreso = df.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fechaIngreso;
	}


}
