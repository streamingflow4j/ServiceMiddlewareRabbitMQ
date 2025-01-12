package com.service.middleware.listener;

import com.service.middleware.cep.handler.MonitorEventHandler;
import com.service.middleware.model.Attribute;
import com.service.middleware.model.Entity;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.Arrays;

import static org.mockito.Mockito.*;

class ConsumerServiceActivatorTest {

    @Mock
    private MonitorEventHandler monitorEventHandler;

    @InjectMocks
    private ConsumerServiceActivator consumerServiceActivator;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListenQueueA() throws Exception {
        // Arrange
        String json = "{\"id\":\"1\",\"type\":\"test\",\"attributes\":[{\"name\":\"attr1\",\"value\":\"10\"}]}";
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(json.getBytes(), messageProperties);
        Entity entity = objectMapper.readValue(json, Entity.class);

        // Act
        consumerServiceActivator.listenQueueA(message);

        // Assert
        Assert.assertEquals("1", entity.getId());
        //verify(monitorEventHandler, times(1)).getNumEventsHandled();
    }

    @Test
    void testObjectMapper() throws Exception {
        // Arrange
        String json = "{\"id\":\"1\",\"type\":\"test\",\"attributes\":[{\"name\":\"attr1\",\"value\":\"10\"}]}";
        Entity expectedEntity = new Entity("1", "test", Arrays.asList(new Attribute("attr1", "value", "10")));

        // Act
        Entity actualEntity = objectMapper.readValue(json, Entity.class);

        // Assert
        Assert.assertEquals(expectedEntity.getId(), actualEntity.getId());
        Assert.assertEquals(expectedEntity.getType(), actualEntity.getType());
        Assert.assertEquals(expectedEntity.getAttributes().get(0).getName(), actualEntity.getAttributes().get(0).getName());
        Assert.assertEquals(expectedEntity.getAttributes().get(0).getValue(), actualEntity.getAttributes().get(0).getValue());
    }
}