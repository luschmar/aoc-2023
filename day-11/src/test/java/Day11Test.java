import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Math.abs;
import static java.util.Comparator.reverseOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "374"),
			@AocInputMapping(input = "input.txt", expected = "9370588")
	})
	void part1(Stream<String> input, String expected) {
		var u = new Universe(input.toList());
		u.printUniverse();
		var result = u.calculateDistanceWithExpansionFactor(2);
		assertEquals(Long.parseLong(expected), result);
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "1030")
	})
	void part2_10(Stream<String> input, String expected) {
		var u = new Universe(input.toList());
		var result = u.calculateDistanceWithExpansionFactor(10);
		assertEquals(Long.parseLong(expected), result);
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "8410")
	})
	void part2_100(Stream<String> input, String expected) {
		var u = new Universe(input.toList());
		var result = u.calculateDistanceWithExpansionFactor(100);
		assertEquals(Long.parseLong(expected), result);
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "input.txt", expected = "746207878188")
	})
	void part2_1000000(Stream<String> input, String expected) {
		var u = new Universe(input.toList());
		var result = u.calculateDistanceWithExpansionFactor(1000000);
		assertEquals(Long.parseLong(expected), result);
	}

	static class Universe {
		private final List<String> rawUniverse;
		private final List<Galaxy> galaxies;
		private final List<Integer> horizontalExpansionPoints;
		private final List<Integer> verticalExpansionPoints;

		Universe(List<String> rawUniverse) {
			this.rawUniverse = rawUniverse;
			this.galaxies = IntStream.range(0, rawUniverse.size()).boxed()
					.flatMap(x -> IntStream.range(0, rawUniverse.get(x).length()).mapToObj(y -> {
						if (rawUniverse.get(x).charAt(y) == '#') {
							return new Galaxy(x, y);
						}
						return null;
					})).filter(Objects::nonNull).toList();
			this.verticalExpansionPoints = extractVerticalExpansionPoints();
			this.horizontalExpansionPoints = extractHorizontalExpansionPoints();
		}

		private List<Integer> extractVerticalExpansionPoints() {
			return IntStream.range(0, rawUniverse.get(0).length())
					.filter(y -> IntStream.range(0, rawUniverse.size()).allMatch(x -> rawUniverse.get(x).charAt(y) == '.'))
					.boxed().toList();
		}

		private List<Integer> extractHorizontalExpansionPoints() {
			return IntStream.range(0, rawUniverse.size())
					.filter(y -> rawUniverse.get(y).chars().allMatch(c -> c == '.'))
					.boxed().toList();
		}

		long calculateDistanceWithExpansionFactor(int factor) {
			return LongStream.range(0, galaxies.size()).flatMap(gi1 -> LongStream.range(gi1 + 1, galaxies.size()).map(gi2 -> {
				var g1 = galaxies.get((int) gi1);
				var g2 = galaxies.get((int) gi2);

				var sum = 0L;
				sum += g1.calculateDistance(g2);

				// fill up space with factor
				var minX = min(g1.x(), g2.x());
				var maxX = max(g1.x(), g2.x());
				sum += (factor - 1) * horizontalExpansionPoints.stream().filter(b -> b > minX && b < maxX).count();

				var minY = min(g1.y(), g2.y());
				var maxY = max(g1.y(), g2.y());
				sum += (factor - 1) * verticalExpansionPoints.stream().filter(b -> b > minY && b < maxY).count();

				return sum;
			})).sum();
		}
		public static final String ANSI_RESET = "\u001B[0m";
		public static final String ANSI_YELLOW = "\u001B[33m";
		public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";

		void printUniverse() {
			IntStream.range(0, rawUniverse.size()).forEach(x -> {
				if(horizontalExpansionPoints.contains(x)) {
					System.out.println(ANSI_BLACK_BACKGROUND+rawUniverse.get(x)+ANSI_RESET);
				} else {
					var l = new StringBuilder(rawUniverse.get(x));
					verticalExpansionPoints.stream().sorted(reverseOrder()).forEach(y -> {
						l.insert(y+1, ANSI_RESET);
						l.insert(y, ANSI_BLACK_BACKGROUND);
					}
					);
					System.out.println(l.toString().replaceAll("#", ANSI_YELLOW+"#"+ANSI_RESET));
				}
			});
			System.out.println();
			System.out.println();
		}
	}

	record Galaxy(int x, int y) {
		int calculateDistance(Galaxy other) {
			return abs(x() - other.x()) + abs(y() - other.y());
		}
	}
}
