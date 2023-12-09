import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "114"),
			@AocInputMapping(input = "input.txt", solution = "1969958987")
	})
	void part1(Stream<String> input, String solution) {
		var result = input.map(this::readLine).mapToInt(this::predictNextNumber).sum();
		
		
		assertEquals(Integer.parseInt(solution), result);
	}
	
	List<Integer> readLine(String l) {
		return Arrays.stream(l.split(" "))
				.map(Integer::parseInt).toList();
	}
	
	int predictNextNumber(List<Integer> list) {
		if(list.stream().allMatch(k -> k == 0)) {
			return 0;
		}
		var nextLevel = new ArrayList<Integer>();
		for(int i = 0; i < list.size()-1;i++) {
			nextLevel.add(list.get(i+1) - list.get(i));
		}		
		return list.get(list.size()-1) + predictNextNumber(nextLevel);
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "2"),
			@AocInputMapping(input = "input.txt", solution = "1068")
	})
	void part2(Stream<String> input, String solution) {
		var result = input.map(this::readLine).mapToInt(this::predictFirstNumber).sum();
		assertEquals(Integer.parseInt(solution), result);

	}
	
	int predictFirstNumber(List<Integer> list) {
		if(list.stream().allMatch(k -> k == 0)) {
			return 0;
		}
		var nextLevel = new ArrayList<Integer>();
		for(int i = 0; i < list.size()-1;i++) {
			nextLevel.add(list.get(i+1) - list.get(i));
		}		
		return list.get(0) - predictFirstNumber(nextLevel);
	}
}
