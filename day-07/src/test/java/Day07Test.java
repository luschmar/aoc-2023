import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07Test {
	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "6440"),
			@AocInputMapping(input = "input.txt", solution = "253933213") 
	})
	void part1(Stream<String> input, String solution) {
		var cards = input.map(s -> new CardHand(s.split(" ")[0], Integer.parseInt(s.split(" ")[1]))).toList();

		var sorted = cards.stream().sorted((c1, c2) -> c1.compareTo(c2)).toList();

		var result = IntStream.range(0, sorted.size()).map(a -> {
			System.out.println(sorted.get(a).hand + " -> " + sorted.get(a).type + " -> " + sorted.get(a).value + "\t*\t"
					+ (sorted.size() - a));

			return sorted.get(a).value * (sorted.size() - a);
		}).sum();

		assertEquals(Integer.parseInt(solution), result);
	}

	private String replaceString(String string) {
		return string
				.replaceAll("K", "B")
				.replaceAll("Q", "C")
				.replaceAll("J", "D")
				.replaceAll("T", "E")
				.replaceAll("9", "F")
				.replaceAll("8", "G")
				.replaceAll("7", "H")
				.replaceAll("6", "I")
				.replaceAll("5", "J")
				.replaceAll("4", "K")
				.replaceAll("3", "L")
				.replaceAll("2", "M");
	}

	class CardHand implements Comparable<CardHand> {

		private String hand;
		private String forOrder;
		private int value;
		private HandyType type;

		public CardHand(String hand, int value) {
			this.hand = hand;
			this.forOrder = replaceString(hand);
			this.value = value;
			this.type = HandyType.parseString(hand);
		}

		@Override
		public int compareTo(Day07Test.CardHand o) {
			var typeSort = type.ordinal() - o.type.ordinal();

			if (typeSort != 0) {
				return typeSort;
			}

			return forOrder.compareTo(o.forOrder);
		}
	}

	enum HandyType {
		FiveOK, FourOK, FH, ThreeOK, TwoP, OneP, HC;

		static HandyType parseJokerString(String s) {
			var replace = List.of("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2");

			var best = replace.stream().mapToInt(r -> parseString(s.replaceAll("J", r)).ordinal()).min().orElseThrow();

			return HandyType.values()[best];
		}

		static HandyType parseString(String s) {
			Map<Integer, Long> map = s.codePoints().boxed()
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

			if (map.containsValue(5l)) {
				return FiveOK;
			}
			if (map.containsValue(4l)) {
				return FourOK;
			}
			if (map.containsValue(3l) && map.containsValue(2l)) {
				return FH;
			}
			if (map.containsValue(3l)) {
				return ThreeOK;
			}
			if (map.containsValue(2l) && map.values().stream().filter(a -> a == 2l).count() > 1) {
				return TwoP;
			}
			if (map.containsValue(2l)) {
				return OneP;
			}
			return HC;
		}
	}

	private String replaceJokerString(String string) {
		return string
				.replaceAll("K", "B")
				.replaceAll("Q", "C")
				.replaceAll("J", "N")
				.replaceAll("T", "E")
				.replaceAll("9", "F")
				.replaceAll("8", "G")
				.replaceAll("7", "H")
				.replaceAll("6", "I")
				.replaceAll("5", "J")
				.replaceAll("4", "K")
				.replaceAll("3", "L")
				.replaceAll("2", "M");
	}

	class JokerHand implements Comparable<JokerHand> {

		private String hand;
		private String forOrder;
		private int value;
		private HandyType type;

		public JokerHand(String hand, int value) {
			this.hand = hand;
			this.forOrder = replaceJokerString(hand);
			this.value = value;
			this.type = HandyType.parseJokerString(hand);
		}

		@Override
		public int compareTo(Day07Test.JokerHand o) {
			var typeSort = type.ordinal() - o.type.ordinal();

			if (typeSort != 0) {
				return typeSort;
			}

			return forOrder.compareTo(o.forOrder);
		}
	}

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "5905"),
			@AocInputMapping(input = "input.txt", solution = "253473930") 
	})
	void part2(Stream<String> input, String solution) {
		var cards = input.map(s -> new JokerHand(s.split(" ")[0], Integer.parseInt(s.split(" ")[1]))).toList();

		var sorted = cards.stream().sorted((c1, c2) -> c1.compareTo(c2)).toList();

		var result = IntStream.range(0, sorted.size()).map(a -> {
			System.out.println(sorted.get(a).hand + " -> " + sorted.get(a).type + " -> " + sorted.get(a).value + "\t*\t"
					+ (sorted.size() - a));
			return sorted.get(a).value * (sorted.size() - a);
		}).sum();

		assertEquals(Integer.parseInt(solution), result);
	}
}
