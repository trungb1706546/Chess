package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    //ham xay dung
    Player(final Board board,
           final Collection<Move> legalMoves, //nuoc di hop phap cua nguoi choi
           final Collection<Move> opponentMoves) { //nuoc di hop phap cua doi thu

        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastles(legalMoves,opponentMoves))); // noi 2 collection lai
        this.isInCheck=!Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty(); //nhung nuoc di cua đối thủ có tấn công người chơi hiện tại(VUA)
        //neu collection calculateattacks trống nghĩa là ko bị tấn công
    }

    //lay ra playerKing
    public King getPlayerKing(){
        return playerKing;
    }

    //lay ra buoc di chuyen hop phap
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }


    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) { //duyet qua collection nếu vị trí của vua bằng vs vị trí hợp lệ của đối thủ thì thêm move vào tập attackMoves
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move: moves){
            if(piecePosition==move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }


    //thiet lap vua
    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        //tao ra 1 ngoai le
        throw new RuntimeException("Ko phải là trạng thái hop le");
    }


    //co phai la buoc di hop phap
    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move); //Được sử dụng để tìm kiếm phần tử trong Collection
    }

    //tap hop cac quan co hoat dong
    public abstract Collection<Piece> getActivePieces();

    // lay ra Lien Minh
    public abstract Alliance getAlliance();

    // lay ra doi thu
    public abstract Player getOpponent();


    //check trong cờ vua// khi vua bị tấn công
    public boolean isInCheck() {
        return isInCheck;
    }

    //SẼ THỰC HIỆN CÁC PHƯƠNG THỨC NÀY
    //xem dinh nghĩa checkmate trong cờ vua //chieu bí
    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    //có đang bế tắc// xem định nghĩa staleMate //hòa
    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    //có động thái thoát trốn thoát
    protected boolean hasEscapeMoves() {

        for(final Move move:  this.legalMoves){
            final   MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return  false;
    }


    //có bị nhập thành ?
    public boolean isCastled() {
        return false;
    }


    //
    public MoveTransition makeMove(final Move move){

        //di chuyen co hop phap ko
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);

        }
        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks= Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),transitionBoard.currentPlayer().getLegalMoves());

        //neu cac cuoc tan cong ko trong thì
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board,move, MoveStatus.lEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
    }


    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move>opponentsLegals);



}
