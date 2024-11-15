package com.service.middleware.cep.handler;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.service.middleware.cep.subscribe.MonitorEventSubscriber;
import com.service.middleware.model.Attribute;
import com.service.middleware.model.CollectType;
import com.service.middleware.model.Entity;
import com.service.middleware.util.RunTimeEPStatement;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;

@Component
@Scope(value = "singleton")
public class MonitorEventHandler implements InitializingBean {

	/** Esper service */
	private EPServiceProvider epService;

	private EPStatement monitorEventStatement;
	
	final static Logger logger = Logger.getLogger(MonitorEventHandler.class);

    @Autowired
	private MonitorEventSubscriber monitorEventSubscriber;

	private static ConcurrentHashMap<UUID, RunTimeEPStatement> queriesEpl = new ConcurrentHashMap<UUID, RunTimeEPStatement>();
	private static ConcurrentHashMap<String, Object>  cHM = new ConcurrentHashMap<String, Object>();
	private AtomicLong eventsHandledCount;
	private AtomicLong eventsHandledMicroseconds;
	private Configuration config;
	int count = 0;
	
	/**
	 * Configure Esper Statement(s).
	 * @throws Exception 
	 */
	public void initService() throws Exception {
		eventsHandledCount = new AtomicLong(0);
		eventsHandledMicroseconds = new AtomicLong(0);
		config = new Configuration();
		config.addEventTypeAutoName("com.service.middleware.model");
		epService = EPServiceProviderManager.getDefaultProvider(config);
	}

	public void createRequestMonitorExpression(Entity myEntity) throws Exception {

		String verify = "";
		if(myEntity.getType().equals(CollectType.EVENT_CREATE_TYPE.getName())){
			createBeans(myEntity);
		}else{

			verify = monitorEventSubscriber.setMyEntity(myEntity);
			if(verify.equals(CollectType.NONE.getName())){
				String epl = monitorEventSubscriber.getStatement();
				monitorEventStatement = epService.getEPAdministrator().createEPL(epl);
				monitorEventStatement.setSubscriber(monitorEventSubscriber);
				
				 if(myEntity.getType().equals(CollectType.EDIT_RULE_TYPE.getName())){
						String myEpl = getEditEpl(myEntity);
						if(!myEpl.equals(CollectType.NONE.getName())){
						    UUID id = UUID.fromString(getEntityId(myEntity));
						    RunTimeEPStatement etEps = queriesEpl.get(id);
						    if(etEps != null) {
						    	etEps.destroy();
						    	queriesEpl.put(id, new RunTimeEPStatement(monitorEventStatement,myEpl));
						    	logger.info("Runtime EPStatement Updated " + id);
						    }
						
						}else{
							logger.error("Error in query attribute");
						}
				} else{
					UUID uuid = UUID.randomUUID();		
					queriesEpl.put(uuid, new RunTimeEPStatement(monitorEventStatement,epl));
					System.out.println("Runtime EPStatement Created " + uuid + " QUERY: "+epl);
				 }
			
			}else if(myEntity.getType().equals(CollectType.DEL_RULE_TYPE.getName())){
				removeStatement(UUID.fromString(verify));
			}
			
		}
	}

	public String getEditEpl(Entity entity) {	
		for (Attribute rule : entity.getAttributes()) {
			if (rule.getName().equals(CollectType.RULE_ATTR_NAME.getName())) {
					return rule.getValue();
			}
			
		}
		return CollectType.NONE.getName();
	}
	
	public String getEntityId(Entity entity) {	
		for (Attribute rule : entity.getAttributes()) {
			if (rule.getName().equals(CollectType.RULE_ATTR_ID.getName())) {
					return rule.getValue();
			}
			
		}
		return CollectType.NONE.getName();
	}
	
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		initService();
	}

	/**
	 * Handle the incoming Entity.
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws NumberFormatException 
	 */
	public void handleEntity(Entity event) throws NoSuchMethodException, SecurityException, NumberFormatException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object bean = cHM.get(event.getType());
		if (bean != null) {
			Method setter = bean.getClass().getMethod("setId", Double.class);
			setter.invoke(bean, Double.parseDouble(event.getId()));
			for (Attribute attr : event.getAttributes()) {
				setter = bean.getClass().getMethod(
						"set" + attr.getName().substring(0, 1).toUpperCase() + attr.getName().substring(1),
						Double.class);
				setter.invoke(bean, Double.parseDouble(attr.getValue()));
			}

			epService.getEPRuntime().sendEvent(bean);
		}
	}
	
	public boolean removeStatement(UUID id) {
	    RunTimeEPStatement etEps = queriesEpl.get(id);
	    if(etEps != null) {
	    	queriesEpl.remove(id);
	    	etEps.destroy();
	    	if(logger.isInfoEnabled()){
				logger.info("Runtime EPStatement Destroyed " + id);
			}
	      return true;
	    }
	    return false;
	}
	

	public boolean editStatement(UUID id, RunTimeEPStatement runTimeEPStatement) {
		    RunTimeEPStatement etEps = queriesEpl.get(id);
		    if(etEps != null) {
		    	etEps = runTimeEPStatement;
		    	queriesEpl.put(id, runTimeEPStatement);
		    	logger.info("Runtime EPStatement Updated " + id);
		      return true;
		    }
		    return false;
	}

	public boolean isEplRegistered(UUID id) {
		return queriesEpl.containsKey(id);
	}

	public long getNumEventsHandled() {
		return eventsHandledCount.longValue();
	}

	public long getMicrosecondsHandlingEvents() {
		return eventsHandledMicroseconds.longValue();
	}
	

	public Class<?> createBeanClass(
		    /* fully qualified class name */
		    final String className,
		    /* bean properties, name -> type */
		    final Map<String, Class<?>> properties){

		    final BeanGenerator beanGenerator = new BeanGenerator();

		    /* use our own hard coded class name instead of a real naming policy */
		    beanGenerator.setNamingPolicy(new NamingPolicy(){
		        @Override public String getClassName(final String prefix,
		            final String source, final Object key, final Predicate names){
		            return className;
		        }});

		    BeanGenerator.addProperties(beanGenerator, properties);
		    cHM.put(className, beanGenerator.create());
		    return (Class<?>) beanGenerator.createClass();
		    
		}
	
	@SuppressWarnings("unused")
	public void createBeans(Entity entity){
		String className = entity.getId();
	    final Map<String, Class<?>> properties = new HashMap<String, Class<?>>();
	    for (Attribute attr : entity.getAttributes()) {
	    	 properties.put(attr.getName(), Double.class);
		}	   	    
		final Class<?> beanClass = createBeanClass(className, properties);
	    Object myBean = cHM.get(className);
	    epService.getEPAdministrator().getConfiguration().addEventType(className, myBean.getClass());
		System.out.println("Add Event class =====> " + className);
	}
	

}
