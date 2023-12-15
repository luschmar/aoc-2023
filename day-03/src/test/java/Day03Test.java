import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test {
	Pattern p = Pattern.compile("\\d+");
	Pattern star = Pattern.compile("\\*");
	Pattern symbolWithoutDot = Pattern.compile("[^0-9^.]");
	
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "4361"),
			@AocInputMapping(input = "input.txt", expected = "521515")
	})
	void part1(Stream<String> input, String expected) {
		var lines = input.toList();
		var parts = new ArrayList<Part>();
		
		for(int i = 0; i < lines.size(); i++) {
			var line = lines.get(i);

			var pmatch = p.matcher(line);
			
			while(pmatch.find()) {				
				parts.add(new Part(Integer.parseInt(line.substring(pmatch.start(), pmatch.end())), pmatch.start(), i, pmatch.end() - pmatch.start()));
			}
			
		}
		
		var result = parts.stream().filter(p -> {
			// previous
			var previousY = p.y - 1 > 0 ? p.y - 1 : 0;
			var previousStart = p.x - 1 > 0 ? p.x-1 :0;
			var previousEnd = previousStart+p.lenght+2 < lines.get(previousY).length() ?  previousStart+p.lenght+2 : lines.get(previousY).length();
			var line1 = lines.get(previousY).substring(previousStart, previousEnd);
			System.out.println(line1);
			
			// current
			var currentY = p.y;
			var currentStart = p.x - 1 > 0 ? p.x-1 :0;
			var currentEnd = currentStart+p.lenght+2 < lines.get(currentY).length() ?  currentStart+p.lenght+2 : lines.get(currentY).length();
			var line2 = lines.get(currentY).substring(currentStart, currentEnd);
			System.out.println(line2);
			
			
			// next
			var nextY = p.y+1 >= lines.size() ? p.y : p.y+1;
			var nextStart = p.x - 1 > 0 ? p.x-1 : 0;
			var nextEnd = nextStart+p.lenght+2 < lines.get(nextY).length() ?  nextStart+p.lenght+2 : lines.get(nextY).length();
			var line3 = lines.get(nextY).substring(nextStart, nextEnd);
			System.out.println(line3);
			
			
			
			return symbolWithoutDot.matcher(line1).find() || symbolWithoutDot.matcher(line2).find() || symbolWithoutDot.matcher(line3).find();
		}).mapToLong(a -> a.number).sum();
		
		assertEquals(Long.parseLong(expected), result);
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "467835"),
			@AocInputMapping(input = "input.txt", expected = "69527306")
	})
	void part2(Stream<String> input, String expected) {

		var lines = input.toList();
		var parts = new ArrayList<Part>();
		var stars = new ArrayList<Point>();
		
		for(int i = 0; i < lines.size(); i++) {
			var line = lines.get(i);

			var pMatch = p.matcher(line);
			
			while(pMatch.find()) {				
				parts.add(new Part(Integer.parseInt(line.substring(pMatch.start(), pMatch.end())), pMatch.start(), i, pMatch.end() - pMatch.start()));
			}
			
			var smatch = star.matcher(line);
			while(smatch.find()) {
				stars.add(new Point(i, smatch.start()));
			}
		}
		
		var result = stars.stream().mapToLong(s -> {
			var list = parts.stream().filter(p -> p.checkInRange(s.x, s.y)).toList();
			
			if(list.size() > 1) {
				return list.stream().mapToLong(k ->(long)k.number).reduce(1, (a, b) -> a * b);
			}
			return 0;
		}).sum();
		
		assertEquals(Long.parseLong(expected), result);
	}
	
	record Point(int x, int y) {
		
	}
	
	record Part(int number, int x, int y, int lenght) {
		boolean checkInRange(int cX, int cY) {
			return IntStream.range(x-1, x+lenght+1).anyMatch(a -> a == cY) && IntStream.range(y-1, y+2).anyMatch(a -> a == cX);
		}
	}
	
}
