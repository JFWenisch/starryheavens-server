package severeLobster.backend.spiel;

public class OnlineHighscoreComparable implements Comparable<OnlineHighscoreComparable> 
{

	public int iScore;
	public String strUserName;
	public OnlineHighscoreComparable(String strUserName, int iScore)
	{
		this.iScore=iScore;
		this.strUserName=strUserName;
	}
	@Override
	public int compareTo(OnlineHighscoreComparable o) 
	{
		if( ((OnlineHighscoreComparable)o).iScore > iScore) // Das Objekt muss einem Casting unterlaufen,
		{	// damit o auf die Instanzvariablen der Klasse "InterfaceProgram" zugreifen kann.

		return 1;
		}
		else if(((OnlineHighscoreComparable)o).iScore < iScore)
		{

		return -1;
		}
		else
		{
	
		return 0;
		}
	}

}
