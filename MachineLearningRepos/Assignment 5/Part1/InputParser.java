import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InputParser {
	
	private DataPoints[] allPoints;
	BufferedReader reader;
	private int pointCount;
        @SuppressWarnings("empty-statement")
	public InputParser(String filename)
	{
		try {
			reader = new BufferedReader(new FileReader(filename));
			for(pointCount = 0; reader.readLine() != null; pointCount++);
			pointCount--;
			reader.close();
			reader = new BufferedReader(new FileReader(filename));
			allPoints = new DataPoints[pointCount];
			reader.readLine();
			for(int i=0;i<pointCount;i++)
			{
				String line = reader.readLine();
				String[] splitLine = line.split("\t");
				if(splitLine.length == 3)
				{
					int pid = Integer.parseInt(splitLine[0]);
					double px = Double.parseDouble(splitLine[1]);
					double py = Double.parseDouble(splitLine[2]);
					allPoints[i] = new DataPoints(pid,px,py);
				}
			}
			reader.close();
		}
		catch(IOException | NumberFormatException e)
		{
			System.exit(0);
		}
	}
	public DataPoints[] getPoints()
	{
		return this.allPoints;
	}
	public int getPointCount()
	{
		return this.pointCount;
	}
}
