import org.junit.jupiter.params.ParameterizedTest;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "4"),
			@AocInputMapping(input = "test2.txt", solution = "8"),
			@AocInputMapping(input = "input.txt", solution = "6773")
	})
	void part1(Stream<String> input, String solution) {
		var array = input.map(String::toCharArray).toArray(char[][]::new);
		
		// find start
		int x=0,y= 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				var s = Character.toString(array[i][j]);
				if("S".equals(s)) {
					y = i;
					x = j;
				}
			}
		}
		System.out.println("start: "+x+":"+y);
		
		// follow
		var distanceMap = new int[array.length][array[0].length];
		var a = new Position(x, y, array, distanceMap);
		
		while(a.solve()) {
			//a.printMaze();
		}
		a.printMazeTwo();
		
		var dist = new int[] {distanceMap[y][x-1>0?x-1:x]+1,distanceMap[y][x+1]+1, distanceMap[y-1][x]+1, distanceMap[y+1][x]+1};
		int result = IntStream.of(dist).max().getAsInt() / 2;
		
		
		assertEquals(Integer.parseInt(solution),result);
	}

	class Position {
		Integer startX = 0;
		Integer startY = 0;
		AtomicInteger x;
		AtomicInteger y;
		int currentValue;
		
		private char[][] map;
		private int[][] distance;
		
		public Position(int x, int y, char[][] map, int[][] distance) {
			this.startX = x;
			this.startY = y;
			this.x = new AtomicInteger(x);
			this.y = new AtomicInteger(y);
			this.map = map;
			this.distance = distance;
		}
		
		boolean solve() {
			if(canWalkNorth()) {
				y.decrementAndGet();
				distance[y.get()][x.get()] = ++currentValue;
				return true;
			}
			if(canWalkSouth()) {
				y.incrementAndGet();
				distance[y.get()][x.get()] = ++currentValue;
				return true;
			}
			if(canWalkWest()) {
				x.decrementAndGet();
				distance[y.get()][x.get()] = ++currentValue;
				return true;
			}
			if(canWalkEast()) {
				x.incrementAndGet();
				distance[y.get()][x.get()] = ++currentValue;
				return true;
			}
			
			// reset && check
			x.set(startX);
			y.set(startY);
			
			return canWalkNorth() || canWalkSouth() || canWalkWest() || canWalkEast();
		}

		private boolean canWalkNorth() {
			int candidateX = x.get();
			int candidateY = y.get()-1;
			if(outOfBound(candidateX, candidateY)) {
				return false;
			}
			if(distance[candidateY][candidateX] > 0) {
				return false;
			}
			var currentTile = map[y.get()][x.get()];
			var nextTile = map[candidateY][candidateX];
			if(currentTile == 'S' || currentTile == '|'|| currentTile == 'L'|| currentTile == 'J') {
				if(nextTile == '|' || nextTile == 'F' || nextTile == '7') {
					return true;
				}
			}
			
			return false;
		}
		

		private boolean canWalkSouth() {
			int candidateX = x.get();
			int candidateY = y.get()+1;
			if(outOfBound(candidateX, candidateY)) {
				return false;
			}
			if(distance[candidateY][candidateX] > 0) {
				return false;
			}
			var currentTile = map[y.get()][x.get()];
			var nextTile = map[candidateY][candidateX];
			if(currentTile == 'S' || currentTile == '|'|| currentTile == 'F'|| currentTile == '7') {
				if(nextTile == '|' || nextTile == 'L' || nextTile == 'J') {
					return true;
				}
			}
			
			return false;
		}

		private boolean canWalkWest() {
			int candidateX = x.get()-1;
			int candidateY = y.get();
			if(outOfBound(candidateX, candidateY)) {
				return false;
			}
			if(distance[candidateY][candidateX] > 0) {
				return false;
			}
			var currentTile = map[y.get()][x.get()];
			var nextTile = map[candidateY][candidateX];
			if(currentTile == 'S' || currentTile == '-'|| currentTile == 'J'|| currentTile == '7') {
				if(nextTile == '-' || nextTile == 'L' || nextTile == 'F') {
					return true;
				}
			}
			
			return false;
		}
		

		private boolean canWalkEast() {
			int candidateX = x.get()+1;
			int candidateY = y.get();
			if(outOfBound(candidateX, candidateY)) {
				return false;
			}
			if(distance[candidateY][candidateX] > 0) {
				return false;
			}
			var currentTile = map[y.get()][x.get()];
			var nextTile = map[candidateY][candidateX];
			if(currentTile == 'S' || currentTile == '-'|| currentTile == 'L'|| currentTile == 'F') {
				if(nextTile == '-' || nextTile == 'J' || nextTile == '7') {
					return true;
				}
			}
			
			return false;
		}


		private boolean outOfBound(int candidateX, int candidateY) {
			return candidateX < 0 || candidateX >= map[0].length || candidateY < 0 || candidateY >= map.length;
		}
		
		void printMaze(){
			for(int i = 0; i < distance.length; i++) {
				for(int j = 0; j < distance[i].length; j++) {
					System.out.print(distance[i][j]);
				}
				System.out.println();
			}
			System.out.println();
			System.out.println();
		}
		
		void printMazeTwo(){
			for(int i = 0; i < distance.length; i++) {
				for(int j = 0; j < distance[i].length; j++) {
					if(map[i][j] == 'S') {
						System.out.print("S");
					}
					else {
						System.out.print(distance[i][j] > 0 ? getPipe(map[i][j]) : "#");
					}
				}
				System.out.println();
			}
			System.out.println();
			System.out.println();
		}
		
		int countInner() {
			int count = 0;
			boolean shouldCount = false;
			
			for(int i = 0; i < distance.length; i++) {
				for(int j = 0; j < distance[i].length; j++) {
					if(distance[i][j] > 0 || map[i][j] == 'S') {
						if(map[i][j] == '|' || map[i][j] == 'L' || map[i][j] == 'J') {
							shouldCount = !shouldCount;
							if(shouldCount) {
								System.out.print("↑");
							}
							else {
								System.out.print("↓");
							}
						} else {
							System.out.print(getPipe(map[i][j]));
						}
						
					} else {
						if(shouldCount) {
							count++;
							System.out.print("+");
						} else {
							System.out.print("#");
						}
					}
				}
				shouldCount = false;
				System.out.println();
			}

			return count;
		}

		private String getPipe(char c) {
			if(c == 'F')
				return "┌";
			if(c == '7')
				return "┐";
			if(c == 'J')
				return "┘";
			if(c == 'L')
				return "└";
			return Character.toString(c);
		}
	}
	

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test3.txt", solution = "4"),
			@AocInputMapping(input = "test4.txt", solution = "8"),
			@AocInputMapping(input = "test5.txt", solution = "10"),
			@AocInputMapping(input = "input.txt", solution = "493")
	})
	void part2(Stream<String> input, String solution) {
		var array = input.map(String::toCharArray).toArray(char[][]::new);
		
		// find start
		int x=0,y= 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				var s = Character.toString(array[i][j]);
				if("S".equals(s)) {
					y = i;
					x = j;
				}
			}
		}
		
		// follow
		var distanceMap = new int[array.length][array[0].length];
		var a = new Position(x, y, array, distanceMap);
		
		while(a.solve()) {
			//a.printMaze();
		}
		a.printMazeTwo();
		
		var result = a.countInner();
		
		assertEquals(Integer.parseInt(solution),result);	}
}
