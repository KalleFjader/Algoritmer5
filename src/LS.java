//package tictactoe;

import java.awt.*;

// Class to represent Move objects
class Move {
    int val;   // Value of the move
    int row;   // Row and column coordinates
    int col;
    int temp;

    public int getVal() {
        return val;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getTemp() {
        return temp;
    }

    public Move(int v, int r, int c) {
        val=v;
        row=r;
        col=c;
    }




}

// Class Button extends JButton with (x,y) coordinates
class Button extends javax.swing.JButton {
    public int i;   // The row and column coordinate of the button in a GridLayout
    public int j;

    public Button (int x, int y) {
        // Create a JButton with a blank icon. This also gives the button its correct size.
        super();
        super.setIcon(new javax.swing.ImageIcon(getClass().getResource("None.png")));
        this.i = x;
        this.j = y;
    }

    // Return row coordinate
    public int get_i () {
        return i;
    }

    // Return column coordinate
    public int get_j () {
        return j;
    }

}

public class LS extends javax.swing.JFrame {

    // Marks on the board
    public static final int EMPTY    = 0;
    public static final int HUMAN    = 1;
    public static final int COMPUTER = 2;

    // Outcomes of the game
    public static final int HUMAN_WIN    = 4;
    public static final int DRAW         = 5;
    public static final int CONTINUE     = 6;
    public static final int COMPUTER_WIN = 7;

    public static final int SIZE = 3;
     int[][] board = new int[SIZE][SIZE];  // The marks on the board
    private javax.swing.JButton[][] jB;           // The buttons of the board
    private int turn = HUMAN;                    // HUMAN starts the game

    public int[] results = {4, 5, 6, 7, 8, 9, 10, 11, 12};
    public int counter = 0;


    /* Constructor for the Tic Tac Toe game */
    public LS() {
        // Close the window when the user exits
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        initBoard();      // Set up the board with all marks empty
    }



