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

public class Pawn extends Piece{

	
	private final static int[] CANDIDATE_MOVE_COORDINATE= {8, 16,7,9};
	
	
	public Pawn(final Alliance pieceAlliance,final int piecePosition) {
		super(PieceType.PAWN,piecePosition, pieceAlliance,true);
		// TODO Auto-generated constructor stub
	}

	public Pawn(final Alliance pieceAlliance,final int piecePosition,final boolean isFristMove) {
		super(PieceType.PAWN,piecePosition, pieceAlliance,isFristMove);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		final List<Move>legalMoves =new ArrayList<>(); // danh sach nuoc di hop le
		
		for(int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE) {
																	//tra ve -1 hoac 1 de xac dinh MAU co va nhan vs 8			
			final int candidateDestinationCoordinate = this.piecePosition+(this.pieceAlliance.getDirection()*currentCandidateOffset);
			
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {// neu vi tri nam ngoai ban co thi bo qua la lap
				
				continue;
			}
			if(currentCandidateOffset==8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {//di chuyen ve o phia truoc va o do ko bi chiem //buoc di 1 o cua chot
				
				//ToDo more work to do here !!;
				legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
			
			}else if(currentCandidateOffset==16 && this.isFristMove() && 
					((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
					(BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))) {
					//if buoc di dau tien cua con chot va nhay 2 o && quan Chot ở dòng thứ 2 và thuộc liên minh đen hoặc..
				
				final int behindCandidateDestination = this.piecePosition + (this.getPieceAlliance().getDirection()*8);// biến phía sau ô hiện hành... hiện hành là 8 thì biến là 16 tùy màu cờ mà cộng trừ
				if(!board.getTile(behindCandidateDestination).isTileOccupied() && 
					!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					//luc nay behindcandidaDestination =this.position +8
					//luc nay candidatedestination = this.position +16
					//neu 2 o deu trong thi them vao danhsach hop phap
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					
				}//neu buoc di la 7 va khac (cot 8 quan trang) || khac (cot 1 quan den) thi
				else if(candidateDestinationCoordinate==7 && 
						!((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())||
						(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
						if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
							final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
							if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
								//TODO more to do here
								legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
							}
						}
					
				}else if(candidateDestinationCoordinate==9 && 
						!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())||
						(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
						if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
							final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
							if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
								//TODO more to do here
								legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
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

}

