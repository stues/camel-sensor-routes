package ch.stue.transmitter.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Abstract Test class for JSONPredicates
 *
 * @author stue
 */
@ContextConfiguration(locations = { "classpath:websocket-context-test.xml" })
public abstract class AbstractJSONPredicateTests extends AbstractTestNGSpringContextTests {

	@Autowired
	@Qualifier("jsonPredicateMapper")
	protected ObjectMapper jsonPredicateMapper;

}