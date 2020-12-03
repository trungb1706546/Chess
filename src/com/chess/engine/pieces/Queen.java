package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;
import com.chess.engine.Alliance;
public class Queen extends Piece {

	
	//Quan Hau la ket hop cua quan Xe va quan Tuong
	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES= {-9, -8, -7, -1, 1, 7, 8, 9};
	
	
	public Queen(final Alliance pieceAlliance,final int piecePosition) {
		super(PieceType.QUEEN,piecePosition, pieceAlliance,true);
		// TODO Auto-generated constructor stub
	}
	public Queen(final Alliance pieceAlliance,final int piecePosition,final boolean isFristMove) {
		super(PieceType.QUEEN,piecePosition, pieceAlliance,isFristMove);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		final List<Move> legalMoves= new ArrayList<>(); // danh sach luu nhung vi tri hop le
		
		for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES) {
			
			int candidateDestinationCoordinate=this.piecePosition;
			
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

				if(isFirstColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)||isEightHColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)) break;
				
				
				candidateDestinationCoordinate+=candidateCoordinateOffset;
				
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					
					final Tile candidateDestinationonTile=board.getTile(candidateDestinationCoordinate);
					
					if(!candidateDestinationonTile.isTileOccupied()) {
						
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
						
					}else {
						
						final Piece pieceAtDestination=candidateDestinationonTile.getPiece();
						final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
						if(this.pieceAlliance!=pieceAlliance) {
							
							legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						}
						break;
					}
					
				}
				
			}
		}
		
		return ImmutableList.copyOf(legalMoves);
	}


	@Override
	public Queen movePiece(final Move move) {
		return new Queen(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
	}

	@Override
	public String toString() {
		return PieceType.QUEEN.toString();
	}
	
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition]&&(candidateOffset==-1||candidateOffset==-9||candidateOffset==7);
	}
	
	private static boolean isEightHColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(candidateOffset==-7||candidateOffset==1||candidateOffset==9);
	}

}