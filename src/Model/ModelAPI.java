package Model;

import Controller.Observer;
import Controller.Observable;

import java.util.*;

public class ModelAPI implements Observable {
    private static ModelAPI instance;
    private Jogador jogador1;
    private Jogador jogador2;
    private List<Observer> observers = new ArrayList<>();
    private boolean inseriuCerto, inseriuErrado, inseriuTodos1, tiro1, tiro2, tiro3, tiro4, tiro5, tiro6, tiro7 = false;
    int count1, count2 = 0;

    public ModelAPI() {
        jogador1 = new Jogador();
        jogador2 = new Jogador();
    }

    public static ModelAPI getInstance() {
        if (instance == null) {
            instance = new ModelAPI();
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
    public Object[] get(){
        Object[] dados = new Object[6];
        if (inseriuCerto) {
            dados[0] = "inseriu certo";
            inseriuCerto = false;
        } else if (inseriuErrado) {
            dados[0] = "inseriu errado";
            dados[1]=
            inseriuErrado = false;
        }
        else if(inseriuTodos1){
            dados[0] = "inseriu todos";
            inseriuTodos1 = false;
        }
        else if(tiro1){
            dados[0]= "Acertou um submarino!";
            dados[1]= 1;
            tiro1 = false;
        }
        else if(tiro2){
            dados[0]="Acertou um destroyer!";
            dados[1]= 2;
            tiro2 = false;
        }
        else if(tiro3){
            dados[0]="Acertou um hidroavião!";
            dados[1]= 3;
            tiro3 = false;
        }
        else if(tiro4){
            dados[0]="Acertou um cruzador!";
            dados[1]= 4;
            tiro4 = false;
        }
        else if(tiro5){
            dados[0]="Acertou um couraçado!";
            dados[1]= 5;
            tiro5 = false;
        }
        else if(tiro6){
            dados[0]="Acertou a água";
            dados[1]= 10;
            tiro6 = false;
        }
        else if(tiro7){
            dados[0]="Tentou acertar um lugar já atirado";
            dados[1]= 11;
            tiro7 = false;
        }


        return dados;
    }

    public void inserirNavio(int jogador, int tipoNavio, int linhaInicial, int colunaInicial, String orientacao) {
        if (orientacao == null) {
            System.out.println("Error: orientacao is null.");
            inseriuErrado = true;
            notifyObservers();
            return;
        }

        boolean result;
        System.out.println("Inserting ship: jogador=" + jogador + ", tipoNavio=" + tipoNavio + ", linhaInicial=" + linhaInicial + ", colunaInicial=" + colunaInicial + ", orientacao=" + orientacao);

        if (jogador == 1) {
            if(count1<15){
                System.out.println(count1);
                result = jogador1.inserirNavio(tipoNavio, linhaInicial, colunaInicial, orientacao);
                if(result){
                    count1++;
                    if(count1==15){
                        inseriuTodos1 =true;
                        notifyObservers();
                    }
                }
                else{
                    inseriuErrado = true;
                    count1=0;
                    notifyObservers();
                    return;
                }

            }



        } else {
            if (count2 < 15) {
                System.out.println(count1);
                result = jogador2.inserirNavio(tipoNavio, linhaInicial, colunaInicial, orientacao);
                if (result) {
                    count2++;
                    if (count2 == 15) {
                        inseriuTodos1 = true;
                        notifyObservers();
                    }
                } else {
                    inseriuErrado = true;
                    count2 = 0;
                    notifyObservers();
                    return;
                }

            }
        }
    }



    public void resetTabuleiro(int jogador) {
        if (jogador == 1) {
            jogador1.resetTabuleiro();
        } else {
            jogador2.resetTabuleiro();
        }
        notifyObservers();
    }

    public void registrarTiro(int linha, int coluna, int jogador) {
        int result;
        if (jogador == 1) {
            result = jogador2.registrarTiro(linha, coluna);
        } else {
            result = jogador1.registrarTiro(linha, coluna);
        }
        if(result==1){

            tiro1 = true;
        }
        else if(result==2){
            tiro2 = true;
        }
        else if(result==3){
            tiro3 = true;
        }
        else if(result==4){
            tiro4 = true;
        }
        else if(result==5){
            tiro5 = true;

        }
        else if(result==10){
            tiro6 = true;

        }
        else if(result==-1){
            tiro7 = true;

        }
        notifyObservers();
        return;
    }

    public void setNome(int jogador, String nome) {
        if (jogador == 1) {
            jogador1.setNome(nome);
        } else {
            jogador2.setNome(nome);
        }
        notifyObservers();
    }

    public boolean[][] getTiros(int jogador) {
        if (jogador == 1) {
            return jogador1.getTiros();
        } else {
            return jogador2.getTiros();
        }
    }

    public int[][] getTabuleiro(int jogador) {
        if (jogador == 1) {
            return jogador1.getMatriz();
        } else {
            return jogador2.getMatriz();
        }
    }

    public void salvarMatriz(int jogador) {
        if (jogador == 1) {
            jogador1.salvarMatrizEmArquivo("matriz1nova.txt");
        } else {
            jogador2.salvarMatrizEmArquivo("matriz2nova.txt");
        }
    }

    public Jogador getJogador(int jogador) {
        if (jogador == 1) {
            return jogador1;
        } else {
            return jogador2;
        }
    }
}
