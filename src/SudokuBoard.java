class SudokuBoard {

	int[][] gameBoard;
	int[][] initialBoard;
	int moves;
	int[][] savedMoves;

	
	public SudokuBoard(int[][] initialBoard) {
		// Inicializar o tabuleiro e o estado inicial com a matriz fornecida
		this.initialBoard = initialBoard;
		this.gameBoard = new int[9][9];
		this.savedMoves = new int[nZeros()][2];
		this.moves = 0;
		// Copia a matriz inicial para o tabuleiro atual
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				this.gameBoard[i][j] = initialBoard[i][j];
			}
		}
	}

	//Obter o número que está numa coordenada, ou zero caso não esteja preenchida
	public int getNumber(int line, int column) {
		if(line >= 9 || line < 0)
			throw new IllegalArgumentException("Linha selecionada não é válida");
		if(column >= 9 || column < 0)
			throw new IllegalArgumentException("Coluna selecionada não é válida");
		return gameBoard[line][column];
	}


	/* Efetuar uma jogada dando a coordenada e o valor. Não é permitida uma jogada que
	se sobreponha a um número da matriz inicial. Por outro lado, a jogada pode resultar
	num tabuleiro inválido. */

	public void move(int line, int column, int n) {
		if(n < 0 || n > 9){
			System.out.println("Erro 1");
			throw new IllegalArgumentException("O valor deve estar contido entre 1 e 9");
		}
		if(line < 0 || line > 9){
			System.out.println("Erro 2");
			throw new IllegalArgumentException("O número da linha deve estar entre 0 e 8");
		}
		if(column < 0 || column > 9) {
			System.out.println("Erro 3");
			throw new IllegalArgumentException("O número da coluna deve estar entre 0 e 8");
		}
		if(initialBoard[line][column] == 0){
			gameBoard[line][column] = n;
			moves++;
			saveMoves(line,column,savedMoves);
		} 
		if(initialBoard[line][column] != 0)
			throw new IllegalArgumentException("Casa já preenchida");
	}

	//VerIfica se um certo numero se encontra num segmento	
	boolean isNumberInSegment(int line, int column, int n) {
		int segmentLine = (line / 3) * 3; // Índice inicial da linha do segmento
		int segmentColumn = (column / 3) * 3; // Índice inicial da coluna do segmento
		for (int i = segmentLine; i < segmentLine + 3; i++) {
			for (int j = segmentColumn; j < segmentColumn + 3; j++) {
				if (gameBoard[i][j] == n) {
					return true; // O número está no segmento
				}
			}
		}
		return false; // O número não está no segmento
	}


	// Jogada aleatória num espaço vazio apenas seguindo a regra de nao haver numeros repetidos no segmento
	public void randomMove() {
		int n = (int)(Math.random()*10);
		int x = (int)(Math.random()*9);
		int y = (int)(Math.random()*9);
		if(initialBoard[x][y] == 0){
			if(!isNumberInSegment(x,y,n))
				gameBoard[x][y] = n;	
		}
	}


	// Reseta o tabuleiro de jogo, voltando ao initialBoard 
	public void resetBoard() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				gameBoard[i][j] = initialBoard[i][j];
			}
		}
	}

	//Verifica se o gameBoard está completo, ou seja, se o jogo está terminado
	public boolean isFinished() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(gameBoard[i][j] == 0)
					return false;
			}
		}
		return true;
	}


	// Obter as linhas que não estão válidas para a solução do Sudoku, ou seja, as que contêm números repetidos.
	public int[] wrongLines() {
		int[] wrongLines = new int[gameBoard.length];
		int i = 0;

		// Verificação de linhas
		for (int line = 0; line < gameBoard.length; line++) {
			if (linhaTemRepetidos(line)) {
				wrongLines[i] = line; // Adiciona o número da linha com erro no vetor
				i++;
			}
		}

		// Cria um novo vetor do tamanho exato para armazenar as linhas com erro
		int[] linhasComErroFinal = new int[i];
		System.arraycopy(wrongLines, 0, linhasComErroFinal, 0, i);

		return linhasComErroFinal; // Retorna o vetor com os números das linhas erradas
	}

	public boolean linhaTemRepetidos(int linha) {
		// Utiliza a lógica de verificação de valores repetidos para a linha específica do tabuleiro
		for (int i = 0; i < gameBoard[linha].length; i++) {
			for (int j = i + 1; j < gameBoard[linha].length; j++) {
				// Se encontrar dois números iguais na mesma linha, retorna true
				if (gameBoard[linha][i] == gameBoard[linha][j] && gameBoard[linha][i] != 0) {
					return true;
				}
			}
		}
		// Não foram encontrados números repetidos na linha
		return false;
	}


	public boolean segmentoTemRepetidos(int line, int column) {
		int linhaInicial = (line / 3) * 3; // Índice inicial da linha do segmento
		int colunaInicial = (column / 3) * 3; // Índice inicial da coluna do segmento
		// Utiliza a lógica de verificação de valores repetidos para o segmento específico do tabuleiro
		for (int i = linhaInicial; i < linhaInicial + 3; i++) {
			for (int j = colunaInicial; j < colunaInicial + 3; j++) {
				for (int k = linhaInicial; k < linhaInicial + 3; k++) {
					for (int l = colunaInicial; l < colunaInicial + 3; l++) {
						if ((i != k || j != l) && gameBoard[i][j] == gameBoard[k][l] && gameBoard[i][j] != 0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public int[] wrongSegments() {
		int[] segmentosComErro = new int[gameBoard.length];
		int contador = 0;

		// Verificação de segmentos
		for (int linha = 0; linha < gameBoard.length; linha += 3) {
			for (int coluna = 0; coluna < gameBoard[linha].length; coluna += 3) {
				if (segmentoTemRepetidos(linha, coluna)) {
					segmentosComErro[contador] = linha * 10 + coluna; // Representação única do segmento com erro
					contador++;
				}
			}
		}

		// Cria um novo vetor do tamanho exato para armazenar os segmentos com erro
		int[] segmentosComErroFinal = new int[contador];
		System.arraycopy(segmentosComErro, 0, segmentosComErroFinal, 0, contador);

		return segmentosComErroFinal; // Retorna o vetor com os segmentos errados
	}


	public boolean colunaTemRepetidos(int coluna) {
		// Utiliza a lógica de verificação de valores repetidos para a coluna específica do tabuleiro
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = i + 1; j < gameBoard.length; j++) {
				// Se encontrar dois números iguais na mesma coluna, retorna true
				if (gameBoard[i][coluna] == gameBoard[j][coluna] && gameBoard[i][coluna] != 0) {
					return true;
				}
			}
		}
		// Não foram encontrados números repetidos na coluna
		return false;
	}

	public int[] wrongColumns() {
		int[] colunasComErro = new int[gameBoard.length];
		int contador = 0;

		// Verificação de colunas
		for (int coluna = 0; coluna < gameBoard.length; coluna++) {
			if (colunaTemRepetidos(coluna)) {
				colunasComErro[contador] = coluna; // Adiciona o número da coluna com erro no vetor
				contador++;
			}
		}

		// Cria um novo vetor do tamanho exato para armazenar as colunas com erro
		int[] colunasComErroFinal = new int[contador];
		System.arraycopy(colunasComErro, 0, colunasComErroFinal, 0, contador);

		return colunasComErroFinal; // Retorna o vetor com os números das colunas erradas
	}

	//Conta o numero de zeros da matriz inicial
	public int nZeros() {
		int z = 0;
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(initialBoard[i][j] == 0)
					z++;
			}
		}
		return z;
	}
	
	//Procedimento que salva as jogadas
	public void saveMoves(int line, int column, int[][]m) {
		int n = moves;
		m[n-1][0] = line;
		m[n-1][1] = column;
	}
	
	//Procedimento que faz undo das jogadas efetuadas
	public void undo() {
		int n = moves;
		int i = savedMoves[n-1][0];
		int j = savedMoves[n-1][1];
		gameBoard[i][j] = 0;
		moves = moves - 1;
	}
	
	//Id number de cada segmento
	static int segmentId(int line, int column) {
		int segment= 0;
		if(line >= 0 && line <= 2 && column >= 0 && column <= 2)
			segment = 1;
		if(line >= 0 && line <= 2 && column >= 3 && column <= 5)
			segment = 2;
		if(line >= 0 && line <= 2 && column >= 6 && column <= 8)
			segment = 3;
		if(line >= 3 && line <= 5 && column >= 0 && column <= 2)
			segment = 4;
		if(line >= 3 && line <= 5 && column >= 3 && column <= 5)
			segment = 5;
		if(line >= 3 && line <= 5 && column >= 6 && column <= 8)
			segment = 6;
		if(line >= 6 && line <= 8 && column >= 0 && column <= 2)
			segment = 7;
		if(line >= 6 && line <= 8 && column >= 2 && column <= 5)
			segment = 8;
		if(line >=6 && line <= 8 && column >= 6 && column <= 8)
			segment = 9;
		return segment;
	}
	
	//Função de teste da classe
	static SudokuBoard test() {
		int[][] m = SudokuAux.matrixZerada(90);
		SudokuBoard s = new SudokuBoard(m);
		return s;
	}
}