import org.junit.jupiter.params.ParameterizedTest;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01Test {
	Pattern p = Pattern.compile("%d");
	
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
		var one = s.replaceAll("one", "one1one");
		var two = one.replaceAll("two", "two2two");
		var three = two.replaceAll("three", "three3three");
		var four = three.replaceAll("four", "four4four");
		var five = four.replaceAll("five", "five5five");
		var six = five.replaceAll("six", "six6six");
		var seven = six.replaceAll("seven", "seven7seven");
		var eight = seven.replaceAll("eight", "eight8eight");
		var nine = eight.replaceAll("nine", "nine9nine");
		return nine;
	}
}
