import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test {
	@ParameterizedTest
	@AocFileSource(inputs = { @AocInputMapping(input = "test.txt", expected  = "46"),
	 @AocInputMapping(input = "input.txt", expected = "7472")
	})
	void part1(Stream<String> input, String expected) {
		var mirrorData = input.map(StringBuilder::new).toList();

		var mirrorSet = new HashSet<Mirror>();
		var initBeam = new LightBeamSolver(mirrorData, 0,0, LightDir.EAST, mirrorSet);
		var beams = new ArrayList<LightBeamSolver>();
		beams.add(initBeam);
		
		while(beams.stream().anyMatch(a -> !a.isSolved())) {
			beams.forEach(b -> b.move());
			var forked = beams.stream().map(LightBeamSolver::copyOnMirror).filter(Objects::nonNull).toList();
			beams.addAll(forked);
		}
		
		AtomicInteger count = new AtomicInteger(0);
		IntStream.range(0, mirrorData.size()).forEach(x -> {
				IntStream.range(0, mirrorData.getFirst().length()).forEach(y -> {
					if(beams.stream().anyMatch(b -> b.energyzeData.get(x).charAt(y) == '#')) {
						count.incrementAndGet();
						System.out.print("#");
					} else {
						System.out.print(".");
					}
				});
				System.out.println();
		});
		
		assertEquals(Integer.parseInt(expected), count.get());
	}

	class LightBeamSolver {
		int x;
		int y;
		LightDir dir;
		List<StringBuilder> mirrorData;
		List<StringBuilder> energyzeData;
		Set<Mirror> mirrorProcessed;
		boolean skipProcessed;

		LightBeamSolver(List<StringBuilder> mirrorData, int x, int y, LightDir dir, Set<Mirror> mirrorProcessed) {
			this.mirrorData = mirrorData;
			this.energyzeData = copyData(mirrorData);
			this.x = x;
			this.y = y;
			this.dir = dir;
			determDir(mirrorData.get(x).charAt(y));
			this.mirrorProcessed = mirrorProcessed;
			this.skipProcessed = true;

			energizeCurrent();
		}

		void energizeCurrent() {
			if(isSolved()) {
				return;
			}
			energyzeData.get(x).setCharAt(y, '#');
		}

		void move() {
			// prevent move
			if (isSolved()) {
				return;
			}

			switch (dir) {
			case NORTH:
				this.x--;
				break;
			case SOUTH:
				this.x++;
				break;
			case EAST:
				this.y++;
				break;
			case WEST:
				this.y--;
				break;
			}
			// do nothing
			if (isSolved()) {
				return;
			}
			energizeCurrent();
			
			var c = mirrorData.get(x).charAt(y);
			determDir(c);
		}
		
		void moveWithoutCheck() {
			switch (dir) {
			case NORTH:
				this.x--;
				break;
			case SOUTH:
				this.x++;
				break;
			case EAST:
				this.y++;
				break;
			case WEST:
				this.y--;
				break;
			}
		}
		
		private void determDir(char c) {
			if(c == '/' && this.dir == LightDir.EAST) {
				this.dir = LightDir.NORTH;
				return;
			}
			if(c == '/' && this.dir == LightDir.WEST) {
				this.dir = LightDir.SOUTH;
				return;
			}
			if(c == '/' && this.dir == LightDir.NORTH) {
				this.dir = LightDir.EAST;
				return;
			}
			if(c == '/' && this.dir == LightDir.SOUTH) {
				this.dir = LightDir.WEST;
				return;
			}
			

			if(c == '\\' && this.dir == LightDir.EAST) {
				this.dir = LightDir.SOUTH;
				return;
			}
			if(c == '\\' && this.dir == LightDir.WEST) {
				this.dir = LightDir.NORTH;
				return;
			}
			if(c == '\\' && this.dir == LightDir.NORTH) {
				this.dir = LightDir.WEST;
				return;
			}
			if(c == '\\' && this.dir == LightDir.SOUTH) {
				this.dir = LightDir.EAST;
				return;
			}
		}

		LightBeamSolver copyOnMirror() {
			if(isSolved()) {
				return null;
			}
			
			var c = mirrorData.get(x).charAt(y);
			if(c == '|' && (this.dir == LightDir.EAST || this.dir == LightDir.WEST)) {
				if(!this.mirrorProcessed.add(new Mirror(x, y))) {
					skipProcessed = false;
					return null;
				}
				this.dir = LightDir.NORTH;
				var newBeam = new LightBeamSolver(mirrorData, x, y, LightDir.SOUTH, mirrorProcessed);
				return newBeam;
			}
			if(c == '-' && (this.dir == LightDir.NORTH || this.dir == LightDir.SOUTH)) {
				if(!this.mirrorProcessed.add(new Mirror(x, y))) {
					skipProcessed = false;
					return null;
				}
				this.dir = LightDir.WEST;
				var newBeam = new LightBeamSolver(mirrorData, x, y, LightDir.EAST, mirrorProcessed);
				return newBeam;
			}
			
			return null;
		}

		private boolean isSolved() {
			if(!skipProcessed) {
				if(this.mirrorProcessed.contains(new Mirror(this.x, this.y))) {
					return true;
				}
			}
			return this.x < 0 || 
					this.x >= this.mirrorData.size() || 
					this.y < 0 || 
					this.y >= this.mirrorData.get(0).length();
		}
	}
	
	record Mirror(int x, int y) {
		
	}

	enum LightDir {
		NORTH, EAST, SOUTH, WEST
	}
	
	static List<StringBuilder> copyData(List<StringBuilder> data) {
		return data.stream().map(a -> new StringBuilder(a.toString())).toList();
	}


	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", expected  = "51"),
			@AocInputMapping(input = "input.txt", expected = "7716")
	})
	void part2(Stream<String> input, String expected) {
		var mirrorData = input.map(StringBuilder::new).toList();
		var possibleSolList = new ArrayList<Integer>();

		for(int i = 0; i < mirrorData.size(); i++) {
			var mirrorSet = new HashSet<Mirror>();
			var initBeam = new LightBeamSolver(mirrorData, i,0, LightDir.EAST, mirrorSet);
			var beams = new ArrayList<LightBeamSolver>();
			beams.add(initBeam);
			
			var forked = beams.stream().map(LightBeamSolver::copyOnMirror).filter(Objects::nonNull).toList();
			beams.addAll(forked);
			while(beams.stream().anyMatch(a -> !a.isSolved())) {
				beams.forEach(b -> b.move());
				forked = beams.stream().map(LightBeamSolver::copyOnMirror).filter(Objects::nonNull).toList();
				beams.addAll(forked);
			}
			
			AtomicInteger count = new AtomicInteger(0);
			IntStream.range(0, mirrorData.size()).forEach(x -> {
					IntStream.range(0, mirrorData.getFirst().length()).forEach(y -> {
						if(beams.stream().anyMatch(b -> b.energyzeData.get(x).charAt(y) == '#')) {
							count.incrementAndGet();
						} else {
						}
					});
			});
			possibleSolList.add(count.get());
		}
		for(int i = 0; i < mirrorData.size(); i++) {
			var mirrorSet = new HashSet<Mirror>();
			var initBeam = new LightBeamSolver(mirrorData, i, mirrorData.get(i).length()-1, LightDir.WEST, mirrorSet);
			var beams = new ArrayList<LightBeamSolver>();
			beams.add(initBeam);
			
			var forked = beams.stream().map(LightBeamSolver::copyOnMirror).filter(Objects::nonNull).toList();
			beams.addAll(forked);
			while(beams.stream().anyMatch(a -> !a.isSolved())) {
				beams.forEach(b -> b.move());
				forked = beams.stream().map(LightBeamSolver::copyOnMirror).filter(Objects::nonNull).toList();
				beams.addAll(forked);
			}
			
			AtomicInteger count = new AtomicInteger(0);
			IntStream.range(0, mirrorData.size()).forEach(x -> {
					IntStream.range(0, mirrorData.getFirst().length()).forEach(y -> {
						if(beams.stream().anyMatch(b -> b.energyzeData.get(x).charAt(y) == '#')) {
							count.incrementAndGet();
						} else {
						}
					});
			});
			possibleSolList.add(count.get());
		}
		for(int i = 0; i < mirrorData.get(0).length(); i++) {
			var mirrorSet = new HashSet<Mirror>();
			var initBeam = new LightBeamSolver(mirrorData, 0, i, LightDir.SOUTH, mirrorSet);
			var beams = new ArrayList<LightBeamSolver>();
			beams.add(initBeam);
			
			var forked = beams.stream().map(LightBeamSolver::copyOnMirror).filter(Objects::nonNull).toList();
			beams.addAll(forked);
			while(beams.stream().anyMatch(a -> !a.isSolved())) {
				beams.forEach(b -> b.move());
				forked = beams.stream().map(LightBeamSolver::copyOnMirror).filter(Objects::nonNull).toList();
				beams.addAll(forked);
			}
			
			AtomicInteger count = new AtomicInteger(0);
			IntStream.range(0, mirrorData.size()).forEach(x -> {
					IntStream.range(0, mirrorData.getFirst().length()).forEach(y -> {
						if(beams.stream().anyMatch(b -> b.energyzeData.get(x).charAt(y) == '#')) {
							count.incrementAndGet();
						} else {
						}
					});
			});
			possibleSolList.add(count.get());
		}
		for(int i = 0; i < mirrorData.get(0).length(); i++) {
			var mirrorSet = new HashSet<Mirror>();
			var initBeam = new LightBeamSolver(mirrorData, mirrorData.size()-1, i, LightDir.NORTH, mirrorSet);
			var beams = new ArrayList<LightBeamSolver>();
			beams.add(initBeam);
			
			var forked = beams.stream().map(LightBeamSolver::copyOnMirror).filter(Objects::nonNull).toList();
			beams.addAll(forked);
			while(beams.stream().anyMatch(a -> !a.isSolved())) {
				beams.forEach(b -> b.move());
				forked = beams.stream().map(LightBeamSolver::copyOnMirror).filter(Objects::nonNull).toList();
				beams.addAll(forked);
			}
			
			AtomicInteger count = new AtomicInteger(0);
			IntStream.range(0, mirrorData.size()).forEach(x -> {
					IntStream.range(0, mirrorData.getFirst().length()).forEach(y -> {
						if(beams.stream().anyMatch(b -> b.energyzeData.get(x).charAt(y) == '#')) {
							count.incrementAndGet();
						} else {
						}
					});
			});
			possibleSolList.add(count.get());
		}
		
		var result = possibleSolList.stream().mapToInt(a -> a.intValue()).max().getAsInt();
		
		assertEquals(Integer.parseInt(expected), result);
	}
}
