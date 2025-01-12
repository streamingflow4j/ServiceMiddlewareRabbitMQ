package com.service.middleware.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectTypeTest {

    @Test
    void testToString() {
        assertEquals("NONE", CollectType.NONE.toString());
        assertEquals("EVENT_CREATE", CollectType.EVENT_CREATE_TYPE.toString());
        assertEquals("RULE_CREATE", CollectType.RULE_CREATE_TYPE.toString());
        assertEquals("RULE_QUERY", CollectType.RULE_ATTR_NAME.toString());
        assertEquals("RULE_QUEUE", CollectType.ADD_RULE_ATTR_QUEUE.toString());
        assertEquals("RULE_UPDATE", CollectType.EDIT_RULE_TYPE.toString());
        assertEquals("RULE_ID", CollectType.RULE_ATTR_ID.toString());
        assertEquals("RULE_DELETE", CollectType.DEL_RULE_TYPE.toString());
    }

    @Test
    void testGetEnumFromString() {
        assertEquals(CollectType.NONE, CollectType.getEnumFromString("NONE"));
        assertEquals(CollectType.EVENT_CREATE_TYPE, CollectType.getEnumFromString("EVENT_CREATE"));
        assertEquals(CollectType.RULE_CREATE_TYPE, CollectType.getEnumFromString("RULE_CREATE"));
        assertEquals(CollectType.RULE_ATTR_NAME, CollectType.getEnumFromString("RULE_QUERY"));
        assertEquals(CollectType.ADD_RULE_ATTR_QUEUE, CollectType.getEnumFromString("RULE_QUEUE"));
        assertEquals(CollectType.EDIT_RULE_TYPE, CollectType.getEnumFromString("RULE_UPDATE"));
        assertEquals(CollectType.RULE_ATTR_ID, CollectType.getEnumFromString("RULE_ID"));
        assertEquals(CollectType.DEL_RULE_TYPE, CollectType.getEnumFromString("RULE_DELETE"));
        assertNull(CollectType.getEnumFromString("INVALID"));
    }
}
