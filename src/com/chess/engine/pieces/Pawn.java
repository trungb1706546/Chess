package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.Alliance;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece{

	
	private final static int[] CANDIDATE_MOVE_COORDINATE= {8, 16,7,9};
	
	
	public Pawn(final Alliance pieceAlliance,final int piecePosition) {
		super(PieceType.PAWN,piecePosition, pieceAlliance,true);
		// TODO Auto-generated constructor stub
	}

	public Pawn(final Alliance pieceAlliance,
				final int piecePosition,
				final boolean isFristMove) {
		super(PieceType.PAWN,piecePosition, pieceAlliance,isFristMove);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {

		final List<Move> legalMoves = new ArrayList<>(); // danh sach nuoc di hop le
		for(int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE) {
			//tra ve -1 hoac 1 de xac dinh MAU co va nhan vs 8
			final int candidateDestinationCoordinate = this.piecePosition+(this.pieceAlliance.getDirection()*currentCandidateOffset);

			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {// neu vi tri nam ngoai ban co thi bo qua la lap

				continue;
			}
			if(currentCandidateOffset==8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){

				//Phong Co
				if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
						legalMoves.add(new PawnPromotion(new PawnMove(board,this,candidateDestinationCoordinate)));
					}else{

						legalMoves.add(new PawnMove(board,this,candidateDestinationCoordinate));
				}
			}else if(currentCandidateOffset==16 && this.isFristMove()&&
					((BoardUtils.SEVENTH_RANK[this.piecePosition]&&this.getPieceAlliance().isBlack())||
							(BoardUtils.SECOND_RANK[this.piecePosition]&&this.getPieceAlliance().isWhite()))){
				final int behindCandidateDestinationCoordinate=this.piecePosition+(this.pieceAlliance.getDirection()*8);
				if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()&&
						!board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					legalMoves.add(new PawnJump(board,this,candidateDestinationCoordinate));
				}

			}else if(currentCandidateOffset==7 &&
					!((BoardUtils.EIGHTH_COLUMN[this.piecePosition]&&this.pieceAlliance.isWhite()||
							(BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.pieceAlliance.isBlack())))){

				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
						//Phong co
						if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board,this,candidateDestinationCoordinate, pieceOnCandidate)));
						}else {
							legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}

				}//bat chot qua duong
				else if(board.getEnPassantPawn()!=null){
					if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition+(this.pieceAlliance.getOppositeDirection()))){
						final Piece pieceOnCandidate=board.getEnPassantPawn();
						if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
							legalMoves.add(new PawnEnPassantAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate));
						}
					}
				}
			}else if(currentCandidateOffset==9 &&
					!((BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.pieceAlliance.isWhite()||
							(BoardUtils.EIGHTH_COLUMN[this.piecePosition]&&this.pieceAlliance.isBlack())))){

				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
						if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
							//Phong co
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate)));
						}else{
							legalMoves.add(new PawnAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate));
						}

					}
				}//bat chot qua duong
				else if(board.getEnPassantPawn()!=null){
					if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition - (this.pieceAlliance.getOppositeDirection()))){
						final Piece pieceOnCandidate=board.getEnPassantPawn();
						if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
							legalMoves.add(new PawnEnPassantAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate));
						}
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public Pawn movePiece(final Move move) {
		return new Pawn(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
	}

	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}

	//Phong Hau
	public  Piece getPromotionPiece(){
		return new Queen(this.pieceAlliance, this.piecePosition,false);
	}

}

