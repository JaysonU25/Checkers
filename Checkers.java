package Checkers;

import java.util.Scanner;

public class Checkers {

    public static void main(String[] args){
        Scanner userInput = new Scanner(System.in);
        String[][] board = {
                {"#", "A", "B", "C", "D", "E", "F", "G", "H", "#"},
                {"1", "2", "-", "2", "-", "2", "-", "2", "-", "C"},
                {"2", "-", "2", "-", "2", "-", "2", "-", "2", "H"},
                {"3", "2", "-", "2", "-", "2", "-", "2", "-", "E"},
                {"4", "-", "0", "-", "0", "-", "0", "-", "0", "C"},
                {"5", "0", "-", "0", "-", "0", "-", "0", "-", "K"},
                {"6", "-", "1", "-", "1", "-", "1", "-", "1", "E"},
                {"7", "1", "-", "1", "-", "1", "-", "1", "-", "R"},
                {"8", "-", "1", "-", "1", "-", "1", "-", "1", "S"},
                {"#", "C", "H", "E", "C", "K", "E", "R", "S", "#"}
        };
        String player = "1";
        String piece = "";
        String move = "";
        boolean newTurn = false;
        boolean inProgress = true;

        while(inProgress) {
            if(newTurn){
                if(player.equals("1")){
                    player = "2";
                } else {
                    player = "1";
                }
            }
            newTurn = false;
            System.out.printf("Player %s's turn!\n", player);
            while(!newTurn) {
                inProgress = printBoard(board);
                System.out.printf("Player %s: Which piece would you like to move?(ex. A1)\n", player);
                piece = userInput.nextLine();
                System.out.printf("Player %s: Where would you like the piece on %s to move to?(ex. B2)\n", player, piece);
                move = userInput.nextLine();
                System.out.printf("Player %s: Piece %s to %s\n\n", player, piece, move);
                newTurn = movePiece(board, player, piece, move);
            }
        }
        System.out.println("GAME OVER");

    }

    private static boolean movePiece(String[][] board, String player, String piece, String move){
        move = convertCoord(move);
        piece = convertCoord(piece);

        if(viableMove(board, player, piece, move)){
            /** NOT KING  **/
            if((player.equals("1") && board[parseI(move.charAt(0))][parseI(move.charAt(1))].charAt(0) == '2') || (player.equals("2") && board[parseI(move.charAt(0))][parseI(move.charAt(1))].charAt(0) == '1')){ //Jump
                killPiece(board, move);
                String newPiece = jump(board, player, piece, move);
                if(jumpAvail(board, player, newPiece)) {
                    return false;
                } else {
                    return true;
                }
            } else { //Normal move
                board[parseI(move.charAt(0))][parseI(move.charAt(1))] = board[parseI(piece.charAt(0))][parseI(piece.charAt(1))];
                killPiece(board, piece);
                return true;
            }
        } else {
            System.out.println("Invalid move!!");
            return false;
        }
    }

    // Checks to see if the move is valid
    private static boolean viableMove(String[][] board, String player, String piece, String move){
            if (move.length() != 2 || piece.length() != 2) { //Did the player insert the correct coordinates
                return false;
            } else if (!(Character.isDigit(move.charAt(0))) || !(Character.isDigit(move.charAt(1))) || !(Character.isDigit(piece.charAt(0))) || !(Character.isDigit(piece.charAt(0)))) { //Are the coords only digits
                return false;
            } else if (move.charAt(0) > '8' || move.charAt(1) > '8' || piece.charAt(0) > '8' || piece.charAt(1) > '8') { // is the move inside the board
                return false;
            } else if (move.charAt(0) < '1' || move.charAt(1) < '1' || piece.charAt(0) < '1' || piece.charAt(1) < '1') { // is the move inside the board
                return false;
            } else if(Math.abs((parseI(move.charAt(0)) - parseI(piece.charAt(0)))) != 1 || Math.abs((parseI(move.charAt(1)) - parseI(piece.charAt(1)))) != 1 ){
                return false;
            } else if (!(player.equals(board[parseI(piece.charAt(0))][parseI(piece.charAt(1))].substring(0,1)))) { //Checks to see if piece belongs to player
                return false;
            } else if (board[parseI(move.charAt(0))][parseI(move.charAt(1))].equals(player)) { // Checks to see if the space is occupied by the current player's piece
                return false;
            } else if (jumpAvail(board, player)){
                if(player.equals("1") && board[parseI(move.charAt(0))][parseI(move.charAt(1))].charAt(0) != '2'){
                    return false;
                } else if(player.equals("2") && board[parseI(move.charAt(0))][parseI(move.charAt(1))].charAt(0) != '1'){
                    return false;
                }
            } else if(board[parseI(move.charAt(0))][parseI(move.charAt(1))].equals("-")) { // Is the space the right color/symbol
                return false;
            }
        return true; // everything looked good if it made it here
    }

