package controller;

import org.simmetrics.StringDistance;
import org.simmetrics.builders.StringDistanceBuilder;
import org.simmetrics.metrics.EuclideanDistance;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;
public class StringComparison {
	
	public static float euclideanDistance(String str1, String str2) {

		StringDistance metric = StringDistanceBuilder
				.with(new EuclideanDistance<String>())
				.simplify(Simplifiers.replaceNonWord())
				.tokenize(Tokenizers.whitespace()).build();	

		float result = metric.distance (str1, str2); 

		return result;
	}

}
