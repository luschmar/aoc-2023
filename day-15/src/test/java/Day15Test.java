import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "1320"),
			@AocInputMapping(input = "input.txt", expected = "503487")
	})
	void part1(Stream<String> input, String expected) {
		var result = Arrays.stream(input.toList().getFirst().split(",")).mapToInt(this::doHASH).sum();

		assertEquals(Integer.parseInt(expected), result);
	}

	int doHASH(String string) {
		int hash = 0;
		for(var c : string.toCharArray()) {
			hash += c;
			hash *= 17;
			hash %= 256;
		}
		return hash;
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "145"),
			@AocInputMapping(input = "input.txt", expected = "261505")
	})
	void part2(Stream<String> input, String expected) {
		var boxes = IntStream.range(0, 256).mapToObj(i -> new Box(i, new ArrayList<>())).toList();

		var steps = input.toList().getFirst().split(",");
		for(var step : steps) {
			var label = step.split("[-=]")[0];
			var box = boxes.get(doHASH(label));
			var lensList = box.lenses();
			if(step.contains("=")) {
				var focal = Integer.parseInt(step.split("=")[1]);
				var oldLensI = IntStream.range(0, lensList.size()).filter(a -> label.equals(lensList.get(a).label())).findFirst();
				if(oldLensI.isPresent()) {
					lensList.set(oldLensI.getAsInt(), new Lens(label, focal));
				}else {
					lensList.add(new Lens(label, focal));
				}
			}
			if(step.contains("-")) {
				var lensToRemove = lensList.stream().filter(a -> label.equals(a.label())).findFirst();
				lensToRemove.ifPresent(lensList::remove);
			}
		}

		var result = boxes.stream().mapToInt(Box::focusingPower).sum();

		assertEquals(Integer.parseInt(expected), result);
	}

	record Box(int index, List<Lens> lenses) {
		int focusingPower() {
			return IntStream.range(0, lenses().size()).map(l -> (index+1)*(l+1)*lenses().get(l).focalLength()).sum();
		}
	}

	record Lens(String label, int focalLength) {
	}
}