    // Initalize an empty board. 
    private void initBoard(){
        // Create a SIZE*SIZE gridlayput to hold the buttons
        java.awt.GridLayout layout = new GridLayout(SIZE, SIZE);
        getContentPane().setLayout(layout);

        // The board is a grid of buttons
        jB = new Button[SIZE][SIZE];
        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                // Create a new button and add an actionListerner to it
                jB[i][j] = new Button(i,j);
                // Add an action listener to the button to handle mouse clicks
                jB[i][j].addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent act) {
                        jBAction(act);
                    }
                });
                add(jB[i][j]);   // Add the buttons to the GridLayout

                board[i][j] = EMPTY;     // Initialize all marks on the board to empty
            }
        }
        // Pack the GridLayout and make it visible
        pack();
    }



    // Action listener which handles mouse clicks on the buttons
    private void jBAction(java.awt.event.ActionEvent act) {
        Button thisButton = (Button) act.getSource();   // Get the button clicked on
        // Get the grid coordinates of the clicked button
        int i = thisButton.get_i();
        int j = thisButton.get_j();
        //insertResults(i, j, turn);
        System.out.println("Button[" + i + "][" + j + "] was clicked by " + turn);  // DEBUG


        // Check if this square is empty.
        // If it is empty then place a HUMAN mark (X) in it and check if it was a winning move

        // In this version no checks are done, the marks just alter between HUMAN and COMPUTER

        // Set an X or O mark in the clicked button
        //if (checkResult() == CONTINUE) {
            thisButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("X.png")));
            place (i, j, HUMAN);
            insertResults(i, j, HUMAN);
            counter++;

            Move move = findCompMove();
            thisButton = (Button) jB[move.getRow()][move.getCol()];
            thisButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("O.png")));
            place (move.getRow(), move.getCol(), COMPUTER);
            counter++;
            insertResults(move.getRow(), move.getCol(), COMPUTER);
            System.out.println(board[0][0]);
            System.out.print(board[0][1]);
            System.out.print(board[0][2]);
            System.out.println(board[1][0]);
            System.out.print(board[1][1]);
            System.out.print(board[1][2]);
            System.out.println(board[2][0]);
            System.out.print(board[2][1]);
            System.out.print(board[2][2]);
            }

        //}
            // Mark the move on the board

        // Give the turn to the opponent

        // In a real game, you should instead call a method and compute the response move of the computer
        // The computer chooses a successor position with a maximal value

        // Check if we are done (that is COMPUTER or HUMAN wins)
        //if (checkResult() != CONTINUE) {
        //    return;
        //}
    //}

    public Move findCompMove() {
        int responseValue;
        int value;
        int bestMove = 0;
        Move quickWinInfo;
        /* Undersök först terminala positiner */
        if (fullBoard()) {            /* Oavgjort */
            value = LS.DRAW;
        }
        else {
            if ((quickWinInfo = immediateCompWin()) != null) {
                return quickWinInfo;
            }
            else {        								/* Undersök icke-terminala positiner */
                value = LS.HUMAN_WIN; 					/* Börja med minsta värde */
                for (int i = 0; i < 9; i++) {			/* Kolla alla rutor */
                    if (isEmpty(i)) { 				/* Välj en tom ruta */
                        place(i, LS.COMPUTER); 			/* Undersök den */
                        responseValue = findHumanMove().val;
                        unplace(i); 					/* Återställ brädet */
                        if (responseValue > value) { 	/* Uppdatera bästa draget */
                            value = responseValue;
                            bestMove = i;
                        }
                    }
                }
            }
        }
        int r = bestMove/LS.SIZE;
        int c = bestMove%LS.SIZE;
        return new Move(value, r, c);
    }


    public Move findHumanMove() {
        int i;
        int responseValue;
        int value;
        int bestMove = 1;
        Move quickWinInfo;
        /* Undersök först terminala positiner */
        if (fullBoard()) {            /* Oavgjort */
            value = LS.DRAW;
        }
        else {
            if ((quickWinInfo = immediateHumanWin()) != null) {
                return quickWinInfo;
            }
            else {        /* Undersök icke-terminala positiner */
                value = LS.COMPUTER_WIN; /* Börja med största värde */
                for (i = 0; i < 9; i++) /* Kolla alla rutor */ {
                    if (isEmpty(i)) { /* Välj en tom ruta */
                        place(i, LS.HUMAN); /* Undersök den */
                        responseValue = findCompMove().val;
                        unplace(i); /* Återställ brädet */
                        if (responseValue < value) { /* Uppdatera bästa draget */
                            value = responseValue;
                            bestMove = i;
                        }
                    }
                }
            }
        }
        int r = bestMove/LS.SIZE;
        int c = bestMove%LS.SIZE;
        return new Move(value,r,c);
    }

    private void unplace(int i) {
        int r = i / SIZE;
        int c = i % SIZE;
        place(r, c, EMPTY);
    }

    private void place(int i, int computer) {
        int r = i / SIZE;
        int c = i % SIZE;
        place(r, c, computer);
    }


    private Move immediateCompWin() {
        for(int i = 0; i < 9; i++) {
            int r = i/3;
            int c = i%3;
            if(board[r][c] == LS.EMPTY) {
                place(i, LS.COMPUTER);
                if(checkResult() == COMPUTER_WIN) {
                    return new Move(COMPUTER_WIN, r, c);
                }
                unplace(i);
            }
        }
        return null;
    }

    private Move immediateHumanWin() {
        for(int i = 0; i < 9; i++) {
            int r = i/3;
            int c = i%3;
            if(board[r][c] == LS.EMPTY) {
                place(i, LS.HUMAN);
                if(checkResult() == HUMAN_WIN) {
                    return new Move(HUMAN_WIN, r, c);
                }
                unplace(i);
            }
        }
        return null;
    }


//Våra metoder
    public void insertResults(int i, int j, int turn) {
        if (i == 0) {
            if (j == 0) {
                results[0] = turn;
            } else if (j == 1) {
                results[1] = turn;
            } else if (j == 2) {
                results[2] = turn;
            }
        } else if (i == 1) {
            if (j == 0) {
                results[3] = turn;
            } else if (j == 1) {
                results[4] = turn;
            } else if (j == 2) {
                results[5] = turn;
            }
        } else if (i == 2) {
            if (j == 0) {
                results[6] = turn;
            } else if (j == 1) {
                results[7] = turn;
            } else if (j == 2) {
                results[8] = turn;
            }
        }
    }

    private int checkResult() {
        // This function should check if one player (HUMAN or COMPUTER) wins, if the board is full (DRAW)
        // or if the game should continue. You implement this.
       // counter++;
//
       // if (results[0] == results[1] && results[0] == results[2]) {
       //     return results[0];
       // } else if (results[3] == results[4] && results[3] == results[5]) {
       //     return results[3];
       // } else if (results[6] == results[7] && results[6] == results[8]) {
       //     return results[6];
       // } else if (results[0] == results[3] && results[0] == results[6]) {
       //     return results[0];
       // } else if (results[1] == results[4] && results[1] == results[7]) {
       //     return results[1];
       // } else if (results[2] == results[5] && results[2] == results[8]) {
       //     return results[2];
       // } else if (results[2] == results[4] && results[2] == results[6]) {
       //     return results[2];
       // } else if (results[0] == results[4] && results[0] == results[8]) {
       //     return results[0];
       // } else if(counter == 9) {
       //     return DRAW;
       // }else{
       //         return CONTINUE;
       // }

        for (int i=0;i<3;i++){
            if (board[0][i] == COMPUTER && board[1][i] == COMPUTER && board[2][i] == COMPUTER){
                return COMPUTER_WIN;
            }
        }
        //rader
        for (int i=0;i<3;i++){
            if (board[i][0] == COMPUTER && board[i][1] == COMPUTER && board[i][2] == COMPUTER) {
                return COMPUTER_WIN;
            }
        }
        //diagonalerna
        if (board[0][0] == COMPUTER && board[1][1] == COMPUTER && board[2][2] == COMPUTER) {
            return COMPUTER_WIN;
        }
        if(board[0][2] == COMPUTER && board[1][1] == COMPUTER && board[2][0] == COMPUTER){
            return COMPUTER_WIN;
        }

        for (int i=0;i<3;i++){
            if (board[0][i] == HUMAN && board[1][i] == HUMAN && board[2][i] == HUMAN){
                return HUMAN_WIN;
            }
        }
        //rader
        for (int i=0;i<3;i++){
            if (board[i][0] == HUMAN && board[i][1] == HUMAN && board[i][2] == HUMAN) {
                return HUMAN_WIN;
            }
        }
        //diagonalerna
        if (board[0][0] == HUMAN && board[1][1] == HUMAN && board[2][2] == HUMAN) {
            return HUMAN_WIN;
        }
        if(board[0][2] == HUMAN && board[1][1] == HUMAN && board[2][0] == HUMAN){
            return HUMAN_WIN;
        }

        if(fullBoard()){
            return DRAW;
        }
        return CONTINUE;
    }
