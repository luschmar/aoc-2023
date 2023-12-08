import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {
	@ParameterizedTest
	@AocFileSource(inputs = {@AocInputMapping(input = "test.txt", solution = "2"), @AocInputMapping(input = "test2.txt", solution = "6"), @AocInputMapping(input = "input.txt", solution = "20777")})
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
		}).filter(Objects::nonNull).toList();

		// follow instructions
		var currentNode = findNode(nodes, "AAA");
		int i = 0;
		while (!"ZZZ".equals(currentNode.name())) {
			for (var c : lines.get(0).toCharArray()) {
				if ('R' == c) {
					currentNode = findNode(nodes, currentNode.right());
					i++;
				} else {
					currentNode = findNode(nodes, currentNode.left());
					i++;
				}
				if ("ZZZ".equals(currentNode.name())) {
					break;
				}
			}
		}

		assertEquals(Integer.parseInt(solution), i);
	}

	private Node findNode(List<Node> nodes, String search) {
		return nodes.stream().filter(a -> search.equals(a.name())).findFirst().orElseThrow();
	}

	private List<Node> findNodes(List<Node> nodes, String search) {
		return nodes.stream().filter(a -> a.name().endsWith(search)).toList();
	}

	record Node(String name, String left, String right) {
	}

	@ParameterizedTest
	@AocFileSource(inputs = {@AocInputMapping(input = "test3.txt", solution = "6"), @AocInputMapping(input = "input.txt", solution = "13289612809129")})
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
		}).filter(Objects::nonNull).toList();

		// follow instructions
		var currentNodes = findNodes(nodes, "A");

		long result = currentNodes.stream().mapToLong(n -> {
			var currentNode = n;
			long i = 0;
			while (!currentNode.name().endsWith("Z")) {
				for (var c : lines.get(0).toCharArray()) {
					if ('R' == c) {
						currentNode = findNode(nodes, currentNode.right());
						i++;
					} else {
						currentNode = findNode(nodes, currentNode.left());
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

	private static long gcd(long x, long y) {
		return (y == 0) ? x : gcd(y, x % y);
	}
}
