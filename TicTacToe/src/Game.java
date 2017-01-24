import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Basic Tic Tac Toe game to play with Java arrays, std in/out
 *
 * Created by Kirill on 17-Jan-17.
 */
public class Game {
    private static final char FIG_X = 'X';
    private static final char FIG_O = 'O';

    private final Scanner in = new Scanner(System.in);
    private char userFig;
    private char compFig;

    private char[][] board = new char[3][3];

    private enum Result {DRAW, WIN_USER, WIN_COMP};

    private Result result = null;

    private boolean usersMove;

    /**
     * Start the game of Tic Tac Toe
     */
    public void play() {
        readUserFig();

        resetBoard();

        drawBoard();

        while (result == null) {
            if (usersMove) {
                moveUser();
            } else {
                moveComp();
            }
            drawBoard();

            usersMove = !usersMove;

            evaluateResult();
        }

        System.out.println("Game result is: " + result);
    }

    /**
     * Check if user/comp won or it's a draw
     *
     * @TODO: it now uses a basic way of determining the result, it could be optimized to have less iterations
     * and less copy-pasted code (but atm it's out of scope of playing with Java's functionality and data structures)
     */
    private void evaluateResult() {
        // looking if there is a winning row
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == ' ') {
                // this row is not complete yet
                continue;
            }
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                result = board[row][0] == userFig ? Result.WIN_USER : Result.WIN_COMP;
            }
        }
        // looking if there is a winning column
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == ' ') {
                // this column is not complete yet
                continue;
            }
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                result = board[0][col] == userFig ? Result.WIN_USER : Result.WIN_COMP;
            }
        }
        // looking if there is a winning diagonal
        if (board[1][1] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            result = board[0][0] == userFig ? Result.WIN_USER : Result.WIN_COMP;
        }
        if (board[1][1] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            result = board[0][2] == userFig ? Result.WIN_USER : Result.WIN_COMP;
        }

        if (result == null && !hasFreeCells()) {
            result = Result.DRAW;
        }
    }

    /**
     * Check if board has still free cells
     * @return bool true if there are still free cells on the board
     */
    private boolean hasFreeCells() {
        // @TODO: atm this is a plain stupid way, it could be optimized by e.g. counting number of moves,
        // or keeping set of available free cells and reducing this set after each move

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Reads the figure that user will be playing with: X or O
     */
    private void readUserFig() {
        userFig = ' ';
        do {
            System.out.print("Choose your figure (X or O): ");
            String line = in.nextLine();
            if (line.length() != 1) {
                System.out.println("Figure should be exactly 1 character long!");
                continue;
            }

            userFig = line.toUpperCase().charAt(0);
            if (userFig != FIG_X && userFig != FIG_O) {
                System.out.println("Wrong figure, try again!");
                userFig = ' ';
            }
        } while (userFig == ' ');

        compFig = userFig == FIG_O ? FIG_X : FIG_O;

        System.out.printf("You're playing with: %s%n", userFig);
        System.out.printf("And computer plays with: %s%n", compFig);

        usersMove = userFig == FIG_X;
    }

    /**
     * Initialize board with empty cells
     */
    private void resetBoard() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    /**
     * Output board with the current state
     */
    private void drawBoard() {
        System.out.printf("%n  1   2   3%n");

        for (int i=0; i<3; i++) {
            System.out.printf("%d ", i+1);

            // damn, no sane implode() alternative, gotta loop again
            for (int j=0; j<3; j++) {
                System.out.print(board[i][j]);
                if (j < 2) {
                    System.out.print(" | ");
                }
            }

            System.out.println();
            if (i < 2) {
                System.out.println("  ---------");
            }
        }

        System.out.println();
    }

    /**
     * Handle user's move
     */
    private void moveUser() {
        boolean userMoved = false;
        do {
            int userMoveRow = getIntPosition("row");
            int userMoveColumn = getIntPosition("column");

            if (board[userMoveRow - 1][userMoveColumn - 1] == ' ') {
                board[userMoveRow - 1][userMoveColumn - 1] = userFig;
                userMoved = true;
            } else {
                System.out.println("This field is already taken!");
            }
        } while (!userMoved);
    }

    /**
     * Get position for row or column of the user's move
     * @param type row or column
     * @return int
     */
    private int getIntPosition(String type) {
        int position = 0;
        while (position == 0) {
            System.out.printf("Your next move %s is: ", type);
            if (!in.hasNextInt()) {
                in.nextLine();
                continue;
            }
            position = in.nextInt();
            if (position > 3 || position < 1) {
                position = 0;
            }
        }
        return position;
    }

    /**
     * Handle computer's move.
     * Very stupid AI, the goal is to try basic Java tooling, not to build a smart robot
     *
     * @TODO: in future AI can look through columns/rows/diagonals and detect if there is already his mark there,
     * so it can put another mark accordingly.
     */
    private void moveComp()
    {
        System.out.println("Computer moves...");

        // getting list of empty cells
        List<Integer> availableMoves = new ArrayList<Integer>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    availableMoves.add(row * 3 + col);
                }
            }
        }

        Random rand = new Random();
        int moveIndex = rand.nextInt(availableMoves.size());
        int cellIndex = availableMoves.get(moveIndex);

        board[cellIndex / 3][cellIndex % 3] = compFig;
    }
}
