// Project by Alexander Flores
/*
This file creates a chess game in the terminal. The rules implemented are listed
in the chess wikipedia (https://en.wikipedia.org/wiki/Chess).

The only rules not implemented are:

* win conditions : resignation, win on time, and forfeit
* draw condition is not implemented
* time control
 */

import java.util.Scanner;
import java.util.LinkedList;

public class chessBoard {
    /**
     This class holds an 8x8 board. Every method in the board in one way or another
     interacts with board. They can either modify the board, copy it, print the board
     (or print a message), move a piece, or do something if a condition is met.
     [List the rules; use chess wiki]****** explain at top and tell what function imitates that
     [also to stay consistent use getColor() method]*******
     */
    private final int boardHorizontal = 8;
    private final int boardVertical = 8;
    private chessPiece blackKing;
    private chessPiece whiteKing;
    private chessPiece[][] board;
    // 2-D chessboard. The top of the board is index 0 and the bottom of the board
    // is index 7
    private LinkedList<chessPiece[][]> stack;
    // holds previous chessboards

    private enum Color {BLACK, WHITE};
    private enum Piece {ROOK__, KNIGHT, BISHOP, QUEEN_, KING__, PAWN__}



    private static class chessPiece {
        /**
         This class defines what a chess piece is. Each chess piece has a vertical
         and horizontal position, a name (a title), and a color associated with them.
         Some piece have a special move, like castling (rook and king) or en passant
         (pawns). Including to this each piece has a way of moving and checking for
         how it move is done through calling the method isMoveLegal(). Note that the
         only possible color a chess piece can have is black or white. Also, the only
         possible piece are rook, knight, bishop, queen, king, or pawn.
         */
        private int horizontalPosition;
        private int verticalPosition;
        private Piece chessPieceName;
        private final Color color;
        private int specialMove;
        // the special move is only for king/pawn/rook chess pieces.
        // helps tell if a king/rook can castle and if a pawn can en passant
        // this keeps track if they moved


        public chessPiece( int vertical, int horizontal, Color c, Piece name) {
            horizontalPosition = horizontal;
            verticalPosition = vertical;
            color = c;
            chessPieceName = name;
            specialMove = 0;
        }


        public int getHorizontalPosition() {
            // returns a piece horizontal position
            return horizontalPosition;
        }


        public int getVerticalPosition() {
            // returns a piece vertical position
            return verticalPosition;
        }


        public Color getColor() {
            // returns a piece color
            return color;
        }


        public Piece getChessPieceName() {
            // returns a piece name
            return chessPieceName;
        }


        public boolean isMoveLegal(int newVertical, int newHorizontal, chessPiece[][] currentBoard) {
            /**
             This method defines how a chess piece may move within the board. It
             uses the newVertical and newHorizontal parameters (intended place to move)
             and the piece vertical and horizontal Position (where it currently is at)
             to calculate the difference between these two points and using its color
             and the currentBoard parameter to find if a piece may move. Note that
             this method does not modify the board, just checks if a piece can move.

             @param newVertical = intended change in vertical position
             @param newHorizontal = intended change in horizontal position
             @param currentBoard = current board environment

             @return true - if piece can move; false - if piece cannot move
             */
            int yMinuend;
            int ySubtrahend;

            int xMinuend;
            int xSubtrahend;

            int verticalDifference;
            int horizontalDifference;

            // figure out how many squares it will go forward/backwards
            yMinuend =  newVertical;
            ySubtrahend = verticalPosition;

            // figure out how many squares it will go left/right
            xMinuend = newHorizontal;
            xSubtrahend = horizontalPosition;

            if (color.equals(Color.WHITE) && !chessPieceName.equals(Piece.KNIGHT)
                    && !chessPieceName.equals(Piece.BISHOP)) {
                // going 'forward' is opposite for black pawns
                // knight and bishop pieces do no work if Minuend/Subtrahend
                // are switched
                yMinuend = verticalPosition;
                ySubtrahend = newVertical;

                xMinuend = horizontalPosition;
                xSubtrahend = newHorizontal;
            }

            verticalDifference = yMinuend - ySubtrahend;
            // calculate vertical difference
            horizontalDifference = xMinuend - xSubtrahend;
            // calculate horizontal difference


            if (chessPieceName.equals(Piece.PAWN__)) {
                // piece only comes here if it is a pawn piece
                return pawnMove(newVertical, newHorizontal, currentBoard,
                        verticalDifference, horizontalDifference);


            } else if (chessPieceName.equals(Piece.ROOK__)) {
                // piece only comes here if it is a rook piece
                return rookMove(newVertical, newHorizontal, currentBoard,
                        verticalDifference, horizontalDifference, yMinuend,
                        ySubtrahend, xMinuend, xSubtrahend);


            } else if (chessPieceName.equals(Piece.KNIGHT)) {
                // piece only comes here if it is a knight piece
                return knightMove(newVertical, newHorizontal,
                        currentBoard, verticalDifference, horizontalDifference);

            } else if (chessPieceName.equals(Piece.BISHOP)) {
                // piece only comes here if it is a bishop piece
                return bishopMove(newVertical, newHorizontal, currentBoard,
                        verticalDifference, horizontalDifference);

            } else if (chessPieceName.equals(Piece.QUEEN_)) {
                // piece only comes here if it is a queen piece
                // a queen is a rook and bishop combined
                if (verticalDifference != 0 && horizontalDifference == 0) {
                    // if there is no horizontal change, but there is vertical
                    // change then it comes here
                    return rookMove(newVertical, newHorizontal, currentBoard,
                            verticalDifference, horizontalDifference, yMinuend,
                            ySubtrahend, xMinuend, xSubtrahend);

                } else if (verticalDifference == 0 && horizontalDifference != 0) {
                    // if there is no vertical change, but there is horizontal
                    // change then it comes here
                    return rookMove(newVertical, newHorizontal, currentBoard,
                            verticalDifference, horizontalDifference, yMinuend,
                            ySubtrahend, xMinuend, xSubtrahend);

                } else {
                    // if there is a vertical/horizontal difference then it comes here
                    // need to recalculate Minuend, Subtrahend, and Difference
                    // because it was flipped earlier
                    yMinuend =  newVertical;
                    ySubtrahend = verticalPosition;

                    xMinuend = newHorizontal;
                    xSubtrahend = horizontalPosition;

                    verticalDifference = yMinuend - ySubtrahend;
                    horizontalDifference = xMinuend - xSubtrahend;

                    return bishopMove(newVertical, newHorizontal, currentBoard,
                            verticalDifference, horizontalDifference);
                }

            } else if (chessPieceName.equals(Piece.KING__)) {
                // piece only comes here if it is a king piece
                return kingMove(newVertical, newHorizontal, currentBoard,
                        verticalDifference, horizontalDifference);
            }

            return false;
        }


