package com.ge.predix.solsvc.machinedata.simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.Body;
import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.DatapointsIngestion;
import com.ge.predix.solsvc.machinedata.simulator.config.Constants;

import com.ge.predix.solsvc.machinedata.simulator.vo.CIMPOSAttributesVO;
import com.ge.predix.solsvc.machinedata.simulator.vo.CIMPOSBody;
import com.ge.predix.solsvc.machinedata.simulator.vo.CIMPOSObjectVO;

import com.ge.predix.solsvc.restclient.impl.RestClient;

/**
 * 
 * @author predix -
 */
@Component
public class MachineDataSimulator {
	/**
	 * 
	 */
	static final Logger log = LoggerFactory
			.getLogger(MachineDataSimulator.class);
	private ObjectMapper mapper = new ObjectMapper();

	public MachineDataSimulator() {
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		// mapper.configure(SerializationFeature.INDENT_OUTPUT,true);
		mapper.setSerializationInclusion(Include.NON_NULL);

	}

	/**
	 * 
	 */
	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	private MapperService mapperService;

	@Autowired
	private RestClient restClient;
	
	public static float data=(float) 1.0009;
	

	/**
	 * -
	 */
	@Scheduled(fixedDelayString = "${dataingestion.sleepTimeMillis}")
	public void run() {

		try {
			generateAndPushRandomCimposData();

		} catch (Throwable e) {
			log.error("unable to run Machine DataSimulator Thread", e); //$NON-NLS-1$
		}
	}
	private String generateAndPushRandomCimposData()
			throws JsonGenerationException, JsonMappingException, IOException {
		Long currentTimeMillis = System.currentTimeMillis();

		CIMPOSObjectVO cimposObjVO = new CIMPOSObjectVO();
		List<CIMPOSBody> cimposBodyList = new ArrayList<>();

		for (Constants.Cycles name : Constants.Cycles.values()) {
			createZonewiseCimposBody(cimposBodyList, Constants.Zone1, Constants.Zone01,name);
			createZonewiseCimposBody(cimposBodyList, Constants.Zone2, Constants.Zone02,name);
			createZonewiseCimposBody(cimposBodyList, Constants.Zone3, Constants.Zone03,name);
		}

		cimposObjVO.setBody(cimposBodyList);
		cimposObjVO.setMessageId(currentTimeMillis);

		DatapointsIngestion dataPtIngest = createCIMPOSDataIngestionRequest(cimposObjVO);

		StringWriter writer = new StringWriter();

		mapper.writeValue(writer, dataPtIngest);
		System.out.println("Final CIMPOS JSON sending to saveTimeSeries >> "
				+ writer.toString());
		return postData(writer.toString());
		// return writer.toString();


	}

	

	private void createZonewiseCimposBody(List<CIMPOSBody> cimposBodyList, String[] assetArray, int Zone, Constants.Cycles name) {
		for(int i=0; i < assetArray.length; i++){
			CIMPOSBody cimposBody = generateCimposData(Zone, assetArray[i],name);
			cimposBodyList.add(cimposBody);
		}
	}
	private CIMPOSBody generateCimposData(int Zone, String cimposAssetName, Constants.Cycles name) {

		Long currentTimeMillis = System.currentTimeMillis();

		CIMPOSBody cimposAreaBody = getRandomCIMPOSDataVO(currentTimeMillis, cimposAssetName, Zone, name);

		return cimposAreaBody;

	}
	
	
	public String runTest() throws Exception {
		List<JSONData> list = generateMockDataMap_RT();

		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();

		mapper.writeValue(writer, list);
		return postData(writer.toString());

	}

	

