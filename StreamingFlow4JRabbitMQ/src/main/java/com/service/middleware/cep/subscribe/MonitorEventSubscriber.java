package com.service.middleware.cep.subscribe;

import java.util.*;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Component;

import com.service.middleware.model.Attribute;
import com.service.middleware.model.CollectType;
import com.service.middleware.model.Entity;

@Component
public class MonitorEventSubscriber implements StatementSubscriber {

	public static String mainRule = "";
	Map<String, String> eventUpdate = new HashMap<>();
	static Map<String, List<String>> mapQueue = new HashMap<>();
	Entity myEntity;

	@Setter
    @Getter
    Entity myEvent;

	public static String newPayload;

	@Autowired
	private ConnectionFactory connectionFactory;

	@Autowired
	RabbitTemplate template;

    /**
	 * {@inheritDoc}
	 */
	public String getStatement() {
		return mainRule;
	}

	public void setStatement(String statement) {
		this.mainRule = statement;
	}

    /**
	 * Listener method called when Esper has detected a pattern match.
	 * 
	 * @throws Exception
	 */
	public void update(Map<String, Double> eventMap) throws Exception {
		StringBuilder sb = new StringBuilder();
		int count = 0;

		for (Entry<String, Double> entry : eventMap.entrySet()) {
			if (count != 0) {
				sb.append(",");
			}
			sb.append(getPayloadProperty(entry.getKey(), String.valueOf(entry.getValue())));
			count++;
		}
		newPayload = getPayload(sb.toString());
	}

	public void sendEvent(){
		if (connectionFactory == null || template == null) {
			template = new RabbitTemplate(connectionFactory);
		}

		if(newPayload!=null) {
			boolean b = false;
			for (Entry<String, String> entry : eventUpdate.entrySet()) {
				String queueDest = getQueueDest();
				if (entry.getKey().equals(CollectType.ADD_RULE_ATTR_QUEUE.getName()) && Optional.of(queueDest).isPresent()) {
					template.setRoutingKey(queueDest);
					b = true;
				}
			}
			if (b) {
				template.convertAndSend(newPayload);
			}
			newPayload = null;
		}
	}

	public String getQueueDest(){
		String result = null;
		for (Entry<String, List<String>> item : mapQueue.entrySet()) {
			String key = item.getKey();
			List<String> value = item.getValue();
			if(mainRule != null && mainRule.equals(value.get(1))){
				result = value.get(0);
			}
		}
		return result;
	}

	public String getPayloadProperty(String key, String value) {
		return "{\"name\":\"" + key + "\",\"type\" :\"String\",\"value\":\"" + value + "\"}";
	}

	public String getPayload(String value) {
		return "{\"type\" : \"EventCEP\",\"id\" : \"" + String.valueOf(System.currentTimeMillis())
				+ "\",\"attributes\" : [" + value + "]}";
	}

	public String getMainRule() {
		return mainRule;
	}

	private void setMainRule(String rule) {
		this.mainRule = rule;
	}

	public Map<String, String> getEventUpdate() {
		return eventUpdate;
	}

	private void setEventUpdate(Map<String, String> eventUpdate) {
		this.eventUpdate = eventUpdate;
	}

	public Entity getMyEntity() {
		return myEntity;
	}

	public String setMyEntity(Entity myEntity) {
		this.myEntity = myEntity;
		Map<String, String> update = new HashMap<>();
		for (Attribute rule : myEntity.getAttributes()) {
			if (verifyDelRule(myEntity)) {
				if (rule.getName().equals(CollectType.RULE_ATTR_ID.getName())) {
					return rule.getValue();
				}
			} else {
				if (rule.getName().equals(CollectType.RULE_ATTR_NAME.getName())) {
					if (myEntity.getType().equals(CollectType.EDIT_RULE_TYPE.getName())) {
						this.setMainRule(rule.getValue());
					}else{
						this.setMainRule("");
					}
				} else {
					update.put(rule.getType(), rule.getValue());
				}
			}
		}
		this.setEventUpdate(update);
		return CollectType.NONE.getName();
	}

	public void setQueueMapping(String id, Entity myEntity){
		String epl = myEntity.getAttributes().get(0).getValue();
		String queueDest = myEntity.getAttributes().get(1).getValue();
		ArrayList<String> arr = new ArrayList<>();
		arr.add(0,queueDest);
		arr.add(1,epl);
		mapQueue.put(id, arr);
	}

	public String editQueueDest(String id, Entity myEntity){
		String epl = myEntity.getAttributes().get(1).getValue();
		String newQueue = myEntity.getAttributes().get(2).getValue();
		for (Entry<String, List<String>> item : mapQueue.entrySet()) {
			String key = item.getKey();
			if(key.equals(id)){
				List<String> values = new ArrayList<>();
				values.add(0,newQueue);
				values.add(1,epl);
				mapQueue.remove(key);
				mapQueue.put(id, values);
				return id;
			}
		}
		return "";
	}

	public boolean verifyDelRule(Entity myEntity) {
		boolean result = false;

		if (myEntity.getType().equals(CollectType.DEL_RULE_TYPE.getName())) {
			result = true;
		}

		return result;
	}
	public void removeQueueDest(String id) {
		mapQueue.remove(id);
	}
}