        public boolean pawnMove (int newVertical, int newHorizontal, chessPiece[][] currentBoard,
                                 int verticalDifference, int horizontalDifference) {
            /**
             This method figures out if a pawn can move (a pawn can only move forward).
             In most cases a pawn can move one square forward if there is no pawn
             in front of it, and can move diagonally one square if there is an enemy
             piece. If a piece has not moved from its starting position then it
             can move forward two squares if no enemy/ally piece is between these
             two squares. Note that a black pawn going forward is going positive
             direction in the board and for a white pawn is going in the negative
             direction.

             @param newVertical = intended change in vertical position
             @param newHorizontal = intended change in horizontal position
             @param currentBoard = current board environment
             @param verticalDifference = difference between piece verticalPosition
             and newVertical
             @param horizontal difference = difference between piece horizontalPosition
             and newHorizontal

             @return true - if pawn can move; false - if pawn cannot move
             */
            if (verticalPosition == 1 || verticalPosition == 6) {
                // figure out if pawn can move forward if pawn is at start
                if (horizontalDifference == 0) {
                    // pawn moving vertically forward
                    chessPiece pieceOne;
                    // piece "in front" of pawn
                    chessPiece pieceTwo;
                    // piece "in front" of piece one

                    if (color.equals(Color.BLACK)) {
                        // going "forward" for black pawn
                        int tempY = verticalPosition + 2;

                        pieceOne = currentBoard[verticalPosition+1][newHorizontal];

                        if (tempY <= 7)  {
                            // this check is for when the method isMyKingInCheck
                            // is called
                            pieceTwo = currentBoard[verticalPosition+2][newHorizontal];
                        } else {
                            pieceTwo = pieceOne;
                        }
                    } else {
                        // going "forward" for white pawn
                        int tempY = verticalPosition - 2;

                        pieceOne = currentBoard[verticalPosition-1][newHorizontal];

                        if (tempY >= 0) {
                            // this check is for when the method isMyKingInCheck
                            // is called
                            pieceTwo = currentBoard[verticalPosition-2][newHorizontal];
                        } else {
                            pieceTwo = pieceOne;
                        }
                    }

                    if (pieceOne == null && verticalDifference == 1) {
                        return true;
                    }

                    if (verticalDifference == 2 && pieceOne == null && pieceTwo == null) {
                        return true;
                    }

                } else {
                    // if at pawn is at start and moving one square diagonaly

                    chessPiece newSpot = currentBoard[newVertical][newHorizontal];
                    // spot pawn is intending to move to
                    chessPiece oldSpot = currentBoard[verticalPosition][horizontalPosition];
                    // current spot of pawn

                    if (newSpot != null && !newSpot.color.equals(oldSpot.color)) {
                        if ((horizontalDifference == 1 || horizontalDifference == -1)
                                && (verticalDifference == 1 || verticalDifference == -1)) {
                            return true;
                        }
                    }
                }

            } else {
                // any move not at start is checked here

                chessPiece newSpot = currentBoard[newVertical][newHorizontal];
                // spot pawn is intending to move to
                chessPiece oldSpot = currentBoard[verticalPosition][horizontalPosition];
                // current spot of pawn

                if (verticalDifference == 1 && horizontalDifference == 0 && newSpot == null) {
                    // check if pawn is moving forward
                    return true;
                } else if ( verticalDifference == 1  && newSpot != null &&
                        !newSpot.color.equals(oldSpot.color)) {
                    if (horizontalDifference == 1 || horizontalDifference == -1) {
                        // check if pawn is moving diagonally
                        return true;
                    }
                }
            }

            return false;
        }


