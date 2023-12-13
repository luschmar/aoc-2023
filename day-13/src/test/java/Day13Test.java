import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "405"),
			@AocInputMapping(input = "input.txt", solution = "33975")
	})
	void part1(Stream<String> input, String solution) {
		var all = input.toList();

		var tables = new ArrayList<List<String>>();
		var currentTable = new ArrayList<String>();
		tables.add(currentTable);
		for (var l : all) {
			if ("".equals(l)) {
				currentTable = new ArrayList<>();
				tables.add(currentTable);
				continue;
			}
			currentTable.add(l);
		}

		var sumH = tables.stream().mapToInt(this::findMirror).sum() * 100;
		var sumV = tables.stream().map(this::transpose).mapToInt(this::findMirror).sum();

		var result = sumH + sumV;

		assertEquals(Integer.parseInt(solution), result);
	}

	List<String> transpose(List<String> t) {
		var newTable = new ArrayList<String>();
		IntStream.range(0, t.getFirst().length()).forEach(i -> newTable.add(t.stream().map(s -> Character.toString(s.charAt(i))).collect(Collectors.joining())));
		return newTable;
	}

	int findMirror(List<String> t) {
		return IntStream.range(0, t.size() - 1)
				// prefilter candidates
				.filter(i -> t.get(i).equals(t.get(i + 1)))
				// now filter
				.map(c -> {
					// mirror candidate; check rest
					int mirrorSize = Integer.min(c + 1, t.size() - c - 1);

					// all ok...
					var res = IntStream.range(0, mirrorSize).allMatch(m -> t.get(c - m).equals(t.get(c + 1 + m)));
					if (res) {
						//printTable(t);
						//System.out.println(" -> " + c + " mirrorsize " + mirrorSize);
						return c + 1;
					}
					return 0;
				}).sum();
	}

	void printTable(List<String> t) {
		System.out.println();
		t.forEach(System.out::println);
		System.out.println();
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "400"),
			@AocInputMapping(input = "input.txt", solution = "29083")
	})
	void part2(Stream<String> input, String solution) {
		var all = input.toList();

		var tables = new ArrayList<List<String>>();
		var currentTable = new ArrayList<String>();
		tables.add(currentTable);
		for (var l : all) {
			if ("".equals(l)) {
				currentTable = new ArrayList<>();
				tables.add(currentTable);
				continue;
			}
			currentTable.add(l);
		}

		var result = tables.stream().mapToInt(t -> {
			var s1 = findAxisWithOneSmudge(t);
			var s2 = findAxisWithOneSmudge(transpose(t));

			if (s1 == 0) {
				return s2;
			}
			return s1 * 100;
		}).sum();

		assertEquals(Integer.parseInt(solution), result);
	}

	int findAxisWithOneSmudge(List<String> t) {
		// check all possibilities 🙄 - but stop after more than 1 error
		return IntStream.range(0, t.size() - 1).map(c -> {

			int mirrorSize = Integer.min(c + 1, t.size() - c - 1);

			int error = 0;
			// check each pair; error total more than 1 exit!
			for (int i = 0; (error <= 1) && i < mirrorSize; i++) {
				var up = t.get(c - i);
				var down = t.get(c + 1 + i);
				if (up.equals(down)) {
					continue;
				}
				error += (int) IntStream.range(0, up.length()).filter(e -> up.charAt(e) != down.charAt(e)).count();
			}
			if (error == 1) {
				// found mirror-axis with exact 1 error
				return c + 1;
			}
			return 0;
		}).sum();
	}
}
