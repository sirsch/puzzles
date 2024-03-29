package software.sirsch.sa4e.puzzles;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.iterators.PermutationIterator;

import static java.util.Spliterators.spliteratorUnknownSize;

/**
 * Diese Klasse stellt die Funktionalität zum Lösen eines Puzzles mittels Brute-Force-Ansatz.
 *
 * @author sirsch
 * @since 12.12.2022
 */
public class PuzzleSolver {

	/**
	 * Diese Methode löst das Puzzle durch Durchprobieren von Permutationen.
	 *
	 * @param puzzle das zu lösende Puzzle
	 * @return die Symbole, mit gesetzten Werten oder eine leere Liste, falls keine Lösung gefunden
	 * wurde
	 */
	public List<Symbol> solvePuzzle(@Nonnull final Puzzle puzzle) {
		return this.createPermutationStream()
				.filter(puzzle::isSolution)
				.findFirst()
				.map(permutation -> puzzle.getSymbols())
				.orElseGet(Collections::emptyList);
	}

	/**
	 * Diese Methode erzeugt einen Stream der Permutationen der Ziffern.
	 *
	 * @return der erzeugte Stream
	 */
	@Nonnull
	private Stream<List<Byte>> createPermutationStream() {
		return StreamSupport.stream(this.createPermutationSpliterator(), false);
	}

	/**
	 * Diese Methode erzeugt einen Spliterator der Permutationen der Ziffern.
	 *
	 * @return der erzeugte Spliterator
	 */
	@Nonnull
	private Spliterator<List<Byte>> createPermutationSpliterator() {
		return spliteratorUnknownSize(this.createPermutationIterator(), Spliterator.ORDERED);
	}

	/**
	 * Diese Methode erzeugt einen Iterator der Permutationen der Ziffern.
	 *
	 * @return der erzeugte Iterator
	 */
	@Nonnull
	private Iterator<List<Byte>> createPermutationIterator() {
		return new PermutationIterator<>(this.createDigits());
	}

	/**
	 * Diese Methode erzeugt die Liste der Werte für die einzelnen Stellen von 0 bis 10.
	 *
	 * @return die erzeugte Liste
	 */
	@Nonnull
	private List<Byte> createDigits() {
		final int base = 10;

		return IntStream.range(0, base)
				.mapToObj(value -> (byte) value)
				.collect(Collectors.toList());
	}
}
