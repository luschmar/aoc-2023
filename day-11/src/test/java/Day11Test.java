import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "374"),
			@AocInputMapping(input = "input.txt", solution = "9370588")
	})
	void part1(Stream<String> input, String solution) {
		var universe = new ArrayList<>(input.toList());

		//printUniverse(universe);
		expandString(universe);
		//printUniverse(universe);

		var galaxies = IntStream.range(0, universe.size()).boxed().flatMap(x -> IntStream.range(0, universe.get(x).length()).mapToObj(y -> {
			if (universe.get(x).charAt(y) == '#') return new Galaxy(x, y);
			return null;
		})).filter(Objects::nonNull).toList();

		var toPair = new ArrayList<>(galaxies);
		var sum = 0;
		for (var g1 : galaxies) {
			toPair.remove(g1);

			for (var g2 : toPair) {
				// System.out.println("G("+g1.x()+","+g1.y()+") <-> G("+g2.x()+","+g2.y()+") --> "+g1.calculateDistance(g2));
				sum += g1.calculateDistance(g2);
			}
		}

		assertEquals(Integer.parseInt(solution), sum);
	}

	record Galaxy(int x, int y) {
		int calculateDistance(Galaxy other) {
			return abs(x() - other.x()) + abs(y() - other.y());
		}
	}


	private void printUniverse(List<String> universe) {
		for (String s : universe) {
			System.out.println(s);
		}
		System.out.println();
	}

	void expandString(List<String> expand) {
		// expand vertical
		int initialSize = expand.size();
		for (int i = 0; i < initialSize; i++) {
			var a = initialSize - i - 1;
			var line = expand.get(a);
			if (line.chars().allMatch(c -> c == '.')) {
				expand.add(a, line);
			}
		}

		// expand horizontal
		int initialLength = expand.get(0).length();
		for (int i = 0; i < initialLength; i++) {
			var a = initialLength - i - 1;
			var test = expand.stream().map(s -> s.charAt(a)).allMatch(c -> c == '.');
			if (test) {
				for (int j = 0; j < expand.size(); j++) {
					expand.add(j, addEmpty(expand.remove(j), a));
				}
			}
		}
	}

	String addEmpty(String str, int p) {
		return str.substring(0, p) + '.' + str.substring(p);
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "1030")
	})
	void part2_10(Stream<String> input, String solution) {
		var universe = new ArrayList<>(input.toList());

		var horizontalExpansion = getHorizontalExpansions(universe);
		var verticalExpansion = getVerticalExpansions(universe);

		var galaxies = IntStream.range(0, universe.size()).boxed().flatMap(x -> IntStream.range(0, universe.get(x).length()).mapToObj(y -> {
			if (universe.get(x).charAt(y) == '#') return new Galaxy(x, y);
			return null;
		})).filter(Objects::nonNull).toList();

		var sum = calculateDistancesWithFactor(galaxies, horizontalExpansion, verticalExpansion, 10);

		assertEquals(Long.parseLong(solution), sum);
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "8410")
	})
	void part2_100(Stream<String> input, String solution) {
		var universe = new ArrayList<>(input.toList());

		var horizontalExpansion = getHorizontalExpansions(universe);
		var verticalExpansion = getVerticalExpansions(universe);

		var galaxies = IntStream.range(0, universe.size()).boxed().flatMap(x -> IntStream.range(0, universe.get(x).length()).mapToObj(y -> {
			if (universe.get(x).charAt(y) == '#') return new Galaxy(x, y);
			return null;
		})).filter(Objects::nonNull).toList();

		var sum = calculateDistancesWithFactor(galaxies, horizontalExpansion, verticalExpansion, 100);

		assertEquals(Long.parseLong(solution), sum);
	}

	long calculateDistancesWithFactor(List<Galaxy> galaxies, List<Integer>horizontalExpansion, List<Integer> verticalExpansion, int factor) {
		var toPair = new ArrayList<>(galaxies);
		var sum = 0L;
		for (var g1 : galaxies) {
			toPair.remove(g1);
			for (var g2 : toPair) {
				sum += g1.calculateDistance(g2);

				var minX = Integer.min(g1.x(), g2.x());
				var maxX = Integer.max(g1.x(), g2.x());
				sum += (factor-1) * verticalExpansion.stream().filter(b -> b > minX && b < maxX).count();


				var minY = Integer.min(g1.y(), g2.y());
				var maxY = Integer.max(g1.y(), g2.y());
				sum += (factor-1) * horizontalExpansion.stream().filter(b -> b > minY && b < maxY).count();
			}
		}
		return sum;
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "input.txt", solution = "746207878188")
	})
	void part2_1000000(Stream<String> input, String solution) {
		var universe = new ArrayList<>(input.toList());

		var horizontalExpansion = getHorizontalExpansions(universe);
		var verticalExpansion = getVerticalExpansions(universe);

		var galaxies = IntStream.range(0, universe.size()).boxed().flatMap(x -> IntStream.range(0, universe.get(x).length()).mapToObj(y -> {
			if (universe.get(x).charAt(y) == '#') return new Galaxy(x, y);
			return null;
		})).filter(Objects::nonNull).toList();


		var sum = calculateDistancesWithFactor(galaxies, horizontalExpansion, verticalExpansion, 1000000);

		assertEquals(Long.parseLong(solution), sum);
	}

	private List<Integer> getHorizontalExpansions(ArrayList<String> universe) {
		var expansions = new ArrayList<Integer>();
		// expand horizontal
		int initialLength = universe.get(0).length();
		for (int i = 0; i < initialLength; i++) {
			var a = initialLength - i - 1;
			var test = universe.stream().map(s -> s.charAt(a)).allMatch(c -> c == '.');
			if (test) {
				expansions.add(a);
			}
		}
		return expansions;
	}

	private List<Integer> getVerticalExpansions(ArrayList<String> universe) {
		var expansions = new ArrayList<Integer>();
		int initialSize = universe.size();
		for (int i = 0; i < initialSize; i++) {
			var a = initialSize - i - 1;
			var line = universe.get(a);
			if (line.chars().allMatch(c -> c == '.')) {
				expansions.add(a);
			}
		}
		return expansions;
	}
}
