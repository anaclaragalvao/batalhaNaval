# batalhaNaval

`batalhaNaval` is an engaging implementation of the classic Battleship board game, developed in Java. This project provides a graphical user interface (GUI) that allows players to strategically place their fleet, engage in naval combat, and track hits and misses against an opponent. Designed with a modular architecture, likely following the Model-View-Controller (MVC) pattern, it aims for clear separation of concerns, enhancing maintainability and scalability.

## File Structure

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

The project primarily relies on the Java Development Kit (JDK) for compilation and execution, leveraging standard Java libraries for its core functionalities and graphical user interface.

*   **Java Development Kit (JDK) 8 or higher**

## Instructions for Using the Project

### Installation

To get a local copy up and running, follow these simple steps:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/batalhaNaval.git
    cd batalhaNaval
    ```
    (Replace `https://github.com/your-username/batalhaNaval.git` with the actual repository URL if available.)

2.  **Ensure Java Development Kit (JDK) is installed:**
    Verify your JDK installation by running:
    ```bash
    java -version
    javac -version
    ```
    If not installed, download and install the appropriate JDK version from Oracle or OpenJDK.

### Running

To compile and run the game from the command line:

1.  **Compile the source code:**
    Navigate to the project's root directory (`batalhaNaval/`) and compile all Java files.
    ```bash
    javac -d out src/**/*.java
    ```

2.  **Run the application:**
    After successful compilation, execute the main class:
    ```bash
    java -cp out Main
    ```
    Alternatively, if using an IDE like IntelliJ IDEA (indicated by `batalhaObserver.iml`), you can open the project directly and run `src/Main.java`.

## How to Get Help

If you encounter any issues or have questions, please feel free to open an issue on the project's GitHub repository.