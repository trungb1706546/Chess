package com.chess.engine.pieces;

import com.chess.engine.board.Move;
import com.chess.engine.board.Board;
import com.chess.engine.Alliance;



import java.util.Collection;
//import java.util.List;

public abstract class Piece {
	protected  final PieceType pieceType;
	protected final int piecePosition; //vi tri cua quan co
	protected final Alliance pieceAlliance; // lien minh cua quan co
	protected final boolean isFristMove; //buoc di chuyen dau tien
	private final int cachedHashCode; // bo nho dem cua hashcode
	
	public Piece(final PieceType pieceType,final int piecePosition, final Alliance pieceAlliance, final boolean isFristMove){
		this.pieceType=pieceType;
		this.pieceAlliance= pieceAlliance;
		this.piecePosition= piecePosition;
		//TODO more work here !
		this.isFristMove=isFristMove;
		this.cachedHashCode=computerHasCode();
	}

	private int computerHasCode() {
		int result = pieceType.hashCode();
		result = 31 * result + pieceAlliance.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (isFristMove ? 1: 0);
		return result;
	}


	//ghi de equals cua lop object
	@Override
	public boolean equals(final Object other){

		if(this==other){
			return true;
		}
		if(!(other instanceof Piece)){
			return false;
		}
		final Piece otherPiece =(Piece) other;
		return  piecePosition == ((Piece) other).getPiecePosition() && pieceType==otherPiece.getPieceType()&&
				pieceAlliance ==otherPiece.getPieceAlliance() && isFristMove==((Piece) other).isFristMove();

	}

	//hash code tra ve địa chỉ của đối tượng trong bộ nhớ là một số nguyên
	@Override
	public int hashCode(){

		return this.cachedHashCode;


	}
	
	public int getPiecePosition() {
		
		return this.piecePosition;
	}

	//lay lien minh quan cờ
	public Alliance getPieceAlliance() {

		return this.pieceAlliance;
	}

	//co phai lan di chuyen dau tien
	public boolean isFristMove() {

		return this.isFristMove;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);

	//lay loai quan cờ
	public  PieceType getPieceType(){

		return this.pieceType;
	}

	//tra về quân cờ củ với vị trí dc cập nhật
	public abstract Piece movePiece(Move move);



	public enum PieceType{

		PAWN("P"){
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KNIGHT("N"){
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		BISHOP("B"){
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK("R"){
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN("Q"){
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING("K"){
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		};

		private String pieceName;

		PieceType(final String pieceName){
			this.pieceName=pieceName;
		}

		@Override
		public String toString() {
			return this.pieceName;
		}

		public abstract boolean isKing();

		public abstract boolean isRook();
	}


}//last
