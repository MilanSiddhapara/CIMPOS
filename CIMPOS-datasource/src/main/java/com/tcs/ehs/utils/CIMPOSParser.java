package com.tcs.ehs.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.ge.predix.entity.timeseries.datapoints.queryresponse.DatapointsResponse;
import com.ge.predix.entity.timeseries.datapoints.queryresponse.Results;
import com.ge.predix.entity.timeseries.datapoints.queryresponse.Tag;
import com.ge.predix.entity.util.map.Map;
@Component
public class CIMPOSParser {

	public List<CIMResponseBody> CimposResponse(DatapointsResponse datapointsResponse ,String status)
	{
		
		Tag tag = datapointsResponse.getTags().get(0);
		List<Results> results = tag.getResults();
		int j=0;
		Random r=new Random();
		System.out.println("::::::::::::::::::::"+results.size()+":::::::::::::::::::");
		List<CIMResponseBody> responseBodies=new ArrayList<CIMResponseBody>();
		for (int i = 0; i < results.size(); i=i+5)
		{
			System.out.println("::::::::::::::::::::hiiiiiii:::::::::::::::::::");
			CIMResponseBody cimResponseBody =new CIMResponseBody();
			
			Map attributes = results.get(i).getAttributes();
			if (attributes.size() > 0)
			{
				long time;
				int value1, value2;
				String assetNameStirng = null;
				String assetNameStirng2 = null;
				if (null != (List<String>) attributes.get("assetname")) {

					assetNameStirng = ((List<String>) attributes.get("assetname")).get(0);
					cimResponseBody.setEquipment(assetNameStirng);

				

				}
			
			}
	
			List<Object> values = results.get(j).getValues();
			j++;
			List<Object> values1 = results.get(j).getValues();
			j++;
			List<Object> values2 = results.get(j).getValues();
			j++;
			List<Object> values3 = results.get(j).getValues();
			j++;
			List<Object> values4 = results.get(j).getValues();
			j++;
		ArrayList<CIMBody> cimBodies=new ArrayList<CIMBody>();
		
			for(int k=0;k<values.size();k++)
			{
				CIMBody cimBody=new CIMBody();
				
				 System.out.println("------------------helloooo-----");
				ArrayList<Integer> arr= (ArrayList<Integer>) values.get(k);
				cimBody.setAvgValue(arr.get(1));
				ArrayList<Integer> arr1= (ArrayList<Integer>) values1.get(k);
				cimBody.setGoodValue(arr1.get(1));
				ArrayList<Integer> arr2= (ArrayList<Integer>) values2.get(k);
				cimBody.setBadValue(arr2.get(1));
				ArrayList<Integer> arr3= (ArrayList<Integer>) values3.get(k);
				cimBody.setParts(arr3.get(1));
				ArrayList<Integer> arr4= (ArrayList<Integer>) values4.get(k);
				cimBody.setStd(arr4.get(1));
				ArrayList<Long> arr5= (ArrayList<Long>) values4.get(k);
				cimBody.setTimestamp(arr5.get(0));
				int random=r.nextInt(7);
				if(random==0)
				{
					cimBody.setStatus("Blocked");
				}else if(random==1)
				{
					cimBody.setStatus("Manual");
				}else if(random==2)
				{
					cimBody.setStatus("Straved");
				}else if(random==3)
				{
					cimBody.setStatus("Break");
				}else if(random==4)
				{
					cimBody.setStatus("Fualt/E-Stop");
				}else if(random==5)
				{
					cimBody.setStatus("Cycling");
				}else if(random==6)
				{
					cimBody.setStatus("Auto");
				}
				else
				{
					
				}
				
				cimBodies.add(cimBody);
				//cimResponseBody.setCimBody(cimBody);
				System.out.println("cimBodies :::::: "+cimBodies);
				
				
			}
			
			cimResponseBody.setCimBodies(cimBodies);
			responseBodies.add(cimResponseBody);
		}
		
		return responseBodies;
	}
	
