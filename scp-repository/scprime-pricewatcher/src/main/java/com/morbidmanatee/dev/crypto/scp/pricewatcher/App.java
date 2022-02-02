package com.morbidmanatee.dev.crypto.scp.pricewatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

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
	public static String SCP_API_PASSWD = null;
	public static String SCP_ADDR_STRING = null;
	
	public static int MAX_STORAGE_PRICE_mS = 5000;
	public static int MIN_STORAGE_PRICE_mS = 1000;
	public static int MAX_NETWORK_PRICE_mS = 5000;
	public static int MIN_NETWORK_PRICE_mS = 100;
	public static int MAX_CONTRACT_PRICE_mS = 1000;
	public static int MIN_CONTRACT_PRICE_mS = 1;
	

	
	public static double CURRENT_STORAGE_PRICE_$ = 2.0;	// $/TB
	public static double CURRENT_CONTRACT_PRICE_$ = 0.005;	// $/contract
	public static double CURRENT_NETWORK_PRICE_$ = 1.0;	// $/TB
	
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
    	{
    		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    		Date date = new Date(System.currentTimeMillis());
    		System.out.println("# Generated " + formatter.format(date));   		
    	}
    	System.out.println("#");
    	
    	if(args.length==1)
    	{
    		PROPS_FILE_PATH = args[0];
    	}
    	
		Properties props = loadProperties(PROPS_FILE_PATH);
		if(!PROPS_FILE_PATH.startsWith("/") && !PROPS_FILE_PATH.contains(":\\"))
			System.out.println("# Using " + System.getProperty("user.dir") + "/" + PROPS_FILE_PATH);
		else
			System.out.println("# Using " + PROPS_FILE_PATH);
			
		System.out.println("# Properties loaded: " + props.toString());
		setSystemParameters(props);
    	System.out.println("#");
		

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

			try
			{
	        	rate = jsonObject.get("rate").getAsDouble();				
			}
			catch(Exception e)
			{
				System.out.println("# Please check that the litecoinwatch.apikey property is set to your api-key!");
				System.out.println("# The configured value is [" + LCW_API_KEY + "]");
				System.out.println("# Exiting");
				return;
			}
    	}

    	System.out.println("# SCP Price: " + rate);
    	System.out.println("#");

    	int storagePrice_mS = Math.max(MIN_STORAGE_PRICE_mS, Math.min(MAX_STORAGE_PRICE_mS, (int)(1000.0 * CURRENT_STORAGE_PRICE_$ / rate)));
    	int contractPrice_mS =  Math.max(MIN_CONTRACT_PRICE_mS, Math.min(MAX_CONTRACT_PRICE_mS, (int)(1000.0 * CURRENT_CONTRACT_PRICE_$ / rate)));
    	int networkPrice_mS = Math.max(MIN_NETWORK_PRICE_mS, Math.min(MAX_NETWORK_PRICE_mS, (int)(1000.0 * CURRENT_NETWORK_PRICE_$ / rate)));
    	
//    	System.out.println("storagePrice_mS: " + storagePrice_mS);
//    	System.out.println("contractPrice_mS: " + contractPrice_mS);
//    	System.out.println("networkPrice_mS: " + networkPrice_mS);  	
    	
    	systemExec("./spc host config minstorageprice " + storagePrice_mS + "mS" 
    			+ (SCP_API_PASSWD==null ? "" : " --apipassword " + SCP_API_PASSWD)
    			+ (SCP_ADDR_STRING==null ? "" : " --addr " + SCP_ADDR_STRING)
    			);
    	systemExec("./spc host config collateral " + storagePrice_mS + "mS" 
    			+ (SCP_API_PASSWD==null ? "" : " --apipassword " + SCP_API_PASSWD)
    			+ (SCP_ADDR_STRING==null ? "" : " --addr " + SCP_ADDR_STRING)
    			);
    	systemExec("./spc host config mincontractprice " + contractPrice_mS + "mS" 
    			+ (SCP_API_PASSWD==null ? "" : " --apipassword " + SCP_API_PASSWD)
    			+ (SCP_ADDR_STRING==null ? "" : " --addr " + SCP_ADDR_STRING)
    			);
    	systemExec("./spc host config mindownloadbandwidthprice " + networkPrice_mS + "mS" 
    			+ (SCP_API_PASSWD==null ? "" : " --apipassword " + SCP_API_PASSWD)
    			+ (SCP_ADDR_STRING==null ? "" : " --addr " + SCP_ADDR_STRING)
    			);
    	systemExec("./spc host config minuploadbandwidthprice " + networkPrice_mS + "mS" 
    			+ (SCP_API_PASSWD==null ? "" : " --apipassword " + SCP_API_PASSWD)
    			+ (SCP_ADDR_STRING==null ? "" : " --addr " + SCP_ADDR_STRING)
    			);
    }
    
    public void systemExec(String commandLine) throws Exception
    {
    	System.out.println(commandLine);
    }
    
    private Properties loadProperties(String propertiesPath) 
    {
        Properties props = new Properties();
        
        try 
        {
        	File propsFile = new File(propertiesPath);
        	FileInputStream fin = new FileInputStream(propsFile);
        	props.load(fin);
        	fin.close();
        } 
        catch (Exception e) 
        {
        	//e.printStackTrace();
        	// file doesn't exist - make it
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
    	val = props.getProperty("scp.host.address");
    	if(val!=null)
    		App.SCP_ADDR_STRING = val;
    	
/////////
    	val = props.getProperty("app.target.storagePrice");
    	if(val!=null)
    		App.CURRENT_STORAGE_PRICE_$ = Double.valueOf(val);
    	
/////////
    	val = props.getProperty("app.target.contractPrice");
    	if(val!=null)
    		App.CURRENT_CONTRACT_PRICE_$ = Double.valueOf(val);
    	
/////////
    	val = props.getProperty("app.target.networkPrice");
    	if(val!=null)
    		App.CURRENT_NETWORK_PRICE_$ = Double.valueOf(val);
    	
///////// incentive limits
    	val = props.getProperty("app.max.networkPrice");
    	if(val!=null)
    		App.MAX_NETWORK_PRICE_mS = Integer.valueOf(val);
    	val = props.getProperty("app.min.networkPrice");
    	if(val!=null)
    		App.MIN_NETWORK_PRICE_mS = Integer.valueOf(val);
    	
    	val = props.getProperty("app.max.storagePrice");
    	if(val!=null)
    		App.MAX_STORAGE_PRICE_mS = Integer.valueOf(val);
    	val = props.getProperty("app.min.storagePrice");
    	if(val!=null)
    		App.MIN_STORAGE_PRICE_mS = Integer.valueOf(val);
    	
    	val = props.getProperty("app.max.contractPrice");
    	if(val!=null)
    		App.MAX_CONTRACT_PRICE_mS = Integer.valueOf(val);
    	val = props.getProperty("app.min.contractPrice");
    	if(val!=null)
    		App.MIN_CONTRACT_PRICE_mS = Integer.valueOf(val);

    	
    }
    
}
