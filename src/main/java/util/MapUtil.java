package util;

import java.util.LinkedHashMap;
import java.util.Map;

import model.MockDrug;

public class MapUtil {

	public static Map<MockDrug,Double> sortByValue(Map<MockDrug,Double> map){
		Map<MockDrug, Double> sortedMap = new LinkedHashMap<>();
		map.entrySet().stream().sorted(Map.Entry.<MockDrug,Double> comparingByValue().reversed())
			.forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));
		return sortedMap;
	}
}