        public boolean rookMove (int newVertical, int newHorizontal, chessPiece[][] currentBoard,
                                 int verticalDifference, int horizontalDifference, int yMinuend,
                                 int ySubtrahend, int xMinuend, int xSubtrahend) {
            /**
             This method figures out if a rook piece can move. A rook can move forward
             , backward, left, and right as far as it wants if it is within the boards
             vertical and horizontal length.

             @param newVertical = intended change in vertical position
             @param newHorizontal = intended change in horizontal position
             @param currentBoard = current board environment
             @param verticalDifference = difference between piece verticalPosition
             and newVertical
             @param horizontalDifference = difference between piece horizontalPosition
             and newHorizontal
             @param yMinuend = possibly newVertical or verticalPosition
             @param ySubtrahend = possibly newVertical or verticalPosition
             @param xMinuend = possibly newHorizontal or horizontalPosition
             @param xSubtrahend = possibly newHorizontal or horizontalPosition

             @return true - if rook can move ; false if rook cannot move
             */
            if (verticalDifference != 0 && horizontalDifference == 0) {
                // check if rook is moving forward or backwards
                if (verticalDifference < 0) {
                    // switch yMinuend and ySubtrahend if yMinuend < ySubtrahend
                    int temp = yMinuend;
                    yMinuend = ySubtrahend;
                    ySubtrahend = temp;
                }

                if (currentBoard[newVertical][newHorizontal] != null
                        && color.equals(currentBoard[newVertical][newHorizontal].color)) {
                    // check that the new position does not have an ally piece
                    return false;
                }

                yMinuend -= 1;

                for (int i = yMinuend;  i != ySubtrahend; i--) {
                    // make sure there is no chessPiece between new position and
                    // old position
                    if (currentBoard[i][newHorizontal] != null) {
                        return false;
                    }
                }

                return true;

            } else if (verticalDifference == 0 && horizontalDifference != 0) {
                // check if rook is moving left or right
                if (horizontalDifference < 0) {
                    // switch xMinuend and xSubtrahend if xMinuend < xSubtrahend
                    int temp = xMinuend;
                    xMinuend = xSubtrahend;
                    xSubtrahend = temp;
                }

                if (currentBoard[newVertical][newHorizontal] != null
                        && color.equals(currentBoard[newVertical][newHorizontal].color)) {
                    // check that the new position does not have an ally piece
                    return false;
                }

                xMinuend -= 1;

                for (int i = xMinuend; i != xSubtrahend; i--) {
                    // make sure there is no chessPiece between new position and
                    // old position
                    if (currentBoard[newVertical][i] != null) {
                        return false;
                    }
                }

                return true;
            }

            return false;
        }


        public boolean knightMove(int newVertical, int newHorizontal, chessPiece[][] currentBoard,
                                  int verticalDifference, int horizontalDifference) {
            /**
             This method figures out if a knight can move. A knight can only move
             by going vertically or horizontally by 2 squares and move one square
             in the direction not chosen to move two squares (an L shape move).

             @param newVertical = intended change in vertical position
             @param newHorizontal = intended change in horizontal position
             @param currentBoard = current board environment
             @param verticalDifference = difference between piece verticalPosition
             and newVertical
             @param horizontalDifference = difference between piece horizontalPosition
             and newHorizontal

             @return true - if knight can move; false - if knight cannot move
             */
            chessPiece oldPiece = currentBoard[newVertical][newHorizontal];
            // old piece at new spot

            if (oldPiece == null || !color.equals(oldPiece.color)) {
                if (verticalDifference == 2 || verticalDifference == -2) {
                    if (horizontalDifference == 1 || horizontalDifference == -1) {
                        // check it is moving in an L direction and if oldPiece
                        // does not have the same color as the piece moving
                        return true;
                    }
                }

                if (verticalDifference == 1 || verticalDifference == -1) {
                    if (horizontalDifference == 2 || horizontalDifference == -2) {
                        // check it is moving in an L direction and if oldPiece
                        // does not have the same color as the piece moving
                        return true;
                    }
                }
            }

            return false;
        }


        public boolean bishopMove(int newVertical, int newHorizontal, chessPiece[][] currentBoard,
                                  int verticalDifference, int horizontalDifference) {
            /**
             This method check if a bishop can move. A bishop can only move diagonally.
             in the board. If it wants to move north-east in the board, then the
             verticalDifference is negative and the horizontalDifference is positive.
             If it wants to move south-east in the board then the verticalDifference is
             positive and the horizontalDifference is positive. If it wants to move
             south-west in the board then the verticalDifference is positive and the
             horizontalDifference is negative. If it wants to move north-west in the
             board then the verticalDifference is negative and horizontalDifference is
             negative.

             @param newVertical = intended change in vertical position
             @param newHorizontal = intended change in horizontal position
             @param currentBoard = current board environment
             @param verticalDifference = difference between piece verticalPosition
             and newVertical
             @param horizontalDifference = difference between piece horizontalPosition
             and newHorizontal

             @return true - if bishop can move; false - if bishop cannot move
             */
            int yStart = verticalPosition;
            // starting y position in the board
            int xStart = horizontalPosition;
            // starting x position in the board

            if (verticalDifference > 0 && horizontalDifference > 0) {
                // south-East condition
                while (true) {
                    yStart++;
                    xStart++;

                    if (yStart >= 8 || xStart >= 8) break;
                    // check that y and x won't be outside the boards index bounds

                    chessPiece currentP = currentBoard[yStart][xStart];


                    if (yStart == newVertical && xStart == newHorizontal) {
                        // check if you reached your destination
                        if (currentP == null || !color.equals(currentP.color)) {
                            // if you are at your destination then check it is null
                            // or an enemy piece
                            return true;
                        }
                    } else if (currentP != null) break;
                    // otherwise, break if there exists a piece
                }

            } else if (verticalDifference > 0 && horizontalDifference < 0) {
                // south-west condition
                while (true) {
                    yStart++;
                    xStart--;

                    if (yStart >= 8 || xStart < 0) break;
                    // check that y and x won't be outside the boards index bounds

                    chessPiece currentP = currentBoard[yStart][xStart];

                    if (yStart == newVertical && xStart == newHorizontal) {
                        // check if you reached your destination
                        if (currentP == null || !color.equals(currentP.color)) {
                            // if you are at your destination then check it is null
                            // or an enemy piece
                            return true;
                        }
                    } else if (currentP != null) break;
                    // otherwise, break if there exists a piece
                }
            }

            if (verticalDifference < 0 && horizontalDifference > 0) {
                // north-east condition
                while (true) {
                    yStart--;
                    xStart++;

                    if (yStart < 0 || xStart >= 8) break;
                    // check that y and x won't be outside the boards index bounds

                    chessPiece currentP = currentBoard[yStart][xStart];

                    if (yStart == newVertical && xStart == newHorizontal) {
                        // check if you reached your destination
                        if (currentP == null || !color.equals(currentP.color)) {
                            // if you are at your destination then check it is null
                            // or an enemy piece
                            return true;
                        }
                    }  else if (currentP != null) break;
                    // otherwise, break if there exists a piece
                }

            } else if (verticalDifference < 0 && horizontalDifference < 0) {
                // north-west condition
                while (true) {
                    yStart--;
                    xStart--;

                    if (yStart < 0 || xStart < 0) break;
                    // check that y and x won't be outside the boards index bounds

                    chessPiece currentP = currentBoard[yStart][xStart];

                    if (yStart == newVertical && xStart == newHorizontal) {
                        // check if you reached your destination
                        if (currentP == null || !color.equals(currentP.color)) {
                            // if you are at your destination then check it is null
                            // or an enemy piece
                            return true;
                        }
                    } else if (currentP != null) break;
                    // otherwise, break if there exists a piece
                }
            }

            return false;
        }


