package schedulingproject;

/*The Thermal Map Parser checks that an incoming Thermal Map is a 2D array of double

It will also check to ensure that there are no crazy temperatures, e.g. negative values
as we assume they are impossible. If one is found, it is increased to 110 to prevent the
schedule maker from using it (no schedule maker will assign a job to a very hot CPU)
There will also be methods to repair out-of-shape arrays.
*/

public class ThermalMapParser
{
	public ThermalMapParser(){}

	public double[][] parseThermalMap(double thermalMap[][])
	{
		boolean errorsfound = false;
		do
		{
			errorsfound = false;
			int rows = 0;
			if (thermalMap == null)
			{
				//if the module takes a null thermalMap
				//make an array of dead-looking CPUs
				thermalMap = makeEmptyTM();
				return thermalMap;  //we can do nothing else
			}

			if (thermalMap.length < 4)
			{
				thermalMap = expandOuter(thermalMap);
				errorsfound = true;
			}
			if (thermalMap.length > 4)
			{
				thermalMap = truncateOuter(thermalMap);
				errorsfound = true;
			}


			if (thermalMap.length != 4)
			{
				System.out.println("Major Parser error, array length repair system fault");
				System.exit(1);
			}
			while (rows < 4)
			{
				if (thermalMap[rows] == null) //if any inner array is null, a new "don't use me" array will be needed.
				{
					double t1[] = {110, 110, 110, 110, 110, 110, 110, 110, 110, 110};
					thermalMap[rows] = t1;
					System.out.println("sub-array" +rows +"of the Thermal Map was found to be non-existent");
					System.out.println("it has been created and filled with default values");
					errorsfound = true;
				}

				if (thermalMap[rows].length < 10)
				{
					System.out.println("Thermal Map inner array too small at: " +rows);
					System.out.println("had only the following no of elements: " + thermalMap[rows].length);
					/* if an inner array is less than 10 values long
					 * it needs to be lengthened with "do not use me" values
					 */
					double t1[] = {110, 110, 110, 110, 110, 110, 110, 110, 110, 110}; // initialise the repair array
					for (int fi = 0; fi < thermalMap[rows].length; fi++)
					{
						t1[fi] = thermalMap[rows][fi]; // add the "good" values from the old array into the repair array
					}
					thermalMap[rows] = t1; // overwrite the damaged array with the repair array.
					errorsfound = true;
				}
				if (thermalMap[rows].length > 10)
				{
					/* if an inner array is more than 10 values long
					 * parser needs to truncate it
					 */
					System.out.println("Thermal Map inner array too large at: " +rows);
					double t1[] = new double[10];
					for (int fi = 0; fi < 10; fi++)//for the first 10 elements in the old array
					{
						t1[fi] = thermalMap[rows][fi];//add the "good" values from the old array into the repair array
						errorsfound = true;
					}
					thermalMap[rows] = t1; // overwrite the damaged array with the repair array.
				}

				/* Temperature checker code
				 * makes sure all values make sense
				 * any CPU temperature value below Freezing is considered erroneous
				 * must be set to a high temperature so it is not used.
				 */
				int i = 0;
				while (i < 10)
				{
					double value;
					value = thermalMap[rows][i];
					if (value < 5)
					{
						System.out.print("Found erroneous value at: " +rows +", " +i);
						System.out.print(" currently:");
						System.out.println(value);
						thermalMap[rows][i] = 110;
						System.out.println("Corrected it to 110");
						System.out.println("Scheduler will not use this CPU");
						errorsfound = true;
					}
					i++;
				}
				/*End of temperature check code */
				rows++; //increment rows for the while loop.
			}

		} while (errorsfound == true);
		double tm[][] = detectKelvin(thermalMap);
		return tm;
	}

	private double[][] truncateOuter(double big[][])
	{
		double temp[][] = new double[4][10];

		for (int i = 0; i < 4; i++)
		{
			temp[i] = big[i];
		}
		big = temp;
		System.out.println("Outer array too large error caught on Thermal Map, has been repaired");
		return temp;
	}

	private double[][] expandOuter(double big[][])
	{
		System.out.println("Outer array too short: " +big.length);
		double nThermalMap[][] = new double[4][10];
		for (int i = 0; i<nThermalMap.length; i++)
		{
			if (i < big.length)
			{
				nThermalMap[i] = big[i];
			}
			else
			{
				double temp[] = {110, 110, 110, 110, 110, 110, 110, 110, 110, 110};
				nThermalMap[i] = temp;
				System.out.println("Inner array had to be created at position: " +i);
			}
		}// After nThermalMap has been built as a repair 2D array
		return nThermalMap; //send back the repair array
	}
	private double[][] makeEmptyTM()
	{
		System.out.println("WTF? The parser received a null Thermal Map ...");
		System.out.println("Creating an array of 110 values ...");
		double repair[][] = new double[4][10];
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				repair[i][j] = 110;
			}
		}
		return repair;
	}

	/*Now the module provides Kelvin detection, i.e. if I think the Thermal module has passed
	  a Thermal Map that contains Kelvin values.*/
	private double[][] detectKelvin(double map[][])
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				if (map[i][j] > 278.14)
				{
					double t = map[i][j];
					//We've found what looks like a Kelvin value
					//278.15K = 5C, scale it back to Centigrade
					map[i][j] = t - 273.15;
				}
			}
		}
		return map;
	}
}

