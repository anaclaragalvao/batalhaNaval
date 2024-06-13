package View;

import Controller.Controller;
import Controller.Observable;
import Controller.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.WHITE;

public class Tabuleiro extends JFrame implements Observable{
    public static final int SIZE = 15;
    public static final int CELL_SIZE = 30;

    private String player1Name;
    private String player2Name;

    private List<Embarcacao> ships;
    private Embarcacao selectedShip;
    private boolean isShipSelected;
    private int currentPlayer;
    private boolean isConfirming;
    private static Tabuleiro instance;

    private Controller controller;
    private List<Observer> observers = new ArrayList<>();
    boolean inserir = false;

    public Tabuleiro(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;


        ships = new ArrayList<>();
        selectedShip = null;
        isShipSelected = false;
        currentPlayer = 1; // Começa com o jogador 1
        isConfirming = false;


        setTitle("Batalha Naval");
        setSize(SIZE * CELL_SIZE + 300, SIZE * CELL_SIZE + 70);
        //setUndecorated(true); // Remove a barra de título
        //setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        TabuleiroPanel tabuleiroPanel = new TabuleiroPanel();
        tabuleiroPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
        tabuleiroPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        tabuleiroPanel.setFocusable(true);
        add(tabuleiroPanel);
        tabuleiroPanel.requestFocusInWindow();

        initializeShips();
    }

    public static Tabuleiro getInstance(String player1Name, String player2Name) {
        if (instance == null) {
            instance = new Tabuleiro(player1Name,player2Name);
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
        Object[] dados = new Object[6];  // Ensure the array has correct length
        if (inserir ) {
            int startX = getBoardStartX();
            int startY = getBoardStartY();
            String coordenadaInicial = selectedShip.getCoordenadaInicial(startX, startY);
            int[] coordenada = converterCoordenada(coordenadaInicial);
            if (coordenada != null) {
                String orientacao;
                if (selectedShip.getOrientacao() == 0) {
                    orientacao = "leste";
                } else if (selectedShip.getOrientacao() == 1) {
                    orientacao = "sul";
                } else if (selectedShip.getOrientacao() == 2) {
                    orientacao = "oeste";
                } else if (selectedShip.getOrientacao() == 3) {
                    orientacao = "norte";
                } else {
                    orientacao = "leste"; // Orientação padrão
                }

                dados[0] = "Inserir";
                dados[1] = currentPlayer;
                dados[2] = selectedShip.getTipoNavio();
                dados[3] = coordenada[0];
                dados[4] = coordenada[1];
                dados[5] = orientacao;
                System.out.println("Dados: " + java.util.Arrays.toString(dados));
            } else {
                // Handle null coordinate case (if necessary, add specific handling here)
                System.out.println("Error: Coordenada is null.");
                return new Object[0];
            }
            inserir = false;
        }
        return dados;
    }

    private class TabuleiroPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int tabuleiroWidth = SIZE * CELL_SIZE;
            int tabuleiroHeight = SIZE * CELL_SIZE;

            int startX = getBoardStartX();
            int startY = getBoardStartY();
            for (int j = 0; j < SIZE; j++) {
                String num = Integer.toString(j + 1);
                int numWidth = g2d.getFontMetrics().stringWidth(num);
                g2d.drawString(num, startX + j * CELL_SIZE + (CELL_SIZE - numWidth) / 2, startY - 5);
            }

            g2d.drawString("Jogador 1: " + player1Name, 10, 20);
            g2d.drawString("Jogador 2: " + player2Name, 10, 40);
            g2d.drawString("Jogador atual: " + (currentPlayer == 1 ? player1Name : player2Name), 10, 60);

            for (int i = 0; i < SIZE; i++) {
                char letter = (char) ('A' + i);
                int letterHeight = g2d.getFontMetrics().getAscent();
                g2d.drawString(Character.toString(letter), startX - 20, startY + i * CELL_SIZE + (CELL_SIZE + letterHeight) / 2);
            }

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    Rectangle2D.Double cell = new Rectangle2D.Double(startX + j * CELL_SIZE, startY + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g2d.setColor(WHITE);
                    g2d.fill(cell);
                    g2d.setColor(Color.BLACK);
                    g2d.draw(cell);
                }
            }

            for (Embarcacao ship : ships) {
                ship.draw(g2d);
            }

