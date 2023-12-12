import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

	@ParameterizedTest
	@ValueSource(strings = {
			"???.###",
			".??..??...?##.",
			"????.#...#...",
	})
	void bitMaskTest(String input) {
		var bitMaskList = bitMaskStream(input).toList();
		for(var a : bitMaskList) {
			System.out.println(a);
		}
	}

	private Stream<String> bitMaskStream(String input) {
		var size = input.replaceAll("\\.|#", "").length();
		return IntStream.range(0,  (int)Math.pow(2, size))
				.mapToObj(a -> String.format("%" + size + "s", Integer.toString(a, 2))
						.replaceAll(" ", "0"))
				.map(s ->
				{
					var buffer = new StringBuffer(input);
					for(int i = 0; i < s.length(); i++) {
						if(s.charAt(i) == '1') {
							buffer.replace(buffer.indexOf("?"), buffer.indexOf("?")+1, "#");
						} else {
							buffer.replace(buffer.indexOf("?"), buffer.indexOf("?")+1, ".");
						}
					}
					return buffer.toString();
				});
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "21"),
			@AocInputMapping(input = "input.txt", solution = "7922")
	})
	void part1(Stream<String> input, String solution) {
		var result = input
				.map(s -> new SpringRow(s.split(" ")[0], s.split(" ")[1]))
				.mapToLong(sr -> bitMaskStream(sr.map()).filter(s -> filterMatching(s, sr.regex())).count())
				.sum();

		assertEquals(Long.parseLong(solution), result);
	}

	private boolean filterMatching(String input, String contiguousGroup) {
		var g = Arrays.stream(input.split("\\.")).mapToInt(String::length).filter(a -> a != 0).boxed().map(i -> i.toString()).collect(joining(","));

		if(g.equals(contiguousGroup)) {
			//System.out.println(input +" -> "+contiguousGroup);
		}

		return g.equals(contiguousGroup);
	}

	record SpringRow(String map, String regex) {

	}

	/**
	 * TODO: figur out, how algorithm works 🫣
	 */
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "525152"),
			@AocInputMapping(input = "test1.txt", solution = "16384"),
			@AocInputMapping(input = "input.txt", solution = "18093821750095")
	})
	void part2(Stream<String> input, String solution) {
		var list = input.mapToLong(s -> {
			var springs = IntStream.range(0, 5).mapToObj(k -> s.split(" ")[0]).collect(joining("?"));
			var groups = Arrays.stream(IntStream.range(0, 5).mapToObj(k -> s.split(" ")[1]).collect(joining(",")).split(",")).mapToInt(Integer::parseInt).toArray();

			return countArrangements(new HashMap<>(), springs, groups,0,0,0);
		}).sum();

		assertEquals(Long.parseLong(solution), list);
	}

	/**
	 * 😭 - not my solution - https://github.com/SimonBaars/AdventOfCode-Java/blob/master/src/main/java/com/sbaars/adventofcode/year23/days/Day12.java
	 */
	long countArrangements(Map<String, Long> blockMap, String map, int[] amounts, int i, int j, int cur) {
		var key = Arrays.stream(new int[]{i,j,cur}).boxed().map(Object::toString).collect(joining(","));
		if (blockMap.containsKey(key)) {
			return blockMap.get(key);
		}
		if (i == map.length()) {
			return (j == amounts.length && cur == 0) || (j == amounts.length - 1 && amounts[j] == cur) ? 1 : 0;
		}
		long total = 0;
		char c = map.charAt(i);
		if ((c == '.' || c == '?') && cur == 0) {
			total += countArrangements(blockMap, map, amounts, i + 1, j, 0);
		} else if ((c == '.' || c == '?') && cur > 0 && j < amounts.length && amounts[j] == cur) {
			total += countArrangements(blockMap, map, amounts, i + 1, j + 1, 0);
		}
		if (c == '#' || c == '?') {
			total += countArrangements(blockMap, map, amounts, i + 1, j, cur + 1);
		}
		blockMap.put(key, total);
		return total;
	}
}
