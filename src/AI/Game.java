package AI;

import javafx.util.Pair;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Nam Phung on 30/11/2017.
 */
public class Game {
    private static final int[] COMP_MOVE = {2, 2, 2, 2, 2, 2, 2, 2, 2, 4};
    private Board board;
    private ComputerAI computerAI;
    private Random random;
    private BaseAI AI;

    public Game(BaseAI AI) {
        this.board = new Board();
        this.computerAI = new ComputerAI();
        this.random = new Random();
        this.AI = AI;
        startGame();
    }

    public void startGame() {
        ArrayList<Position> cells = board.getAvailableCells();
        for (int i = 0; i < 2; i++) {
            Position pos = cells.get(random.nextInt(cells.size()));
            cells.remove(pos);
            board.insertTile(pos, COMP_MOVE[random.nextInt(10)]);
        }
    }

    public static int[] getCompMove() {
        return COMP_MOVE;
    }

    public void run() {
        board.display();
        System.out.println("Available moves: " + board.getAvailableMoves());

        while (true) {
//            System.out.println("Player's turn: ");
//            int dir = AI.getMove(board);
//            board.move(dir);
//            board.printMove(dir);
//            board.display();
            playerTurn();
//            Thread.sleep(500);

//            computerMove();
//
//
//            System.out.println();
//            System.out.println("Computer's move: ");
//            board.display();
//            System.out.println("Available moves: " + board.getAvailableMoves());

            computerTurn();

            if (isOver()) {
                board.display();
                break;
            }
        }

        System.out.println("Game over!");
    }

    public void playerTurn() {
        System.out.println("Player's turn: ");
        int dir = AI.getMove(board);
        board.move(dir);
        board.printMove(dir);
        board.display();
    }

    public void computerTurn() {
        computerMove();


        System.out.println();
        System.out.println("Computer's move: ");
        board.display();
        System.out.println("Available moves: " + board.getAvailableMoves());
    }

    public void computerMove() {
        Position movePos = computerAI.getPosition(board);
        board.insertTile(movePos, computerAI.getMove(board));
    }

    public boolean isOver() {
        return !computerAI.canMove(board) && !AI.canMove(board);
    }

    public Board getBoard() {
        return board;
    }

    public static void main(String[] args) {
        BaseAI playerAI = new PlayerAI();
        Game game = new Game(playerAI);
        game.run();
    }
}
