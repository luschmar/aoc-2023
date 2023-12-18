import org.junit.jupiter.params.ParameterizedTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.math.BigDecimal.*;
import static java.math.RoundingMode.FLOOR;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day18Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "62"),
			@AocInputMapping(input = "input.txt", expected = "36679")
	})
	void part1(Stream<String> input, String expected) {
		var instruction = input.map(s -> {
			var dir = Dir.valueOf(s.split(" ")[0]);
			var deep = Integer.parseInt(s.split(" ")[1]);
			var color = s.split(" ")[2];
			return new DigInstruction(dir, deep, color);
		}).toList();

		var pointList = new ArrayList<Point>();
		var currentPoint = new Point(0, 0);
		pointList.add(currentPoint);
		for (var ins : instruction) {
			for (int s = 0; s < ins.deep(); s++) {
				currentPoint = switch (ins.dir()) {
					case U -> currentPoint.nextUp();
					case D -> currentPoint.nextDown();
					case L -> currentPoint.nextLeft();
					case R -> currentPoint.nextRight();
				};
				pointList.add(currentPoint);
			}
		}

		var minX = pointList.stream().mapToInt(Point::x).min().getAsInt();
		var maxX = pointList.stream().mapToInt(Point::x).max().getAsInt();
		var minY = pointList.stream().mapToInt(Point::y).min().getAsInt();
		var maxY = pointList.stream().mapToInt(Point::y).max().getAsInt();

		var size = new AtomicInteger(0);
		var in = new AtomicBoolean(false);
		IntStream.rangeClosed(minY, maxY).forEach(y -> {
			IntStream.rangeClosed(minX, maxX).forEach(x -> {
				// System.out.print(pointList.contains(new Point(x, y)) ? "#" : ".");

				var current = new Point(x, y);
				if (isEdgeIn(pointList, current)) {
					var old = in.get();
					in.set(!old);
					if (in.get()) {
						System.out.print("↑");
						size.addAndGet(1);
					} else {
						System.out.print("↓");
						size.addAndGet(1);
					}
				} else {
					if (in.get()) {
						System.out.print("#");
						size.addAndGet(1);
					} else {
						if (pointList.contains(current)) {
							System.out.print("#");
							size.addAndGet(1);
						} else {
							System.out.print(".");
						}
					}
				}
			});
			System.out.println();
			in.set(false);
		});

		assertEquals(Integer.parseInt(expected), size.get());
	}

	boolean isEdgeIn(List<Point> points, Point current) {
		if (!points.contains(current)) {
			return false;
		}
		var down = current.nextDown();
		var up = current.nextUp();
		var left = current.nextLeft();
		var right = current.nextRight();
		// L
		if ((points.contains(up)) && points.contains(right)) {
			return true;
		}
		// J
		if ((points.contains(left) && points.contains(up))) {
			return true;
		}

		// |
		return points.contains(down) && points.contains(up);
	}

	record Point(int x, int y) {
		Point nextUp() {
			return new Point(x, y - 1);
		}

		Point nextDown() {
			return new Point(x, y + 1);
		}

		Point nextLeft() {
			return new Point(x - 1, y);
		}

		Point nextRight() {
			return new Point(x + 1, y);
		}
	}

	record DigInstruction(Dir dir, int deep, String color) {
	}

	class DigHexInstruction {
		final long deep;
		final Dir dir;

		DigHexInstruction(String color) {
			var hex = color.replaceAll("[()#]", "");
			var a = hex.substring(0, hex.length() - 1);
			var dIndex = Integer.parseInt(hex.substring(hex.length() - 1));
			this.deep = Long.parseLong(a, 16);
			this.dir = Dir.values()[dIndex];
		}

		long deep() {
			return deep;
		}

		Dir dir() {
			return dir;
		}
	}

	enum Dir {
		R, D, L, U
	}

	record LongPoint(long x, long y) {
		LongPoint nextUp(long deep) {
			return new LongPoint(x, y - deep);
		}

		LongPoint nextDown(long deep) {
			return new LongPoint(x, y + deep);
		}

		LongPoint nextLeft(long deep) {
			return new LongPoint(x - deep, y);
		}

		LongPoint nextRight(long deep) {
			return new LongPoint(x + deep, y);
		}
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "952408144115"),
			@AocInputMapping(input = "input.txt", expected = "88007104020978")
	})
	void part2(Stream<String> input, String expected) {
		var instruction = input.map(s -> {
			var color = s.split(" ")[2];
			return new DigHexInstruction(color);
		}).toList();

		var pointList = new ArrayList<LongPoint>();
		var currentPoint = new LongPoint(0, 0);
		pointList.add(currentPoint);
		for (var ins : instruction) {
			currentPoint = switch (ins.dir()) {
				case U -> currentPoint.nextUp(ins.deep());
				case D -> currentPoint.nextDown(ins.deep());
				case L -> currentPoint.nextLeft(ins.deep());
				case R -> currentPoint.nextRight(ins.deep());
			};
			pointList.add(currentPoint);
		}

		var area = shoelaceFormula(pointList);
		// add boundary 🤦
		var boundary = instruction.stream().mapToLong(DigHexInstruction::deep).sum();
		var b = valueOf(boundary).divide(TWO, FLOOR).add(ONE);

		assertEquals(valueOf(Long.parseLong(expected)), area.add(b));
	}

	BigDecimal shoelaceFormula(ArrayList<LongPoint> pointList) {
		// https://de.wikipedia.org/wiki/Gau%C3%9Fsche_Trapezformel
		var sum = new AtomicReference<>(ZERO);
		IntStream.range(0, pointList.size() - 1).forEach(l -> {
			var a = pointList.get(l);
			var b = pointList.get(l + 1);

			var aX = valueOf(a.x());
			var aY = valueOf(a.y());
			var bX = valueOf(b.x());
			var bY = valueOf(b.y());

			var left = aX.multiply(bY);
			var right = aY.multiply(bX);
			sum.set(sum.get().add(left.subtract(right)));
		});

		return sum.get().divide(TWO, RoundingMode.UNNECESSARY);
	}
}
