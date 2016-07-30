import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.*;

public class InputParser {

	HashMap<Long,String> tweetDataList = new HashMap<>();
	
	String localFileName;

	public InputParser(String inputFileName)
	{
		this.localFileName = inputFileName;
	}
	
        
        public ArrayList<Long> parseInitialCentroids(String filename)
	{
		ArrayList<Long> result = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String inputLine = br.readLine();
			String[] initialCentroidValue;
                        
                        int i=0;
			while(inputLine != null)
			{
				initialCentroidValue = inputLine.split(",");
				result.add(Long.parseLong(initialCentroidValue[0]));
				inputLine = br.readLine();
			}			
		}	catch(IOException | NumberFormatException e)
		{
		}
		return result;
	}
        
        ///////////////////////////// HELPER METHODS //////////////////////////////////////////////
	public void createJsonObjects()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(localFileName));
			String line = br.readLine();
			while(line != null)
			{
				this.deSerialize(line);
				line = br.readLine();
			}
		}	catch(IOException | JSONException e)
		{
		}
	}
	
	public void deSerialize(String inputString) throws JSONException
	{
                String tweetData = "";
                long extractedId;
		JSONObject ObjValue;
                
                ObjValue = new JSONObject(inputString);
		tweetData = ObjValue.getString("text");
		extractedId = ObjValue.getLong("id");
		
                this.tweetDataList.put(extractedId, tweetData);
	}
	
        ///////////////////////////// HELPER METHODS //////////////////////////////////////////////
        
}
