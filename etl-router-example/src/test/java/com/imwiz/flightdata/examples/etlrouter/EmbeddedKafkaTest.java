package com.imwiz.flightdata.examples.etlrouter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class EmbeddedKafkaTest {

	//@Autowired
    //private KafkaEmbedded kafkaEmbedded;

    @Autowired
    private MessageChannel inputChannel;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void testIntegration() throws Exception {
    	log.debug("Begin testIntegration");
        String payload = "test message";
        Message<String> message = new GenericMessage<>(payload);

        inputChannel.send(message);

        Thread.sleep(1000L);
        
       
        
        
        //KafkaProducerMessageHandler<String, String> handler =
       // 		new KafkaProducerMessageHandler<String, String>(kafkaTemplate);
       // handler.

      //  assertThat(KafkaTestUtils.getSingleRecord(kafkaTemplate, "test").value()).isEqualTo(payload);
    }

	
	
}