    // Checks to see if the piece is a king
    private static boolean isKing(String[][] board, String piece){
        if(board[parseI(piece.charAt(0))][parseI(piece.charAt(1))].length() > 1) {
            return true;
        }
        return false;
    }

    // Converts user input to array coordinates
    private static String convertCoord(String move){
        if(move.length() >= 2) {
            String coordinates = "";
            move = move.toUpperCase();
            coordinates += move.charAt(1);
            if (move.charAt(0) == 'A') {
                coordinates += 1;
            } else if (move.charAt(0) == 'B') {
                coordinates += 2;
            } else if (move.charAt(0) == 'C') {
                coordinates += 3;
            } else if (move.charAt(0) == 'D') {
                coordinates += 4;
            } else if (move.charAt(0) == 'E') {
                coordinates += 5;
            } else if (move.charAt(0) == 'F') {
                coordinates += 6;
            } else if (move.charAt(0) == 'G') {
                coordinates += 7;
            } else if (move.charAt(0) == 'H') {
                coordinates += 8;
            } else {
                coordinates += "INVALID";
            }
            return coordinates;
        } else {
            return "INVALID";
        }
    }

    // Parses characters to integers
    private static int parseI(char c){
        return Integer.parseInt(String.valueOf(c));
    }

    // Removes a piece from the board
    private static void killPiece(String[][] board, String piece){
        board[parseI(piece.charAt(0))][parseI(piece.charAt(1))] = "0";
    }

    // Prints the board to the string
    private static boolean printBoard(String[][]board){
        int onePieces = 0;
        int twoPieces = 0;
        System.out.println("Pieces can only land on a \"0\" spot");
        System.out.println("---------------------------------------------------------------------------------");
        for(int rows = 0; rows < board[0].length ; rows++){
            System.out.print("|\t");
            for(int columns = 0; columns < board.length; columns++){
                System.out.print(board[rows][columns] + "\t|\t");
                if(board[rows][columns].charAt(0) == '1'){
                    onePieces++;
                    if((rows == 1 && columns >= 1 && columns <= 8) && board[rows][columns].length() < 2){
                        board[rows][columns] += "K";
                    }
                } else if (board[rows][columns].charAt(0) == '2'){
                    twoPieces++;
                    if((rows == 9 && columns >= 1 && columns <= 8) && board[rows][columns].length() < 2){
                        board[rows][columns] += "K";
                    }
                }
            }
            System.out.println();
            System.out.println("---------------------------------------------------------------------------------");
        }
        System.out.println();
        return onePieces != 0 && twoPieces != 0;
    }

    // Checks to see if the player has a jump available
    private static boolean jumpAvail(String[][] board, String player){
        String piece = "";
        for(int rows = 1; rows < board[0].length - 1 ; rows++) {
            for (int columns = 1; columns < board.length - 1; columns++) {
                if (board[rows][columns].equals(player)) {
                    piece = String.valueOf(rows);
                    piece += String.valueOf(columns);

                    if ((player.equals("1") && board[parseI(piece.charAt(0)) - 1][parseI(piece.charAt(1)) + 1].charAt(0) == '2') && (board[parseI(piece.charAt(0)) - 2][parseI(piece.charAt(1)) + 2].equals("0"))) { // is the jump to the right in the board
                        System.out.println("You have to jump!");
                        return true;
                    } else if ((player.equals("1") && board[parseI(piece.charAt(0)) - 1][parseI(piece.charAt(1)) - 1].charAt(0) == '2') && (board[parseI(piece.charAt(0)) - 2][parseI(piece.charAt(1)) - 2].equals("0"))) { // is the jump to the left in the board
                        System.out.println("You have to jump!");
                        return true;
                    } else if ((player.equals("2") && board[parseI(piece.charAt(0)) + 1][parseI(piece.charAt(1)) + 1].charAt(0) == '1') && (board[parseI(piece.charAt(0)) + 2][parseI(piece.charAt(1)) + 2].equals("0"))) { // is the jump to the right in the board
                        System.out.println("You have to jump!");
                        return true;
                    } else if ((player.equals("2") && board[parseI(piece.charAt(0)) + 1][parseI(piece.charAt(1)) - 1].charAt(0) == '1') && (board[parseI(piece.charAt(0)) + 2][parseI(piece.charAt(1)) - 2].equals("0"))) { // is the jump to the left in the board
                        System.out.println("You have to jump!");
                        return true;

                    } else if (isKing(board, piece) && (player.equals("1") && board[parseI(piece.charAt(0)) + 1][parseI(piece.charAt(1)) - 1].charAt(0) == '2') && (board[parseI(piece.charAt(0)) + 2][parseI(piece.charAt(1)) - 2].equals("0"))) { // is the jump to the left in the board
                        System.out.println("You have to jump!");
                        return true;
                    } else if (isKing(board, piece) && (player.equals("1") && board[parseI(piece.charAt(0)) + 1][parseI(piece.charAt(1)) + 1].charAt(0) == '1') && (board[parseI(piece.charAt(0)) + 2][parseI(piece.charAt(1)) + 2].equals("0"))) { // is the jump to the right in the board
                        System.out.println("You have to jump!");
                        return true;
                    } else if (isKing(board, piece) && (player.equals("2") && board[parseI(piece.charAt(0)) - 1][parseI(piece.charAt(1)) - 1].charAt(0) == '1') && (board[parseI(piece.charAt(0)) - 2][parseI(piece.charAt(1)) - 2].equals("0"))) { // is the jump to the left in the board
                        System.out.println("You have to jump!");
                        return true;
                    } else if (isKing(board, piece) && (player.equals("2") && board[parseI(piece.charAt(0)) - 1][parseI(piece.charAt(1)) - 1].charAt(0) == '1') && (board[parseI(piece.charAt(0)) - 2][parseI(piece.charAt(1)) - 2].equals("0"))) { // is the jump to the left in the board
                        System.out.println("You have to jump!");
                        return true;
                    }
                }
            }
        }
            return false;
    }

