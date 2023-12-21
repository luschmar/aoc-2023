import org.junit.jupiter.params.ParameterizedTest;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day21Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "16"),
			@AocInputMapping(input = "input.txt", expected = "3743")
	})
	void part1(Stream<String> input, String expected) {
		var orginal = input.toList();
		var map = orginal.stream().map(StringBuilder::new).toList();

		IntStream.range(0, "16".equals(expected) ? 6 : 64).forEach(i -> {
			var steps = extractPoints(map);
			// insert ne points
			for(var s : steps) {
				map.get(s.x()).setCharAt(s.y(), '.');

				if(s.x()-1 >= 0) {
					map.get(s.x() - 1).setCharAt(s.y(), 'O');
				}
				if(s.x()+1 < map.size()) {
					map.get(s.x() + 1).setCharAt(s.y(), 'O');
				}

				if(s.y()-1 >= 0) {
					map.get(s.x()).setCharAt(s.y() - 1, 'O');
				}
				if(s.y()+1 < map.get(s.x()).length()) {
					map.get(s.x()).setCharAt(s.y() + 1, 'O');
				}
			}
			//mask with original map
			IntStream.range(0, map.size()).forEach(
					x->{
						IntStream.range(0, map.get(x).length()).forEach(y -> {
							if(orginal.get(x).charAt(y) == '#') {
								map.get(x).setCharAt(y, '#');
							}
						});
					}
			);

			map.forEach(System.out::println);
			System.out.println();
		});

		var result = map.stream().mapToInt(s -> s.toString().replaceAll("[.#]","").length()).sum();

		assertEquals(Integer.parseInt(expected), result);

	}

	private List<Step> extractPoints(List<StringBuilder> map) {
		var steps = new ArrayList<Step>();

		IntStream.range(0, map.size()).forEach(x -> {
			IntStream.range(0, map.get(x).length()).forEach(y -> {
				if(map.get(x).charAt(y) == 'O' || map.get(x).charAt(y) == 'S') {
					steps.add(new Step(x, y));
				}
			});
		});
		return steps;
	}

	record Step(int x, int y) {

	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "16733044"),
			@AocInputMapping(input = "input.txt", expected = "-1")
	})
	void part2(Stream<String> input, String expected) {
		var orginal = input.toList();
		var map = orginal.stream().map(StringBuilder::new).toList();

		var result = 0;
		assertEquals(Integer.parseInt(expected), result);
	}
}
