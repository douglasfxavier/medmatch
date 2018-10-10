package util;

public class Functions {
	
	private static int RecursiveSum(int n) {
		if (n == 1)
			return 1;
		else
			return n + RecursiveSum(n-1);
	}
	
	public static double weightByPosition(int totalNumber, int position) {
		double weight = 100.00 / RecursiveSum(totalNumber) * position;
		return weight;
	}
}
