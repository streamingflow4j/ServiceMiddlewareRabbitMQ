# StreamingFlow4J: Event Driven Framework with inference engine for Complex Event Processing

## 🤔 What is it used for? 
Add Complex Event Processing with Event-Driven, CQRS, Message Route to Java applications.

# About this project (ServiceMiddlewareRabbitMQ)
This project is a contextual middleware made with java, Spring Integration and EsperCEP.

## 1. Running the RabbitMQ project
Install and run RabbitMQ. More informations(https://www.rabbitmq.com/#getstarted).

## 2. Guide install
1. Download the project file
2. Unzip the project file
3. Configure docker-compose.yml:
 - queue.streaming.data=si.test.queue (Queue of data streming)
 - queue.rule.cep=si.ceprule.queue (Queue of data rules)
4. Using Docker follow the steps:
 
 a)StreamingFlow4JKafka:
 - mvn clean install
 - docker build --tag streamingflow4j-rabbitmq .
 - docker compose -f  docker-compose.yml up --detach
 
 b)RestEndpointKafka:
 - mvn clean install
 - docker build --tag endpoint-rabbitmq . 
 - docker compose -f  docker-compose.yml up --detach

 c)APIExamplesConsumerKafka: 
 - mvn clean install
 - docker build --tag consumer-rabbitmq .
 - docker compose -f  docker-compose.yml up --detach

## 3. API examples (Monitoring Temperature):
Sending message to rabbitMQ queues with the following scope.
Execute RestEndpointRabbitMQ Spring Boot application

### API elements
In Event ID is defined as one Unique Identifier name and
The event type defines the operation in the following steps:

### Step 1 - Scope Addiction Event:
- EVENT_CREATE -- Type to define the creation of a new event

### Step 2 - Scope Addiction Rule:
- RULE_CREATE  ```Type to define the creation of a rule```
- RULE_QUERY ```Indicate an EPL(Esper Query Language) this QUERY should be added(EX.: select temperature from Thermometer.win:time(5 sec))```
- RULE_QUEUE ```Define destination queue of rule trigger```

### Step 3 - Scope Edition Rule:
- RULE_UPDATE  ```Type for defining the EDITION of a rule```
- RULE_QUERY ```Indicate an EPL(Esper Query Language) this QUERY should be added(EX.: select temperature from Thermometer.win:time(5 sec))```
- RULE_QUEUE ```Define destination queue of rule trigger```
- RULE_ID ```ID of the rule to be Edited```

### Step 4 - Scope Delete Rule:
- RULE_DELETE ```Type for deleting a Rule```
- RULE_ID ```ID of the rule to be Deleted```

a) Defining a context element entity for event types:

-- post RabbitMQ endpoint: </endpoint-address-uri>/rabbitmq/event/create
-- post Body:
```
{
"type" : "EVENT_CREATE",
"id" : "Thermometer",
"attributes" : [
{ 
"name"  : "id",
"type"  : "String",
"value" : "0"
},
{
"name" : "temperature",
"type"  : "Double",
"value" : "0"
}
]
}
```
b) Creating context rules for event types:

-- post RabbitMQ endpoint: </endpoint-address-uri>/rabbitmq/rule/create
-- post Body:
```
{
"type" : "RULE_CREATE", 
"id" : "Rule7",
"attributes" : [
{ "name"  : "RULE_QUERY",   --Attribute for rule definition
"type"  : "String",
"value" : "select temperature from Thermometer.win:time(5 sec)"
},
{
"name"  : "QUEUE_1",
"type"  : "RULE_QUEUE",         --Attribute for destination queue of rule outcomes
"value" : "si.cep.queue" 
}
]
}
```
c) Updating context rules for event types:

-- put RabbitMQ endpoint: </endpoint-address-uri>/rabbitmq/rule/update
-- put Body:
```
{
"type" : "RULE_UPDATE",
"id" : "Rule3",
"attributes" : [
{ 
"name"  : "RULE_ID",   --Obs.: rule id can be obtained in the logfile.
"type"  : "String",
"value" : "f9c9c1c1-d1ed-4f03-99cb-7083533ac2e0"
},
{
"name"  : "RULE_QUERY",    --Attribute for rule update
"type"  : "String",  
"value" : "select temperature from Thermometer.win:time(10 sec)"
},
{
"name"  : "QUEUE_1",
"type"  : "RULE_QUEUE",
"value" : "si.cep.queue"
}
]
}
```

d) Delete a rule defined:

-- delete RabbitMQ endpoint: </endpoint-address-uri>/rabbitmq/rule/delete
-- post Body:
```
{
"type" : "RULE_DELETE",
"id" : "DelRule3",
"attributes" : [
{
"name"  : "RULE_ID",
"type"  : "String",
"value" : "d081f1bc-4d68-4427-bd47-59684e480327"
}
]
}
```

e) Scope of data producer:

-- post RabbitMQ endpoint: </endpoint-address-uri>/rabbitmq/data/create
-- post Body:
```
{
"type" : "Thermometer",
"id" : "1",
"attributes" : [
{ 
"name" : "temperature",
"type" : "Double",
"value" : "2"
}
]
}
```

f) Scope of data Consumer:

```
{
"type" : "EventCEP",
"id" : "1582033487619",
"attributes" : [
{
"name":"temperature",
"type" :"String",
"value":"13.0"
}
]
}
```
## ⭐ Give us a star!

Like what you see? Please consider giving this a star (★)!

## 🏷️ License and Citation

The code is available under Apache License.
If you find this project helpful in your research, please cite this work at

```
@misc{sf4j2019,
    title = {StreamingFlow4J: A modern Java Event Driven CEP Framework for Microservices},
    url = {https://github.com/streamingflow4j},
    author = {H Diniz},
    month = {January},
    year = {2019}
}
```
