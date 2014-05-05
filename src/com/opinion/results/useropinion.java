package com.opinion.results;
import java.io.IOException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
@Path("opinion")
public class useropinion {
	@GET
	@Path("/at/{param}")
	@Produces(MediaType.TEXT_HTML)
	public String answerType(@PathParam("param") String question) throws IOException, JSONException 
	{
		ClassifyQuestion cq=new ClassifyQuestion();
		//ClassAResults car=new ClassAResults();
		System.out.print("calling returnans"+question);
		String answer=cq.returnAnswer(question);
		//String answer=car.classA(question);
		return  answer;
	}
}
