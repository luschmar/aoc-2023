import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "-1"),
			@AocInputMapping(input = "input.txt", expected = "-1")
	})
	void part1(Stream<String> input, String expected) {
		assertEquals(expected, Long.toString(input.count()));
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "-1"),
			@AocInputMapping(input = "input.txt", expected = "-1")
	})
	void part2(Stream<String> input, String expected) {
		assertEquals(expected, Long.toString(input.count()));
	}
}
