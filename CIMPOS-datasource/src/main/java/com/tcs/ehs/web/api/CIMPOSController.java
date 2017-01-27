package com.tcs.ehs.web.api;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.predix.entity.timeseries.datapoints.queryresponse.DatapointsResponse;
import com.tcs.ehs.services.TimeseriesRequester;

import com.tcs.ehs.utils.CIMResponseBody;
import com.tcs.ehs.utils.CIMEquipmentBody;
import com.tcs.ehs.utils.CIMPOSParser;
import com.tcs.ehs.utils.Constants;
import com.tcs.ehs.utils.TimeUtils;
import com.tcs.ehs.utils.TimeUtils.Value;

@RestController
@RequestMapping("/api/CIMPOS")
public class CIMPOSController {
	private Logger log = Logger.getLogger(CIMPOSController.class);
	@Autowired
	TimeseriesRequester timeseriesRequester;
	
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private CIMPOSParser CIMPOSParser;
	@Autowired
	private CIMEquipmentBody cimEquipmentBody;
	@RequestMapping(value = "/Factory/{zone}", method = RequestMethod.GET)
	public ResponseEntity<Object> CIMPOSQuery(@RequestHeader("Authorization") String authorization,
			@RequestParam Long interval,@PathVariable String zone) throws JsonProcessingException {
		Value value = TimeUtils.calculateInterval(interval);
		DatapointsResponse datapointsResponse = timeseriesRequester.requestForData(zone,authorization, value.getStartTime(), value.getEndTime());
		log.info("::::::::::: Response:::::::: "+objectMapper.writeValueAsString(datapointsResponse));
		
		
		List<CIMResponseBody> cimposData=null;
		
		cimposData =CIMPOSParser.CimposResponse(datapointsResponse,"all");
	
		if (cimposData!=null)
			return new ResponseEntity<Object>(cimposData, HttpStatus.OK);
		else
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/Factory/{zone}/{equipment}", method = RequestMethod.GET)
	public ResponseEntity<Object> CIMPOSQueryForEquipment(@RequestHeader("Authorization") String authorization,
			@RequestParam Long interval,@PathVariable String zone,@PathVariable String equipment) throws JsonProcessingException {
		Value value = TimeUtils.calculateInterval(interval);
		DatapointsResponse datapointsResponse = timeseriesRequester.requestForData(zone,authorization, value.getStartTime(), value.getEndTime());
		log.info("::::::::::: Response:::::::: "+objectMapper.writeValueAsString(datapointsResponse));
		
		
		//List<CIMResponseBody> cimposData=null;
		
		cimEquipmentBody =CIMPOSParser.CimposResponseForEquipment(datapointsResponse,equipment);
	
		if (cimEquipmentBody!=null)
			return new ResponseEntity<Object>(cimEquipmentBody, HttpStatus.OK);
		else
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	
	
}