    // Checks to see if the player has another jump available
    private static boolean jumpAvail(String[][] board, String player, String piece){
        if ((player.equals("1") && board[parseI(piece.charAt(0)) - 1][parseI(piece.charAt(1)) + 1].charAt(0) == '2') && (board[parseI(piece.charAt(0)) - 2][parseI(piece.charAt(1)) + 2].equals("0"))) { // if the jump is to the right in the board
            System.out.println("You have to jump again!");
            return true;
        } else if ((player.equals("1") && board[parseI(piece.charAt(0)) - 1][parseI(piece.charAt(1)) - 1].charAt(0) == '2') && (board[parseI(piece.charAt(0)) - 2][parseI(piece.charAt(1)) - 2].equals("0"))) { // if the jump is to the left in the board
            System.out.println("You have to jump again!");
            return true;
        } else if ((player.equals("2") && board[parseI(piece.charAt(0)) + 1][parseI(piece.charAt(1)) + 1].charAt(0) == '1') && (board[parseI(piece.charAt(0)) + 2][parseI(piece.charAt(1)) + 2].equals("0"))) { // if the jump is to the right in the board
            System.out.println("You have to jump again!");
            return true;
        } else if ((player.equals("2") && board[parseI(piece.charAt(0)) + 1][parseI(piece.charAt(1)) - 1].charAt(0) == '1') && (board[parseI(piece.charAt(0)) + 2][parseI(piece.charAt(1)) - 2].equals("0"))) { // if the jump is to the left in the board
            System.out.println("You have to jump again!");
            return true;

        } else if (isKing(board, piece) && (player.equals("1") && board[parseI(piece.charAt(0)) + 1][parseI(piece.charAt(1)) - 1].charAt(0) == '2') && (board[parseI(piece.charAt(0)) + 2][parseI(piece.charAt(1)) - 2].equals("0"))) { // if the jump is to the left in the board
            System.out.println("You have to jump again!");
            return true;
        } else if (isKing(board, piece) && (player.equals("1") && board[parseI(piece.charAt(0)) + 1][parseI(piece.charAt(1)) + 1].charAt(0) == '1') && (board[parseI(piece.charAt(0)) + 2][parseI(piece.charAt(1)) + 2].equals("0"))) { // if the jump is to the right in the board
            System.out.println("You have to jump again!");
            return true;
        } else if (isKing(board, piece) && (player.equals("2") && board[parseI(piece.charAt(0)) - 1][parseI(piece.charAt(1)) - 1].charAt(0) == '1') && (board[parseI(piece.charAt(0)) - 2][parseI(piece.charAt(1)) - 2].equals("0"))) { // if the jump is to the left in the board
            System.out.println("You have to jump again!");
            return true;
        } else if (isKing(board, piece) && (player.equals("2") && board[parseI(piece.charAt(0)) - 1][parseI(piece.charAt(1)) + 1].charAt(0) == '1') && (board[parseI(piece.charAt(0)) - 2][parseI(piece.charAt(1)) + 2].equals("0"))) { // if the jump is to the right in the board
            System.out.println("You have to jump again!");
            return true;
        }
        return false;
    }

