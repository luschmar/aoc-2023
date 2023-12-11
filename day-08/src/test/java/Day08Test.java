import org.junit.jupiter.params.ParameterizedTest;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "2"),
			@AocInputMapping(input = "test2.txt", solution = "6"),
			@AocInputMapping(input = "input.txt", solution = "20777")
	})
	void part1(Stream<String> input, String solution) {
		var lines = input.toList();
		var nodes = lines.stream().map(s -> {
			if (s.contains("=")) {
				var name = s.split(" = ")[0];
				var left = s.split(", ")[0].substring(s.split(", ")[0].indexOf("(") + 1);
				var right = s.split(", ")[1].substring(0, s.split(", ")[1].indexOf(")"));

				return new Node(name, left, right);
			}
			return null;
		}).filter(Objects::nonNull).collect(toMap(Node::name, identity()));

		// follow instructions
		var currentNode = nodes.get("AAA");
		int i = 0;
		while (!"ZZZ".equals(currentNode.name())) {
			for (var c : lines.get(0).toCharArray()) {
				if ('R' == c) {
					currentNode = nodes.get(currentNode.right());
					i++;
				} else {
					currentNode = nodes.get(currentNode.left());
					i++;
				}
				if ("ZZZ".equals(currentNode.name())) {
					break;
				}
			}
		}

		assertEquals(Integer.parseInt(solution), i);
	}

	record Node(String name, String left, String right) {
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test3.txt", solution = "6"),
			@AocInputMapping(input = "input.txt", solution = "13289612809129")
	})
	void part2(Stream<String> input, String solution) {
		var lines = input.toList();
		var nodes = lines.stream().map(s -> {
			if (s.contains("=")) {
				var name = s.split(" = ")[0];
				var left = s.split(", ")[0].substring(s.split(", ")[0].indexOf("(") + 1);
				var right = s.split(", ")[1].substring(0, s.split(", ")[1].indexOf(")"));

				return new Node(name, left, right);
			}
			return null;
		}).filter(Objects::nonNull).collect(toMap(Node::name, identity()));

		// follow instructions
		var starterNodeStream = nodes.values().stream().filter(e -> e.name().endsWith("A"));

		long result = starterNodeStream.mapToLong(n -> {
			var currentNode = n;
			long i = 0;
			while (!currentNode.name().endsWith("Z")) {
				for (var c : lines.get(0).toCharArray()) {
					if ('R' == c) {
						currentNode = nodes.get(currentNode.right());
						i++;
					} else {
						currentNode = nodes.get(currentNode.left());
						i++;
					}
					if (currentNode.name().endsWith("Z")) {
						break;
					}
				}
			}
			return i;
		}).reduce(1, (x, y) -> x * (y / gcd(x, y))); // lcm

		assertEquals(Long.parseLong(solution), result);
	}

	static long gcd(long x, long y) {
		return (y == 0) ? x : gcd(y, x % y);
	}
}