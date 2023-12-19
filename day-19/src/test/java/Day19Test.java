import org.junit.jupiter.params.ParameterizedTest;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day19Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "19114"),
			@AocInputMapping(input = "input.txt", expected = "-1")
	})
	void part1(Stream<String> input, String expected) {
		var rules = new HashMap<String, Rule>();
		var pieces = new ArrayList<Piece>();
		var rulesParsing = new AtomicBoolean(true);
		input.forEach(l -> {
			if(l.isEmpty()) {
				rulesParsing.set(false);
			}
			else {
				if(rulesParsing.get()) {
					var r = new Rule(l);
					rules.put(r.name, r);
				} else {

					var str = l.replaceAll("[{}xmas=]", "").split(",");
					var x = Integer.parseInt(str[0]);
					var m = Integer.parseInt(str[1]);
					var a = Integer.parseInt(str[2]);
					var s = Integer.parseInt(str[3]);

					pieces.add(new Piece(x,m,a,s));
				}
			}
		});

		var ruleProcessor = new RuleProcessor(rules);

		var sum = pieces.stream().filter(ruleProcessor::doAccept).mapToLong(Piece::sum).sum();

		assertEquals(Long.parseLong(expected), sum);
	}

	class RuleProcessor {
		final HashMap<String, Rule> rules;

		RuleProcessor(HashMap<String, Rule> rules) {
			this.rules = rules;
		}

		boolean doAccept(Piece p) {
			var nextRule = "in";
			while(!nextRule.equals("R") && !nextRule.equals("A")) {

				//System.out.print(nextRule+" -> ");
				var rule = rules.get(nextRule);
				var evals = rule.evals;
				Optional<String> opt = Optional.empty();
				for(int i = 0; i < evals.size() && opt.isEmpty(); i++) {
					opt = evals.get(i).eval(p);
				}
				nextRule = opt.orElseGet(() -> rule.fallback);
			}
			//System.out.print(nextRule);
			//System.out.println();

			return nextRule.equals("A");
		}

		List<RangedPiece> processRanged(int fourThousand) {
			List<RangedPiece> pieces = new ArrayList<>();

			pieces.add(new RangedPiece(
					IntStream.rangeClosed(1, 4000).boxed().toList(),
					IntStream.rangeClosed(1, 4000).boxed().toList(),
					IntStream.rangeClosed(1, 4000).boxed().toList(),
					IntStream.rangeClosed(1, 4000).boxed().toList(),
					"in",
					"in"
			));

			// all ranged pieces must reach end!
			while(!pieces.stream().allMatch(p ->
					p.target().equals("A") ||
					p.target().equals("R"))) {
				pieces = pieces.stream().flatMap(
						p -> {
							if(p.target().equals("A") || p.target().equals("R")) {
								return Stream.of(p);
							}
							var rule = rules.get(p.target());

							var evalStream = rule.evals.stream().flatMap(k -> {
								if(k.operator().contains(">")) {
									if(k.operator().contains("x")) {
										return Stream.of(new RangedPiece(
												IntStream.rangeClosed(k.toEval+1, 4000).boxed().toList(),
												p.m(),
												p.a(),
												p.s(),
												k.target(), k.target()),
												new RangedPiece(
														IntStream.rangeClosed(1, k.toEval).boxed().toList(),
														p.m(),
														p.a(),
														p.s(),
														rule.fallback, rule.fallback));
									}
									if(k.operator().contains("m")) {
										return Stream.of(new RangedPiece(
														p.x(),
														IntStream.rangeClosed(k.toEval+1, 4000).boxed().toList(),
														p.a(),
														p.s(),
														k.target(), k.target()),
												new RangedPiece(
														p.x(),
														IntStream.rangeClosed(1, k.toEval).boxed().toList(),
														p.a(),
														p.s(),
														rule.fallback, rule.fallback));
									}
									if(k.operator().contains("a")) {
										return Stream.of(new RangedPiece(
														p.x(),
														p.m(),
														IntStream.rangeClosed(k.toEval+1, 4000).boxed().toList(),
														p.s(),
														k.target(), k.target()),
												new RangedPiece(
														p.x(),
														p.m(),
														IntStream.rangeClosed(1, k.toEval).boxed().toList(),
														p.s(),
														rule.fallback, rule.fallback));
									}
									if(k.operator().contains("s")) {
										return Stream.of(new RangedPiece(
														p.x(),
														p.m(),
														p.a(),
														IntStream.rangeClosed(k.toEval+1, 4000).boxed().toList(),
														k.target(), k.target()),
												new RangedPiece(
														p.x(),
														p.m(),
														p.a(),
														IntStream.rangeClosed(1, k.toEval).boxed().toList(),
														rule.fallback, rule.fallback));
									}
								}
								if(k.operator().contains("x")) {
									return Stream.of(new RangedPiece(
													IntStream.rangeClosed(k.toEval+1, 4000).boxed().toList(),
													p.m(),
													p.a(),
													p.s(),
													rule.fallback, rule.fallback),
											new RangedPiece(
													IntStream.range(1, k.toEval).boxed().toList(),
													p.m(),
													p.a(),
													p.s(),
													k.target(), k.target()));
								}
								if(k.operator().contains("m")) {
									return Stream.of(new RangedPiece(
													p.x(),
													IntStream.rangeClosed(k.toEval+1, 4000).boxed().toList(),
													p.a(),
													p.s(),
													rule.fallback, rule.fallback),
											new RangedPiece(
													p.x(),
													IntStream.range(1, k.toEval).boxed().toList(),
													p.a(),
													p.s(),
													k.target(), k.target()));
								}
								if(k.operator().contains("a")) {
									return Stream.of(new RangedPiece(
													p.x(),
													p.m(),
													IntStream.rangeClosed(k.toEval+1, 4000).boxed().toList(),
													p.s(),
													rule.fallback, rule.fallback),
											new RangedPiece(
													p.x(),
													p.m(),
													IntStream.range(1, k.toEval).boxed().toList(),
													p.s(),
													k.target(), k.target()));
								}
								if(k.operator().contains("s")) {
									return Stream.of(new RangedPiece(
													p.x(),
													p.m(),
													p.a(),
													IntStream.rangeClosed(k.toEval+1, 4000).boxed().toList(),
													rule.fallback, rule.fallback),
											new RangedPiece(
													p.x(),
													p.m(),
													p.a(),
													IntStream.range(1, k.toEval).boxed().toList(),
													k.target(), k.target()));
								}
								throw new IllegalArgumentException("");
							}).map(
								p::intersect
							).toList();

							// for debug
							return evalStream.stream();
							// return evalStream;
						}
				).toList();
			}

			return pieces;
		}
	}

	record Piece(int x, int m, int a, int s){
		int sum() {
			return x+m+a+s;
		}
	}

	record Eval(String operator, int toEval , String target) {
		Optional<String> eval(Piece input) {
			if(operator.contains(">")) {
				if(operator.contains("x") && input.x() > toEval) {
					return Optional.of(target);
				}
				if(operator.contains("m") && input.m() > toEval) {
					return Optional.of(target);
				}
				if(operator.contains("a") && input.a() > toEval) {
					return Optional.of(target);
				}
				if(operator.contains("s") && input.s() > toEval) {
					return Optional.of(target);
				}
			} else {
				if(operator.contains("x") && input.x() < toEval) {
					return Optional.of(target);
				}
				if(operator.contains("m") && input.m() < toEval) {
					return Optional.of(target);
				}
				if(operator.contains("a") && input.a() < toEval) {
					return Optional.of(target);
				}
				if(operator.contains("s") &&  input.s() < toEval) {
					return Optional.of(target);
				}
			}
			return Optional.empty();
		}
	}

	class Rule {
		final String name;

		final List<Eval> evals;

		final String fallback;


		Rule(String rule) {
			this.name = rule.substring(0, rule.indexOf("{"));
			var str = rule.substring(rule.indexOf("{")+1, rule.length()-1);

			var rList = new ArrayList<>(List.of(str.split(",")));
			fallback = rList.getLast();
			rList.remove(fallback);
			evals = rList.stream().map(s -> new Eval(s.split("[:]")[0], Integer.parseInt(s.split("[<>:]")[1]), s.split("[:]")[1])).toList();
		}
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", expected = "167409079868000"),
		//	@AocInputMapping(input = "input.txt", expected = "-1")
	})
	void part2(Stream<String> input, String expected) {
		var rules = new HashMap<String, Rule>();
		var rulesParsing = new AtomicBoolean(true);
		input.forEach(l -> {
			if(l.isEmpty()) {
				rulesParsing.set(false);
			}
			else {
				if(rulesParsing.get()) {
					var r = new Rule(l);
					rules.put(r.name, r);
				}
			}
		});

		var solver = new RuleProcessor(rules);
		var a = solver.processRanged(2).stream().filter(g -> "A".equals(g.target())).toList();

		var result = a.stream().mapToLong(RangedPiece::calcPos).sum();

		assertEquals(Long.parseLong(expected), result);
	}


	record RangedPiece(List<Integer> x, List<Integer>  m, List<Integer>  a, List<Integer>  s, String target, String path){
		RangedPiece intersect(RangedPiece other) {
			var intersectX = this.x.stream().filter(i -> other.x().contains(i)).toList();
			var intersectM = this.m.stream().filter(i -> other.m().contains(i)).toList();
			var intersectA = this.a.stream().filter(i -> other.a().contains(i)).toList();
			var intersectS = this.s.stream().filter(i -> other.s().contains(i)).toList();
			return new RangedPiece(intersectX, intersectM, intersectA, intersectS, other.target(), path()+" -> "+other.path());
		}

		long calcPos() {
			return (long)x.size()*m.size()*a.size()*s.size();
		}
	}
}
