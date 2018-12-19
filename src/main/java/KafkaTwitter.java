import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import kafka.consumer.ConsumerConfig;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import kafka.consumer.Consumer;
import model.format;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaTwitter {

    private static final String catsTopic = "cats-topic";

    private static void produce(String consumerKey, String consumerSecret,
                           String token, String secret) throws InterruptedException {
        Properties properties = new Properties();
        properties.put("metadata.broker.list", "localhost:9092");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("client.id","samy");
        ProducerConfig producerConfig = new ProducerConfig(properties);
        kafka.javaapi.producer.Producer<String, String> producer = new kafka.javaapi.producer.Producer<String, String>(
                producerConfig);


        BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(Lists.newArrayList("cats"));

        Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
        Client client = new ClientBuilder().hosts(Constants.STREAM_HOST)
                .endpoint(endpoint).authentication(auth)
                .processor(new StringDelimitedProcessor(queue)).build();

        client.connect();

        try {
            while (true) {
                KeyedMessage<String, String> message = null;
                try {
                    message = new KeyedMessage<String, String>(catsTopic, queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                producer.send(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
            client.stop();
        }
    }

    private static void consumer (String topic)  {
        final String BOOTSTRAP_SERVERS = "localhost:9092";

        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("group.id", "samy");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer(props);

        consumer.subscribe(Arrays.asList(topic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    JsonObject lineJson = new JsonObject(record.value());
                    System.out.println(lineJson.encodePrettily());

                    format formattedLine = new format();
                    formattedLine.setId(lineJson.getLong("id"));
                    formattedLine.setCreated_at(lineJson.getString("created_at"));
                    formattedLine.setText(new StringBuilder(lineJson.getString("text")).reverse().toString());
                    formattedLine.setRetweeted(lineJson.getBoolean("retweeted"));
                    JsonObject user = new JsonObject();
                    user.put("screen_name", lineJson.getJsonObject("screen_name"));
                    user.put("screen_name", lineJson.getJsonObject("screen_name"));
                    formattedLine.setRetweeted(lineJson.getBoolean("retweeted"));
                }
            }
        } finally {
            consumer.close();
        }

    }

    public static void main (String[] args) {
        try {
            KafkaTwitter.produce(args[0], args[1], args[2], args[3]);
            consumer(catsTopic);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
