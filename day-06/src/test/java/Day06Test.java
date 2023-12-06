import org.junit.jupiter.params.ParameterizedTest;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "288"),
			@AocInputMapping(input = "input.txt", solution = "4403592")
	})
	void part1(Stream<String> input, String solution) {
		var lines = input.toList();
		var time = Arrays.stream(lines.get(0).substring(lines.get(0).indexOf(":") + 1).split(" ")).filter(a -> !"".equals(a)).map(Integer::parseInt).toList();
		var distance = Arrays.stream(lines.get(1).substring(lines.get(1).indexOf(":") + 1).split(" ")).filter(a -> !"".equals(a)).map(Integer::parseInt).toList();

		var raceList = IntStream.range(0, time.size())
				.mapToObj(a -> new Race(time.get(a), distance.get(a))).toList();

		var result = raceList.stream()
				.mapToLong(r -> IntStream.range(1, (int)r.time()).filter(r::win).count())
				.reduce(1, (a, b) -> a * b);

		assertEquals(Integer.parseInt(solution), result);
	}

	record Race(long time, long distance) {
		long distance(int buttonTime) {
			var timeToTravel = time() - buttonTime;
			return timeToTravel * buttonTime;
		}

		boolean win(int buttonTime) {
			return distance(buttonTime) > distance();
		}
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "71503"),
			@AocInputMapping(input = "input.txt", solution = "38017587")
	})
	void part2(Stream<String> input, String solution) {
		var lines = input.toList();
		var time = Arrays.stream(lines.get(0).substring(lines.get(0).indexOf(":") + 1).replaceAll(" ", "").split(" ")).filter(a -> !"".equals(a)).map(Long::parseLong).toList();
		var distance = Arrays.stream(lines.get(1).substring(lines.get(1).indexOf(":") + 1).replaceAll(" ", "").split(" ")).filter(a -> !"".equals(a)).map(Long::parseLong).toList();

		var raceList = IntStream.range(0, time.size())
				.mapToObj(a -> new Race(time.get(a), distance.get(a))).toList();

		var result = raceList.stream()
				.mapToLong(r -> IntStream.range(1, (int)r.time()).parallel().filter(r::win).count())
				.reduce(1, (a, b) -> a * b);

		assertEquals(Long.parseLong(solution), result);
	}
}
