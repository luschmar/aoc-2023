import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
		for(var l : all) {
			if("".equals(l)) {
				currentTable = new ArrayList<>();
				tables.add(currentTable);
				continue;
			}
			currentTable.add(l);
		}

		var sumH = tables.stream().mapToInt(this::findMirror).sum()*100;
		var sumV = tables.stream().map(this::transpose).mapToInt(this::findMirror).sum();

		var result = sumH+sumV;


		assertEquals(Integer.parseInt(solution), result);
	}

	private List<String> transpose(List<String> t) {
		var newTable = new ArrayList<String>();
		IntStream.range(0, t.getFirst().length()).forEach(i -> newTable.add(t.stream().map(s -> Character.toString(s.charAt(i))).collect(Collectors.joining()))); ;
		return newTable;
	}

	private int findMirror(List<String> t) {
		var horizontal = IntStream.range(0, t.size()-1).filter(i -> t.get(i).equals(t.get(i+1)))
				.filter(c -> {
					// mirror candidate; check rest
					int mirrorSize = Integer.min(c+1, t.size()-c-1);

					var res = IntStream.range(0, mirrorSize).allMatch(m -> t.get(c-m).equals(t.get(c+1+m)));
					if(res) {
						printTable(t);
						System.out.println(" -> "+c+" mirrorsize "+mirrorSize);
					}
					return res;
				}).map(cc -> cc+1).boxed().toList();
		if(horizontal.size() > 1) {
			System.err.println("Oh no...");
		}
		if(horizontal.isEmpty()){
			return 0;
		}

		return horizontal.stream().mapToInt(a -> a).min().getAsInt();
	}

	private void printTable(List<String> t) {
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
		for(var l : all) {
			if("".equals(l)) {
				currentTable = new ArrayList<>();
				tables.add(currentTable);
				continue;
			}
			currentTable.add(l);
		}

		var result = tables.stream().mapToInt(t -> {
			var t1 = t;
			var t2 = transpose(t);

			var s1 = findSmudges(t1);
			var s2 = findSmudges(t2);

			if(s1.isEmpty()) {
				return s2.getFirst().c();
			}
			return s1.getFirst().c() * 100;
		}).sum();

		assertEquals(Integer.parseInt(solution), result);
	}

	private List<String> findSmudgeAndRepair(List<String> t) {
		var smudges = findSmudges(t);
		if(smudges.isEmpty()) {
			System.err.println("Oh no Smudge");
			return List.of();
		}
		if(smudges.size() > 1) {
			System.err.println("Oh no multiple Smudge");
		}
		var s = smudges.getFirst();
		var newTable = new ArrayList<String>();

		for(int i = 0; i < t.size(); i++) {
			if(i == s.x()) {
				var b = new StringBuilder(t.get(i));
				var replace = b.charAt(s.y()) == '#' ? '.' : '#';
				b.replace(s.y(), s.y()+1, Character.toString(replace));
				newTable.add(b.toString());
			} else {
				newTable.add(t.get(i));
			}
		}
		return newTable;
	}

	private List<Smudge> findSmudges(List<String> t) {
		return IntStream.range(0, t.size()-1)
				.mapToObj(c -> {
					// mirror candidate; check rest
					int mirrorSize = Integer.min(c+1, t.size()-c-1);

					int error = 0;
					int errorLine = 0;
					int errorLocation = 0;
					for(int i = 0; (error <= 1) && i < mirrorSize; i++) {
						errorLine = c-i;
						var up = t.get(c-i);
						var down = t.get(c+1+i);
						if(up.equals(down)) {
							continue;
						}
						error += (int) IntStream.range(0, up.length()).filter(e -> up.charAt(e) != down.charAt(e)).count();
						errorLocation = IntStream.range(0, up.length()).filter(e -> up.charAt(e) != down.charAt(e)).findFirst().getAsInt();
					}

					if(error == 1) {
						System.out.println("ERROR: "+(errorLine)+":"+errorLocation+" -> "+c);
						return new Smudge(errorLine, errorLocation, c+1);
					}
					return null;
				}).filter(Objects::nonNull).toList();
	}

	record Smudge(int x, int y, int c) {
	}
}
