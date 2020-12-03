package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public  class BlackPlayer extends Player{

    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board,whiteStandardLegalMoves,blackStandardLegalMoves);
    }

    @Override
    //tap hop cac quan co DEN hoat dong
    public Collection<Piece> getActivePieces(){

        return this.board.getBlackPieces();
    }

    @Override
    //lay ra lien minh DEN
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    //lay ra doi thu
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,final Collection<Move> opponentsLegals) {

        final List<Move> kingCastles= new ArrayList<>();
        //neu day la nuoc di chuyen dau tien cua vua va nguoi choi ko bi Check
        if(this.playerKing.isFristMove() && !this.isInCheck()) {
            //black king side castle
            //tile 6 7 chua bi chiem
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);
                //neu rooktile bi chiem va la nuoc di dau tien cua xe
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFristMove()) {
                    //o 5 6 ko nam trong tap bi check va o 7 la quan xe
                    if(Player.calculateAttacksOnTile(5,opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(6,opponentsLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()){
                        //TODO add a castlemove;
                        kingCastles.add(new KingSideCastleMove(this.board,this.playerKing,6,(Rook)rookTile.getPiece(),rookTile.getTileCoordinate(),5));
                    }
                }
            }
            if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                //neu rooktile bi chiem va la nuoc di dau tien cua xe
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFristMove() &&
                    Player.calculateAttacksOnTile(2,opponentsLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(3,opponentsLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()) {
                    //TODO add a castlemove;
                    kingCastles.add(new QueenSideCastleMove(this.board,this.playerKing,2,(Rook)rookTile.getPiece(),rookTile.getTileCoordinate(),3));
                }
            }
        }


        return ImmutableList.copyOf(kingCastles);// tra ve danh sach bat bien cac phan tu da cho theo thu tu
    }

}//class
