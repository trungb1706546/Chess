package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;
import com.chess.engine.Alliance;


public class Rook extends Piece{

	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES= {-8,-1,1,8};
	
	public Rook( final Alliance pieceAlliance,final int piecePosition) {
		super(PieceType.ROOK,piecePosition, pieceAlliance,true);
		
	}

	public Rook(final Alliance pieceAlliance,final int piecePosition, final  boolean isFristMove){
		super(PieceType.ROOK,piecePosition, pieceAlliance,isFristMove);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		final List<Move> legalMoves= new ArrayList<>(); // danh sach luu nhung vi tri hop le
		
		for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES) {
			
			int candidateDestinationCoordinate=this.piecePosition;
			
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)|| isEightHColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset))
					break;

				
				
				candidateDestinationCoordinate+=candidateCoordinateOffset;
				
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					
					final Tile candidateDestinationonTile=board.getTile(candidateDestinationCoordinate);
					
					if(!candidateDestinationonTile.isTileOccupied()) {
						
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
						
					}else {
						
						final Piece pieceAtDestination=candidateDestinationonTile.getPiece();
						final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
						if(this.pieceAlliance!=pieceAlliance) {
							
							legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						}
						break;
					}
					
				}
				
			}
		}
		
		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public Rook movePiece(final Move move) {
		return new Rook(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
	}


	@Override
	public String toString() {
		return PieceType.ROOK.toString();
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition]&&(candidateOffset==-1);
	}
	
	private static boolean isEightHColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(candidateOffset==1);
	}
	

}
