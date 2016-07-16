package main;
import main.Pokemon.Payload;
import main.Pokemon.Request.Builder;

public abstract class Request 
{
	private Builder builder;
	
	
	public abstract int getRpcId();
	public abstract void handleResponse(Payload payload);

	
	public Request()
	{
		builder = Pokemon.Request.newBuilder();
		builder.setType(getRpcId());
	}
	
	public Builder getBuilder()
	{
		return this.builder;
	}
	

	
	public Pokemon.Request getRequest()
	{
		return builder.build();
	}
	
	
	public void handleExtensions()
	{
		
	}
	

}
