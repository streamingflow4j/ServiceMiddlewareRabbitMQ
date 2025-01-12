package com.service.middleware.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AttributeTest {

    @Test
    void testGetName() {
        Attribute attribute = new Attribute("name1", "type1", "value1");
        assertEquals("name1", attribute.getName());
    }

    @Test
    void testSetName() {
        Attribute attribute = new Attribute();
        attribute.setName("name2");
        assertEquals("name2", attribute.getName());
    }

    @Test
    void testGetType() {
        Attribute attribute = new Attribute("name1", "type1", "value1");
        assertEquals("type1", attribute.getType());
    }

    @Test
    void testSetType() {
        Attribute attribute = new Attribute();
        attribute.setType("type2");
        assertEquals("type2", attribute.getType());
    }

    @Test
    void testGetValue() {
        Attribute attribute = new Attribute("name1", "type1", "value1");
        assertEquals("value1", attribute.getValue());
    }

    @Test
    void testSetValue() {
        Attribute attribute = new Attribute();
        attribute.setValue("value2");
        assertEquals("value2", attribute.getValue());
    }
}