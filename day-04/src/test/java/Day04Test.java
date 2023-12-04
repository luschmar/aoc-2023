import org.junit.jupiter.params.ParameterizedTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "13"),
			@AocInputMapping(input = "input.txt", solution = "26443")
	})
	void part1(Stream<String> input, String solution) {
		var sum = input.map(a -> {
			var winningString = a.split("\\|")[0].substring(a.indexOf(":")+1);
			var winning = Arrays.asList(winningString
					.split(" ")).stream()
					.filter(n -> !n.trim().equals(""))
					.map(s -> Integer.parseInt(s.trim())).toList();
			
			var numberString = a.split("\\|")[1];
			var numbers =  Arrays.asList(numberString.split(" ")).stream()
			 .filter(n -> !n.trim().equals(""))
			 .map(s -> Integer.parseInt(s.trim())).toList();
			
			return new Card(winning, numbers);
		}).mapToDouble(a -> a.winningPoints()).sum();
		
		
		assertEquals(Double.parseDouble(solution), sum);
	}
	
	record Card(List<Integer> winning, List<Integer> numbers) {
		
		long count() {
			return winning.stream().filter(f -> numbers.contains(f)).count();
		}
		
		double winningPoints() {
			var matches = count();
			if(matches == 0) {
				return 0;
			}
			return Math.pow(2, matches-1);
		}
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "30"),
			@AocInputMapping(input = "input.txt", solution = "6284877")
	})
	void part2(Stream<String> input, String solution) {
		var cards = input.map(a -> {
			var winningString = a.split("\\|")[0].substring(a.indexOf(":")+1);
			var winning = Arrays.asList(winningString
					.split(" ")).stream()
					.filter(n -> !n.trim().equals(""))
					.map(s -> Integer.parseInt(s.trim())).toList();
			
			var numberString = a.split("\\|")[1];
			var numbers =  Arrays.asList(numberString.split(" ")).stream()
			 .filter(n -> !n.trim().equals(""))
			 .map(s -> Integer.parseInt(s.trim())).toList();
			
			var index = Integer.parseInt(a.substring(0, a.indexOf(":")).replaceAll("\\D", ""));
			
			return new Card2(index, winning, numbers);
		}).toList();
		
		var instances = new HashMap<Card2, Integer>();
		
		
		IntStream.range(0, cards.size()).forEach(a -> {
			var current = cards.get(a);
			var matchingNumbers = current.count();
			instances.putIfAbsent(current, 1);
			var countCurrent = instances.get(current);
			for(int i = 0; i < matchingNumbers; i++) {
				var toCopy = cards.get(a+i+1);
				var oldCount = instances.getOrDefault(toCopy, 1);
				instances.put(toCopy, oldCount+countCurrent);
			}
		});

		var result = instances.values().stream().mapToLong(e -> e.intValue()).sum();
		assertEquals(Long.parseLong(solution), result);
	}
	
	
	record Card2(int index, List<Integer> winning, List<Integer> numbers) {
		
		long count() {
			return winning.stream().filter(f -> numbers.contains(f)).count();
		}
		
		double winningPoints() {
			var matches = count();
			if(matches == 0) {
				return 0;
			}
			return Math.pow(2, matches-1);
		}
	}
}
