package eu.trentorise.smartcampus.service.trentinotrack.test;

import it.sayservice.platform.client.InvocationException;
import it.sayservice.platform.client.ServiceBusClient;
import it.sayservice.platform.client.ServiceBusListener;
import it.sayservice.platform.client.jms.JMSServiceBusClient;
import it.sayservice.platform.core.common.exception.ServiceException;
import it.sayservice.platform.core.message.Core.ActionInvokeParameters;
import it.sayservice.platform.servicebus.test.DataFlowTestHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.ConnectionFactory;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.protobuf.ByteString;

import eu.trentorise.smartcampus.service.trentinotrack.impl.GetWalkTracksDataFlow;

public class TestWalkTracksDataFlow extends TestCase {

	public void testFlow() throws ServiceException {
		DataFlowTestHelper helper = new DataFlowTestHelper("test");
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String, Object> out = helper.executeDataFlow(
				"smartcampus.service.trentinotrack",
				"GetWalkTracks", new GetWalkTracksDataFlow(),
				map);
		System.err.println(out);
	}
	
	public void testRemote() throws InvocationException {
		Map<String, Object> parameters = new HashMap<String, Object>();

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		ServiceBusClient client = new JMSServiceBusClient(connectionFactory);
		
		ActionInvokeParameters invokeService = client.invokeService("smartcampus.service.trentinotrack",
				"GetWalkTracks", parameters);
		System.err.println(invokeService.getDataCount());
	}

}
