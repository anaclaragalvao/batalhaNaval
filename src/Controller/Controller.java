package Controller;

import Model.ModelAPI;
import View.InserirNome;
import View.Tabuleiro;
import View.TabuleiroTiro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller implements Observer {
    private ModelAPI model;
    private Tabuleiro tabuleiro;
    private TabuleiroTiro tabuleiroTiro;
    public int currentPlayer;
    private static Controller instance;
    Observable obs;
    Object[] dados;
    String tipo;

    public Controller() {
        model = ModelAPI.getInstance();
        model.addObserver(this);

        currentPlayer = 1;
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void setTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public void setTabuleiroTiro(TabuleiroTiro tabuleiroTiro) {
        this.tabuleiroTiro = tabuleiroTiro;
    }

    @Override
    public void notify(Observable arg){
        // Recebe o objeto observável que notificou a mudança.
        obs = arg;

        // Obtém os dados transmitidos pelo objeto observável.
        dados = (Object []) obs.get();

        // Obtém o tipo de notificação, que determina a natureza da mudança.
        tipo = (String) dados[0];

        switch(tipo){


            case"Inserir":
                System.out.println("Oi 1");
                inserir((int) dados[1], (int) dados[2], (int) dados[3], (int) dados[4], (String) dados[5]);
                System.out.println("Dados: " + java.util.Arrays.toString(dados));

                break;

            case "inseriu certo":
                //handleInsercao(true);
                break;
            case "inseriu errado":
                handleInsercao(false);
                break;
            case "inseriu todos":
                switchPlayer();;
                break;

            case "atirou":
                registrarTiro((int) dados[1], (int) dados[2], (int) dados[3]);

            case "Acertou um submarino!":
            case "Acertou um destroyer!":
            case "Acertou um hidroavião!":
            case "Acertou um cruzador!":
            case "Acertou um couraçado!":
            case "Acertou a água":
            case "Tentou acertar um lugar já atirado":
                ResultadoTiro((int) dados[1]);
                System.out.println("Dados: " + java.util.Arrays.toString(dados));
                break;

        }
        return;
    }

    public void ResultadoTiro(int resultado){
        if (tabuleiroTiro != null) {
            tabuleiroTiro.atirou(resultado);
        }
    }

    public void inserir(int jogador, int tipoNavio, int linhaInicial, int colunaInicial, String orientacao) {
        model.salvarMatriz(jogador);
        model.inserirNavio(jogador, tipoNavio, linhaInicial, colunaInicial, orientacao);
        return ;
    }



    public void handleInsercao(boolean result) {
        if (!result) {
            JOptionPane.showMessageDialog(tabuleiro, "Erro ao inserir navio. Tente novamente.");
            tabuleiro.selectShip();
            tabuleiro.setIsConfirming(false);
            resetTabuleiro(currentPlayer);
        } else {
            System.out.println("Ta chegando aqui");

            // Check if all ships for the current player are inserted

        }
    }

    public void switchPlayer(){
        if(currentPlayer==1) {
            currentPlayer = 2;
            tabuleiro.setCurrentPlayer(2);
            tabuleiro.initializeShips();  // Initialize ships for player 2
            tabuleiro.setIsConfirming(false);
            tabuleiro.repaint();
        }

        else {
            JOptionPane.showMessageDialog(tabuleiro, "Ambos os jogadores posicionaram suas embarcações. O jogo pode começar!");
            tabuleiro.dispose();
        }
    }

    public void resetTabuleiro(int jogador) {
        model.resetTabuleiro(jogador);
    }

    public void registrarTiro(int linha, int coluna, int jogador) {
        model.registrarTiro(linha, coluna, jogador);
        return;
    }

    public boolean[][] getTiros(int jogador) {
        return model.getTiros(jogador);
    }

    public int[][] getTabuleiro(int jogador) {
        return model.getTabuleiro(jogador);
    }

    public void salvarMatriz(int jogador) {
        model.salvarMatriz(jogador);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame panel = new JFrame();
            InserirNome gui = InserirNome.getInstance(panel);
            gui.setVisible(true);

            if (gui.nome1 != null && gui.nome2 != null) {
                ModelAPI.getInstance().setNome(1, gui.nome1);
                ModelAPI.getInstance().setNome(2, gui.nome2);

                Controller controller = getInstance();
                Tabuleiro tabuleiro = Tabuleiro.getInstance(gui.nome1, gui.nome2);
                controller.setTabuleiro(tabuleiro);
                tabuleiro.addObserver(controller);
                tabuleiro.setVisible(true);

                // Adicionar um WindowListener para detectar quando a janela de inserção for fechada
                tabuleiro.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        SwingUtilities.invokeLater(() -> {
                            TabuleiroTiro tabuleiroTiro = TabuleiroTiro.getInstance(gui.nome1, gui.nome2);
                            tabuleiroTiro.addObserver(controller);
                            controller.setTabuleiroTiro(tabuleiroTiro);
                            tabuleiroTiro.setVisible(true);
                        });
                    }
                });
            } else {
                System.out.println("Nomes dos jogadores não inseridos corretamente.");
            }
        });
    }
}
