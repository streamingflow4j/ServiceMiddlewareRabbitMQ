package com.service.middleware.cep.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.espertech.esper.client.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

	private static final Logger logger = LoggerFactory.getLogger(MonitorEventHandler.class);

	/** Esper cep service */
	private EPServiceProvider epService;
	private EPStatement monitorEventStatement;
	@Autowired
	private MonitorEventSubscriber monitorEventSubscriber;

	public static ConcurrentHashMap<UUID, RunTimeEPStatement> queriesEpl = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Object> cHM = new ConcurrentHashMap<>();
	public AtomicLong eventsHandledCount;
	public AtomicLong eventsHandledMicroseconds;


	static String listEpl;


    /**
	 * Configure Esper Statement(s).
	 *
	 * @throws Exception
	 */

	public void initService() throws Exception {
		eventsHandledCount = new AtomicLong(0);
		eventsHandledMicroseconds = new AtomicLong(0);
		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.service.middleware.model");
		epService = EPServiceProviderManager.getDefaultProvider(config);
	}

	public void createRequestMonitorExpression(@NotNull Entity myEntity) throws Exception {
		String verify = "";
		if (myEntity.getType().equals(CollectType.EVENT_CREATE_TYPE.getName())) {
			createBeans(myEntity);
		} else {
			verify = monitorEventSubscriber.setMyEntity(myEntity);
			if (verify.equals(CollectType.NONE.getName())) {
				String epl = myEntity.getAttributes().get(0).getValue();
				if (myEntity.getType().equals(CollectType.EDIT_RULE_TYPE.getName())) {
					epl = monitorEventSubscriber.getStatement();
				}
				monitorEventStatement = epService.getEPAdministrator().createEPL(epl);
				monitorEventStatement.setSubscriber(monitorEventSubscriber);
				////Edit event query
				if (myEntity.getType().equals(CollectType.EDIT_RULE_TYPE.getName())) {
					String myEpl = getEditEpl(myEntity);
					if (!myEpl.equals(CollectType.NONE.getName())) {
						UUID id = UUID.fromString(getEntityId(myEntity));
						RunTimeEPStatement etEps = queriesEpl.get(id);

						if (etEps != null) {
							etEps.destroy();
							queriesEpl.put(id, new RunTimeEPStatement(monitorEventStatement, myEpl));
							monitorEventSubscriber.editQueueDest(id.toString(), myEntity);
							////logger.info("==============================================================");
							logger.info("Runtime EPStatement Updated. id: {}", id);
							////logger.info("==============================================================");
						}
					} else {
						logger.error("Error in query attribute");
					}
				} else {
					////Add evento query
					UUID uuid = UUID.randomUUID();
					queriesEpl.put(uuid, new RunTimeEPStatement(monitorEventStatement, epl));
					monitorEventSubscriber.setQueueMapping(uuid.toString(),myEntity);
					if (logger.isInfoEnabled()) {
						////logger.info("==============================================================");
						logger.info("Runtime EPStatement Created. id: {} QUERY: {}", uuid, epl);
						////logger.info("==============================================================");
					}
				}
			} else if (myEntity.getType().equals(CollectType.DEL_RULE_TYPE.getName())) {
				removeStatement(UUID.fromString(verify));
				monitorEventSubscriber.removeQueueDest(UUID.fromString(verify).toString());
			}

		}
	}

	public String getEditEpl(@NotNull Entity entity) {
		for (Attribute rule : entity.getAttributes()) {
			if (rule.getName().equals(CollectType.RULE_ATTR_NAME.getName())) {
				return rule.getValue();
			}
		}
		return CollectType.NONE.getName();
	}

	public String getEntityId(@NotNull Entity entity) {
		for (Attribute rule : entity.getAttributes()) {
			if (rule.getName().equals(CollectType.RULE_ATTR_ID.getName())) {
				return rule.getValue();
			}

		}
		return CollectType.NONE.getName();
	}

	public void afterPropertiesSet() throws Exception {
		initService();
	}

	/**
	 * Handle the incoming Entity.
	 *
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NumberFormatException
	 */
	public void handleEntity(Entity event) throws NoSuchMethodException, SecurityException, NumberFormatException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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

			//monitorEventSubscriber.setMyEvent(event);
			epService.getEPRuntime().sendEvent(bean);
			getListenerRule();
			monitorEventSubscriber.sendEvent();

		}
	}

	// function for remove rule cep
	public boolean removeStatement(UUID id) {
		RunTimeEPStatement etEps = queriesEpl.get(id);
		if (etEps != null) {
			queriesEpl.remove(id);
			etEps.destroy();
			if (logger.isInfoEnabled()) {
				////logger.info("==============================================================");
				logger.info("Runtime EPStatement Destroyed {}", id);
				////logger.info("==============================================================");
			}
			return true;
		}
		return false;
	}

	// function for edit rule cep #deprecated
/*
	public boolean editStatement(UUID id, RunTimeEPStatement runTimeEPStatement) {
		RunTimeEPStatement etEps = queriesEpl.get(id);
		if (etEps != null) {
			etEps = runTimeEPStatement;
			queriesEpl.put(id, runTimeEPStatement);
			logger.info("Runtime EPStatement Updated: {}", id);
			return true;
		}
		return false;
	}
*/
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
			final Map<String, Class<?>> properties) {

		BeanGenerator beanGenerator = new BeanGenerator();

		/* use our own hard coded class name instead of a real naming policy */
		beanGenerator.setNamingPolicy(new NamingPolicy() {
			@Override
			public String getClassName(final String prefix, final String source, final Object key,
									   final Predicate names) {
				return className;
			}
		});

		BeanGenerator.addProperties(beanGenerator, properties);
		cHM.put(className, beanGenerator.create());
		return (Class<?>) beanGenerator.createClass();

	}
	// function for create rule cep
	@SuppressWarnings("unused")
	public void createBeans(@NotNull Entity entity) {
		String className = entity.getId();
		final Map<String, Class<?>> properties = new HashMap<>();
		for (Attribute attr : entity.getAttributes()) {
			properties.put(attr.getName(), Double.class);
		}

		final Class<?> beanClass = createBeanClass(className, properties);
		Object myBean = cHM.get(className);
		epService.getEPAdministrator().getConfiguration().addEventType(className, myBean.getClass());
		////logger.info("==============================================================");
		logger.info("Add Event class =====> {}", className);
		////logger.info("==============================================================");
	}

	public void getListenerRule(){
		for (String listener : epService.getEPAdministrator().getStatementNames()) {
			EPStatement gSignal = epService.getEPAdministrator().getStatement(listener);
			gSignal.addListener(new UpdateListener() {
				@Override
				public void update(EventBean[] newEvents, EventBean[] oldEvents) {
					if (newEvents == null) {
						return;
					}
					listEpl = gSignal.getText();
					monitorEventSubscriber.setStatement(listEpl);
				}
			});

		}
	}

}