	@SuppressWarnings("unchecked")
	private DatapointsIngestion createCIMPOSDataIngestionRequest(
			CIMPOSObjectVO inputObj)
					throws com.fasterxml.jackson.core.JsonParseException,
					com.fasterxml.jackson.databind.JsonMappingException, IOException {

		DatapointsIngestion dpIngestion = new DatapointsIngestion();
		// dpIngestion.setMessageId(String.valueOf(System.currentTimeMillis()));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		dpIngestion.setMessageId(String.valueOf(calendar.getTimeInMillis()));
		List<Body> bodies = new ArrayList<Body>();

		//
		List<CIMPOSBody> cimposBodies = inputObj.getBody();
		for (CIMPOSBody cimposBody : cimposBodies) {
			CIMPOSAttributesVO cimposAttributes = cimposBody.getAttributes();
			ArrayList<ArrayList<Long>> cimposDatapoints = cimposBody.getDatapoints();
			String cimposBodyName = cimposBody.getName();

			// Create DataIngestion Object
			Body body = new Body();
			body.setName(cimposBodyName);

			List<Object> datapoints = new ArrayList<Object>();
			List<Object> assetDatapoint = new ArrayList<Object>();
			assetDatapoint.add(String.valueOf(calendar.getTimeInMillis()));
			assetDatapoint.add(cimposDatapoints.get(0).get(1));
			assetDatapoint.add(cimposDatapoints.get(0).get(2));

			datapoints.add(assetDatapoint);

			body.setDatapoints(datapoints);

			com.ge.predix.entity.util.map.Map map = new com.ge.predix.entity.util.map.Map();
			if (cimposAttributes.getAssetName() != null) {
				map.put("assetname",
						String.valueOf(cimposAttributes.getAssetName()));
			}

			map.put("Zone", String.valueOf(cimposAttributes.getFloor()));

			if(cimposAttributes.getName() != null){
				map.put("name", String.valueOf(cimposAttributes.getName()));
			}



			body.setAttributes(map);
			bodies.add(body);

		}

		dpIngestion.setBody(bodies);

		return dpIngestion;

	}

	
	private CIMPOSBody getRandomCIMPOSDataVO(Long currentTimeMillis, String areaAssetName, int Zone, Constants.Cycles name){

		String bodyName="Zone0"+Zone;
		CIMPOSBody cimposBodyVo = new CIMPOSBody();

		cimposBodyVo.setName(bodyName);

		ArrayList<Long> datapoint = new ArrayList<>();
		datapoint.add(currentTimeMillis);
		datapoint.add(getValues(name));
		datapoint.add(1l);
		ArrayList<ArrayList<Long>> datapoints = new ArrayList<>();
		datapoints.add(datapoint);
		cimposBodyVo.setDatapoints(datapoints);

		CIMPOSAttributesVO cimposAttVO = new CIMPOSAttributesVO();
		cimposAttVO.setAssetName(areaAssetName);
		cimposAttVO.setFloor(Zone);
		cimposAttVO.setName(name);
		


		cimposBodyVo.setAttributes(cimposAttVO);

		return cimposBodyVo;
	}

	
	public Long getValues(Constants.Cycles aqiField) {
	    Long values = new Long(0);
		Random r = new Random();
		int minLimit = 0;
		int maxLimit = 50;
		long time=System.currentTimeMillis();
		int result=0;
		switch (aqiField) {
		
		case Good:
			maxLimit=100;
			minLimit=50;
			break;
		case Bad:
			maxLimit=10;
			minLimit=2;
			break;
		case Avg:
			maxLimit=100;
			minLimit=80;
			break;
		case Std:
			maxLimit=100;
			minLimit=70;
			break;
		case Parts:
			maxLimit=20;
			minLimit=10;
			break;
		//Good,Bad,Avg,Std,Parts
		default:
			break;
		}
		result=r.nextInt(maxLimit-minLimit);
		return (long) result;
	}
	
	private Double pickOneValue(ArrayList<Double> list) {
		int index = new Random().nextInt(list.size());
		return list.get(index);
	}

	/**
	 * @return -
	 */