        public boolean kingMove(int newVertical, int newHorizontal, chessPiece[][] currentBoard,
                                int verticalDifference, int horizontalDifference) {
            /**
             This method checks if the king piece can move. The king can move one
             square any direction.

             @param newVertical = intended change in vertical position
             @param newHorizontal = intended change in horizontal position
             @param currentBoard = current board environment
             @param verticalDifference = difference between piece verticalPosition
             and newVertical
             @param horizontalDifference = difference between piece horizontalPosition
             and newHorizontal

             @return true - if king can move; false - if king cannot move
            */
            if (verticalDifference <= 1 && verticalDifference >= -1 &&
                    horizontalDifference <= 1 && horizontalDifference >= -1) {
                // this checks if -1 <= verticalDifference <= 1
                // and if -1 <= horizontalDifference <= 1
                chessPiece p = currentBoard[newVertical][newHorizontal];

                if (p == null|| !color.equals(p.color)) {
                    // checks that new spot is null or the piece there is not an
                    // ally piece
                    return true;
                }
            }

            return false;
        }


    }



    public chessBoard () {
        board = new chessPiece[boardVertical][boardHorizontal];
        stack = new LinkedList<chessPiece[][]>();

        for (int i = 0; i < boardVertical; i++) {
            // this loop sets up each piece going vertically
            chessPiece p;

            if (i == 0  || i == 7) {
                // piece are added at the top and bottom of the chessboard
                Color t;

                if (i == 0) {
                    t = Color.BLACK;
                } else {
                    t = Color.WHITE;
                }

                board[i][0] = new chessPiece(i, 0, t, Piece.ROOK__);
                board[i][1] = new chessPiece(i, 1, t, Piece.KNIGHT);
                board[i][2] = new chessPiece(i, 2, t, Piece.BISHOP);
                board[i][3] = new chessPiece(i, 3, t, Piece.QUEEN_);
                board[i][4] = new chessPiece(i, 4, t, Piece.KING__);
                board[i][5] = new chessPiece(i, 5, t, Piece.BISHOP);
                board[i][6] = new chessPiece(i, 6, t, Piece.KNIGHT);
                board[i][7] = new chessPiece(i, 7, t, Piece.ROOK__);

                if (i == 0) {
                    // keeps track of king
                    blackKing = board[i][4];
                } else {
                    whiteKing = board[i][4];
                }


            }

            if (i == 1 || i == 6) {
                // if statement adds pawns to row 1 and 6 (index 0 is not row 1!)
                for (int j = 0; j < boardVertical; j++) {
                    if (i == 1) {
                        p = new chessPiece(1, j, Color.BLACK, Piece.PAWN__);
                    } else {
                        p = new chessPiece(6, j, Color.WHITE, Piece.PAWN__);
                    }

                    board[i][j] = p;
                }
            }
        }

        System.out.println("\nWHITE STARTS\n");
        printBoard(board);
    }


    public void printBoard(chessPiece[][] currentBoard) {
        /**
         This method prints out the given board.

         @param currentBoard = board that will be printed
         */
        StringBuilder boardToString = new StringBuilder();

        boardToString.append("  |");

        for (int i = 0; i < boardHorizontal; i++ ) {
            // adds the letter identifiers for each column to the StringBuilder
            boardToString.append("   ");
            boardToString.append((char) (97 + i) );
            boardToString.append("   ");
            boardToString.append("|");
        }

        boardToString.append("\n");

        for (int i = 0; i < boardVertical; i++) {
            boardToString.append("  ____________________________________" +
                    "_____________________________\n");

            boardToString.append(8 - i);
            // adds current number identifier for each row to the StringBuilder
            boardToString.append(" ");
            boardToString.append("|");

            for (int j = 0; j < boardHorizontal; j++) {
                // this for loop adds each row to the StringBuilder
                chessPiece p = currentBoard[i][j];

                if (p == null) {
                    // if null adds spaces to the StringBuilder
                    boardToString.append("       ");
                } else {
                    // else adds the color and chess piece to the StringBuilder
                    if(p.color.equals(Color.WHITE)) {
                        boardToString.append("w");
                    } else boardToString.append("b");

                    boardToString.append(p.chessPieceName);
                }

                boardToString.append("|");
            }

            boardToString.append(" ");
            boardToString.append(8 - i);
            // adds current number identifier for each row to the StringBuilder
            boardToString.append("\n");
        }

        boardToString.append("  ___________________________________" +
                "______________________________\n");

        boardToString.append("  |");

        for (int i = 0; i < boardHorizontal; i++ ) {
            // adds the letter identifiers for each column to the StringBuilder
            boardToString.append("   ");
            boardToString.append((char) (97 + i) );
            boardToString.append("   ");
            boardToString.append("|");
        }

        System.out.println(boardToString.toString());
    }


