import org.junit.jupiter.params.ParameterizedTest;
import org.junit.platform.commons.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
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
		var time = Arrays.stream(lines.get(0).substring(lines.get(0).indexOf(":")+1).split(" ")).filter(a -> !"".equals(a)).map(Integer::parseInt).toList();
		var distance = Arrays.stream(lines.get(1).substring(lines.get(1).indexOf(":")+1).split(" ")).filter(a -> !"".equals(a)).map(Integer::parseInt).toList();;
		var result = new ArrayList<>(IntStream.range(0, time.size()).mapToObj(a -> new AtomicInteger()).toList());

		for(int i = 0; i < time.size(); i++) {
			var t = time.get(i);
			var d = distance.get(i);
			for(int j= 1; j < t;j++) {
				var timeToTravel = t - j;
				var speedToTravel =j;
				var distanceTravel = timeToTravel*speedToTravel;

				if(distanceTravel > d) {
					result.get(i).incrementAndGet();
				}

			}
		}


		assertEquals(Integer.parseInt(solution), result.stream().mapToInt(AtomicInteger::get).reduce(1, (a,b)->a*b));
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "71503"),
			@AocInputMapping(input = "input.txt", solution = "38017587")
	})
	void part2(Stream<String> input, String solution) {
		var lines = input.toList();
		var time = Arrays.stream(lines.get(0).substring(lines.get(0).indexOf(":")+1).replaceAll(" ","").split(" ")).filter(a -> !"".equals(a)).map(Long::parseLong).toList();
		var distance = Arrays.stream(lines.get(1).substring(lines.get(1).indexOf(":")+1).replaceAll(" ","").split(" ")).filter(a -> !"".equals(a)).map(Long::parseLong).toList();;
		var result = new ArrayList<>(IntStream.range(0, time.size()).mapToObj(a -> new AtomicLong()).toList());

		for(int i = 0; i < time.size(); i++) {
			var t = time.get(i);
			var d = distance.get(i);
			for(int j= 1; j < t;j++) {
				var timeToTravel = t - j;
				var speedToTravel = j;
				var distanceTravel = timeToTravel*speedToTravel;

				if(distanceTravel > d) {
					result.get(i).incrementAndGet();
				}

			}
		}


		assertEquals(Long.parseLong(solution), result.stream().mapToLong(AtomicLong::get).reduce(1, (a,b)->a*b));
	}
}
