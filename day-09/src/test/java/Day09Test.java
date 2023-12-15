import org.junit.jupiter.params.ParameterizedTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test {
	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", expected = "114"),
			@AocInputMapping(input = "input.txt", expected = "1969958987")
			})
	void part1(Stream<String> input, String expected) {
		var result = input.map(this::readLine).mapToInt(this::predictNextNumber).sum();

		assertEquals(Integer.parseInt(expected), result);
	}

	List<Integer> readLine(String l) {
		return Arrays.stream(l.split(" ")).map(Integer::parseInt).toList();
	}

	int predictNextNumber(List<Integer> list) {
		if (list.stream().allMatch(k -> k == 0)) {
			return 0;
		}
		var nextLevel = IntStream.range(0, list.size() - 1)
				.map(i -> list.get(i + 1) - list.get(i))
				.boxed().toList();

		return list.get(list.size() - 1) + predictNextNumber(nextLevel);
	}

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", expected = "2"),
			@AocInputMapping(input = "input.txt", expected = "1068")
			})
	void part2(Stream<String> input, String expected) {
		var result = input.map(this::readLine).mapToInt(this::predictFirstNumber).sum();
		assertEquals(Integer.parseInt(expected), result);

	}

	int predictFirstNumber(List<Integer> list) {
		if (list.stream().allMatch(k -> k == 0)) {
			return 0;
		}

		var nextLevel = IntStream.range(0, list.size() - 1)
				.map(i -> list.get(i + 1) - list.get(i))
				.boxed().toList();

		return list.get(0) - predictFirstNumber(nextLevel);
	}
}