    public void gameBoard() {
        /**
         This method connects everything in this file together and creates a chess games.
         This method ask for a string input in two different instances. The first input is
         for choosing a piece to move and the second input is for where to move that piece.
         A valid input is one lower case letter between a-h with a number between 1-8 (e.g.
         a7, 7a or h7; not A7, 7A, A 7 or 7). If you want to load a previous board
         then type prev when the board asks for 'Pick a Piece:'. Using these two user
         inputs this method will decide if a move is possible. If a move is possible then
         it is reflected on the board and chess piece. Else nothing happens and the same
         player will need to choose another move. Cases where a move will fail is if
         a null piece is picked, user input is outside the boards index, wrong colored piece
         is picked, or if it puts the current players king piece in check.
         */
        Scanner myObj = new Scanner(System.in);

        Color colour = Color.WHITE;

        while (true) {
            System.out.println("\nPick a Piece: ");

            String userInput = myObj.nextLine();

            if (userInput.equals("prev")) {
                // go back to the previous board
                if (stack.isEmpty()) {
                    System.out.println("\nNo Previous Board\n");
                    printBoard(board);

                    continue;
                }

                if (colour.equals(Color.WHITE)) {
                    colour = Color.BLACK;
                } {
                    colour = Color.WHITE;
                }

                previousMove();
                printNewPlayer(colour);

                continue;
            }

            if (userInput.length() != 2) {
                // make sure only a vertical and horizontal move is specified
                // from user
                printInvalidInput(colour);

                continue;
            }

            int x = userInput.charAt(0) - 97;
            // assum userInput[0] is a letter
            int y = userInput.charAt(1) - 48;
            // assum userInput[1] is a #

            if (x > 7 || x < 0) {
                x = userInput.charAt(1) - 97;
                // userInput[1] is a letter
                y = userInput.charAt(0) - 48;
                // userInput[0] is a #

            }

            y = 8 - y;
            // top row is index 0; bottom row is index 7

            if (x > 7 || x < 0 || y > 7 || y < 0) {
                // check if y and x are within board index
                printInvalidInput(colour);

                continue;
            }

            chessPiece p = board[y][x];
            // p is piece that will be moved

            if (p == null) {
                // if chosen piece is null then redo
                System.out.println("Cannot Move Null " +  colour + " Player. " +
                        "Try Again\n");
                printBoard(board);

                continue;
            }

            if (!p.getColor().equals(colour)) {
                // check if piece being moved is the same color as the player
                System.out.println("Cannot Move " + p.color + " " + p.chessPieceName + ", " +
                        colour + " Player.\n");
                printBoard(board);

                continue;
            }

            System.out.println("\nMove that Piece to: ");

            userInput = myObj.nextLine();

            if (userInput.length() != 2) {
                // make sure only a vertical and horizontal move is specified
                // from user
                printInvalidInput(colour);

                continue;
            }

            int newHorizontal = userInput.charAt(0) - 97;
            // assum userInput[0] is a letter
            int newVertical = userInput.charAt(1) - 48;
            // assum userInput[1] is a #

            if (newHorizontal > 7 || newHorizontal < 0) {
                newHorizontal = userInput.charAt(1) - 97;
                // userInput[1] is a letter
                newVertical = userInput.charAt(0) - 48;
                // userInput[0] is a #

            }

            newVertical = 8 - newVertical;
            // top row is index 0; bottom row is index 7

            if (newHorizontal > 7 || newHorizontal < 0 || newVertical > 7
                    || newVertical < 0) {
                // check if vertical and horizontal are within board index
                printInvalidInput(colour);

                continue;
            }

            boolean specialCaseFlag = false;

            if (p.getVerticalPosition() == 0 && newVertical == 0 ||
                    p.getVerticalPosition() == 7 && newVertical == 7) {
                // this condition is only for castling
                // check if the piece being moved either at bottom or top of the board
                if (p.chessPieceName.equals(Piece.KING__) && p.specialMove == 0) {
                    // make sure piece being moved is king and has not been moved
                    chessPiece piece = board[newVertical][7];

                    if (newHorizontal == 6 && piece != null &&
                            piece.chessPieceName.equals(Piece.ROOK__) && piece.specialMove == 0) {
                        // check if the king is castling right and that rook exists
                        // and has not moved before

                        for (int i = 0; i < 3; i++) {
                            // check that the king is not in check, that the two squares
                            // right of the king is null, and that the king is not going
                            // to cross a place that would put the king in check
                            chessPiece[][] tempBoard = copyBoard(board);

                            chessPiece king = new chessPiece(p.getVerticalPosition(),
                                    p.getHorizontalPosition() + i, colour,Piece.KING__);
                            // move the king right by 1

                            if (i != 0 && tempBoard[king.getVerticalPosition()][king.getHorizontalPosition()]
                            != null) {
                                // check that next square is null
                                break;
                            }

                            tempBoard[king.getVerticalPosition()][king.getHorizontalPosition()] = king;
                            tempBoard[p.getVerticalPosition()][p.horizontalPosition] = null;


                            if (isMyKingInCheck(king, tempBoard)) {
                                // if current square would put the king in check then break
                                break;
                            }

                            if (i == 2) {
                                // if it reaches the end then the player can castle
                                storePreviousMove(board);
                                specialCaseFlag = true;

                                piece.specialMove++;
                                p.specialMove++;
                                // mark that the pieces have moved

                                tempBoard[king.getVerticalPosition()]
                                        [king.getHorizontalPosition()-1] = piece;
                                tempBoard[newVertical][7] = null;

                                board = tempBoard;

                                System.out.println(colour + " Player Castled\n");
                                printBoard(board);
                            }

                        }


                    }

                    piece = board[newVertical][0];

                    if (!specialCaseFlag && newHorizontal == 2 && piece != null &&
                            piece.chessPieceName.equals(Piece.ROOK__) && piece.specialMove == 0) {
                        // this checks if a king is trying to castle to the left
                        for (int i = 0; i < 3; i++) {
                            // check that the king is not in check, that the two squares
                            // left of the king is null, and that the king is not going
                            // to cross a place that would put the king in check
                            chessPiece[][] tempBoard = copyBoard(board);

                            chessPiece king = new chessPiece(p.getVerticalPosition(),
                                    p.getHorizontalPosition() - i, colour,Piece.KING__);
                            // moved the king left by 1

                            if (i != 0 && tempBoard[king.getVerticalPosition()][king.getHorizontalPosition()]
                                    != null) {
                                // check that next square is null
                                break;
                            }

                            tempBoard[king.getVerticalPosition()][king.getHorizontalPosition()] = king;
                            tempBoard[p.getVerticalPosition()][p.horizontalPosition] = null;

                            if (isMyKingInCheck(king, tempBoard)) {
                                // if current square would put the king in check then break
                                break;
                            }
                            if (i == 2) {
                                // if it reaches the end then the player can castle
                                storePreviousMove(board);
                                specialCaseFlag = true;

                                piece.specialMove++;
                                p.specialMove++;
                                // mark that the pieces have moved

                                tempBoard[king.getVerticalPosition()]
                                        [king.getHorizontalPosition()+1] = piece;
                                tempBoard[newVertical][0] = null;

                                board = tempBoard;

                                System.out.println(colour + " Player Castled\n");
                                printBoard(board);
                            }

                        }
                    }
                }
            }

            if (p.chessPieceName.equals(Piece.PAWN__) && board[newVertical][newHorizontal] == null ) {
                // check current spot has a pawn and moving spot is null
                // this is for pawns special mvoe: en passant
                if (colour.equals(Color.WHITE) && y == 3 || colour.equals(Color.BLACK) && y == 4) {
                    // white pawn has to be at vertical position 3
                    // or black pawn has to be at vertical position 4
                    chessPiece[][] tempBoard = copyBoard(board);

                if (colour.equals(Color.WHITE)) {
                    // add a black piece to intended spot on the tempBoard
                    tempBoard[newVertical][newHorizontal] = new
                            chessPiece(newVertical, newHorizontal, Color.BLACK, Piece.PAWN__);
                } else {
                    // add a white piece to intended spot on the tempBoard
                    tempBoard[newVertical][newHorizontal] = new
                            chessPiece(newVertical, newHorizontal, Color.WHITE, Piece.PAWN__);
                }

                if (p.isMoveLegal(newVertical, newHorizontal, tempBoard) && tempBoard[y][newHorizontal] != null
                        && !tempBoard[y][newHorizontal].color.equals(colour) &&
                        tempBoard[y][newHorizontal].specialMove == 1) {
                    // check if the pawn trying to en passant can move with isMoveLegal
                    // check that there is an enemy piece (on the left or right of pawn)
                    // where en passant wil happen and that enemy piece has only move once
                    tempBoard = copyBoard(board);

                    tempBoard[newVertical][newHorizontal] = p;

                    p.verticalPosition = newVertical;
                    p.horizontalPosition = newHorizontal;

                    tempBoard[y][x] = null;
                    tempBoard[y][newHorizontal] = null;

                    chessPiece king;

                    if (colour.equals(Color.WHITE)) {
                        king = whiteKing;
                    } else {
                        king = blackKing;
                    }

                    if (!isMyKingInCheck(king, tempBoard)) {
                        // check that king won't be in check in tempBoard
                        // else replace board with tempBoard
                        storePreviousMove(board);
                        board = tempBoard;

                        specialCaseFlag = true;

                    } else {
                        // else don't do anything
                        p.verticalPosition = y;
                        p.horizontalPosition = x;
                    }
                }
                }
            }

            if (!specialCaseFlag && !movePiece(p, newVertical, newHorizontal)) {
                // check if p can be moved
                continue;
           }

           if (colour.equals(Color.WHITE)) {
               if (didIWin(blackKing)) {
                   // check if white player won
                   System.out.println("\nWhite won\n");
                   printBoard(board);

                   break;
               }
           } else {
               if (didIWin(whiteKing)) {
                   // check if Black player won
                   System.out.println("\nBlack won\n");
                   printBoard(board);

                   break;
                }
           }

           if (colour.equals(Color.WHITE)) {
               // add 1 to the count since a piece that has moved
               // only if they already moved once
               specialMoveCheck(Color.BLACK);
           } else {
               specialMoveCheck(Color.WHITE);
           }

           if (p.chessPieceName.equals(Piece.ROOK__) || p.chessPieceName.equals(Piece.KING__)
                   || p.chessPieceName.equals(Piece.PAWN__)) {
               // only rooks, pawns, and kings count since the last time they moved is
               // increased if they currently moved
               p.specialMove++;
           }

           pawnPromotion(p);
           // if a piece is at the end then they can promote

            colour = colour.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
            // changes player; if piece.color == white ? (true) color -> black : (false) color -> white
            printNewPlayer(colour);
        }
    }


