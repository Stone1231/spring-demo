package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebServiceQueue {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("queueId")
	private long queueId;
	@JsonProperty("requestId")
	private long requestId;
	@JsonProperty("logDate")
	private long logDate;
	@JsonProperty("status")
	private String status;
	@JsonProperty("headers")
	private Object headers;
	@JsonProperty("request")
	private Object request;
	@JsonProperty("response")
	private Object response;
	@JsonProperty("serviceType")
	private String serviceType;
	@JsonProperty("api")
	private String api;
	@JsonProperty("tryCount")
	private int tryCount;
	@JsonProperty("tryDate")
	private long tryDate;
	@JsonProperty("isSync")
	private int isSync;
	@JsonProperty("callback")
	private String callback;
	
	public WebServiceQueue() {
		
	}
	
	public WebServiceQueue(String status) {
		this.status = status;
	}
	
	public long getQueueId() {
		return queueId;
	}
	public void setQueueId(long queueId) {
		this.queueId = queueId;
	}
	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	public long getLogDate() {
		return logDate;
	}
	public void setLogDate(long logDate) {
		this.logDate = logDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getHeaders() {
		return headers;
	}
	public void setHeaders(Object headers) {
		this.headers = headers;
	}
	public Object getRequest() {
		return request;
	}
	public void setRequest(Object request) {
		this.request = request;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public int getTryCount() {
		return tryCount;
	}
	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}
	public long getTryDate() {
		return tryDate;
	}
	public void setTryDate(long tryDate) {
		this.tryDate = tryDate;
	}
	public int getIsSync() {
		return isSync;
	}
	public void setIsSync(int isSync) {
		this.isSync = isSync;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	
	@Override
	public Object clone() { 
		WebServiceQueue copy = null;
		try {
			copy = (WebServiceQueue) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return copy;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("api", api);
		builder.append("callback", callback);
		builder.append("headers", headers);
		builder.append("isSync", isSync);
		builder.append("logDate", logDate);
		builder.append("queueId", queueId);
		builder.append("request", request);
		builder.append("requestId", requestId);
		builder.append("response", response);
		builder.append("serviceType", serviceType);
		builder.append("status", status);
		builder.append("tryCount", tryCount);
		builder.append("tryDate", tryDate);
		return builder.toString();
	}

	public boolean equals(Object object) {
		if (!(object instanceof WebServiceQueue)) {
			return false;
		}
		if (this == object) {
			return true;
		}
		WebServiceQueue other = (WebServiceQueue) object;
		return new EqualsBuilder()
		.append(api, other.api)
		.append(callback, other.callback)
		.append(headers, other.headers)
		.append(isSync, other.isSync)
		.append(logDate, other.logDate)
		.append(queueId, other.queueId)
		.append(request, other.request)
		.append(requestId, other.requestId)
		.append(response, other.response)
		.append(serviceType, other.serviceType)
		.append(status, other.status)
		.append(tryCount, other.tryCount)
		.append(tryDate, other.tryDate)
		.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
		.append(api)
		.append(callback)
		.append(headers)
		.append(isSync)
		.append(logDate)
		.append(queueId)
		.append(request)
		.append(requestId)
		.append(response)
		.append(serviceType)
		.append(status)
		.append(tryCount)
		.append(tryDate)
		.toHashCode();
	}
}
