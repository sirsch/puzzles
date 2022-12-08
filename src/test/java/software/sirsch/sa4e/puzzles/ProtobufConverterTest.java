package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link ProtobufConverter} bereit.
 *
 * @author sirsch
 * @since 05.12.2022
 */
public class ProtobufConverterTest {

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol} A enthalten.
	 */
	private Symbol symbolA;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol} B enthalten.
	 */
	private Symbol symbolB;

	/**
	 * Dieses Feld soll den Mock für {@link Cell} enthalten.
	 */
	private Cell cell;

	/**
	 * Dieses Feld soll dem Mock für {@link Puzzle} enthalten.
	 */
	private Puzzle puzzle;

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleBuilder} enthalten.
	 */
	private PuzzleBuilder puzzleBuilder;

	/**
	 * Dieses Feld soll den Mock für die Fabrik für {@link PuzzleBuilder} enthalten.
	 */
	private Supplier<PuzzleBuilder> puzzleBuilderFactory;

	/**
	 * Dieses Feld soll den Mock für die Fabrik für {@link Cell} enthalten.
	 */
	private Function<List<Symbol>, Cell> cellFactory;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private ProtobufConverter objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.symbolA = mock(Symbol.class);
		this.symbolB = mock(Symbol.class);
		this.cell = mock(Cell.class);
		this.puzzle = mock(Puzzle.class);
		this.puzzleBuilder = mock(PuzzleBuilder.class);
		this.puzzleBuilderFactory = mock(Supplier.class);
		this.cellFactory = mock(Function.class);
		when(this.puzzleBuilderFactory.get()).thenReturn(this.puzzleBuilder);
		when(this.puzzleBuilder.findOrCreateSymbol(any())).thenCallRealMethod();
		when(this.puzzleBuilder.findOrCreateSymbol(eq(0), any())).thenReturn(this.symbolA);
		when(this.puzzleBuilder.findOrCreateSymbol(eq(1), any())).thenReturn(this.symbolB);
		when(this.cellFactory.apply(List.of(this.symbolA, this.symbolB, this.symbolA)))
				.thenReturn(this.cell);
		when(this.puzzleBuilder.build()).thenReturn(this.puzzle);

		this.objectUnderTest = new ProtobufConverter(this.puzzleBuilderFactory, this.cellFactory);
	}

	/**
	 * Diese Methode prüft {@link ProtobufConverter#ProtobufConverter()}.
	 */
	@Test
	public void testDefaultConstructor() {
		this.objectUnderTest = new ProtobufConverter();

		assertThrows(
				IllegalArgumentException.class,
				() -> this.objectUnderTest.createPuzzle(
						Puzzles.SolvePuzzleRequest.getDefaultInstance()));
	}

	/**
	 * Diese Methode prüft {@link ProtobufConverter#createPuzzle(SolvePuzzleRequest)}.
	 */
	@Test
	public void testCreatePuzzle() {
		InOrder orderVerifier = inOrder(this.puzzleBuilder);
		Puzzle result;

		result = this.objectUnderTest.createPuzzle(
				SolvePuzzleRequest.newBuilder()
						.addSymbols(Puzzles.Symbol.newBuilder()
								.setId(0)
								.setDescription("A")
								.build())
						.addSymbols(Puzzles.Symbol.newBuilder()
								.setId(1)
								.setDescription("B")
								.build())
						.addCells(Puzzles.Cell.newBuilder()
								.setRow(1)
								.setColumn(2)
								.addAllNumberAsSymbolIds(List.of(0, 1, 0)))
						.build());

		assertEquals(this.puzzle, result);
		orderVerifier.verify(this.puzzleBuilder).findOrCreateSymbol(
				argThat(arg -> arg.getId() == 0 && "A".equals(arg.getDescription())));
		orderVerifier.verify(this.puzzleBuilder).findOrCreateSymbol(
				argThat(arg -> arg.getId() == 1 && "B".equals(arg.getDescription())));
		orderVerifier.verify(this.puzzleBuilder).withCell(1, 2, this.cell);
		orderVerifier.verify(this.puzzleBuilder).build();
	}
}