	public CIMEquipmentBody CimposResponseForEquipment(DatapointsResponse datapointsResponse ,String equipmentname)
	{
		
		Tag tag = datapointsResponse.getTags().get(0);
		List<Results> results = tag.getResults();
		Random r=new Random();
		int j=0;
		String assetNameStirng = null;
		String assetNameStirng1 = null;
		System.out.println("::::::::::::::::::::"+results.size()+":::::::::::::::::::");
		//List<CIMResponseBody> responseBodies=new ArrayList<CIMResponseBody>();
		List<CIMBody> cimBodies=new ArrayList<CIMBody>();
		CIMEquipmentBody cimEquipmentBody =new CIMEquipmentBody();
		for (int i = 0; i < results.size(); i=i+5)
		{
			//CIMEquipmentBody cimEquipmentBody =new CIMEquipmentBody();
			
			Map attributes = results.get(i).getAttributes();
			if (attributes.size() > 0)
			{
				long time;
				int value1, value2;
				
				
				if (null != (List<String>) attributes.get("assetname")) {

					assetNameStirng = ((List<String>) attributes.get("assetname")).get(0);
					

				

				}
			
			}
	
			List<Object> values = results.get(j).getValues();
			j++;
			List<Object> values1 = results.get(j).getValues();
			j++;
			List<Object> values2 = results.get(j).getValues();
			j++;
			List<Object> values3 = results.get(j).getValues();
			j++;
			List<Object> values4 = results.get(j).getValues();
			j++;
			
			for(int k=0;k<values.size();k++)
			{
				CIMBody cimBody=new CIMBody();
				System.out.println("---------=== "+values.size());
				//cimEquipmentBody.setEquipment(assetNameStirng);
			Boolean flag=equipmentname.equalsIgnoreCase(assetNameStirng);
			if(flag==false)
			{
				System.out.println("------------ "+flag);
				break;
			}
			
			assetNameStirng1=assetNameStirng;
			
				ArrayList<Integer> arr= (ArrayList<Integer>) values.get(k);
				cimBody.setAvgValue(arr.get(1));
				System.out.println("Value ::::::::: "+arr.get(1));
				ArrayList<Integer> arr1= (ArrayList<Integer>) values1.get(k);
				cimBody.setGoodValue(arr1.get(1));
				ArrayList<Integer> arr2= (ArrayList<Integer>) values2.get(k);
				cimBody.setBadValue(arr2.get(1));
				ArrayList<Integer> arr3= (ArrayList<Integer>) values3.get(k);
				cimBody.setParts(arr3.get(1));
				ArrayList<Integer> arr4= (ArrayList<Integer>) values4.get(k);
				cimBody.setStd(arr4.get(1));
				ArrayList<Long> arr5= (ArrayList<Long>) values4.get(k);
				cimBody.setTimestamp(arr5.get(0));
				
				
				int random=r.nextInt(7);
				if(random==0)
				{
					cimBody.setStatus("Blocked");
				}else if(random==1)
				{
					cimBody.setStatus("Manual");
				}else if(random==2)
				{
					cimBody.setStatus("Straved");
				}else if(random==3)
				{
					cimBody.setStatus("Break");
				}else if(random==4)
				{
					cimBody.setStatus("Fualt/E-Stop");
				}else if(random==5)
				{
					cimBody.setStatus("Cycling");
				}else if(random==6)
				{
					cimBody.setStatus("Auto");
				}
				
				System.out.println("----------- "+cimBody.getAvgValue());
				System.out.println("----------- "+cimBody.getBadValue());
				System.out.println("----------- "+cimBody.getStd());
				
				cimBodies.add(cimBody);
				System.out.println("------------- "+cimBodies);
			}
			

				
		}
		cimEquipmentBody.setEquipment(assetNameStirng1);
		cimEquipmentBody.setCimBodies(cimBodies);
		return cimEquipmentBody;
	}
}
