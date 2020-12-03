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

public class WhitePlayer extends Player{

    //ham xay dung
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board,whiteStandardLegalMoves,blackStandardLegalMoves);
    }


    @Override
    //tap hop cac quan co TRANG hoat dong
    public Collection<Piece> getActivePieces() {
        return  this.board.getWhitePieces();
    }

    @Override
    //lay ra LIEN MINH TRANG
    public Alliance getAlliance() {
        return Alliance.WHILE;
    }

    @Override
    //lay ra doi thu
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    // tinh toán nhập thành cho vua
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,final Collection<Move> opponentsLegals) {

        final List<Move> kingCastles= new ArrayList<>();
        //neu day la nuoc di chuyen dau tien cua vua va nguoi choi ko bi Check
        if(this.playerKing.isFristMove() && !this.isInCheck()) {
            //white king side castle
            //tile 61 62 chua bi chiem
            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                //neu rooktile bi chiem va la nuoc di dau tien cua xe
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFristMove()) {
                    //o 61 62 ko nam trong tap bi check va o 63 la quan xe
                   if(Player.calculateAttacksOnTile(61,opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(62,opponentsLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()){
                       //TODO add a castlemove;
                       kingCastles.add(new KingSideCastleMove(this.board,this.playerKing,62,(Rook)rookTile.getPiece(),rookTile.getTileCoordinate(),61));
                   }
                }
            }
            if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(56);
                //neu rooktile bi chiem va la nuoc di dau tien cua xe
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFristMove() &&
                    Player.calculateAttacksOnTile(57,opponentsLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(59,opponentsLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()) {
                    //TODO add a castlemove;
                    kingCastles.add(new QueenSideCastleMove(this.board,this.playerKing,58,(Rook)rookTile.getPiece(),rookTile.getTileCoordinate(),59));
                }
            }
        }


            return ImmutableList.copyOf(kingCastles);// tra ve danh sach bat bien cac phan tu da cho theo thu tu
        }

}//class
