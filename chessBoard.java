import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class chessBoard {
    private final int boardHorizontal = 8;
    private final int boardVertical = 8;
    private chessPiece[][] board;

    private enum Color {BLACK, WHITE};
    private enum Piece {ROOK__,KNIGHT, BISHOP, QUEEN_, KING__, PAWN__}

    private static class chessPiece implements Comparable<chessPiece> {
        private int horizontalPosition;
        private int verticalPosition;
        private Piece chessPieceName;
        private Color color;

        public chessPiece(int horizontal, int vertical, Color c, Piece name) {
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

        public boolean isMoveLegal(int vertical, int horizontal) {
            int legalVerticalPosition;
            int legalHorizontalPosition;
            if (chessPieceName.equals(Piece.ROOK__)) {
                return true;
            } else if (chessPieceName.equals(Piece.KNIGHT)) {
                return true;
            } else if (chessPieceName.equals(Piece.BISHOP)) {
                return true;
            } else if (chessPieceName.equals(Piece.QUEEN_)) {
                return true;
            } else if (chessPieceName.equals(Piece.KING__)) {
                return true;
            } else {
                return true;
            }
            //return false;
        }
    }

    public chessBoard (){
        board = new chessPiece[boardVertical][boardHorizontal];

        for (int i = 0; i < boardVertical; i++) {
            chessPiece p;
            if (i == 0  || i == 7) {
                Color t;

                if (i == 0) {
                    t = Color.BLACK;
                } else {
                    t = Color.WHITE;
                }
                board[i][0] = new chessPiece(boardHorizontal,
                        boardVertical, t, Piece.ROOK__);
                board[i][1] = new chessPiece(boardHorizontal,
                        boardVertical, t, Piece.KNIGHT);
                board[i][2] = new chessPiece(boardHorizontal,
                        boardVertical, t, Piece.BISHOP);
                board[i][3] = new chessPiece(boardHorizontal,
                        boardVertical, t, Piece.QUEEN_);
                board[i][4] = new chessPiece(boardHorizontal,
                        boardVertical, t, Piece.KING__);
                board[i][5] = new chessPiece(boardHorizontal,
                        boardVertical, t, Piece.BISHOP);
                board[i][6] = new chessPiece(boardHorizontal,
                        boardVertical, t, Piece.KNIGHT);
                board[i][7] = new chessPiece(boardHorizontal,
                        boardVertical, t, Piece.ROOK__);

            }

            if (i == 1 || i == 6) {
                if (i == 1) {
                    p = new chessPiece(boardHorizontal,
                            boardVertical, Color.BLACK, Piece.PAWN__);
                } else {
                    p = new chessPiece(boardHorizontal,
                            boardVertical, Color.WHITE, Piece.PAWN__);
                }
                for (int j = 0; j < boardVertical; j++) {
                    board[i][j] = p;
                }
            }
        }

        printBoard(board);


    }
    public void printBoard(chessPiece[][] currentBoard){
        StringBuilder boardToString = new StringBuilder();

        for (int i = 0; i < boardVertical; i++) {
            boardToString.append("  ____________________________________" +
                    "_____________________\n");
            boardToString.append(boardVertical - i - 1);
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
        int y = 2;
        String x = "a";

        int p = x.charAt(0) - 97;
        chessPiece[][] newBoard = movePiece(board[1][0], y, p);
    }
    public chessPiece[][] movePiece(chessPiece piece, int vertical, int horizontal) {
        int y = piece.getVerticalPosition();
        int x =  piece.getHorizontalPosition();

        if (board[y][x] != null) return null;

        boolean moveLegal = piece.isMoveLegal(y, x);
        return null;
    }

    public boolean isKingInCheck(chessPiece[][] board) {
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


        }
    }