package View;

import Controller.Observer;
import Controller.Observable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.WHITE;

public class TabuleiroTiro extends JFrame implements Observable {
    private static final int SIZE = 15;  // Tamanho do tabuleiro
    private static final int CELL_SIZE = 30;  // Tamanho de cada célula

    private String player1Name;
    private String player2Name;

    private boolean[][] player1Shots;
    private boolean[][] player2Shots;
    private int[][] player1Embarcacoes;
    private int[][] player2Embarcacoes;
    private int[][] player1Oculto;
    private int[][] player2Oculto;

    private int currentPlayer;
    private int tirosRestantes;
    private static TabuleiroTiro instance;

    private TabuleiroPanel player1Tabuleiro;
    private TabuleiroPanel player2Tabuleiro;
    private JButton atirarButton;
    private JButton salvarButton;
    private JButton comecarButton;

    private int selectedRow = -1;
    private int selectedCol = -1;

    private List<Observer> observers = new ArrayList<>();
    private boolean tiro, salvar = false;

    public TabuleiroTiro(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;

        player1Shots = new boolean[SIZE][SIZE];
        player2Shots = new boolean[SIZE][SIZE];
        player1Embarcacoes = new int[SIZE][SIZE];
        player2Embarcacoes = new int[SIZE][SIZE];
        player1Oculto = new int[SIZE][SIZE];
        player2Oculto = new int[SIZE][SIZE];
        currentPlayer = 1; // Começa com o jogador 1
        tirosRestantes = 3; // Cada jogador tem 3 tiros por turno

        setTitle("Batalha Naval - Tiros");
        setSize(SIZE * CELL_SIZE * 2 + 100, SIZE * CELL_SIZE + 100);  // Dimensões da janela
        setUndecorated(true); // Remove a barra de título
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        player1Tabuleiro = new TabuleiroPanel(player1Shots, player1Oculto, "Tabuleiro de " + player1Name);
        player2Tabuleiro = new TabuleiroPanel(player2Shots, player2Oculto, "Tabuleiro de " + player2Name);

        player1Tabuleiro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e, player1Tabuleiro);
            }
        });

        player2Tabuleiro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e, player2Tabuleiro);
            }
        });

        atirarButton = new JButton("Atirar");
        atirarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleShootButton();
            }
        });

        salvarButton = new JButton("Salvar");
        salvarButton.setPreferredSize(new Dimension(100, 50)); // Define o tamanho do botão "Salvar"
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveButton();
            }
        });

        comecarButton = new JButton("Começar Jogada");
        comecarButton.setPreferredSize(new Dimension(200, 50)); // Define o tamanho do botão "Começar Jogada"
        comecarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleComecarButton();
            }
        });

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());


        JPanel tabuleirosPanel = new JPanel(new GridLayout(1, 2));
        tabuleirosPanel.add(player1Tabuleiro);
        tabuleirosPanel.add(player2Tabuleiro);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(salvarButton, BorderLayout.EAST);
        bottomPanel.add(comecarButton);
        bottomPanel.add(atirarButton);

        add(topPanel, BorderLayout.NORTH);
        add(tabuleirosPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        atualizarVisibilidadeTabuleiros(false);
    }

    public static TabuleiroTiro getInstance(String player1Name, String player2Name) {
        if (instance == null) {
            instance = new TabuleiroTiro(player1Name, player2Name);
        }
        return instance;
    }

    protected void notifyObservers() {
        for (Observer observer : observers) {
            observer.notify(this);
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public Object[] get() {
        Object[] dados = new Object[4];  // Ensure the array has correct length
        if (tiro) {
            dados[0] = "atirou";
            dados[1] = selectedRow;
            dados[2] = selectedCol;
            dados[3] = currentPlayer;
            tiro = false;
        }
        else if(salvar){
            dados[0] = "Salve o jogo";
            salvar = false;
        }
        return dados;
    }

    private class TabuleiroPanel extends JPanel {
        private boolean[][] shots;
        private int[][] embarcacoes;
        private String playerName;

        public TabuleiroPanel(boolean[][] shots, int[][] embarcacoes, String playerName) {
            this.shots = shots;
            this.embarcacoes = embarcacoes;
            this.playerName = playerName;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int tabuleiroWidth = SIZE * CELL_SIZE;
            int tabuleiroHeight = SIZE * CELL_SIZE;

            int startX = (panelWidth - tabuleiroWidth) / 2;
            int startY = (panelHeight - tabuleiroHeight) / 2;

            // Desenhar números na parte superior
            for (int j = 0; j < SIZE; j++) {
                String num = Integer.toString(j + 1);
                int numWidth = g2d.getFontMetrics().stringWidth(num);
                g2d.drawString(num, startX + j * CELL_SIZE + (CELL_SIZE - numWidth) / 2, startY - 5);
            }

            // Desenhar letras no lado esquerdo
            for (int i = 0; i < SIZE; i++) {
                char letter = (char) ('A' + i);
                int letterHeight = g2d.getFontMetrics().getAscent();
                g2d.drawString(Character.toString(letter), startX - 20, startY + i * CELL_SIZE + (CELL_SIZE + letterHeight) / 2);
            }

            // Desenhar células do tabuleiro
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    Rectangle2D.Double cell = new Rectangle2D.Double(startX + j * CELL_SIZE, startY + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g2d.setColor(WHITE);  // Cor do preenchimento da célula
                    g2d.fill(cell);  // Preencher a célula
                    g2d.setColor(Color.BLACK);  // Cor do contorno da célula
                    g2d.draw(cell);  // Desenhar o contorno da célula

                    if (shots[i][j]) {
                        if (embarcacoes[i][j] < 0) {
                            if (embarcacoes[i][j] == -1) {
                                g2d.setColor(Color.GREEN); // Tiro que acertou um submarino
                            } else if (embarcacoes[i][j] == -2){
                                g2d.setColor(Color.YELLOW); // Tiro que acertou um destroyer
                            } else if (embarcacoes[i][j] == -3 || embarcacoes[i][j] == -7) {
                                g2d.setColor(Color.CYAN); // Tiro que acertou um hidroaviao
                            } else if (embarcacoes[i][j] == -4) {
                                g2d.setColor(Color.ORANGE); // Tiro que acertou um cruzador
                            } else if (embarcacoes[i][j] == -5) {
                                g2d.setColor(Color.PINK); // Tiro que acertou um couraçado
                            } else if (embarcacoes[i][j] == -6) {
                                g2d.setColor(Color.RED); // Embarcação afundada
                            } else {
                                g2d.setColor(Color.BLUE); // Tiro na água
                            }
                            g2d.fill(cell);
                        }
                    } else if (selectedRow == i && selectedCol == j && this == player2Tabuleiro && currentPlayer == 1) {
                        g2d.setColor(Color.GRAY);
                        g2d.fill(cell);
                    } else if (selectedRow == i && selectedCol == j && this == player1Tabuleiro && currentPlayer == 2) {
                        g2d.setColor(Color.GRAY);
                        g2d.fill(cell);
                    }
                }
            }

            // Desenhar nome do jogador
            g2d.drawString(playerName, 10, 20);
        }
    }

    private void handleMouseClick(MouseEvent e, TabuleiroPanel tabuleiroPanel) {
        if ((currentPlayer == 1 && tabuleiroPanel == player2Tabuleiro) ||
                (currentPlayer == 2 && tabuleiroPanel == player1Tabuleiro)) { // Permite clique apenas no tabuleiro do oponente
            int x = e.getX();
            int y = e.getY();

            int[] coordenadas = converterCoordenada(x, y, tabuleiroPanel);
            if (coordenadas != null) {
                selectedRow = coordenadas[0];
                selectedCol = coordenadas[1];
                tabuleiroPanel.repaint();  // Repinta o painel para mostrar a seleção
                System.out.println("Selected cell: (" + selectedRow + ", " + selectedCol + ")");
            }
        }
    }

    private int[] converterCoordenada(int x, int y, TabuleiroPanel tabuleiroPanel) {
        int panelWidth = tabuleiroPanel.getWidth();
        int panelHeight = tabuleiroPanel.getHeight();
        int tabuleiroWidth = SIZE * CELL_SIZE;
        int tabuleiroHeight = SIZE * CELL_SIZE;

        int startX = (panelWidth - tabuleiroWidth) / 2;
        int startY = (panelHeight - tabuleiroHeight) / 2;

        int col = (x - startX) / CELL_SIZE;
        int row = (y - startY) / CELL_SIZE;

        if (col >= 0 && col < SIZE && row >= 0 && row < SIZE) {
            return new int[]{row, col};
        }
        return null;
    }

    public void recuperarTiro(int jogador, int resultado, int linha, int coluna){
        if(jogador == 1){
            player1Shots[linha][coluna] = true;
            player1Embarcacoes[linha][coluna] = -resultado;
            if(resultado!=10){
                checarAfundou(1, resultado); // Verifica se a embarcação foi afundada
            }
            repaint();
        }
        else{
            player2Shots[linha][coluna] = true;
            player2Embarcacoes[linha][coluna] = -resultado;
            if(resultado!=10){
                checarAfundou(2, resultado); // Verifica se a embarcação foi afundada
            }
            repaint();
        }
    }

    private void showMessage(String message) {
        final JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.getContentPane().add(new JLabel(message, SwingConstants.CENTER), BorderLayout.CENTER);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(this);
        Timer timer = new Timer(1500, new ActionListener() { // Mostra por 1.5 segundos
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }

    public void atirou(int resultado) {
        if (currentPlayer == 1) {
            player2Shots[selectedRow][selectedCol] = true;
            player2Embarcacoes[selectedRow][selectedCol] = -resultado;
            if(resultado!=10){
                checarAfundou(2, resultado); // Verifica se a embarcação foi afundada
            }

            repaint();

            if (checkVictory(player2Embarcacoes)) {
                JOptionPane.showMessageDialog(this, player1Name + " venceu!");
                System.exit(0);
            }

            tirosRestantes--;

            if (tirosRestantes == 0) {
                SwingUtilities.invokeLater(() -> {
                    repaint();
                    // Adiciona uma pausa de 2 segundos antes de ocultar os tabuleiros
                    Timer timer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            currentPlayer = 2;
                            tirosRestantes = 3;
                            ocultarTabuleiros();
                            showMessage("Vez do " + player2Name);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            } else {
                selectedRow = -1;
                selectedCol = -1;
                System.out.println("Atirou no quadrado selecionado.");
            }
        } else {
            player1Shots[selectedRow][selectedCol] = true;
            player1Embarcacoes[selectedRow][selectedCol] = -resultado;
            if(resultado!=10){
                checarAfundou(1, resultado); // Verifica se a embarcação foi afundada
            }
            repaint();

            if (checkVictory(player1Embarcacoes)) {
                JOptionPane.showMessageDialog(this, player2Name + " venceu!");
                System.exit(0);
            }

            tirosRestantes--;

            if (tirosRestantes == 0) {
                SwingUtilities.invokeLater(() -> {
                    repaint();
                    // Adiciona uma pausa de 2 segundos antes de ocultar os tabuleiros
                    Timer timer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            currentPlayer = 1;
                            tirosRestantes = 3;
                            ocultarTabuleiros();
                            showMessage("Vez do " + player1Name);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            } else {
                selectedRow = -1;
                selectedCol = -1;
                System.out.println("Atirou no quadrado selecionado.");
            }
        }
    }

    private void checarAfundou(int jogador, int tipoEmbarcacao) {
        int[][] embarcacoes = (jogador == 1) ? player1Embarcacoes : player2Embarcacoes;
        boolean afundou = true;

        outerLoop:
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (embarcacoes[i][j] == -tipoEmbarcacao) {
                    // Verificar afundamento específico para hidroavião
                    if (tipoEmbarcacao == 3) {
                        if (!verificarAfundamentoHidroaviao(embarcacoes, i, j)) {
                            afundou = false;
                            break outerLoop;
                        }
                    } else {
                        // Verificar afundamento para outras embarcações
                        if (!verificarAfundamento(embarcacoes, i, j, tipoEmbarcacao)) {
                            afundou = false;
                            break outerLoop;
                        }
                    }
                }
            }
        }


    }

    private boolean verificarAfundamento(int[][] embarcacoes, int i, int j, int tipoEmbarcacao) {
        int direcoes[][] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int tamanho = Math.abs(tipoEmbarcacao); // Tamanho da embarcação (1 para submarino, 2 para destroyer, etc.)

        for (int[] direcao : direcoes) {
            int di = direcao[0], dj = direcao[1];
            boolean afundouNaDirecao = true;
            for (int k = 1; k < tamanho; k++) {
                int ni = i + k * di;
                int nj = j + k * dj;
                if (ni < 0 || ni >= SIZE || nj < 0 || nj >= SIZE || embarcacoes[ni][nj] != -tipoEmbarcacao) {
                    afundouNaDirecao = false;
                    break;
                }
            }
            if (afundouNaDirecao) {
                // Marcar todas as partes da embarcação como afundadas
                for (int k = 0; k < tamanho; k++) {
                    int ni = i + k * di;
                    int nj = j + k * dj;
                    embarcacoes[ni][nj] = -6;
                }
                return true;
            }
        }
        return false;
    }

    private boolean verificarAfundamentoHidroaviao(int[][] embarcacoes, int i, int j) {
        int[][] diagonais = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        // Verificar todas as diagonais do ponto inicial
        for (int[] diagonal : diagonais) {
            int di = diagonal[0], dj = diagonal[1];
            int ni = i + di;
            int nj = j + dj;
            if (ni >= 0 && ni < SIZE && nj >= 0 && nj < SIZE && embarcacoes[ni][nj] == -3) {
                embarcacoes[i][j] = -7;
                embarcacoes[ni][nj] = -7;
                return false;
            }
        }

        // Verificar todas as diagonais novamente para encontrar -7 e marcar como -6 (afundado)
        for (int[] diagonal : diagonais) {
            int di = diagonal[0], dj = diagonal[1];
            int ni = i + di;
            int nj = j + dj;
            if (ni >= 0 && ni < SIZE && nj >= 0 && nj < SIZE && embarcacoes[ni][nj] == -7) {
                embarcacoes[i][j] = -7;
                marcarAfundado(embarcacoes, i, j);
                return true;
            }
        }

        return false;
    }

    private void marcarAfundado(int[][] embarcacoes, int i, int j) {
        int[][] diagonais = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        List<int[]> encontrados = new ArrayList<>();
        encontrados.add(new int[]{i, j});

        // Marcar todas as células -7 nas diagonais como afundadas
        for (int index = 0; index < encontrados.size(); index++) {
            int[] pos = encontrados.get(index);
            int x = pos[0];
            int y = pos[1];
            embarcacoes[x][y] = -6;
            for (int[] diagonal : diagonais) {
                int di = diagonal[0], dj = diagonal[1];
                int ni = x + di;
                int nj = y + dj;
                if (ni >= 0 && ni < SIZE && nj >= 0 && nj < SIZE && embarcacoes[ni][nj] == -7) {
                    System.out.println("oi 2");
                    embarcacoes[ni][nj] = -6;
                    encontrados.add(new int[]{ni, nj});
                }
            }
        }
    }



    private void handleShootButton() {
        if (selectedRow != -1 && selectedCol != -1) {
            if (currentPlayer == 1 && !player2Shots[selectedRow][selectedCol]) {
                tiro = true;
                notifyObservers();
            } else if (currentPlayer == 2 && !player1Shots[selectedRow][selectedCol]) {
                tiro = true;
                notifyObservers();
            } else {
                System.out.println("Este quadrado já foi alvo de um tiro.");
            }
        } else {
            System.out.println("Nenhum quadrado selecionado.");
        }
    }

    private void handleSaveButton() {
        salvar = true;
        notifyObservers();
        JOptionPane.showMessageDialog(this, "Jogo salvo!");
    }

    private void handleComecarButton() {
        if (currentPlayer == 1) {
            player2Tabuleiro.embarcacoes = player2Embarcacoes;
        } else {
            player1Tabuleiro.embarcacoes = player1Embarcacoes;
        }
        atualizarVisibilidadeTabuleiros(true);
    }

    private void ocultarTabuleiros() {
        player1Tabuleiro.embarcacoes = player1Oculto;
        player2Tabuleiro.embarcacoes = player2Oculto;
        atualizarVisibilidadeTabuleiros(false);
    }

    private void atualizarVisibilidadeTabuleiros(boolean visiveis) {
        player1Tabuleiro.setVisible(visiveis);
        player2Tabuleiro.setVisible(visiveis);
        atirarButton.setVisible(visiveis);
        salvarButton.setVisible(!visiveis);
        comecarButton.setVisible(!visiveis);
        repaint();
    }

    private boolean checkVictory(int[][] embarcacoes) {
        System.out.println(embarcacoes);
        int countAcertos = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (embarcacoes[i][j] < 0 && embarcacoes[i][j]!=-10) {
                    countAcertos++;
                }
            }
        }
        return countAcertos == 38;
    }
}