	List<JSONData> generateMockDataMap_RT() {
		String machineControllerId = this.applicationProperties
				.getMachineControllerId();
		List<JSONData> list = new ArrayList<JSONData>();
		JSONData data = new JSONData();
		data.setName("Compressor-2015:CompressionRatio"); //$NON-NLS-1$
		data.setTimestamp(getCurrentTimestamp());
		data.setValue((generateRandomUsageValue(2.5, 3.0) - 1) * 65535.0 / 9.0);
		data.setDatatype("DOUBLE"); //$NON-NLS-1$
		data.setRegister(""); //$NON-NLS-1$
		data.setUnit(machineControllerId);
		list.add(data);

		data = new JSONData();
		data.setName("Compressor-2015:DischargePressure"); //$NON-NLS-1$
		data.setTimestamp(getCurrentTimestamp());
		data.setValue((generateRandomUsageValue(0.0, 23.0) * 65535.0) / 100.0);
		data.setDatatype("DOUBLE"); //$NON-NLS-1$
		data.setRegister(""); //$NON-NLS-1$
		data.setUnit(machineControllerId);
		list.add(data);

		data = new JSONData();
		data.setName("Compressor-2015:SuctionPressure"); //$NON-NLS-1$
		data.setTimestamp(getCurrentTimestamp());
		data.setValue((generateRandomUsageValue(0.0, 0.21) * 65535.0) / 100.0);
		data.setDatatype("DOUBLE"); //$NON-NLS-1$
		data.setRegister(""); //$NON-NLS-1$
		data.setUnit(machineControllerId);
		list.add(data);

		data = new JSONData();
		data.setName("Compressor-2015:MaximumPressure"); //$NON-NLS-1$
		data.setTimestamp(getCurrentTimestamp());
		data.setValue((generateRandomUsageValue(22.0, 26.0) * 65535.0) / 100.0);
		data.setDatatype("DOUBLE"); //$NON-NLS-1$
		data.setRegister(""); //$NON-NLS-1$
		data.setUnit(machineControllerId);
		list.add(data);

		data = new JSONData();
		data.setName("Compressor-2015:MinimumPressure"); //$NON-NLS-1$
		data.setTimestamp(getCurrentTimestamp());
		data.setValue(0.0);
		data.setDatatype("DOUBLE"); //$NON-NLS-1$
		data.setRegister(""); //$NON-NLS-1$
		data.setUnit(machineControllerId);
		list.add(data);

		data = new JSONData();
		data.setName("Compressor-2015:Temperature"); //$NON-NLS-1$
		data.setTimestamp(getCurrentTimestamp());
		data.setValue((generateRandomUsageValue(65.0, 80.0) * 65535.0) / 200.0);
		data.setDatatype("DOUBLE"); //$NON-NLS-1$
		data.setRegister(""); //$NON-NLS-1$
		data.setUnit(machineControllerId);
		list.add(data);

		data = new JSONData();
		data.setName("Compressor-2015:Velocity"); //$NON-NLS-1$
		data.setTimestamp(getCurrentTimestamp());
		data.setValue((generateRandomUsageValue(0.0, 0.1) * 65535.0) / 0.5);
		data.setDatatype("DOUBLE"); //$NON-NLS-1$
		data.setRegister(""); //$NON-NLS-1$
		data.setUnit(machineControllerId);
		list.add(data);

		return list;
	}

	private Timestamp getCurrentTimestamp() {
		java.util.Date date = new java.util.Date();
		Timestamp ts = new Timestamp(date.getTime());
		return ts;
	}

	private static double generateRandomUsageValue(double low, double high) {
		return low + Math.random() * (high - low);
	}

	private String postData(String content) {
		HttpClient client = null;
		try {
			HttpClientBuilder builder = HttpClientBuilder.create();
			if (this.applicationProperties.getDiServiceProxyHost() != null
					&& !"".equals(this.applicationProperties.getDiServiceProxyHost()) //$NON-NLS-1$
					&& this.applicationProperties.getDiServiceProxyPort() != null
					&& !"".equals(this.applicationProperties.getDiServiceProxyPort())) //$NON-NLS-1$
			{
				HttpHost proxy = new HttpHost(
						this.applicationProperties.getDiServiceProxyHost(),
						Integer.parseInt(this.applicationProperties
								.getDiServiceProxyPort()));
				builder.setProxy(proxy);
			}
			client = builder.build();
			String serviceURL = null;
			if (this.applicationProperties.getPredixDataIngestionURL() == null) {
				serviceURL = "http://" + this.applicationProperties.getDiServiceHost() + ":" + this.applicationProperties.getDiServicePort() + "/saveTimeSeriesData"; //$NON-NLS-1$
				URLEncoder.encode(serviceURL, "UTF-8");
			} else {
				serviceURL = this.applicationProperties
						.getPredixDataIngestionURL() + "/saveTimeSeriesData"; //$NON-NLS-1$
				URLEncoder.encode(serviceURL, "UTF-8");

			}
			log.info("Service URL : " + serviceURL); //$NON-NLS-1$
			log.info("Data : " + content);
			HttpPost request = new HttpPost(serviceURL);
			HttpEntity reqEntity = MultipartEntityBuilder
					.create()
					.addTextBody("content", content) //$NON-NLS-1$
					.addTextBody("destinationId", "TimeSeries").addTextBody("clientId", "TimeSeries") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					.addTextBody(
							"tenantId", this.applicationProperties.getTenantId()).build(); //$NON-NLS-1$
			request.setEntity(reqEntity);
			HttpResponse response = client.execute(request);
			log.debug("Send Data to Ingestion Service : Response Code : " + response.getStatusLine().getStatusCode()); //$NON-NLS-1$
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = ""; //$NON-NLS-1$
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			log.debug("Response : " + result.toString());
			if (result.toString().startsWith("You successfully posted")) { //$NON-NLS-1$
				return "SUCCESS ::::: " + result.toString(); //$NON-NLS-1$
			}
			return "FAILED : " + result.toString(); //$NON-NLS-1$

		} catch (Throwable e) {
			log.error("unable to post data ", e); //$NON-NLS-1$
			return "FAILED : " + e.getLocalizedMessage(); //$NON-NLS-1$
		}
	}


}