/*
    public int checkResult() {
        // This function should check if one player (HUMAN or COMPUTER) wins, if the board is full (DRAW)
        // or if the game should continue. You implement this.
        if (fullBoard()){
            return DRAW;
        }

        else if (checkComputerWin()){
            return COMPUTER_WIN;
        }
        return CONTINUE;
    }
*/
    public int checkSquare(int row, int col){
        return board[row][col];
    }

    public boolean fullBoard() {
        for (int i = 0; i < 9; i++){
          int  r = i/SIZE;
          int  c = i/SIZE;
            if (board[r][c] == EMPTY){
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty(int i) {
        int r = i/SIZE;
        int c = i%SIZE;
        return board[r][c] == EMPTY;
    }

private boolean checkHumanWin(){
    //kolumner
    for (int i=0;i<3;i++){
        if (board[0][i] == HUMAN && board[1][i] == HUMAN && board[2][i] == HUMAN){
            return true;
        }
    }
    //rader
    for (int i=0;i<3;i++){
        if (board[i][0] == HUMAN && board[i][1] == HUMAN && board[i][2] == HUMAN) {
            return true;
        }
    }
    //diagonalerna
    if (board[0][0] == HUMAN && board[1][1] == HUMAN && board[2][2] == HUMAN) {
        return true;
    }
    return board[0][2] == HUMAN && board[1][1] == HUMAN && board[2][0] == HUMAN;
}
/*
    private boolean checkComputerWin(){
        //kolumner
        for (int i=0;i<3;i++){
            if (board[0][i] == COMPUTER && board[1][i] == COMPUTER && board[2][i] == COMPUTER){
                return true;
            }
        }
        //rader
        for (int i=0;i<3;i++){
            if (board[i][0] == COMPUTER && board[i][1] == COMPUTER && board[i][2] == COMPUTER) {
                return true;
            }
        }
        //diagonalerna
        if (board[0][0] == COMPUTER && board[1][1] == COMPUTER && board[2][2] == COMPUTER) {
            return true;
        }
        return board[0][2] == COMPUTER && board[1][1] == COMPUTER && board[2][0] == COMPUTER;
    }

    public int checkResult() {
        // This function should check if one player (HUMAN or COMPUTER) wins, if the board is full (DRAW)
        // or if the game should continue. You implement this.
        if (fullBoard()){
            return DRAW;
        }

        else if (checkComputerWin()){
            return COMPUTER_WIN;
        }
        return CONTINUE;
    }

*/
    // Place a mark for one of the playsers (HUMAN or COMPUTER) in the specified position
    public void place(int row, int col, int player){
        board [row][col] = player;
    }


    public static void main (String [] args){

        String threadName = Thread.currentThread().getName();
        LS lsGUI = new LS();      // Create a new user inteface for the game
        lsGUI.setVisible(true);

        java.awt.EventQueue.invokeLater (new Runnable() {
            public void run() {
                while ( (Thread.currentThread().getName() == threadName) &&
                        (lsGUI.checkResult() == CONTINUE) ){
                    try {
                        Thread.sleep(100);  // Sleep for 100 millisecond, wait for button press
                    } catch (InterruptedException e) { };
                }
            }
        });
    }
}