    public void printInvalidInput(Color colour) {
        // print a message: invalid input message
        System.out.println("\nInvalid Input!\n");
        System.out.println(colour + "S TURN AGAIN\n");
        printBoard(board);
    }


    public void printNewPlayer(Color colour) {
        // print a message: change of player message
        System.out.println("\n" + colour + "S TURN\n");
        printBoard(board);
    }


    private void specialMoveCheck(Color colour) {
        // change all pieces that have moved once to 2
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessPiece p = board[i][j];

                if (p == null) continue;

                if (p.getColor().equals(colour) && p.specialMove == 1 ) {
                    p.specialMove++;
                }
            }
        }
    }


    public boolean movePiece(chessPiece piece, int newVertical, int newHorizontal) {
        /**
         This method sets-up to check if a chess piece can move. First it checks
         is a movement is legal. Then it will check if this move will put the
         current players king in check. If both of these aren't true then a piece
         cannot move.

         @param piece = piece that will be moved
         @param newVertical = intended change in vertical position
         @param newHorizontal = intended change in horizontal position

         @return true - if piece can move; false - if piece cannot move
         */

        int y = piece.getVerticalPosition();
        // current vertical position
        int x = piece.getHorizontalPosition();
        // current horizontal position

        if (piece.isMoveLegal(newVertical, newHorizontal, board)) {
            // check if the current chess piece can move

            chessPiece[][] tempBoard = copyBoard(board);
            storePreviousMove(tempBoard);
            // store current board

            board[newVertical][newHorizontal] = piece;
            board[y][x] = null;

            piece.verticalPosition = newVertical;
            piece.horizontalPosition = newHorizontal;

            chessPiece king;

            if (piece.color.equals(Color.WHITE)) {
                king = whiteKing;
            } else {
                king = blackKing;
            }

            if (isMyKingInCheck(king, board)) {
                // check if this move will put the current players king is in check
                // yes then don't move
                stack.removeLast();
                // remove current board

                System.out.println("\nCannot Move There " + piece.getColor() +
                        " Player, King Will be in check!\n");
                System.out.println(piece.getColor() + "S TURN AGAIN\n");

                board = tempBoard;
                printBoard(board);

                piece.verticalPosition = y;
                piece.horizontalPosition = x;

                return false;
            }

            return true;
            }

        System.out.println("\nCannot Move " + piece.chessPieceName +
                " Here " + piece.getColor() + " Player.\n\nMove Again.\n");
        System.out.println(piece.getColor() + "S TURN AGAIN");
        printBoard(board);

        return false;
    }


    private chessPiece[][] copyBoard(chessPiece[][] newBoard) {
        // copies the current board but the chess pieces refer to the same
        // piece as before
        chessPiece[][] tempBoard = new chessPiece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tempBoard[i][j] = newBoard[i][j];
            }
        }

        return tempBoard;
    }


    public boolean isMyKingInCheck(chessPiece king, chessPiece[][] newBoard) {
        /**
         This method checks if the current king is in check. It goes through the
         board and checks if any enemy piece can checkmate a king.

         @param newBoard = the board that will be used to check if a king is in check
         @param colour = color of king that will be checked

         @return true - king is in check; false - king is not in check
         */
        //chessPiece king;

       // if (colour.equals(Color.WHITE)) {
         //   king = whiteKing;
       // } else {
          //  king = blackKing;
        //}

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessPiece p = newBoard[i][j];

                if (p == null || p.chessPieceName.equals(Piece.KING__)) continue;

                if (!p.getColor().equals(king.color) &&
                        p.isMoveLegal(king.getVerticalPosition(),
                        king.getHorizontalPosition(), newBoard)) {
                    // check if an enemy piece can move to the kings current spot
                    return true;
                }

            }
        }

        return false;
    }


    public boolean didIWin(chessPiece king) {
        /**
         This method check if a player won. For a player to win the enemy king needs
         to be in check. This method checks if the king is currently in check and
         then checks if the king can possible be in check in all squares one square
         away from the king. If not then it checks if any piece can be moved to
         put a king not in check.

         @param king = king that will be checked

         @return true - if player who called won; false - if player who called did
         not win
         */

        int winCondition = 8;
        // number of places king can move too; will reduce if there is a chess piece
        // occupying one of those spots
        int num = 0;
        // number of squares king is in check

        int saveY = king.getVerticalPosition();
        int saveX = king.getHorizontalPosition();

        int[][] possibleMoves = { {0,-1},{1,-1},{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1}};
        // possible moves around king

        if (!isMyKingInCheck(king, board)) return false;
        // checks if the king is currently in check

        for (int i = 0; i < 8; i++) {

            chessPiece[][] tempBoard = copyBoard(board);
            // tempBoard is the copy of the current board (board)

            int newX = king.getHorizontalPosition() + possibleMoves[i][0];
            // kings new horizontal position
            int newY = king.getVerticalPosition() + possibleMoves[i][1];
            // kings new vertical position

            if (newX < 0 || newX > 7 || newY < 0 || newY > 7) {
                // check that it is within the boards boundary
                winCondition--;
                // not a possible spot for so win condition is reduced

                continue;
            }


            if (tempBoard[newY][newX] != null &&
                    tempBoard[newY][newX].color.equals(king.color)) {
                // check  if an ally piece is here
                winCondition--;
                // not a possible spot for so win condition is reduced

                continue;
            }

            king.horizontalPosition = newX;
            king.verticalPosition = newY;

            tempBoard[newY][newX] = king;
            tempBoard[saveY][saveX] = null;

            if (!isMyKingInCheck(king, tempBoard)) {
                // if king is not in check in one spot then false
                king.verticalPosition = saveY;
                king.horizontalPosition  = saveX;

                return false;
            }

            king.verticalPosition = saveY;
            // reset vertical position
            king.horizontalPosition  = saveX;
            // reset horizontal position

            num++;
        }

        if (findACheckCounter(board, king.getColor())) {
            // check if an ally piece can counter the enemies check
            // or capture one of the pieces checking the king
            return false;
        }

        return winCondition != 0 && winCondition == num;
    }


    public boolean findACheckCounter (chessPiece[][] newBoard, Color colour) {
        /**
         This method looks at every spot in the newBoard and sees if any piece with
         a certain colour can be moved to  a new spot to counter a checkmate. This is done
         through looking at every square and seeing if a chess piece can move there.
         If a piece can move their then it checks if this leads to the king not being
         in check.

         @param newBoard - board that will be checked to see if an ally chess piece
         (to the parameter colour) can be moved to counter a check.
         @param colour - color of the player that will be checked for

         @return true - if a check can be countered; false - if any check cannot be countered
         */

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // find if an ally piece can move to current square at ((x) j, (y) i)
                // to counter a king being in check
                chessPiece p = newBoard[i][j];

                if (p != null) {
                    if (p.color.equals(colour)) continue;
                    // check that p is an enemy chess piece or null
                }

                LinkedList<chessPiece> counters = possibleCheckCounters(newBoard, i, j, colour);
                // find all pieces that can move to this new spot (at p) to
                // counter an enemy check

                while (!counters.isEmpty()) {
                    // look at all chess pieces that can move to ((x) j, (y) i)
                    // inside the linked list and see if it can counter a check
                    chessPiece[][] tempBoard = copyBoard(newBoard);
                    // board that will be tested for king being in check

                    p = counters.remove();

                    int saveY = p.getVerticalPosition();
                    int saveX = p.getHorizontalPosition();

                    tempBoard[i][j] = tempBoard[saveY][saveX];
                    // piece from counter is moved to a new spot
                    tempBoard[saveY][saveX] = null;

                    p.verticalPosition = i;
                    // piece from counter vertical position is changed
                    p.horizontalPosition = j;
                    // piece from counter horizontal position is changed

                    chessPiece king;

                    if (colour.equals(Color.WHITE)) {
                        king = whiteKing;
                    } else {
                        king = blackKing;
                    }


                    if (!isMyKingInCheck(king, tempBoard))  {
                        // check if any piece leads to the king not being in check
                        p.verticalPosition = saveY;
                        p.horizontalPosition = saveX;
                        return true;
                    }

                    p.verticalPosition = saveY;
                    // reset vertical position
                    p.horizontalPosition = saveX;
                    // reset horizontal position
                }
            }
        }

        return false;
    }


    public LinkedList<chessPiece> possibleCheckCounters(chessPiece[][] newBoard, int newY,
                                            int newX, Color colour) {
        /**
         This method goes through the board and finds all ally chess piece (to
         the colour parameter) that can move to the location newY and newX.

         @param newBoard - board that will be used to find all ally chess pieces
         @param newY - new vertical location
         @param newX - new horizontal location
         @param colour - color of pieces that will be moved to newX and newY

         @return a linkedList of all pieces that can move to newX and newY
         */
        LinkedList<chessPiece> queues = new LinkedList<chessPiece>();
        // contains all ally pieces that can move to the location at newY and newX

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // find all pieces that can move here
                chessPiece p = newBoard[i][j];

                if (p == null) continue;

                if (p.color.equals(colour) &&
                          p.isMoveLegal(newY, newX, board)) {
                    // check that an ally piece can move here
                    queues.add(p);
                }
            }
        }

        return queues;
    }


    public void pawnPromotion(chessPiece piece) {
        /**
         This method promote any pawn if it reaches any end of the board.

         @param piece - chesspiece that will be promoted
         */
        if (piece != null && piece.chessPieceName.equals(Piece.PAWN__) ) {
            // only pawns can be promoted
            if (piece.getVerticalPosition() == 0 || piece.getVerticalPosition() == 7) {
                // only in vertical position 0 or 7 can be promoted
            Scanner myObj = new Scanner(System.in);

            System.out.println(piece.getColor() + " Player Change Pawn to:\n");
            String userInput = myObj.nextLine();

            switch (userInput) {
                // pawn can only be promoted to rook, knight, bishop, and queen
                case "rook" -> piece.chessPieceName = Piece.ROOK__;
                case "knight" -> piece.chessPieceName = Piece.KNIGHT;
                case "bishop" -> piece.chessPieceName = Piece.BISHOP;
                case "queen" -> piece.chessPieceName = Piece.QUEEN_;
                default -> pawnPromotion(piece);
            }
            }
        }
    }


    public void storePreviousMove (chessPiece[][] newBoard) {
        /**
         This method store the current board (newBoard) to a stack before it
         is modified in other methods.

         @param newBoard - board that will be saved
         */
        chessPiece[][] storeBoard = new chessPiece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessPiece p = newBoard[i][j];

                if (p == null) {
                    // stores null
                    storeBoard[i][j] = null;

                    continue;
                }

                storeBoard[i][j] = new chessPiece(p.getVerticalPosition(),
                        p.getHorizontalPosition(), p.color, p.getChessPieceName());
                // create a copy of the current piece

                storeBoard[i][j].specialMove = p.specialMove;
                // store current piece
            }
        }

        stack.add(storeBoard);
    }


    public void previousMove () {
        /**
         This method changes the current board, white king, and black king
         variables to their previous version (before the last move).
         */
        board = stack.removeLast();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessPiece p = board[i][j];

                if (p != null && p.chessPieceName.equals(Piece.KING__) &&
                        p.color.equals(Color.WHITE)) whiteKing = p;
                // change white king variable

                if ( p != null && p.chessPieceName.equals(Piece.KING__) &&
                        p.color.equals(Color.BLACK)) blackKing = p;
                // change black king variable
            }
        }
    }


    public static void main(String[] args) {
        chessBoard playGame = new chessBoard();
        playGame.gameBoard();
    }


}