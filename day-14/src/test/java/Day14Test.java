import org.junit.jupiter.params.ParameterizedTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.time.Instant.now;

class Day14Test {
	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "136"),
			@AocInputMapping(input = "input.txt", solution = "109755")
	})
	void part1(Stream<String> input, String solution) {
		var table = input.toList();
		var tt = transpose(table);

		var toNorth = simulateGravity(tt);

		int result = IntStream.range(0, toNorth.get(0).length()).map(y -> {
			return (toNorth.get(0).length() - y) * (int) IntStream.range(0, toNorth.size()).filter(x -> {
				return toNorth.get(x).charAt(y) == 'O';
			}).count();
		}).sum();

		assertEquals(Integer.parseInt(solution), result);
	}

	private List<String> simulateGravity(List<String> tt) {
		return tt.stream().map(s -> {
			var a = Arrays.stream(s.split("#", -1)).map(u -> {
				var k = u.chars().boxed().sorted(Comparator.reverseOrder()).map(c -> Character.toString(c))
						.collect(Collectors.joining());
				return k;
			}).collect(Collectors.joining("#"));
			return a;
		}).toList();
	}

	void printTable(List<String> t) {
		System.out.println();
		t.forEach(System.out::println);
		System.out.println();
	}

	List<String> transpose(List<String> t) {
		var newTable = new ArrayList<String>();
		IntStream.range(0, t.getFirst().length()).forEach(
				i -> newTable.add(t.stream().map(s -> Character.toString(s.charAt(i))).collect(Collectors.joining())));
		return newTable;
	}

	/**
	 * 🥲 will never finish
	 */
	@ParameterizedTest
	@AocFileSource(inputs = {
	// @AocInputMapping(input = "test.txt", solution = "64"),
	// @AocInputMapping(input = "input.txt", solution = "-1")
	})
	void part2(Stream<String> input, String solution) {
		var table = input.toList();
		var r = new Reflector(transpose(table));
		r.printTable();
		var start = now();
		for (int i = 0; i < 1000000000; i++) {
			r.simulateGravity();
			r.rotate();
			r.rotate();
			r.rotate();
			r.simulateGravity();
			r.rotate();
			r.rotate();
			r.rotate();
			r.simulateGravity();
			r.rotate();
			r.rotate();
			r.rotate();
			r.simulateGravity();
			r.rotate();
			r.rotate();
			r.rotate();
			if (i % 100000 == 0) {
				var finish = now();
				System.out.println(i + " -> " + Duration.between(start, finish).toMillis());
			}
		}
		r.printTable();

	}

	class Reflector {
		private List<String> data;
		private Orientation orientation;

		Reflector(List<String> data) {
			this.orientation = Orientation.NORTH;
			this.data = data;
		}

		private void rotate() {
			var newTable = new ArrayList<String>();
			IntStream.range(0, data.getFirst().length()).forEach(i -> {
				var b = new StringBuilder(
						data.stream().map(s -> Character.toString(s.charAt(i))).collect(Collectors.joining()));
				newTable.add(b.reverse().toString());
			});
			orientation = Orientation.values()[(orientation.ordinal() + 1) % Orientation.values().length];
			data = newTable;
		}

		private void simulateGravity() {
			data = data.stream().map(s -> {
				var a = Arrays.stream(s.split("#", -1)).map(u -> {
					var k = u.chars().boxed().sorted(Comparator.reverseOrder()).map(c -> Character.toString(c))
							.collect(Collectors.joining());
					return k;
				}).collect(Collectors.joining("#"));
				return a;
			}).toList();
		}

		void printTable() {
			System.out.println();
			System.out.println(orientation);
			data.forEach(System.out::println);
			System.out.println();
		}
	}

	enum Orientation {
		NORTH, EAST, SOUT, WEST
	}
}
