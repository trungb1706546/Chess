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
public class King extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATE= {-9,-8,-7,-1,1,7,8,9};

	public King(final Alliance pieceAlliance,final int piecePosition) {
		super(PieceType.KING,piecePosition, pieceAlliance,true);
		// TODO Auto-generated constructor stub
	}

	public King(final Alliance pieceAlliance,final int piecePosition, final boolean isFristMove) {
		super(PieceType.KING,piecePosition, pieceAlliance,isFristMove);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {

		final List<Move> legalMoves = new ArrayList<>();
		
		
		for(int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE) {
			
			final int candidateDestinationCoordinate=this.piecePosition+currentCandidateOffset;
			// truong hop ngoai le o cot 1 va cot 8
			if(isFirstColumnExlusion(this.piecePosition, currentCandidateOffset)||isEighthColumnExlusion(this.piecePosition, currentCandidateOffset)) {
				continue;
			}
			
			
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if (!candidateDestinationTile.isTileOccupied()) { //nếu ô chưa bị chiếm giữ thì thêm vào danh sách hợp lệ
					
					legalMoves.add(new MajorMove(board ,this, candidateDestinationCoordinate));
					
				} else { // nếu ô bị chiêm thì xem nó là bên đen hay trắng
					final Piece pieceAtDestination = candidateDestinationTile.getPiece(); // quân cờ ở điểm đến
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); // màu của quân cờ ở điêm đến

					if (this.pieceAlliance != pieceAlliance) { //nếu con ngựa khác màu vs quân cờ ở ô đến thì hợp lệ
						
						legalMoves.add(new AttackMove(board ,this, candidateDestinationCoordinate,pieceAtDestination));
					}
				}
			}
		}
			
			
	

		return ImmutableList.copyOf(legalMoves);
	}


	@Override
	public King movePiece(final Move move) {
		return new King(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}
	

	private static boolean isFirstColumnExlusion(final int currentPosition, final int candidateOffset) {
		//cot thu nhat 0 8 16 24 32 40 48 56 cong tru cac gia tri nay se ko hop lệ
		return BoardUtils.FIRST_COLUMN[currentPosition]&&(candidateOffset==-9||candidateOffset==-1||candidateOffset==7);
	}

	private static boolean isEighthColumnExlusion(final int currentPosition, final int candidateOffset) {
		//cot thu nhat 1 9 17 25 33 41 49 57 cong tru cac gia tri nay se ko hop lệ
		return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(candidateOffset==-7||candidateOffset==1||candidateOffset==9);
	}

}
