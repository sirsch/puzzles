package software.sirsch.sa4e.puzzles;

import javax.annotation.Nonnull;

/**
 * Diese Klasse stellt das Kommando bereit, das die Hilfe ausgibt.
 *
 * @author sirsch
 * @since 16.12.2022
 */
public class HelpCommand implements Command {

	/**
	 * Diese Konstante enthält den Namen des Kommandos.
	 */
	public static final String COMMAND_NAME = "help";

	@Override
	public void execute(@Nonnull final String[] args) {
		System.out.println("usage: generate-puzzle <filename> <?numberOfDigits>");
	}
}