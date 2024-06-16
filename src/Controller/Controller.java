package Controller;

import Model.ModelAPI;
import View.InserirNome;
import View.Tabuleiro;
import View.TabuleiroTiro;
import View.TelaInicio;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Controller implements Observer {
    private ModelAPI model;
    private Tabuleiro tabuleiro;
    private TabuleiroTiro tabuleiroTiro;
    private static int currentPlayer;
    private static Controller instance;
    Observable obs;
    Object[] dados;
    String tipo;
    private File saveFile1;
    private File saveFile2;

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
                break;
            case "inseriu errado":
                handleInsercao(false);
                break;
            case "inseriu todos":
                switchPlayer();;
                break;

            case "atirou":
                registrarTiro((int) dados[1], (int) dados[2], (int) dados[3]);
                break;

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

            case "Salve o jogo":
                salvaroJogo();
                break;

            case "submarino":
            case "destroyer":
            case "couracado":
            case "hidroaviao":
            case "agua":
            case "cruzador":
                recuperarView((int) dados[1], (int) dados[2], (int) dados[3], (int) dados[4]);
                System.out.println("Dados: " + java.util.Arrays.toString(dados));
                break;


        }
        return;
    }

    public void ResultadoTiro(int resultado) {
        if (tabuleiroTiro != null) {
            tabuleiroTiro.atirou(resultado);
        }
    }

    public void inserir(int jogador, int tipoNavio, int linhaInicial, int colunaInicial, String orientacao) {
        model.inserirNavio(jogador, tipoNavio, linhaInicial, colunaInicial, orientacao);
    }

    public void handleInsercao(boolean result) {
        if (!result) {
            JOptionPane.showMessageDialog(tabuleiro, "Erro ao inserir navio. Tente novamente.");
            tabuleiro.selectShip();
            tabuleiro.setIsConfirming(false);
            resetTabuleiro(currentPlayer);
        }
    }

    public void switchPlayer() {
        if(currentPlayer == 1) {
            currentPlayer = 2;
            tabuleiro.setCurrentPlayer(2);
            tabuleiro.initializeShips();
            tabuleiro.setIsConfirming(false);
            tabuleiro.repaint();
        } else {
            JOptionPane.showMessageDialog(tabuleiro, "Ambos os jogadores posicionaram suas embarcações. O jogo pode começar!");
            tabuleiro.dispose();
        }
    }

    public void recuperarView(int jogador, int navio, int linha, int coluna){
        tabuleiroTiro.recuperarTiro(jogador, navio, linha, coluna);
    }

    public void resetTabuleiro(int jogador) {
        model.resetTabuleiro(jogador);
    }

    public void registrarTiro(int linha, int coluna, int jogador) {
        model.registrarTiro(linha, coluna, jogador);
    }


    public void salvaroJogo() {

        model.salvarMatriz(1, saveFile1.getPath());
        model.salvarMatriz(2, saveFile2.getPath());
    }

    private File escolherArquivoParaSalvar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Escolha o local para salvar o arquivo de jogo");
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Criação da tela inicial
            TelaInicio telaInicio = new TelaInicio();
            telaInicio.setVisible(true);

            telaInicio.getStartButton().addActionListener(e -> { // Caso eu selecione a opção de Iniciar um novo jogo
                Controller controller = getInstance();
                controller.saveFile1 = controller.escolherArquivoParaSalvar();
                controller.saveFile2 = controller.escolherArquivoParaSalvar();

                if (controller.saveFile1 != null && controller.saveFile2 != null) {
                    JFrame panel = new JFrame();
                    InserirNome gui = InserirNome.getInstance(panel);
                    gui.setVisible(true);

                    if (gui.nome1 != null && gui.nome2 != null) {
                        ModelAPI.getInstance().setNome(1, gui.nome1);
                        ModelAPI.getInstance().setNome(2, gui.nome2);

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
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione dois arquivos para salvar o jogo.");
                }
            });

            telaInicio.getLoadButton().addActionListener(e -> { // Caso eu selecione a opção de carregar
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(false);
                int returnValue1 = fileChooser.showOpenDialog(null);
                if (returnValue1 == JFileChooser.APPROVE_OPTION) {
                    File saveFile1 = fileChooser.getSelectedFile();
                    System.out.println(saveFile1);

                    int returnValue2 = fileChooser.showOpenDialog(null);
                    if (returnValue2 == JFileChooser.APPROVE_OPTION) {
                        File saveFile2 = fileChooser.getSelectedFile();
                        System.out.println(saveFile2);

                        Controller controller = getInstance();
                        controller.saveFile1 = saveFile1; // Inicializa saveFile1
                        controller.saveFile2 = saveFile2; // Inicializa saveFile2

                        JFrame panel = new JFrame();
                        InserirNome gui = InserirNome.getInstance(panel);
                        gui.setVisible(true);

                        if (gui.nome1 != null && gui.nome2 != null) {
                            ModelAPI.getInstance().setNome(1, gui.nome1);
                            ModelAPI.getInstance().setNome(2, gui.nome2);

                            SwingUtilities.invokeLater(() -> {
                                TabuleiroTiro tabuleiroTiro = TabuleiroTiro.getInstance(gui.nome1, gui.nome2);
                                tabuleiroTiro.addObserver(controller);
                                controller.setTabuleiroTiro(tabuleiroTiro);
                                controller.model.carregarMatriz(1, saveFile1.getPath());
                                controller.model.carregarMatriz(2, saveFile2.getPath());
                                tabuleiroTiro.setVisible(true);
                            });
                        } else {
                            JOptionPane.showMessageDialog(null, "Nomes dos jogadores não inseridos corretamente.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, selecione o segundo arquivo.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione o primeiro arquivo.");
                }
            });
        });
    }



}