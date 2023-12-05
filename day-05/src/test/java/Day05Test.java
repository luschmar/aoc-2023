import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Test {
	@ParameterizedTest
	@AocFileSource(inputs = {@AocInputMapping(input = "test.txt", solution = "35"), @AocInputMapping(input = "input.txt", solution = "278755257")})
	void part1(Stream<String> input, String solution) {
		var lines = input.toList();

		Map<String, Transformer> transformers = new HashMap<>();
		Transformer currentTransformer = null;
		for (var l : lines) {
			if (l.endsWith("map:")) {
				var from = l.split(" ")[0].split("-")[0];
				var to = l.split(" ")[0].split("-")[2];
				currentTransformer = new Transformer(from, to);
				transformers.put(from, currentTransformer);
			} else {
				var range = l.split(" ");
				if (range.length == 3) {
					currentTransformer.addRange(Long.parseLong(range[1]), Long.parseLong(range[0]), Long.parseLong(range[2]));
				}
			}
		}

		var toTest = Arrays.asList(lines.get(0).substring(7).split(" ")).stream().map(Long::parseLong).toList();
		List<Long> locations = new ArrayList<>();
		for (var t : toTest) {
			var currentTrans = transformers.get("seed");
			var currentValue = t;
			while (currentTrans != null) {
				currentValue = currentTrans.lookup(currentValue);
				System.out.println(currentTrans.to + " - " + currentValue);

				currentTrans = transformers.get(currentTrans.to);
			}
			locations.add(currentValue);
		}

		assertEquals(Long.parseLong(solution), locations.stream().mapToLong(a -> a).min().getAsLong());

	}

	class Transformer {

		private final String from;
		private final String to;
		List<Range> ranges = new ArrayList<>();

		Transformer(String from, String to) {
			this.from = from;
			this.to = to;
		}

		void addRange(long source, long destination, long length) {
			ranges.add(new Range(source, destination, length));
		}

		long lookup(long number) {
			for (var r : ranges) {
				if (number >= r.source() && number < r.source() + r.length()) {
					var distance = number - r.source();
					return r.destination() + distance;
				}
			}
			return number;
		}
	}

	record Range(long source, long destination, long length) {
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "46"),
			//@AocInputMapping(input = "input.txt", solution = "26829166") // runs 5min
	})
	void part2(Stream<String> input, String solution) {
		var lines = input.toList();

		Map<String, Transformer> transformers = new HashMap<>();
		Transformer currentTransformer = null;
		for (var l : lines) {
			if (l.endsWith("map:")) {
				var from = l.split(" ")[0].split("-")[0];
				var to = l.split(" ")[0].split("-")[2];
				currentTransformer = new Transformer(from, to);
				transformers.put(from, currentTransformer);
			} else {
				var range = l.split(" ");
				if (range.length == 3) {
					currentTransformer.addRange(Long.parseLong(range[1]), Long.parseLong(range[0]), Long.parseLong(range[2]));
				}
			}
		}

		var noPair = Arrays.asList(lines.get(0).substring(7).split(" ")).stream().map(Long::parseLong).toList();
		var toTest = IntStream.range(0, noPair.size() / 2).mapToObj(i -> LongStream.range(noPair.get(i * 2), noPair.get(i * 2) + noPair.get(i * 2 + 1))).flatMap(a -> a.boxed());

		AtomicReference<Long> minLocation = new AtomicReference<>(Long.MAX_VALUE);

		toTest.forEach(t -> {
			var currentTrans = transformers.get("seed");
			var currentValue = t;
			while (currentTrans != null) {
				currentValue = currentTrans.lookup(currentValue);
				currentTrans = transformers.get(currentTrans.to);
			}
			if (minLocation.get() > currentValue) {
				minLocation.set(currentValue);
			}
		});

		assertEquals(Long.parseLong(solution), minLocation.get());
	}
}
