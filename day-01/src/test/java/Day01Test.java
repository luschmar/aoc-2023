import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "142"),
			@AocInputMapping(input = "input.txt", solution = "54605")
	})
	void part1(Stream<String> input, String solution) {
		var result = input.mapToLong(s -> {
			var digit = s.replaceAll("\\D", "");
			var first = digit.charAt(0);
			var last = digit.charAt(digit.length()-1);

			return Long.parseLong(first+""+last);
		}).sum();
		
		assertEquals(Long.parseLong(solution), result);
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test2.txt", solution = "281"),
			@AocInputMapping(input = "input.txt", solution = "55429")
	})
	void part2(Stream<String> input, String solution) {
		var result = input.mapToLong(s -> {
			var newString = replaceNames(s);
			var digit = newString.replaceAll("\\D", "");
			var first = digit.charAt(0);
			var last = digit.charAt(digit.length()-1);

			return Long.parseLong(first+""+last);
		}).sum();
		assertEquals(Long.parseLong(solution), result);
	}

	private String replaceNames(String s) {
		var one = s.replaceAll("one", "o1e");
		var two = one.replaceAll("two", "t2");
		var three = two.replaceAll("three", "t3e");
		var four = three.replaceAll("four", "4");
		var five = four.replaceAll("five", "5e");
		var six = five.replaceAll("six", "6");
		var seven = six.replaceAll("seven", "7n");
		var eight = seven.replaceAll("eight", "8");
		var nine = eight.replaceAll("nine", "9");
		return nine;
	}
}
