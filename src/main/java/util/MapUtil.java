package util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.MockDrug;

public class MapUtil {
    @SuppressWarnings("hiding")
	public static <MockDrug, Double extends Comparable<? super Double>> Map<MockDrug, Double> sortByValue(Map<MockDrug, Double> map) {
        List<Entry<MockDrug, Double>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<MockDrug, Double> result = new LinkedHashMap<>();
        for (Entry<MockDrug, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}