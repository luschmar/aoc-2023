import org.junit.jupiter.params.ParameterizedTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day02Test {
	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "8"),
			@AocInputMapping(input = "input.txt", solution = "2237") 
	})
	void part1(Stream<String> input, String solution) {
		var result = input.map(s -> {
			var gameNumber = Integer.parseInt(s.substring(5, s.indexOf(":")));
			var gameRuns = Arrays.stream(s.substring(s.indexOf(":")+1).split(";")).map(gameString -> {
				var a = Arrays.asList(gameString.split(","));
				
				var rString = a.stream().filter(c -> c.contains("red")).findFirst().orElse("0 red");
				var bString = a.stream().filter(c -> c.contains("blue")).findFirst().orElse("0 blue");
				var gString = a.stream().filter(c -> c.contains("green")).findFirst().orElse("0 green");
				
				var rInt = Integer.parseInt(rString.replaceAll("\\D", ""));
				var bInt = Integer.parseInt(bString.replaceAll("\\D", ""));
				var gInt = Integer.parseInt(gString.replaceAll("\\D", ""));

				return new Run(bInt,rInt,gInt);
			}).toList();
			
			return new Game(gameNumber, gameRuns);
		}).filter(g -> {
			if(g.run().stream().anyMatch(r -> r.blue > 14)) {
				return false;
			}
			if(g.run().stream().anyMatch(r -> r.green > 13)) {
				return false;
			}
			if(g.run().stream().anyMatch(r -> r.red > 12)) {
				return false;
			}
			return true;
		}).mapToInt(a -> a.id).sum();

		assertEquals(Integer.parseInt(solution), result);
	}

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "2286"),
			@AocInputMapping(input = "input.txt", solution = "66681")
	})
	void part2(Stream<String> input, String solution) {
		var result = input.map(s -> {
			var gameNumber = Integer.parseInt(s.substring(5, s.indexOf(":")));
			var gameRuns = Arrays.stream(s.substring(s.indexOf(":")+1).split(";")).map(gameString -> {
				var a = Arrays.asList(gameString.split(","));
				
				var rString = a.stream().filter(c -> c.contains("red")).findFirst().orElse("0 red");
				var bString = a.stream().filter(c -> c.contains("blue")).findFirst().orElse("0 blue");
				var gString = a.stream().filter(c -> c.contains("green")).findFirst().orElse("0 green");
				
				var rInt = Integer.parseInt(rString.replaceAll("\\D", ""));
				var bInt = Integer.parseInt(bString.replaceAll("\\D", ""));
				var gInt = Integer.parseInt(gString.replaceAll("\\D", ""));

				return new Run(bInt,rInt,gInt);
			}).toList();
			
			return new Game(gameNumber, gameRuns);
		}).mapToLong(g -> {
			var bMax = g.run().stream().mapToLong(r -> r.blue).max().getAsLong();
			var rMax = g.run().stream().mapToLong(r -> r.red).max().getAsLong();
			var gMax = g.run().stream().mapToLong(r -> r.green).max().getAsLong();

			return bMax*rMax*gMax;
		}).sum();	
		
		assertEquals(Long.parseLong(solution), result);

	}
	
	/**
	 * DATA Structures
	 * ---------------
	 */

	record Game(int id, List<Run> run) {
	}

	record Run(int blue, int red, int green) {
	}
}
