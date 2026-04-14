# Batalha Naval

Batalha Naval is a classic turn-based strategy game implemented in Java. This project recreates the popular game of Battleship, where players strategically place their ships on a grid and attempt to sink their opponent's fleet by guessing their locations.

## Project Structure

```
README.md
TelaInicio.jpeg
batalhaObserver.iml
src
    └── Controller
        └── Controller.java
        └── Observable.java
        └── Observer.java
    └── Main.java
    └── Model
        └── Jogador.java
        └── ModelAPI.java
        └── RecuperaMatriz.java
        └── SalvadorMatriz.java
        └── Tiro.java
    └── View
        └── Embarcacao.java
        └── InserirNome.java
        └── Tabuleiro.java
        └── TabuleiroTiro.java
        └── TelaInicio.java
```

## Dependencies

This project is written in Java and does not rely on external libraries beyond the standard Java Development Kit (JDK).

## Usage Instructions

### Installation

1.  Ensure you have a Java Development Kit (JDK) installed on your system.
2.  Clone this repository to your local machine.

### Running the Game

1.  Navigate to the project's root directory in your terminal.
2.  Compile the Java code:
    ```bash
    javac src/Main.java
    ```
3.  Run the main class:
    ```bash
    java Main
    ```
4.  The game's initial screen will appear, and you can follow the on-screen instructions to start playing.

## Additional Documentation

For a visual overview of the game's starting interface, please refer to `TelaInicio.jpeg`.

## How to Get Help

If you encounter any issues or have questions regarding the Batalha Naval game, please open an issue in this project's repository.

## License

This project is open source and available for use and modification.