            if (selectedShip != null) {
                g2d.setColor(selectedShip.getColor());
                for (Rectangle2D.Double cell : selectedShip.getCells()) {
                    g2d.setColor(selectedShip.getColor());
                    g2d.fill(cell);
                    g2d.setColor(Color.BLACK);
                    g2d.draw(cell);
                }
            }
        }
    }

    public int getBoardStartX() {
        int panelWidth = getWidth();
        int tabuleiroWidth = SIZE * CELL_SIZE;
        return panelWidth - tabuleiroWidth - 20;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int Player){
        currentPlayer = Player;
        return;
    }

    public void setIsConfirming(boolean isConfirming) {
        this.isConfirming = isConfirming;
    }

    public int getBoardStartY() {
        int panelHeight = getHeight();
        int tabuleiroHeight = SIZE * CELL_SIZE;
        return (panelHeight - tabuleiroHeight) / 2;
    }

    private void handleMouseClick(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (!isShipSelected) {
                for (Embarcacao ship : ships) {
                    if (ship.contains(x, y)) {
                        selectedShip = ship;
                        isShipSelected = true;
                        selectedShip.setErrored(false); // Reset error flag when selecting a ship
                        repaint();
                        break;
                    }
                }
            } else {
                int startX = getBoardStartX();
                int startY = getBoardStartY();

                int col = (x - startX) / CELL_SIZE;
                int row = (y - startY) / CELL_SIZE;

                if (col >= 0 && col < SIZE && row >= 0 && row < SIZE) {
                    double dx = startX + col * CELL_SIZE - selectedShip.getCells().get(0).getX();
                    double dy = startY + row * CELL_SIZE - selectedShip.getCells().get(0).getY();
                    selectedShip.move(dx, dy);
                    isShipSelected = false;
                    selectedShip = null;
                    repaint();
                }
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (!isShipSelected) {
                for (Embarcacao ship : ships) {
                    if (ship.contains(x, y)) {
                        selectedShip = ship;
                        break;
                    }
                }
            }

            if (selectedShip != null) {
                // Ponto de ancoragem: extremidade esquerda da embarcação (primeira célula)
                Rectangle2D.Double anchorCell = selectedShip.getCells().get(0);
                double anchorX = anchorCell.getX();
                double anchorY = anchorCell.getY();

                selectedShip.rotate(anchorX, anchorY, CELL_SIZE);
                repaint();
            }
        }
    }

