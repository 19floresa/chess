import java.util.Scanner;

public class chessBoard {
    private final int boardHorizontal = 8;
    private final int boardVertical = 8;
    private chessPiece blackKing;
    private chessPiece whiteKing;
    private chessPiece[][] board;

    private enum Color {BLACK, WHITE};
    private enum Piece {ROOK__,KNIGHT, BISHOP, QUEEN_, KING__, PAWN__}

    private static class chessPiece implements Comparable<chessPiece> {
        private int horizontalPosition;
        private int verticalPosition;
        private Piece chessPieceName;
        private final Color color;

        public chessPiece( int vertical, int horizontal, Color c, Piece name) {
            horizontalPosition = horizontal;
            verticalPosition = vertical;
            color = c;
            chessPieceName = name;
        }

        public int getHorizontalPosition() {
            return horizontalPosition;
        }

        public int getVerticalPosition(){
            return verticalPosition;
        }

        public Color getColor() {
            return color;
        }

        public Piece getChessPieceName() {
            return chessPieceName;
        }

        @Override
        public int compareTo(chessPiece o) {
            if (!chessPieceName.equals(o.getChessPieceName())) {
                return 0;
            }
            return 1;
        }

        public boolean isMoveLegal(int newVertical, int newHorizontal, chessPiece[][] currentBoard) {

            int yMinuend;
            int ySubtrahend;

            int xMinuend;
            int xSubtrahend;

            int verticalDifference;
            int horizontalDifference;

            // figure out how many squares it will go forward
            yMinuend =  newVertical;
            ySubtrahend = verticalPosition;

            xMinuend = newHorizontal;
            xSubtrahend = horizontalPosition;

            if (color.equals(Color.WHITE) &&
            !chessPieceName.equals(Piece.KNIGHT) &&
                    !chessPieceName.equals(Piece.BISHOP)) {
                // going 'forward' is opposite for black pawns
                yMinuend = verticalPosition;
                ySubtrahend = newVertical;

                xMinuend = horizontalPosition;
                xSubtrahend = newHorizontal;
            }

            verticalDifference = yMinuend - ySubtrahend;
            horizontalDifference = xMinuend - xSubtrahend;


            if (chessPieceName.equals(Piece.PAWN__)) {
                return pawnMove(newVertical, newHorizontal, currentBoard,
                        verticalDifference, horizontalDifference);

            } else if (chessPieceName.equals(Piece.ROOK__)) {
             return rookMove(newVertical, newHorizontal, currentBoard,
                     verticalDifference, horizontalDifference, yMinuend,
                     ySubtrahend, xMinuend, xSubtrahend);

            } else if (chessPieceName.equals(Piece.KNIGHT)) {
                return knightMove(newVertical, newHorizontal,
                        currentBoard,verticalDifference, horizontalDifference);

            } else if (chessPieceName.equals(Piece.BISHOP)) {
                return bishopMove(newVertical,newHorizontal, currentBoard,
                        verticalDifference, horizontalDifference);

            } else if (chessPieceName.equals(Piece.QUEEN_)) {
                if (verticalDifference != 0 && horizontalDifference == 0) {
                    return rookMove(newVertical, newHorizontal, currentBoard,
                            verticalDifference, horizontalDifference, yMinuend,
                            ySubtrahend, xMinuend, xSubtrahend);

                } else if (verticalDifference == 0 && horizontalDifference != 0) {
                    return rookMove(newVertical, newHorizontal, currentBoard,
                            verticalDifference, horizontalDifference, yMinuend,
                            ySubtrahend, xMinuend, xSubtrahend);

                } else {
                    return bishopMove(newVertical,newHorizontal, currentBoard,
                            verticalDifference, horizontalDifference);
                }

            } else if (chessPieceName.equals(Piece.KING__)) {
                return kingMove(newVertical, newHorizontal, currentBoard,
                        verticalDifference, horizontalDifference);
            }

            return false;
        }


