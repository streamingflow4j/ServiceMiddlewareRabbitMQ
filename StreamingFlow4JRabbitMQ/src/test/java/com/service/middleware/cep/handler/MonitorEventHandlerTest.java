package com.service.middleware.cep.handler;

import com.service.middleware.cep.subscribe.MonitorEventSubscriber;
import com.service.middleware.model.Attribute;
import com.service.middleware.model.CollectType;
import com.service.middleware.model.Entity;
import com.service.middleware.util.RunTimeEPStatement;
import com.espertech.esper.client.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MonitorEventHandlerTest {

    @Mock
    private EPServiceProvider epService;

    @Mock
    private EPStatement monitorEventStatement;

    @Mock
    private MonitorEventSubscriber monitorEventSubscriber;

    @Mock
    private EPAdministrator epAdministrator;

    @InjectMocks
    private MonitorEventHandler monitorEventHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(epService.getEPAdministrator()).thenReturn(mock(EPAdministrator.class));
        when(epService.getEPRuntime()).thenReturn(mock(EPRuntime.class));
        MonitorEventHandler.queriesEpl = new ConcurrentHashMap<>();
        monitorEventHandler.eventsHandledCount = new AtomicLong(0);
        monitorEventHandler.eventsHandledMicroseconds = new AtomicLong(0);
        MonitorEventHandler.cHM = new ConcurrentHashMap<>();
    }
    /*
        @Test
        void testCreateRequestMonitorExpression() throws Exception {
            Entity entity = new Entity("1", CollectType.EVENT_CREATE_TYPE.getName(), new ArrayList<>());
            doNothing().when(monitorEventSubscriber).setMyEntity(any(Entity.class));

            monitorEventHandler.createRequestMonitorExpression(entity);

            verify(monitorEventSubscriber, times(1)).setMyEntity(entity);
        }

        @Test
        void testHandleEntity() throws Exception {
            Entity entity = new Entity("1", "type", Arrays.asList(new Attribute("attr1", "type1", "1.0")));
            Object bean = new Object() {
                public void setId(Double id) {}
                public void setAttr1(Double value) {}
            };
            MonitorEventHandler.cHM.put("type", bean);

            monitorEventHandler.handleEntity(entity);

            verify(epService.getEPRuntime(), times(1)).sendEvent(bean);
        }
    */
    @Test
    void testRemoveStatement() {
        UUID id = UUID.randomUUID();
        RunTimeEPStatement runTimeEPStatement = mock(RunTimeEPStatement.class);
        MonitorEventHandler.queriesEpl.put(id, runTimeEPStatement);

        boolean result = monitorEventHandler.removeStatement(id);

        assertTrue(result);
        verify(runTimeEPStatement, times(1)).destroy();
        assertFalse(MonitorEventHandler.queriesEpl.containsKey(id));
    }

    @Test
    void testGetEditEpl() {
        Attribute attribute = new Attribute(CollectType.RULE_ATTR_NAME.getName(), "type", "value");
        Entity entity = new Entity("1", "type", Arrays.asList(attribute));

        String result = monitorEventHandler.getEditEpl(entity);

        assertEquals("value", result);
    }

    @Test
    void testGetEntityId() {
        Attribute attribute = new Attribute(CollectType.RULE_ATTR_ID.getName(), "type", "value");
        Entity entity = new Entity("1", "type", Arrays.asList(attribute));

        String result = monitorEventHandler.getEntityId(entity);

        assertEquals("value", result);
    }

    @Test
    void testIsEplRegistered() {
        UUID id = UUID.randomUUID();
        MonitorEventHandler.queriesEpl.put(id, new RunTimeEPStatement(null, "testEpl"));

        assertTrue(monitorEventHandler.isEplRegistered(id));
        assertFalse(monitorEventHandler.isEplRegistered(UUID.randomUUID()));
    }

    @Test
    void testGetNumEventsHandled() {
        monitorEventHandler.eventsHandledCount.incrementAndGet();
        assertEquals(1, monitorEventHandler.getNumEventsHandled());
    }

    @Test
    void testGetMicrosecondsHandlingEvents() {
        monitorEventHandler.eventsHandledMicroseconds.addAndGet(1000);
        assertEquals(1000, monitorEventHandler.getMicrosecondsHandlingEvents());
    }

    @Test
    void testCreateBeanClass() {
        // Arrange
        String className = "TestBean";
        Map<String, Class<?>> properties = new HashMap<>();
        properties.put("property1", Double.class);
        properties.put("property2", Double.class);

        // Act
        Class<?> beanClass = monitorEventHandler.createBeanClass(className, properties);

        // Assert
        assertNotNull(beanClass);
        assertEquals(className, beanClass.getName());
        assertTrue(monitorEventHandler.cHM.containsKey(className));
        Object bean = monitorEventHandler.cHM.get(className);
        assertNotNull(bean);
        assertEquals(beanClass, bean.getClass());
    }

}