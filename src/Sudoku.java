import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Sudoku {

	private SudokuBoard sudokuBoard;
	public ColorImage boardImage;

	public Sudoku(int difficulty, String fileName) {
		int[][] completBoard = getInitialBoard(fileName);
		int[][] initialBoard = SudokuAux.matrixZerada(difficulty); // Obtém uma matriz de Sudoku inicial
		//Cria um ficheiro com a grelha inicial
		fileMaker1("initialBoard.txt", initialBoard);
		int[][] m = getInitialBoard("initialBoard.txt");
		sudokuBoard = new SudokuBoard(initialBoard); // Cria um novo tabuleiro Sudoku com base na matriz inicial
		// Imagem de jogo
		boardImage = SudokuAux.sudokuLayout();
		SudokuAux.insertSudokuMatrix(initialBoard, boardImage);
	}

	public void makeMove(int line, int column, int n) {
		boardImage = SudokuAux.backPaintLine(boardImage);
		sudokuBoard.move(line, column, n);
		//Linha, coluna e segmento errados
		if(sudokuBoard.linhaTemRepetidos(line) && sudokuBoard.colunaTemRepetidos(column) && sudokuBoard.segmentoTemRepetidos(line,column)) {
			boardImage = SudokuAux.paintLine(boardImage, line);
			boardImage = SudokuAux.paintColumn(boardImage, column);
			boardImage = SudokuAux.paintSegment(boardImage, line, column);
			sudokuBoard.move(line, column, 0);
		}
		//Linha e coluna erradas
		if(sudokuBoard.linhaTemRepetidos(line) && sudokuBoard.colunaTemRepetidos(column)) {
			boardImage = SudokuAux.paintLine(boardImage, line);
			boardImage = SudokuAux.paintColumn(boardImage, column);
			sudokuBoard.move(line, column, 0);
		}
		//Linha e segmento errados
		if(sudokuBoard.linhaTemRepetidos(line) && sudokuBoard.segmentoTemRepetidos(line,column)) {
			boardImage = SudokuAux.paintLine(boardImage, line);
			boardImage = SudokuAux.paintSegment(boardImage, line, column);
			sudokuBoard.move(line, column, 0);
		}
		//Coluna e segmento errados
		if(sudokuBoard.colunaTemRepetidos(column) && sudokuBoard.segmentoTemRepetidos(line,column)) {
			boardImage = SudokuAux.paintColumn(boardImage, column);
			boardImage = SudokuAux.paintSegment(boardImage, line, column);
			sudokuBoard.move(line, column, 0);
		}
		// Linha errada
		if (sudokuBoard.linhaTemRepetidos(line)) {
			boardImage = SudokuAux.paintLine(boardImage, line);
			System.out.println("O número " + n + " introduzido não é válido para a linha " + line);
			sudokuBoard.move(line, column, 0);
		}
		// Coluna errada
		else if (sudokuBoard.colunaTemRepetidos(column)) {
			boardImage = SudokuAux.paintColumn(boardImage, column);
			System.out.println("O número " + n + " introduzido não é válido para a coluna " + column);
			sudokuBoard.move(line, column, 0);
			SudokuAux.gameFinishedLayout(boardImage);
		}
		// Segmento errado
		else if (sudokuBoard.segmentoTemRepetidos(line, column)) {
			boardImage = SudokuAux.paintSegment(boardImage, line, column);
			System.out.println("O número " + n + " introduzido não é válido para o segmento ");
			sudokuBoard.move(line, column, 0);
		}
		// Jogo concluido
		else if (sudokuBoard.isFinished()) {
			boardImage = SudokuAux.insertNumber(boardImage, sudokuBoard.initialBoard, n, line, column);
			System.out.println("Jogo concluído!!!");
		} else {
			// Faz a jogada
			boardImage = SudokuAux.insertNumber(boardImage, sudokuBoard.initialBoard, n, line, column);
			int[][] m = sudokuBoard.gameBoard;
		}
	}
	
	public void randomMove() {
	sudokuBoard.randomMove();
	int[][] m = sudokuBoard.gameBoard;
	boardImage = SudokuAux.backPaintLine(boardImage);
	SudokuAux.resetBoard(boardImage);
	SudokuAux.insertSudokuMatrix(m, boardImage);
	}

	// Reset do jogo
	public void resetBoard() {
		sudokuBoard.resetBoard();
		SudokuAux.resetBoard(boardImage);
		int[][] m = getInitialBoard("initialBoard.txt");
		SudokuAux.insertSudokuMatrix(m, boardImage);
	}

	//Teste principal
	static Sudoku test() {
		Sudoku s = new Sudoku(50, "CompleteBoard.sud");
		return s;
	}

	// Undo das jogadas efeutadas
	public void undo() {
		sudokuBoard.undo();
		SudokuAux.resetBoard(boardImage);
		int[][] n = sudokuBoard.initialBoard;
		int[][] m = sudokuBoard.gameBoard;
		SudokuAux.insertSudokuMatrix(m, boardImage);
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(n[i][j] == 0 && m[i][j] != 0)
					SudokuAux.insertNumber(boardImage,n,m[i][j], i, j);
			}
		}

	}

	// Obtem a initialBoard através do ficheiro "initialBoard.txt"(formato .sud)
	static int[][] getInitialBoard(String fileName) {
		int[][] initialBoard = new int[9][9];
		try {
			Scanner scanner = new Scanner(new File(fileName));
			while (scanner.hasNextInt()) {
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						initialBoard[i][j] = scanner.nextInt();
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Ficheiro " + fileName + " não encontrado");
		}
		return initialBoard;
	}

	// Obtem a initialBoard e a GameBoard e poe tudo num mesmo ficheiro (formato
	// .sudgame)
	public void saveGame(String fileName) {
		try {
			int m[][] = sudokuBoard.gameBoard;
			Scanner scanner = new Scanner(new File("initialBoard.txt"));
			PrintWriter writer = new PrintWriter(new File(fileName));
			// Copia a matriz inicial do jogo
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					int x = scanner.nextInt();
					writer.print(x + " ");
					if ((j + 1) % 9 == 0) {
						writer.println();
					}
				}
			}
			// Espaço entre as matrizes
			writer.println();
			// Copia o estado do jogo
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 9; y++) {

					int w = m[x][y];
					writer.print(w + " ");
					if ((y + 1) % 9 == 0) {
						writer.println();
					}
				}
			}
			scanner.close();
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Ficheiro" + fileName + "não encontrado");
		}
	}

	public void loadGame1(String fileName) {
		try {
			Scanner scanner = new Scanner(new File(fileName));
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					scanner.nextInt();
				}
			}
			scanner.nextLine();

			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					int a = scanner.nextInt();
					sudokuBoard.initialBoard[i][j] = a;
					sudokuBoard.gameBoard[i][j] = a;
				}
			}
			scanner.close();
			sudokuBoard.resetBoard();
			SudokuAux.resetBoard(boardImage);
			int[][] m = sudokuBoard.gameBoard;
			SudokuAux.insertSudokuMatrix(m, boardImage);
			System.out.println("Jogo carregado com sucesso do arquivo " + fileName);
		}
		catch(FileNotFoundException e) {
			System.out.println("Erro ao ler o fiheiro:" + fileName);
		}
	}



	//Constroi ficheiros com o formato indicado dado uma matriz
	static void fileMaker1(String fileName, int[][] m) {
		try {
			PrintWriter writer = new PrintWriter(new File(fileName));
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					writer.print(m[i][j] + " ");
					if ((j + 1) % 9 == 0) {
						writer.println();
					}
				}
			}
			writer.close();
			System.out.println("A matriz foi escrita no arquivo " + fileName);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo " + fileName + " não encontrado");
		}
	}

}
