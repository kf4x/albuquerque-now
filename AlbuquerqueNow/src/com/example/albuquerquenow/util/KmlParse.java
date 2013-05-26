package com.example.albuquerquenow.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KmlParse {

    private JSONObject json = new JSONObject();
    private Scanner in;
    private URL KMZUrl;
    private ZipInputStream zis;
    private Pattern pattern = Pattern.compile("-?\\d+\\.\\d+");

	/**
	 * 
	 * @param url is path th kml file 
	 * @return	JSON object will be returned.
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws JSONException
	 */
    public JSONObject KMLFromURL(URL url) throws IOException, NumberFormatException, JSONException {
        
        in = new Scanner(url.openStream());
        parse();
        in.close();
        return json;
    }
	/**
	 * 
	 * @param f is path to a local file 
	 * @return JSON object will be returned
	 * @throws FileNotFoundException
	 * @throws NumberFormatException
	 * @throws JSONException
	 */
    public JSONObject KMLFromFile(File f) throws FileNotFoundException, NumberFormatException, JSONException{
        
        in = new Scanner(f);
        parse();
        return json;
    }
    /**
     * 
     * @param is USES InputStream useful in some situations when working with android.
     * @return JSON object will be returned
     * @throws FileNotFoundException
     * @throws NumberFormatException
     * @throws JSONException
     */
    public JSONObject KMLFromFile(InputStream is) throws FileNotFoundException, NumberFormatException, JSONException{
        
        in = new Scanner(is);
        parse();
        return json;
    } 
    /**
     * 
     * @param f 
     * @param outputFolder
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NumberFormatException
     * @throws JSONException
     */
    public JSONObject KMZFromFile(File f, String outputFolder) throws FileNotFoundException, IOException, NumberFormatException, JSONException{
    	
    	zis = new ZipInputStream(new FileInputStream(f));
        return KMLFromFile(unzip(outputFolder));
    }
    
    public JSONObject KMZFromURL(URL url, String tmpFolder) throws IOException, NumberFormatException, JSONException {
        
    	KMZUrl = url;
        zis = new ZipInputStream(KMZUrl.openStream());
        return KMLFromFile(unzip(tmpFolder));
    }
    
    
 private void parse() throws NumberFormatException, JSONException{

        String /*str = "",*/ line, heading="";
        //int markerNumber = 0;

        JSONArray markerArray = new JSONArray();

        while (in.hasNext()){
            line = in.nextLine();
            if (line.contains("<Placemark")) {
                JSONObject mObject = new JSONObject();
                JSONArray geometryArray = new JSONArray();
                //markerNumber++;
                while(in.hasNext()){

                    if (line.contains("</Placemark>")) {
                        //System.out.print("/palcemark");
                        break;
                    }
                    else if(line.contains("<name>")){
                        int start = line.indexOf(">");
                        int end = line.indexOf("<", start);
                      //  System.out.println(line);
                      //  System.out.println(start);
                        //System.out.println(end);
                        try {
                            mObject.put("name", line.subSequence(start+1, end).toString());       //throwing error                    

						} catch (Exception e) {
							// TODO: handle exception
						}
                    }else if (line.contains("<heading>")){
                    	int start = line.indexOf(">",0);
                    	int end = line.lastIndexOf("<");
                    	heading = line.subSequence(start+1, end).toString();
//                    	System.out.println(heading);
                    	mObject.put("heading", Double.parseDouble(heading));
                    }

                    else if(line.contains("<Point>")){
                        JSONObject geoObject = new JSONObject();
                        String lat = "", lng = "", elev = "0";
                        while(in.hasNext()){

                            //coordinates += line;
                            if (line.contains("<coordinates>")) {
                                int start = line.indexOf(">",0);
                                int end = line.indexOf(",", start);
                                lng = line.subSequence(start+1, end).toString();
                                int endLng = line.indexOf("<",end);
                                String lngAndElv = line.subSequence(end+1, endLng).toString();
                                if (lngAndElv.contains(",")) {
                                    int comma = lngAndElv.indexOf(",");
                                    lat = lngAndElv.subSequence(0, comma).toString();
                                    elev = lngAndElv.substring(comma+1);

                                }
                                else{
                                    lat = lngAndElv;
                                }
                                //System.out.print(lngAndElv + "\n");
                            }
// CHECK HERE               //this may error
                            else if(line.contains("</Point>")){
                                break;
                            } 
                            line = in.nextLine();
                        }

                       JSONArray geoArray = new JSONArray();
                       JSONObject loc = new JSONObject();

                       // 

                        //

                        loc.put("lng", Double.parseDouble(lng));
                        loc.put("lat", Double.parseDouble(lat));
                        
                        geoArray.put(loc);
                        geoObject.put("coordinates", geoArray);
                        geoObject.put("elevation", Double.parseDouble(elev));
                        geoObject.put("type", "point");
                        geometryArray.put(geoObject);
                        mObject.put("geometry", geometryArray);    
                    }
                    else if (line.contains("<LineString")){
                        JSONObject geoObject = new JSONObject();
                        String coordinates = "";
                        List<String> latList = new ArrayList<String>();
                        List<String> lngList = new ArrayList<String>();
                        List<String> llList = new ArrayList<String>();
                        while(in.hasNext()){

                        	if(line.contains("<coordinates>") && line.contains(",") ){
                        		String s="";
                        		while(in.hasNext()){                                    
//                                    line = in.nextLine();
                                    line = line.trim();
                                    if(line.contains("</coordinates>")){
                                    	s += line;
                                    	Matcher matcher = pattern.matcher(s);
                                    	while (matcher.find()) {
                                    		//System.out.print("Start index: " + matcher.start());
                                    		//System.out.print(" End index: " + matcher.end() + " ");
                                    		//System.out.println(matcher.group());
                                    		llList.add(matcher.group());

												
											
                                    	}
                                		Iterator<String> i = llList.iterator();
                                		while (i.hasNext()) {
											String il = (String) i.next();
											String ia = (String) i.next();
											i.next();
											System.out.println(il + " " + ia);
											lngList.add(il);
											latList.add(ia);
											
										}
                                        break;
                                    }
                                    
                                    
                                    s += line;
                                    line = in.nextLine();
                                }
                        	}
                        	else if (line.contains("<coordinates>")) {                                
                                while(in.hasNext()){                                    
                                    line = in.nextLine();
                                    line = line.trim();
                                    if(line.contains("</coordinates>")){
                                        break;
                                    }
                                    

                                    /*
                                     * check this line for comma then add to array
                                     */
                                    
                                    coordinates = line;

                                    String[] latlngArray = coordinates.split(",");

                                    
                                    try {
                                    	latList.add(latlngArray[1]); //error
                                    	//if (coordinates.substring(comma+1).contains(",")) {
                                    	//	int commaTwo = coordinates.substring(comma+1).indexOf(",");
                                    		lngList.add(latlngArray[0]);
										//} else{
											//lngList.add(coordinates.substring(comma+1));
										//}
                                        
                                       // System.out.println("------------"+coordinates.substring(0, comma));
                                       // System.out.println(coordinates.substring(comma+1));
									} catch (Exception e) {
										// TODO: handle exception
										//System.out.println("Error");
										//System.out.println("Coordinates: " + coordinates);
										//System.out.println("comma: " + comma);
									}
                                    
                                    
                                    
                                }
                            }
                            if(line.contains("</LineString>")){
                                break;
                            }
                            line = in.nextLine();
                        }
                        
                        JSONArray geoArray = new JSONArray();
                        int i = 0;
                        while(true) {
                            JSONObject loc = new JSONObject();
                            loc.put("lat", Double.parseDouble(latList.get(i))); //these are switched
                            loc.put("lng", Double.parseDouble(lngList.get(i)));
                            
                            i++;
                            geoArray.put(loc);
                            
                            if(i == latList.size()-1){
                                break;
                            }
                        }

                        
                        geoObject.put("coordinates", geoArray);
                        
                        geoObject.put("type", "linestring");
                        geometryArray.put(geoObject);
                        mObject.put("geometry", geometryArray);   
                        

                       
                        
                    }
                    else if (line.contains("<description>")){
                        String desc = "";
                        while(in.hasNext()){

                            desc += line;
                            if (line.contains("</description>")) {
                                break;
                            }
                            line = in.nextLine();
                        }
                        desc = desc.replaceAll("<description>", "");
                        desc = desc.replaceAll("</description>", "");
                        mObject.put("description", desc);
                    }
                    
                    try {
                    	line = in.nextLine();
					} catch (Exception e) {
						// TODO: handle exception
					}
                    
                    
                   // marker.put("description ", "");
                   // marker.put("point", "");

                }
                heading = "";
                markerArray.put(mObject);

            }
            json.put("placemarks", markerArray);

            //if (line.contains("</Placemark>")){
            //    continue;
           // }

        }

        in.close();
        
        //System.out.print(markerNumber);
        //System.out.print(json.toString());



    }
    

	protected File unzip(String outputFolder) throws FileNotFoundException, IOException{
		
        byte[] buffer = new byte[1024];
        ZipEntry ze = zis.getNextEntry();
        File newFile = null;
        
        
        while(ze!=null){

           String fileName = ze.getName();
           
           if(outputFolder == null || outputFolder.isEmpty()){
               newFile = new File(fileName);
           }
           else{
               newFile = new File(outputFolder + File.separator + fileName);
               new File(newFile.getParent()).mkdirs();
           }

            FileOutputStream fos = new FileOutputStream(newFile);             

            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();   
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        System.out.println("Done");        
        
        return newFile;
    }

}
