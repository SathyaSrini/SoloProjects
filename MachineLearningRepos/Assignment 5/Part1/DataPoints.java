
public class DataPoints {
	
	private int id;
	private double x,y;
	public DataPoints()
	{
		
	}
	
	public DataPoints(int id,double x,double y)
	{
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
	public double getX()
	{
		return this.x;
	}
	public double getY()
	{
		return this.y;
	}
	public int getId()
	{
		return this.id;
	}
}
