package schedulingproject;

/*The Health Map Parser checks that an incoming Thermal Map is an ArrayList<ArrayList<Boolean>>
with dimensions of exactly 4 * 10 If it is, then it returns true, otherwise it will
return false or an exception may be thrown*/

public class HealthMapParser
{

	public HealthMapParser(){}
	public boolean[][] parseHealthMap(boolean[][] healthMap)
	{
		boolean errorsfound = false;
		do
		{
			errorsfound = false;
			int rows = 0;
			if (healthMap == null)
			{
				//We can not fix this.
				healthMap = makeEmptyMap();
				errorsfound = true;
				return healthMap;
			}
			if (healthMap.length < 4)
			{
				System.out.println("Whoops, Health Map has too few columns specified:");
				System.out.println("Sending it for repair");
				healthMap = expandOuterArray(healthMap); //send it to a helper method for repair
				errorsfound = true;

			}
			if (healthMap.length > 4)
			{
				healthMap = truncateOuterArray(healthMap); //send it to a helper method for repair
				errorsfound = true;
			}
			while (rows < 4)
			{
				if (healthMap[rows] == null)
				{
					healthMap = createInnerArray (healthMap, rows);
					errorsfound = true;
				}
				//boolean temp[] = new boolean[healthMap[rows].length];
				if (healthMap[rows].length != 10)
				{
					healthMap = fixInnerArray(healthMap, rows);
					errorsfound = true;
				}
				//System.out.println(rows);
				rows++;
			}
		}while (errorsfound == true);
		return healthMap;
	}

	private boolean[][] createInnerArray(boolean big[][], int missing)
	{
		System.out.println("Creating inner array, 10 values of true"); //Fake array with 10 CPUs dead
		boolean temp[] = {true, true, true, true, true, true, true, true, true, true};
		big[missing] = temp;
		System.out.println("TM Null array error caught at position: " +missing +" has now been fixed.");
		//New array will have 10 instances of "true"
		return big;
	}

	private boolean[][] fixInnerArray(boolean big[][], int row)// if an inner array is out-of-shape
	{
		if (big[row].length < 10) //If the array is too small
		{
			boolean temp[] = {true, true, true, true, true, true, true, true, true, true}; // declare a repair array
			for (int i=0; i < big[row].length; i++)
			{
				temp[i] = big[row][i]; // copy the existing values from the old array to the repair array
			}
			big[row] = temp; // overwrite the old array with the repair array
			System.out.println("Problem with inner array caught and fixed: System working in degraded mode");
			return (big);
		}
		else // then the array is too large
		{
			boolean temp[] = new boolean[10];
			for (int i = 0; i < temp.length; i++)
			{
				temp[i] = big[row][i]; //put the first 10 values from the old array into the repair array
			}
			big[row] = temp; // overwrite the old array with the repair array
		}
		System.out.println("Problem with inner array caught and fixed");
		return big;
	}

	private boolean[][] truncateOuterArray(boolean big[][]) //If the main array has more than 4 items its too big
	{
		boolean temp[][] = new boolean[4][10];
		for (int i=0; i<temp.length; i++)
		{
			temp[i] = big[i]; // take 4 samples from the old outer array and put them in the repair array
		}
		System.out.println("Problem with outer array (too large) caught and fixed in Parser");
		return temp;
	}

	private boolean[][] expandOuterArray(boolean big[][]) //Call Me if a Health Map has too few Columns, i.e. less than 4.
	{
		boolean temp[][] = new boolean[4][10];
		temp[0] = null;
		temp[1] = null;
		temp[2] = null;
		temp[3] = null;
		for (int i = 0; i < big.length; i++)// populate the repair array with old array data
		{
			temp[i] = big[i];
		}

		for (int i = 0; i < 4; i++)
		{
			if (temp[i] == null)
			{
				System.out.println("Repairing column of Health Map at: " +i);
				createInnerArray(temp, i);
			}
		}
		System.out.println("Health Map has rows: " +temp.length);
		System.out.println("Problem with outer array (too small) caught and fixed");
		return temp;
	}
	private boolean[][] makeEmptyMap()
	{
		boolean hM[][] = new boolean[4][10];
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				hM[i][j] = true; // CPU is unusable.
			}
		}
		return hM;
	}
}