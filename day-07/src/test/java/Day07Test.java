import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "-1"),
			@AocInputMapping(input = "input.txt", solution = "-1")
	})
	void part1(Stream<String> input, String solution) {
		assertEquals(solution, Long.toString(input.count()));
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "-1"),
			@AocInputMapping(input = "input.txt", solution = "-1")
	})
	void part2(Stream<String> input, String solution) {
		assertEquals(solution, Long.toString(input.count()));
	}
}
