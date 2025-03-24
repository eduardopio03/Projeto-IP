class SudokuAux {

	static final Color RED = new Color(255, 0, 0); // vermelho
	static final Color GREEN = new Color(0, 255, 0); // azul
	static final Color BLUE = new Color(0, 0, 255); // verde
	static final Color WHITE = new Color(255, 255, 255);
	static final Color BLACK = new Color(0, 0, 0);
	static final Color DARKBLUE = new Color(66, 66, 111);

	// Verifica se uma matrix 9x9 representa um sudoku válido

	static boolean isValidSudoku(int[][] board) {
		for (int i = 0; i < 9; i++) {
			boolean[] row = new boolean[9];
			boolean[] col = new boolean[9];
			boolean[] box = new boolean[9];
			for (int j = 0; j < 9; j++) {
				if (board[i][j] != 0) {
					if (row[board[i][j] - 1]) {
						return false;
					}
					row[board[i][j] - 1] = true;
				}
				if (board[j][i] != 0) {
					if (col[board[j][i] - 1]) {
						return false;
					}
					col[board[j][i] - 1] = true;
				}
				int boxRow = 3 * (i / 3) + j / 3;
				int boxCol = 3 * (i % 3) + j % 3;
				if (board[boxRow][boxCol] != 0) {
					if (box[board[boxRow][boxCol] - 1]) {
						return false;
					}
					box[board[boxRow][boxCol] - 1] = true;
				}
			}
		}
		return true;
	}

	//Função de teste que verifica se o sudoku é válido
	static boolean isValidTest() {
		int[][] board = { { 8, 3, 5, 4, 1, 6, 9, 2, 7 }, { 2, 9, 6, 8, 5, 7, 4, 3, 1 }, { 4, 1, 7, 2, 9, 3, 6, 5, 8 },
				{ 5, 6, 9, 1, 3, 4, 7, 8, 2 }, { 1, 2, 3, 6, 7, 8, 5, 4, 9 }, { 7, 4, 8, 5, 2, 9, 1, 6, 3 },
				{ 6, 5, 2, 7, 8, 1, 3, 9, 4 }, { 9, 8, 1, 3, 4, 5, 2, 7, 6 }, { 3, 7, 4, 9, 6, 2, 8, 1, 5 } };
		return isValidSudoku(board);
	}

	// Desenha im ícone numa imagem dada
	static ColorImage drawIcone(ColorImage img, int x, int y, int size, Color charColor, String s) {
		String s1 = String.valueOf(s);
		img.drawCenteredText(x, y, s1, size, charColor);
		return img;
	}

	// Função Teste
	static void drawIconeTest() {
		ColorImage img = new ColorImage(300, 300, BLACK);
		String s = String.valueOf(7);
		Color charColor = WHITE;
		drawIcone(img, 100, 100, 50, charColor, s);
		return;
	}

	// Gera uma Matriz válida para um sudoku
	static int[][] sudokuMatrix() {
		int[][] m = Sudoku.getInitialBoard("CompleteBoard.sud");
		return m;
	}

	// Desenha o layout do sudoku
	static ColorImage sudokuLayout() {
		ColorImage img1 = new ColorImage(451, 451, WHITE);
		//Desenha linhas horizontais e verticais
		for (int i = 0; i <= 9; i++) {
			for (int j = 0; j <= 450; j++) {
				img1.setColor(i * 50, j, DARKBLUE); // linhas verticais
				img1.setColor(j, i * 50, DARKBLUE); // linhas horizontais
			}
		}
		//Desenha linhas mais grossas para realçar os segmentos
		for (int k = 1; k < 3; k++) {
			for (int l = 0; l < img1.getHeight(); l++) {
				img1.setColor(k * 150 - 1, l, DARKBLUE);
				img1.setColor(k * 150 + 1, l, DARKBLUE);
			}
			for (int m = 0; m < img1.getWidth(); m++) {
				img1.setColor(m, k * 150 - 1, DARKBLUE);
				img1.setColor(m, k * 150 + 1, DARKBLUE);
			}
		}

		return img1;
	}

	//Insere uma matriz no tabuleiro 
	static void insertSudokuMatrix(int[][] m, ColorImage img) {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (m[i][j] > 0 && m[i][j] < 10) {
					String s = String.valueOf(m[i][j]);
					drawIcone(img, j * 50 + 25, i * 50 + 25, 40, DARKBLUE, s);
				}
			}
		}
	}

	//Inicia um novo jogo
	static ColorImage newGame(int percentage) {
		ColorImage img = sudokuLayout();
		int[][] m = matrixZerada(percentage);
		insertSudokuMatrix(m, img);
		return img;
	}

	//Coloca zeros em posições aleatórias de uma matriz dada, dada uma percentagem de zeros
	static int[][] matrixZerada(int percentage) {
		int[][] m = sudokuMatrix();
		int numZerar = (int) (81 * percentage / 100.0);
		for (int k = 0; k < numZerar; k++) {
			int rand1 = (int) (Math.random() * 9);
			int rand2 = (int) (Math.random() * 9);
			int a = rand1;
			int b = rand2;
			if (m[a][b] == 0)
				k--;
			else
				m[a][b] = 0;
		}
		return m;
	}

	//Dá reset a imagem do board
	static void resetBoard(ColorImage img) {
		for (int a = 0; a < img.getWidth(); a++) {
			for (int b = 0; b < img.getHeight(); b++) {
				img.setColor(a, b, WHITE);
			}
		}
		for (int i = 0; i <= 9; i++) {
			for (int j = 0; j <= 450; j++) {
				img.setColor(i * 50, j, DARKBLUE); // linhas verticais
				img.setColor(j, i * 50, DARKBLUE); // linhas horizontais
			}
		}
		// Desenha linhas mais grossas para realçar os segmentos
		for (int k = 1; k < 3; k++) {
			for (int l = 0; l < img.getHeight(); l++) {
				img.setColor(k * 150 - 1, l, DARKBLUE);
				img.setColor(k * 150 + 1, l, DARKBLUE);
			}
			for (int m = 0; m < img.getWidth(); m++) {
				img.setColor(m, k * 150 - 1, DARKBLUE);
				img.setColor(m, k * 150 + 1, DARKBLUE);
			}
		}
	}

	//Insere o numero desejado numa posição indicada pela linha e coluna
	static ColorImage insertNumber(ColorImage img, int[][] m, int n, int line, int column) {
		if (m[line][column] == 0) {
			// Preencher o quadrado com a cor branca
			for (int i = line * 50; i < (line + 1) * 50 - 3; i++) {
				for (int j = column * 50; j < (column + 1) * 50 - 3; j++) {
					img.setColor(j + 2, i + 2, WHITE);
				}
			}
			String s = String.valueOf(n);
			drawIcone(img, column * 50 + 25, line * 50 + 25, 40, GREEN, s);
		}
		return img;
	}
	
	//insere um quadrado branco(tira o numero) numa posição desejada
	static ColorImage insertWhiteSquare(ColorImage img, int line, int column) {
			// Preencher o quadrado com a cor branca
			for (int i = line * 50; i < (line + 1) * 50 - 3; i++) {
				for (int j = column * 50; j < (column + 1) * 50 - 3; j++) {
					img.setColor(j + 2, i + 2, WHITE);
				}
			}
		return img;
	}
	
	//Pinta um contorno vermelho numa linha
	static ColorImage paintLine(ColorImage img, int line) {
		for (int y = line * 50; y < (line + 1) * 50 + 1; y++) {
			// Borda esquerda da linha
			img.setColor(0, y, RED);
			// Borda direita da linha
			img.setColor(img.getWidth() - 1, y, RED);
		}

		// Borda superior da linha
		for (int x = 0; x < img.getWidth(); x++) {
			img.setColor(x, line * 50, RED);
		}

		// Borda inferior da linha
		for (int x = 0; x < img.getWidth(); x++) {
			img.setColor(x, (line + 1) * 50, RED);
		}

		return img;
	}

	//Pinta um contorno vermelho numa coluna
	static ColorImage paintColumn(ColorImage img, int column) {
		for (int x = column * 50; x < (column + 1) * 50 + 1; x++) {
			// Borda superior da coluna
			img.setColor(x, 0, RED);
			// Borda inferior da coluna
			img.setColor(x, img.getHeight() - 1, RED);
		}

		// Borda esquerda da coluna
		for (int y = 0; y < img.getHeight(); y++) {
			img.setColor(column * 50, y, RED);
		}

		// Borda direita da coluna
		for (int y = 0; y < img.getHeight(); y++) {
			img.setColor((column + 1) * 50, y, RED);
		}

		return img;
	}

	//Pinta a linha de volta depois de atualizar
	static ColorImage backPaintLine(ColorImage img) {
		// Linhas horizontais e verticais
		for (int i = 0; i <= 9; i++) {
			for (int j = 0; j <= 450; j++) {
				img.setColor(i * 50, j, DARKBLUE); // linhas verticais
				img.setColor(j, i * 50, DARKBLUE); // linhas horizontais
			}
		}
		// Linhas mais grossas para realçar os segmentos
		for (int k = 1; k < 3; k++) {
			for (int l = 0; l < img.getHeight(); l++) {
				img.setColor(k * 150 - 1, l, DARKBLUE);
				img.setColor(k * 150 + 1, l, DARKBLUE);
			}
			for (int m = 0; m < img.getWidth(); m++) {
				img.setColor(m, k * 150 - 1, DARKBLUE);
				img.setColor(m, k * 150 + 1, DARKBLUE);
			}
		}

		return img;
	}

	static ColorImage backPaintColumn(ColorImage img, int column) {
		for (int x = column * 50; x < (column + 1) * 50 + 1; x++) {
			// Pinta a borda superior da coluna
			img.setColor(x, 0, DARKBLUE);
			// Pinta a borda inferior da coluna
			img.setColor(x, img.getHeight() - 1, DARKBLUE);
		}

		// Pinta a borda esquerda da coluna
		for (int y = 0; y < img.getHeight(); y++) {
			img.setColor(column * 50, y, DARKBLUE);
		}

		// Pinta a borda direita da coluna
		for (int y = 0; y < img.getHeight(); y++) {
			img.setColor((column + 1) * 50, y, DARKBLUE);
		}

		return img;
	}

	//Pinta um contorno em volta de um segmento
	static ColorImage paintSegment(ColorImage img, int line, int column) {
		int startY = (line / 3) * 150;
		int startX = (column / 3) * 150;

		// Desenha o contorno vermelho ao redor do segmento
		for (int x = startX; x < startX + 150; x++) {
			img.setColor(x, startY, RED);
			img.setColor(x, startY + 149, RED);
		}
		for (int y = startY; y < startY + 150; y++) {
			img.setColor(startX, y, RED);
			img.setColor(startX + 149, y, RED);
		}

		return img;
	}

	static String joinInts(int[][] m) {
		String join = "";

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				join += m[i][j] + " ";
			}
			join += "\n";
		}

		return join;
	}

	//Layout final do jogo
	static ColorImage gameFinishedLayout(ColorImage img) {
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.setColor(i, j, BLACK);
				img.drawCenteredText(i, j, "JOGO CONCLUÍDO", 50, GREEN);
			}
		}
		return img;
	}
	
	//TESTES
//-----------------------------------------------------------------------------------------
	
	static void testIsValid() {
		int[][] m = sudokuMatrix();
		isValidSudoku(m);
		String s = String.valueOf(isValidSudoku(m));
		return;
	}

	static ColorImage completeGameSudoku(int percentage) {
		ColorImage img = newGame(percentage);
		return img;
	}

	static ColorImage testInsertNumber(int percentage) {
		ColorImage img = newGame(percentage);
		int[][] m = matrixZerada(percentage);
		insertNumber(img, m, 0, 0, 8);
		insertWhiteSquare(img,0,8);
		return img;
	}

	static void testePaintLineColumnSegment() {
		ColorImage img = newGame(50);
		backPaintLine(img);
		// paintColumn(img,0);
		// paintSegment(img, 3, 0);
		return;
	}

}
