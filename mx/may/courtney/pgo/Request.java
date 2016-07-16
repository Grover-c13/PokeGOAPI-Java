package mx.may.courtney.pgo;

import mx.may.courtney.pgo.Pgo.Payload;
import mx.may.courtney.pgo.Pgo.Request.Builder;

public abstract class Request 
{
	private Builder builder;
	
	
	public abstract int getRpcId();
	public abstract void handleResponse(Payload payload);

	
	public Request()
	{
		builder = Pgo.Request.newBuilder();
		builder.setType(getRpcId());
	}
	
	public Builder getBuilder()
	{
		return this.builder;
	}
	

	
	public mx.may.courtney.pgo.Pgo.Request getRequest()
	{
		return builder.build();
	}
	
	
	public void handleExtensions()
	{
		
	}
	

}