        public boolean pawnMove (int newVertical, int newHorizontal, chessPiece[][] currentBoard,
                                 int verticalDifference, int horizontalDifference) {
            if (verticalPosition == 1 || verticalPosition == 6) {
                // figure out if pawn can move forward if pawn is at start
                if (horizontalDifference == 0) {

                    //************************************check null before entering legal function***************************************************
                    chessPiece pieceOne = null;
                    chessPiece pieceTwo = null;

                    if (color.equals(Color.BLACK)) {
                        pieceOne = currentBoard[verticalPosition + 1][newHorizontal];
                        pieceTwo = currentBoard[verticalPosition + 2][newHorizontal];
                    }
                    if (color.equals(Color.WHITE)) {
                        pieceOne = currentBoard[verticalPosition-1][newHorizontal];
                        pieceTwo = currentBoard[verticalPosition-2][newHorizontal];
                    }

                    if (pieceOne == null && verticalDifference == 1) {
                        return true;
                    }

                    if (verticalDifference == 2 && pieceOne == null && pieceTwo == null) {
                        return true;
                    }

                } else {
                    chessPiece newSpot = currentBoard[newVertical][newHorizontal];
                    chessPiece oldSpot = currentBoard[verticalPosition][horizontalPosition];

                    if (newSpot != null &&
                            horizontalDifference == 1 &&
                            !newSpot.color.equals(oldSpot.color)) {
                        return true;
                    }
                }

            } else {

                chessPiece newSpot = currentBoard[newVertical][newHorizontal];
                chessPiece oldSpot = currentBoard[verticalPosition][horizontalPosition];

                if (verticalDifference == 1 && horizontalDifference == 0 &&
                        newSpot == null) {
                    return true;
                } else if ( verticalDifference == 1  && newSpot != null &&
                        !newSpot.color.equals(oldSpot.color)) {
                    if (horizontalDifference == 1 || horizontalDifference == -1) {
                        return true;
                    }
                }
            }

            return false;
        }


        public boolean rookMove (int newVertical, int newHorizontal, chessPiece[][] currentBoard,
                                 int verticalDifference, int horizontalDifference, int yMinuend,
                                 int ySubtrahend, int xMinuend, int xSubtrahend) {

            if (verticalDifference != 0 && horizontalDifference == 0) {
                if (verticalDifference < 0) {
                    int temp = yMinuend;
                    yMinuend = ySubtrahend;
                    ySubtrahend = temp;
                }

                if (currentBoard[newVertical][newHorizontal] != null &&
                        color.equals(currentBoard[newVertical][newHorizontal].color)) {
                    return false;
                }

                yMinuend -= 1;

                for (int i = yMinuend;  i != ySubtrahend; i--) {
                    if (currentBoard[i][newHorizontal] != null) {
                        return false;
                    }
                }
                return true;

            } else if (verticalDifference == 0 && horizontalDifference != 0) {
                if (horizontalDifference < 0) {
                    int temp = xMinuend;
                    xMinuend = xSubtrahend;
                    xSubtrahend = temp;
                }

                if (currentBoard[newVertical][newHorizontal] != null &&
                        color.equals(currentBoard[newVertical][newHorizontal].color)) {
                    return false;
                }

                xMinuend -= 1;

                for (int i = xMinuend; i != xSubtrahend; i--) {
                    if (currentBoard[newVertical][i] != null) {
                        return false;
                    }
                }

                return true;
            }

            return false;
        }


        public boolean knightMove(int newVertical, int newHorizontal,
                                  chessPiece[][] currentBoard, int verticalDifference,
                                  int horizontalDifference) {

            chessPiece oldPiece = currentBoard[newVertical][newHorizontal];

            if (oldPiece == null || !color.equals(oldPiece.color)) {
                if (verticalDifference == 2 ||
                        verticalDifference == -2) {
                    if (horizontalDifference == 1
                            || horizontalDifference == -1) {
                        return true;
                    }
                }

                if (verticalDifference == 1 ||
                        verticalDifference == -1) {
                    if (horizontalDifference == 2 ||
                            horizontalDifference == -2) {
                        return true;
                    }
                }

            }
            return false;
        }


