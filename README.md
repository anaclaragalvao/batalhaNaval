# batalhaNaval

"batalhaNaval" is a classic Battleship game implemented in Java. This project provides an interactive and engaging experience of the popular naval strategy game, where players attempt to sink their opponent's hidden fleet. Developed with a clear separation of concerns using the Model-View-Controller (MVC) architectural pattern, it offers a robust and maintainable codebase for an intuitive gameplay experience.

## Main File Structure

```
.
├── README.md
├── TelaInicio.jpeg
├── batalhaObserver.iml
├── src
│   ├── Controller
│   │   ├── Controller.java
│   │   ├── Observable.java
│   │   └── Observer.java
│   ├── Main.java
│   ├── Model
│   │   ├── Jogador.java
│   │   ├── ModelAPI.java
│   │   ├── RecuperaMatriz.java
│   │   ├── SalvadorMatriz.java
│   │   └── Tiro.java
│   └── View
│       ├── Embarcacao.java
│       ├── InserirNome.java
│       ├── Tabuleiro.java
│       ├── TabuleiroTiro.java
│       └── TelaInicio.java
```

## Project Dependencies

*   Java Development Kit (JDK) 8 or higher.
*   Standard Java libraries (e.g., Swing for GUI components).

## Instructions for using the project

### Installation

To get a local copy up and running, follow these simple steps.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/batalhaNaval.git
    cd batalhaNaval
    ```
2.  **Ensure Java is installed:** Make sure you have a Java Development Kit (JDK) installed on your system (version 8 or higher is recommended). You can download it from Oracle or use an open-source alternative like OpenJDK.

### Configuration

This project does not require specific configuration beyond ensuring your Java environment is correctly set up. If you are using an Integrated Development Environment (IDE) like IntelliJ IDEA, you can simply import the project as an existing Java project.

### Running

After cloning the repository and ensuring your Java environment is ready, you can compile and run the application from the command line:

1.  **Navigate to the `src` directory:**
    ```bash
    cd src
    ```
2.  **Compile the Java source files:**
    ```bash
    javac Main.java Controller/*.java Model/*.java View/*.java
    ```
3.  **Run the application:**
    ```bash
    java Main
    ```
Alternatively, if using an IDE, you can run the `Main.java` file directly.

## Additional documentation / resources

No specific external documentation is provided for this project at this time. The codebase is designed to be self-explanatory, particularly through its MVC structure.

## How to get help

If you encounter any issues or have questions regarding `batalhaNaval`, please open an issue on the project's GitHub repository.

## Terms of use / license

No specific license information has been provided for this project.