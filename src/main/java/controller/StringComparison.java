package controller;

import org.simmetrics.StringDistance;
import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringDistanceBuilder;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.metrics.EuclideanDistance;
import org.simmetrics.metrics.Levenshtein;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;
import static org.simmetrics.builders.StringMetricBuilder.with;

import com.google.common.base.Function;

public class StringComparison {
	
	public static float euclideanDistance(String str1, String str2) {

		StringDistance metric = StringDistanceBuilder
				.with(new EuclideanDistance<String>())
				.simplify(Simplifiers.replaceNonWord())
				.tokenize(Tokenizers.whitespace()).build();	

		float result = metric.distance (str1, str2); 

		return result;
		
	}
	
	public static float levenshteinDistance(String str1, String str2) {

		StringDistance metric = new Levenshtein();

		return metric.distance(str1, str2); 
	}


	public static float levenshteinMetric(String str1, String str2) {

		StringMetric metric = new Levenshtein();

		return metric.compare(str1, str2); 
	}


	public static float cosineSimilarity(String str1, String str2) {

		Function<String, String> reverse = new Function<String, String>() {

			@Override
			public String apply(String input) {
				return new StringBuilder(input).reverse().toString();
			}

		};
		
		StringMetric metric = 
				with(new CosineSimilarity<String>())
				.simplify(Simplifiers.toLowerCase())
				.simplify(Simplifiers.removeNonWord())
				.simplify(Simplifiers.removeDiacritics())
				.tokenize(Tokenizers.whitespace())
				.tokenize(Tokenizers. qGram(1))
				.transform(reverse)
				.build();

		return metric.compare(str1, str2); 
	}
	
	
}