        public boolean bishopMove(int newVertical, int newHorizontal, chessPiece[][] currentBoard,
                                  int verticalDifference, int horizontalDifference) {
            int yStart = verticalPosition;
            int xStart = horizontalPosition;

            if (verticalDifference > 0 && horizontalDifference > 0) {
                while (true) {
                    yStart++;
                    xStart++;

                    if (yStart > 8 || xStart > 8) break;

                    chessPiece currentP = currentBoard[yStart][xStart];


                    if (yStart == newVertical && xStart == newHorizontal) {
                        if (currentP == null ||
                                !color.equals(currentP.color)) {
                            return true;
                        }
                    } else if (currentP != null) break;

                }

            } else if (verticalDifference > 0 && horizontalDifference < 0) {
                while (true) {
                    yStart++;
                    xStart--;

                    if (yStart > 8 || xStart < 0) break;

                    chessPiece currentP = currentBoard[yStart][xStart];

                    if (yStart == newVertical && xStart == newHorizontal) {
                        if (currentP == null ||
                                !color.equals(currentP.color)) {
                            return true;
                        }
                    } else if (currentP != null) break;

                }
            }

            if (verticalDifference < 0 && horizontalDifference > 0) {
                while (true) {
                    yStart--;
                    xStart++;

                    if (yStart < 0 || xStart > 8) break;

                    chessPiece currentP = currentBoard[yStart][xStart];

                    if (yStart == newVertical && xStart == newHorizontal) {
                        if (currentP == null ||
                                !color.equals(currentP.color)) {
                            return true;
                        }
                    }  else if (currentP != null) break;

                }

            } else if (verticalDifference < 0 && horizontalDifference < 0) {
                while (true) {
                    yStart--;
                    xStart--;

                    if (yStart < 0 || xStart < 0) break;

                    chessPiece currentP = currentBoard[yStart][xStart];

                    if (yStart == newVertical && xStart == newHorizontal) {
                        if (currentP == null ||
                                !color.equals(currentP.color)) {
                            return true;
                        }
                    } else if (currentP != null) break;

                }
            }
            return false;
        }


        public boolean kingMove(int newVertical, int newHorizontal, chessPiece[][] currentBoard,
                                int verticalDifference, int horizontalDifference) {

            if (verticalDifference <= 1 && verticalDifference >= -1 &&
                    horizontalDifference <= 1 && horizontalDifference >= -1) {
                chessPiece p = currentBoard[newVertical][newHorizontal];

                if (p == null|| !color.equals(p.color)) {
                    return true;
                }
            }

            return false;
        }

    }



    public chessBoard (){
        board = new chessPiece[boardVertical][boardHorizontal];


        for (int i = 0; i < boardVertical; i++) {
            chessPiece p;
            if (i == 0  || i == 7) {
                Color t;
                //int vertical;
                if (i == 0) {
                    t = Color.BLACK;
                } else {
                    t = Color.WHITE;
                }
                board[i][0] = new chessPiece(i,
                        0, t, Piece.ROOK__);
                board[i][1] = new chessPiece(i,
                        1, t, Piece.KNIGHT);
                board[i][2] = new chessPiece(i,
                        2, t, Piece.BISHOP);
                board[i][3] = new chessPiece(i,
                        3, t, Piece.QUEEN_);
                board[i][4] = new chessPiece(i,
                        4, t, Piece.KING__);
                board[i][5] = new chessPiece(i,
                        5, t, Piece.BISHOP);
                board[i][6] = new chessPiece(i,
                        6, t, Piece.KNIGHT);
                board[i][7] = new chessPiece(i,
                        7, t, Piece.ROOK__);

                if (i == 0) {
                    blackKing = board[i][4];
                } else {
                    whiteKing = board[i][4];
                }


            }

            if (i == 1 || i == 6) {

                for (int j = 0; j < boardVertical; j++) {
                    if (i == 1) {
                        p = new chessPiece(1,
                                j, Color.BLACK, Piece.PAWN__);
                    } else {
                        p = new chessPiece(6,
                                j, Color.WHITE, Piece.PAWN__);
                    }

                    board[i][j] = p;
                }
            }
        }

        System.out.println("\nWHITE STARTS\n");
        printBoard(board);


    }
    public void printBoard(chessPiece[][] currentBoard){
        StringBuilder boardToString = new StringBuilder();

        for (int i = 0; i < boardVertical; i++) {
            boardToString.append("  ____________________________________" +
                    "_____________________\n");
            boardToString.append(8 - i);
            boardToString.append(" ");
            boardToString.append("|");

            for (int j = 0; j < boardHorizontal; j++) {
                if (currentBoard[i][j] == null) {
                    boardToString.append("      ");
                } else {
                    boardToString.append(currentBoard[i][j].chessPieceName);
                }

                boardToString.append("|");
            }

            boardToString.append("\n");
        }

        boardToString.append("  ___________________________________" +
                "______________________\n");
        boardToString.append("  |");

        for (int i = 0; i < boardHorizontal; i++ ) {
            boardToString.append("  ");
            boardToString.append((char) (97 + i) );
            boardToString.append("   ");
            boardToString.append("|");
        }

        System.out.println(boardToString.toString());
    }

