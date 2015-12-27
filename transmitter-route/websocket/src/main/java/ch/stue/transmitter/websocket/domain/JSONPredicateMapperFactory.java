package ch.stue.transmitter.websocket.domain;

import org.springframework.beans.factory.FactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JSONPredicateMapperFactory implements FactoryBean<ObjectMapper> {

	@Override
	public ObjectMapper getObject() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JSONPredicateDeserializer deserializer = new JSONPredicateDeserializer();
		SimpleModule module = new SimpleModule("JSONPredicateDeserializer");
		module.addDeserializer(JSONPredicate.class, deserializer);
		mapper.registerModule(module);
		return mapper;
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectMapper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