    // Allows a piece to jump over another piece
    private static String jump(String[][] board, String player, String piece, String move){
            killPiece(board, move);
            String newPiece =  "";
            if (player.equals("1") && parseI(move.charAt(1)) < parseI(piece.charAt(1)) && parseI(move.charAt(0)) < parseI(piece.charAt(0))) { // if the jump is to the left in the board
                board[parseI(move.charAt(0)) - 1][parseI(move.charAt(1)) - 1] = board[parseI(piece.charAt(0))][parseI(piece.charAt(1))];
                killPiece(board, piece);
                newPiece = String.format("%s%s", String.valueOf(parseI(move.charAt(0)) - 1), String.valueOf(parseI(move.charAt(1)) - 1));
            } else if (player.equals("1") && parseI(move.charAt(1)) > parseI(piece.charAt(1)) && parseI(move.charAt(0)) < parseI(piece.charAt(0))) { // if the jump is to the right in the board
                board[parseI(move.charAt(0)) - 1][parseI(move.charAt(1)) + 1] = board[parseI(piece.charAt(0))][parseI(piece.charAt(1))];
                killPiece(board, piece);
                newPiece = String.format("%s%s", String.valueOf(parseI(move.charAt(0)) - 1), String.valueOf(parseI(move.charAt(1)) + 1));
            } else if (player.equals("2") && parseI(move.charAt(1)) < parseI(piece.charAt(1)) && parseI(move.charAt(0)) > parseI(piece.charAt(0))) { // if the jump is to the left in the board
                board[parseI(move.charAt(0)) + 1][parseI(move.charAt(1)) - 1] = board[parseI(piece.charAt(0))][parseI(piece.charAt(1))];
                killPiece(board, piece);
                newPiece = String.format("%s%s", String.valueOf(parseI(move.charAt(0)) + 1), String.valueOf(parseI(move.charAt(1)) - 1));
            } else if (player.equals("2") && parseI(move.charAt(1)) > parseI(piece.charAt(1)) && parseI(move.charAt(0)) > parseI(piece.charAt(0))) { // if the jump is to the right in the board
                board[parseI(move.charAt(0)) + 1][parseI(move.charAt(1)) + 1] = board[parseI(piece.charAt(0))][parseI(piece.charAt(1))];
                killPiece(board, piece);
                newPiece = String.format("%s%s", String.valueOf(parseI(move.charAt(0)) + 1), String.valueOf(parseI(move.charAt(1)) + 1));

                /** IS A KING **/

            } else if ((player.equals("1") && parseI(move.charAt(1)) < parseI(piece.charAt(1)) && parseI(move.charAt(0)) > parseI(piece.charAt(0))) && isKing(board, piece)) { // if the jump is to the left in the board
                board[parseI(move.charAt(0)) + 1][parseI(move.charAt(1)) - 1] = board[parseI(piece.charAt(0))][parseI(piece.charAt(1))];
                killPiece(board, piece);
                newPiece = String.format("%s%s", String.valueOf(parseI(move.charAt(0)) + 1), String.valueOf(parseI(move.charAt(1)) - 1));
            } else if ((player.equals("1") && parseI(move.charAt(1)) > parseI(piece.charAt(1)) && parseI(move.charAt(0)) > parseI(piece.charAt(0))) && isKing(board, piece)) { // if the jump is to the right in the board
                board[parseI(move.charAt(0)) + 1][parseI(move.charAt(1)) + 1] = board[parseI(piece.charAt(0))][parseI(piece.charAt(1))];
                killPiece(board, piece);
                newPiece = String.format("%s%s", String.valueOf(parseI(move.charAt(0)) + 1), String.valueOf(parseI(move.charAt(1)) + 1));
            } else if ((player.equals("2") && parseI(move.charAt(1)) < parseI(piece.charAt(1)) && parseI(move.charAt(0)) < parseI(piece.charAt(0))) && isKing(board, piece)) { // if the jump is to the left in the board
                board[parseI(move.charAt(0)) - 1][parseI(move.charAt(1)) - 1] = board[parseI(piece.charAt(0))][parseI(piece.charAt(1))];
                killPiece(board, piece);
                newPiece = String.format("%s%s", String.valueOf(parseI(move.charAt(0)) - 1), String.valueOf(parseI(move.charAt(1)) - 1));
            } else if ((player.equals("2") && parseI(move.charAt(1)) > parseI(piece.charAt(1)) && parseI(move.charAt(0)) < parseI(piece.charAt(0))) && isKing(board, piece)) { // if the jump is to the right in the board
                board[parseI(move.charAt(0)) - 1][parseI(move.charAt(1)) + 1] = board[parseI(piece.charAt(0))][parseI(piece.charAt(1))];
                killPiece(board, piece);
                newPiece = String.format("%s%s", String.valueOf(parseI(move.charAt(0)) - 1), String.valueOf(parseI(move.charAt(1)) + 1));
            }
            return newPiece;
    }
}