    public void gameBoard() {
        Scanner myObj = new Scanner(System.in);

        Color colour = Color.WHITE;

        while (true) {
            System.out.println("\nPick a Piece: ");

            String userInput = myObj.nextLine();

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

            y = 8 - y; // top row is index 0; bottom row is index 7

            if (x > 7 || x < 0 || y > 7 || y < 0) {
                // check if y and x are within board index
                printInvalidInput(colour);
                continue;
            }

            chessPiece p = board[y][x];

            if (!p.color.equals(colour)) {
                // check if piece being moved is the same color as the player
                System.out.println("Cannot Move " + p.color + " Piece, " +
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

            movePiece(p, newVertical, newHorizontal);

            colour = colour.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
            // change player
            printNewPlayer(colour);
        }
/*
        int yInteger = 3;
        String x = "e";

        int xInteger = x.charAt(0) - 97;

        if (yInteger < 0 || yInteger > 7 || xInteger < 0 || xInteger > 7) {
            System.out.println("invalid move!");
            return;
        }
        if (board[1][4] == null) {
            System.out.println("Null Piece!");
            return;
        }

        boolean newBoard = movePiece(board[1][4], yInteger, xInteger);

        System.out.println(newBoard);
        printBoard(board);

    */
    }

    public void printInvalidInput(Color colour) {
        System.out.println("\nInvalid Input!\n");
        System.out.println(colour + "S TURN AGAIN\n");
        printBoard(board);
    }
    public void printNewPlayer(Color colour) {
        System.out.println("\n" + colour + "S TURN\n");
        printBoard(board);

    }

    public boolean movePiece(chessPiece piece, int newVertical, int newHorizontal) {

        int y = piece.getVerticalPosition();
        int x = piece.getHorizontalPosition();

        if (piece.isMoveLegal(newVertical, newHorizontal, board)) {

             chessPiece[][] tempBoard = copyBoard();

            board[newVertical][newHorizontal] = piece;
            board[y][x] = null;

            piece.verticalPosition = newVertical;
            piece.horizontalPosition = newHorizontal;

            if (isKingInCheck(board)) {
                System.out.println("\nCannot move there, king will be in check!\n");
                board = tempBoard;

                piece.verticalPosition = y;
                piece.horizontalPosition = x;

                return false;
            }

            return true;
            }

        return false;
    }

    private chessPiece[][] copyBoard() {
        chessPiece[][] tempBoard = new chessPiece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tempBoard[i][j] = board[i][j];
            }
        }

        return tempBoard;

    }

    public boolean isKingInCheck(chessPiece[][] board) {
//
        // loop: find king (w/ right color), if no king then return true
        // search for knights, closest Piece above/below/left/right/diagonal him
        // put in stack/queue
        // pop them 1 by 1
        // can use isMoveLegal????
        // if all false then return false
        return false;
    }

    public boolean didIWin(chessPiece[][] board) {
        return false;
    }

    public boolean draw(chessPiece[][] board) {
        return false;
    }


    public static void main(String[] args) {
        chessBoard playGame = new chessBoard();
        //chessPiece[][] p = playGame.getBoard();
        playGame.gameBoard();

        //Scanner myObj = new Scanner(System.in);
        //System.out.println("Who has ligma?:\n");
        //String userInput = myObj.nextLine();
        //System.out.println(userInput.length());


        }
    }