/*
    private void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!isConfirming) {
                if (selectedShip != null) {
                    isShipSelected = false;
                    selectedShip.setErrored(false); // Reset error flag when deselecting a ship
                    selectedShip = null;
                    repaint();
                } else {
                    int option = JOptionPane.showConfirmDialog(this, "Confirma o posicionamento das suas embarcações?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        isConfirming = true;
                        if (!checarInsercao()) {
                            JOptionPane.showMessageDialog(this, "Erro ao inserir navio. Tente novamente.");
                            isConfirming = false;
                            controller.resetTabuleiro(currentPlayer);
                        } else {
                            if (currentPlayer == 1) {
                                currentPlayer = 2;
                                initializeShips();  // Limpa e inicializa as embarcações para o jogador 2
                                isConfirming = false;
                                repaint();
                            } else {
                                JOptionPane.showMessageDialog(this, "Ambos os jogadores posicionaram suas embarcações. O jogo pode começar!");
                                dispose();
                            }
                        }
                    }
                }
            }
        }
    }
    */

    private void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!isConfirming) {
                if (selectedShip != null) {
                    isShipSelected = false;
                    selectedShip.setErrored(false); // Reset error flag when deselecting a ship
                    selectedShip = null;
                    repaint();
                } else {
                    int option = JOptionPane.showConfirmDialog(this, "Confirma o posicionamento das suas embarcações?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        isConfirming = true;
                        checarInsercao();
                        notifyObservers(); // Notify observers about the insertion attempt
                    }
                }
            }
        }
    }


    public boolean areAllShipsInserted() {
        for (Embarcacao ship : ships) {
            if (ship.isErrored()) {
                return false;
            }
        }
        return true;
    }

    private void checarInsercao() {
        /*
        int startX = getBoardStartX();
        int startY = getBoardStartY();

        for (Embarcacao ship : ships) {
            ship.setErrored(false); // Reset error flag before checking
            String coordenadaInicial = ship.getCoordenadaInicial(startX, startY);
            int[] coordenada = converterCoordenada(coordenadaInicial);
            if (coordenada != null) {
                String orientacao = ship.getOrientacao() == 0 ? "horizontal" : "vertical";
                inserir = true;
                if (!controller.inserirNavio(currentPlayer, ship.getTipoNavio(), coordenada[0], coordenada[1], orientacao)) {
                    ship.setErrored(true); // Marcar navio com erro
                    selectedShip = ship;
                    repaint();
                    return false; // Falha ao inserir navio
                }
            } else {
                ship.setErrored(true); // Marcar navio com erro
                selectedShip = ship;
                repaint();
                return false; // Coordenada inválida
            }
        }
        repaint();
        return true; // Todos os navios foram inseridos corretamente

         */
        inserir = false; // Reset insertion flag
        List<Embarcacao> shipsToProcess = new ArrayList<>(ships); // Create a copy of the ships list

        for (Embarcacao ship : shipsToProcess) {
            ship.setErrored(false); // Reset error flag before checking
            selectedShip = ship;
            String coordenadaInicial = ship.getCoordenadaInicial(getBoardStartX(), getBoardStartY());
            int[] coordenada = converterCoordenada(coordenadaInicial);
            if (coordenada != null) {
                inserir = true;
                notifyObservers();
            } else {
                ship.setErrored(true); // Mark ship with error
                repaint();
            }
            selectedShip = null;
        }

    }

    private int[] converterCoordenada(String coordenada) {
        if (coordenada.length() < 2) {
            return null;
        }

        char letra = coordenada.charAt(0);
        int linha = Character.toUpperCase(letra) - 'A';
        int coluna;

        try {
            coluna = Integer.parseInt(coordenada.substring(1)) - 1;
        } catch (NumberFormatException e) {
            return null;
        }

        return new int[]{linha, coluna};
    }

    public boolean isShipSelected() {
        return isShipSelected;
    }

    public void deselectShip() {
        isShipSelected = false;
        if (selectedShip != null) {
            selectedShip.setErrored(false); // Reset error flag when deselecting a ship
        }
        selectedShip = null;
        repaint();
    }

    public void selectShip(){
        selectedShip.setErrored(true); // Marcar navio com erro
        //selectedShip = ship;
        repaint();
    }

    public void initializeShips() {
        ships.clear();

        int shipStartX = 50;
        int shipStartY = 100;
        for (int i = 0; i < 4; i++) {
            List<Rectangle2D.Double> cells = new ArrayList<>();
            int x = shipStartX + i * 2 * CELL_SIZE;
            int y = shipStartY;
            cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            ships.add(new Embarcacao(cells, new Color(144, 238, 144), 1));
        }

        for (int i = 0; i < 3; i++) {
            List<Rectangle2D.Double> cells = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                int x = shipStartX + i * 3 * CELL_SIZE + j * CELL_SIZE;
                int y = shipStartY + CELL_SIZE * 2;
                cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            }
            ships.add(new Embarcacao(cells, Color.YELLOW, 2));
        }

        for (int i = 0; i < 2; i++) {
            List<Rectangle2D.Double> cells = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                int x = shipStartX + i * 5 * CELL_SIZE + j * CELL_SIZE;
                int y = shipStartY + CELL_SIZE * 4;
                cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            }
            ships.add(new Embarcacao(cells, Color.ORANGE, 4));
        }

        List<Rectangle2D.Double> redShipCells = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            int x = shipStartX + j * CELL_SIZE;
            int y = shipStartY + CELL_SIZE * 6;
            redShipCells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
        }
        ships.add(new Embarcacao(redShipCells, Color.PINK, 5));

        for (int i = 0; i < 5; i++) {
            List<Rectangle2D.Double> cells = new ArrayList<>();
            int baseX = shipStartX + i * (3 * CELL_SIZE + CELL_SIZE);
            int baseY = shipStartY + CELL_SIZE * 8;
            int[][] coords = {{0, 1}, {1, 0}, {2, 1}};
            for (int[] coord : coords) {
                int x = baseX + coord[0] * CELL_SIZE;
                int y = baseY + coord[1] * CELL_SIZE;
                cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            }
            ships.add(new Embarcacao(cells, Color.BLUE, 3));
        }
    }

}