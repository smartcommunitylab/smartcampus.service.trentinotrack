package eu.trentorise.smartcampus.service.trentinotrack.test;

import it.sayservice.platform.core.common.exception.ServiceException;
import it.sayservice.platform.servicebus.test.DataFlowTestHelper;

import java.util.HashMap;
import java.util.Map;

import eu.trentorise.smartcampus.service.trentinotrack.impl.GetBikeTracksDataFlow;

public class TestBikeTracksDataFlow {

	public static void main(String[] args) throws ServiceException {
		DataFlowTestHelper helper = new DataFlowTestHelper();
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String, Object> out = helper.executeDataFlow(
				"smartcampus.service.trentinotrack",
				"GetBikeTracks", new GetBikeTracksDataFlow(),
				map);
		System.err.println(out);
	}
}
