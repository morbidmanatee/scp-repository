package com.morbidmanatee.dev.crypto.scp.pricewatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.function.Consumer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/**
 * See https://livecoinwatch.github.io/lcw-api-docs/?java#overviewhistory
 * https://livecoinwatch.github.io/lcw-api-docs/?java#introduction
 * Unirest documentation https://kong.github.io/unirest-java/#requests
 *
 */
public class App 
{
	private static String PROPS_FILE_PATH = "pricewatcher.properties";

	public static String LCW_API_KEY = "change-me";
//	public static String API_PASSWD = " --api 0c14c5b2cac5f21791613c953392489b";
	public static String SCP_API_PASSWD = null;
//	public static int maxPriceSCP = 5000;
//	public static int minPriceSCP = 1000;
	
	public static double STORAGE_PRICE = 2.0;	// $/TB
	public static double CONTRACT_PRICE = 0.5;	// $/contract
	public static double NETWORK_PRICE = 2.0;	// $/TB
	
    public static void main( String[] args )
    {
    	try {
			new App().exec(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
    public void exec( String[] args ) throws Exception
    {
		Properties props = loadProperties();
//		System.out.println("Properties loaded:");
//		System.out.println(props.toString());
		setSystemParameters(props);

    	double rate =  1.0;
    	{
//    		HttpResponse<JsonNode> response = Unirest.post("https://api.livecoinwatch.com/credits")
//      		      .header("accept", "application/json")
//      		      .header("x-api-key", LCW_API_KEY)
//      		      .asJson();
      	
//        	System.out.println(response.getBody().toPrettyString());    		
    	}
//    	{
//    		HttpResponse<String> response = Unirest.post("https://api.livecoinwatch.com/overview")
//      		      .header("content-type", "application/json")
//      		      .header("x-api-key", API_KEY)
//      		      .body("{\n\t\"currency\": \"USD\"\n}")
//      		      .asString();
//      	
//        	System.out.println(response.getBody());    		
//    	}
    	{
    		HttpResponse<JsonNode> response = Unirest.post("https://api.livecoinwatch.com/coins/single")
      		      .header("content-type", "application/json")
      		      .header("x-api-key", LCW_API_KEY)
      		      .body("{\"currency\": \"USD\",\"code\": \"SCP\",\"meta\": false\n}")
      		      .asJson();
      	
//        	System.out.println(response.getBody().toPrettyString());
        	
    		// see https://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
			JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();

        	rate = jsonObject.get("rate").getAsDouble();
    	}

//    	System.out.println("SCP Price: " + rate);
    	int storagePrice_mS = Math.max(1000, Math.min(5000, (int)(1000.0 * STORAGE_PRICE / rate)));
    	int contractPrice_mS =  Math.max(50, Math.min(500, (int)(1000.0 * CONTRACT_PRICE / rate)));
    	int networkPrice_mS = Math.max(1000, Math.min(2000, (int)(1000.0 * NETWORK_PRICE / rate)));
    	
//    	System.out.println("storagePrice_mS: " + storagePrice_mS);
//    	System.out.println("contractPrice_mS: " + contractPrice_mS);
//    	System.out.println("networkPrice_mS: " + networkPrice_mS);
/*    	
./spc host config minstorageprice 3521mS --apipassword 0c14c5b2cac5f21791613c953392489b
./spc host config collateral 3521mS --apipassword 0c14c5b2cac5f21791613c953392489b
./spc host config mincontractprice 352mS  --apipassword 0c14c5b2cac5f21791613c953392489b
./spc host config mindownloadbandwidthprice 1408mS --apipassword 0c14c5b2cac5f21791613c953392489b
./spc host config minuploadbandwidthprice 1408mS --apipassword 0c14c5b2cac5f21791613c953392489b	
*/	
    	
    	
    	systemExec("./spc host config minstorageprice " + storagePrice_mS + "mS" + (SCP_API_PASSWD==null ? "" : " --apipassword " + SCP_API_PASSWD));
    	systemExec("./spc host config collateral " + storagePrice_mS + "mS" + (SCP_API_PASSWD==null ? "" : " --apipassword " + SCP_API_PASSWD));
    	systemExec("./spc host config mincontractprice " + contractPrice_mS + "mS" + (SCP_API_PASSWD==null ? "" : " --apipassword " + SCP_API_PASSWD));
    	systemExec("./spc host config mindownloadbandwidthprice " + networkPrice_mS + "mS" + (SCP_API_PASSWD==null ? "" : " --apipassword " + SCP_API_PASSWD));
    	systemExec("./spc host config minuploadbandwidthprice " + networkPrice_mS + "mS" + (SCP_API_PASSWD==null ? "" : " --apipassword " + SCP_API_PASSWD));
    }
    
    public void systemExec(String commandLine) throws Exception
    {
    	System.out.println(commandLine);
    }
    
    private Properties loadProperties() 
    {
        Properties props = new Properties();
        
        try 
        {
        	File propsFile = new File(PROPS_FILE_PATH);
        	FileInputStream fin = new FileInputStream(propsFile);
        	props.load(fin);
        	fin.close();
        } 
        catch (Exception e) 
        {
        	//e.printStackTrace();
        	// file doesn't exist - make it
            props.setProperty("sessionTimeout", "300");
        	saveProperties(props);
        } 
        finally 
        {
        }
        
        return props;       
    }
    
    private void saveProperties(Properties props) 
    {
        try 
        {            
        	File propsFile = new File(PROPS_FILE_PATH);

        	//Create the file if it doesn't already exist;
            //otherwise, props.store will throw an exception

            propsFile.createNewFile();

            //Write out the list of properties to the file
            FileOutputStream fout = new FileOutputStream(propsFile);
            props.store(fout, "SCP Pricewatcher properties");
            fout.flush();
            fout.close();

        } catch (Exception e) {
            //pass
        }
    }
    
    private static void setSystemParameters(Properties props)
    {
    	String val;
    	
/////////
    	val = props.getProperty("litecoinwatch.apikey");
    	if(val!=null)
    		App.LCW_API_KEY = val;
    	
/////////
    	val = props.getProperty("scp.apipassword");
    	if(val!=null)
    		App.SCP_API_PASSWD = val;
    	
/////////
    	val = props.getProperty("app.storagePrice");
    	if(val!=null)
    		App.STORAGE_PRICE = Double.valueOf(CONTRACT_PRICE);
    	
/////////
    	val = props.getProperty("app.target.contractPrice");
    	if(val!=null)
    		App.CONTRACT_PRICE = Double.valueOf(val);
    	
/////////
    	val = props.getProperty("app.target.networkPrice");
    	if(val!=null)
    		App.NETWORK_PRICE = Double.valueOf(val);
    	
    }
}
