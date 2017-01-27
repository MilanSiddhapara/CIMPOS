package com.ge.predix.solsvc.machinedata.simulator.vo;

import java.util.ArrayList;
import java.util.List;

public class CIMPOSObjectVO {
	
	private Long messageId;
	
	private List<CIMPOSBody> body = new ArrayList<CIMPOSBody>();
	
	

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public List<CIMPOSBody> getBody() {
		return body;
	}

	public void setBody(List<CIMPOSBody> body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "EHSObjectVO [messageId=" + messageId + ", body=" + body + "]";
	}
	

	
}
