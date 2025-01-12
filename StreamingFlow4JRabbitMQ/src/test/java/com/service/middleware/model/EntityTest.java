package com.service.middleware.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class EntityTest {

        @Test
        void testGetType() {
            Entity entity = new Entity("1", "type1", null);
            assertEquals("type1", entity.getType());
        }

        @Test
        void testSetType() {
            Entity entity = new Entity();
            entity.setType("type2");
            assertEquals("type2", entity.getType());
        }

        @Test
        void testGetId() {
            Entity entity = new Entity("1", "type1", null);
            assertEquals("1", entity.getId());
        }

        @Test
        void testSetId() {
            Entity entity = new Entity();
            entity.setId("2");
            assertEquals("2", entity.getId());
        }

        @Test
        void testGetAttributes() {
            Attribute attr1 = new Attribute("name1", "value1", "value3");
            Attribute attr2 = new Attribute("name2", "value2", "value3");
            List<Attribute> attributes = Arrays.asList(attr1, attr2);
            Entity entity = new Entity("1", "type1", attributes);
            assertEquals(attributes, entity.getAttributes());
        }

        @Test
        void testSetAttributes() {
            Attribute attr1 = new Attribute("name1", "value1", "value3");
            Attribute attr2 = new Attribute("name2", "value2", "value3");
            List<Attribute> attributes = Arrays.asList(attr1, attr2);
            Entity entity = new Entity();
            entity.setAttributes(attributes);
            assertEquals(attributes, entity.getAttributes());
        }
}