package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public final class StandardBoardEvaluator implements BoardEvalutaor {


    private static  final int CHECK_BONUS =50;
    private static  final int CHECK_MAKE_BONUS =10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 61;

    @Override
    public int evaluate(final Board board,final int depth) {

        return scorePlayer(board, board.whitePlayer(),depth)-scorePlayer(board, board.blackPlayer(),depth);
        //neu tra ve so DUONG trang co loi the va AM thi den co loi the
    }

    //tinh diem cho player
    private int scorePlayer(final Board board,
                            final Player player,
                            final int depth) {

        return pieceValue(player) + moibility(player) + check(player) + checkmake(player,depth) + castled(player);
    }

    private static int castled(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    //tinh diem cho checkmake
    private static int checkmake(final Player player, int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MAKE_BONUS * depthBonus(depth) : 0;
    }

    //check make ở độ sâu có thưởng thêm //
    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    //tinh diem check
    private static int check(final Player player) {
        return player.getOpponent().isInCheck() ?CHECK_BONUS :0;
        //neu doi thu check thi 50 nguoc lai 0
    }


    //tinh di dong cua player //tong cac nuoc di hop phap
    private static int moibility(final Player player) {
        return player.getLegalMoves().size();

    }

    //tinh gia tri cua cac quan co
    private static int pieceValue(final  Player player){
        int pieceValueScore = 0;
        for(final Piece piece: player.getActivePieces()){
            pieceValueScore+= piece.getPieceValue();
        }
        return pieceValueScore;
    }
}
