# batalhaNaval

batalhaNaval is a classic Battleship game implemented in Java. This project provides a graphical user interface (GUI) for players to enjoy the strategic naval combat game. It follows an MVC (Model-View-Controller) architectural pattern to separate concerns, allowing for a clear structure and maintainable codebase, and incorporates an Observer pattern for communication between components.

## Main File Structure

```
batalhaNaval/
├── TelaInicio.jpeg
├── batalhaObserver.iml
├── src/
│   ├── Controller/
│   │   ├── Controller.java
│   │   ├── Observable.java
│   │   └── Observer.java
│   ├── Main.java
│   ├── Model/
│   │   ├── Jogador.java
│   │   ├── ModelAPI.java
│   │   ├── RecuperaMatriz.java
│   │   ├── SalvadorMatriz.java
│   │   └── Tiro.java
│   └── View/
│       ├── Embarcacao.java
│       ├── InserirNome.java
│       ├── Tabuleiro.java
│       ├── TabuleiroTiro.java
│       └── TelaInicio.java
```

## Project Dependencies

The project is built using standard Java Development Kit (JDK) features. It does not rely on any complex external libraries or frameworks beyond the core Java SE API for its functionality, including GUI components. A JDK (version 8 or newer is recommended) is the primary requirement.

## Instructions for using the project

### Installation

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/YOUR_USERNAME/batalhaNaval.git
    cd batalhaNaval
    ```
2.  **Ensure Java Development Kit (JDK) is installed**: This project requires a JDK (version 8 or newer is recommended) to compile and run.

### Configuration

The project requires no special configuration. It can be opened and run directly in any Java-compatible Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse, or compiled and executed from the command line.

### Running

To run the game, you can either use an IDE or the command line:

**Using an IDE (e.g., IntelliJ IDEA):**

1.  Open the `batalhaNaval` project in your IDE.
2.  Locate the `Main.java` file within the `src` directory.
3.  Run `Main.java` directly from your IDE.

**Using the Command Line:**

1.  Navigate to the project's root directory in your terminal (where `src` and `batalhaObserver.iml` are located).
2.  Compile the source files:
    ```bash
    mkdir -p bin # Create a directory for compiled classes
    javac -d bin src/Main.java src/Controller/*.java src/Model/*.java src/View/*.java
    ```
3.  Run the game:
    ```bash
    java -cp bin Main
    ```

## How to get help

If you encounter any issues or have questions, please open an issue on the GitHub repository for this project.