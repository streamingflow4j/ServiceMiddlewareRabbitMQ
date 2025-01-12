package com.service.middleware.cep.subscribe;

import com.service.middleware.model.Attribute;
import com.service.middleware.model.CollectType;
import com.service.middleware.model.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MonitorEventSubscriberTest {

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private MonitorEventSubscriber monitorEventSubscriber;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStatement() {
        monitorEventSubscriber.setStatement("testStatement");
        assertEquals("testStatement", monitorEventSubscriber.getStatement());
    }

    @Test
    void testUpdate() throws Exception {
        Map<String, Double> eventMap = new HashMap<>();
        eventMap.put("key1", 1.0);
        eventMap.put("key2", 2.0);

        monitorEventSubscriber.update(eventMap);

        assertNotNull(MonitorEventSubscriber.newPayload);
    }
/*
    @Test
    void testSendEvent() {
        monitorEventSubscriber.setStatement("testStatement");
        MonitorEventSubscriber.newPayload = "testPayload";
        monitorEventSubscriber.sendEvent();

        verify(rabbitTemplate, times(1)).convertAndSend("testPayload");
    }
*/
    @Test
    void testGetQueueDest() {
        List<String> queueInfo = Arrays.asList("queue1", "rule1");
        MonitorEventSubscriber.mapQueue.put("id1", queueInfo);
        monitorEventSubscriber.setStatement("rule1");

        assertEquals("queue1", monitorEventSubscriber.getQueueDest());
    }
/*
    @Test
    void testSetMyEntity() {
        Attribute attribute1 = new Attribute("RULE_QUERY", "type1", "value1");
        Attribute attribute2 = new Attribute("RULE_ID", "type2", "value2");
        List<Attribute> attributes = Arrays.asList(attribute1, attribute2);
        Entity entity = new Entity("id1", "RULE_CREATE", attributes);

        String result = monitorEventSubscriber.setMyEntity(entity);

        assertEquals(CollectType.NONE.getName(), result);
        assertEquals("value1", monitorEventSubscriber.getMainRule());
    }
*/
    @Test
    void testSetQueueMapping() {
        Attribute attribute1 = new Attribute("attr1", "type1", "epl1");
        Attribute attribute2 = new Attribute("attr2", "type2", "queue1");
        List<Attribute> attributes = Arrays.asList(attribute1, attribute2);
        Entity entity = new Entity("id1", "type1", attributes);

        monitorEventSubscriber.setQueueMapping("id1", entity);

        assertTrue(MonitorEventSubscriber.mapQueue.containsKey("id1"));
    }

    @Test
    void testEditQueueDest() {
        List<String> queueInfo = Arrays.asList("queue1", "epl1");
        MonitorEventSubscriber.mapQueue.put("id1", queueInfo);
        Attribute attribute1 = new Attribute("attr1", "type1", "id1");
        Attribute attribute2 = new Attribute("attr2", "type2", "epl1");
        Attribute attribute3 = new Attribute("attr3", "type3", "newQueue");
        List<Attribute> attributes = Arrays.asList(attribute1, attribute2, attribute3);
        Entity entity = new Entity("id1", "type1", attributes);

        String result = monitorEventSubscriber.editQueueDest("id1", entity);

        assertEquals("id1", result);
        assertEquals("newQueue", MonitorEventSubscriber.mapQueue.get("id1").get(0));
    }

    @Test
    void testRemoveQueueDest() {
        List<String> queueInfo = Arrays.asList("queue1", "epl1");
        MonitorEventSubscriber.mapQueue.put("id1", queueInfo);

        monitorEventSubscriber.removeQueueDest("id1");

        assertFalse(MonitorEventSubscriber.mapQueue.containsKey("id1"));
    }
}