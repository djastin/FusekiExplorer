package discover;
import java.util.UUID;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;

public class FusekiTest 
{
	private static final String UPDATE_TEMPLATE = 
            "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
            + "INSERT DATA"
            + "{ <http://example/%s>    dc:title    \"A new book\" ;"
            + "                         dc:creator  \"A.N.Other\" ." + "}   ";
	
	public static void main(String args[])
	{	
		String id = UUID.randomUUID().toString();
		System.out.println(String.format("Adding %s", id));
		UpdateProcessor upp = UpdateExecutionFactory.createRemote(
				UpdateFactory.create(String.format(UPDATE_TEMPLATE, id))
				, "http://localhost:3030/ds/update");
		
		upp.execute();
		
		QueryExecution qe = QueryExecutionFactory.sparqlService(
				"http://localhost:3030/ds/query", "SELECT * WHERE {?x ?r ?y}");
		ResultSet results = qe.execSelect();
		ResultSetFormatter.out(System.out, results);
		qe.close();
	}

}
