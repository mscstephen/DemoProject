package schedulingproject;

class RuleParser
{
	public RuleParser(){}

	public double parseRule(double in)
	{
		if (in == 1){return 0.75;}
		if (in == 2){return 0.70;}
		if (in == 3){return 0.65;}
		if (in == 4){return 0.60;}
		if (in == 5){return 0.55;}

		return 0.50;
	}
}