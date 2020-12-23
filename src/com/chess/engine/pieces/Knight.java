package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.Alliance;


public class Knight extends Piece {

	private final int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 }; // các nước ngựa có thể đi

	public Knight(final Alliance pieceAlliance,final int piecePosition) {// hàm xây dựng
		super(PieceType.KNIGHT,piecePosition, pieceAlliance,true);

	}

	public Knight(final Alliance pieceAlliance,final int piecePosition,final boolean isFristMove) {// hàm xây dựng
		super(PieceType.KNIGHT,piecePosition, pieceAlliance,isFristMove);

	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) { // ghi đè phương thức tính toán nước đi của lớp Piece

		//int candidateDestinationCoordinate; // tọa độ điểm đến
		List<Move> legalMoves = new ArrayList<>(); // tạo ra một list để lưu các nước đi hợp pháp

		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) { // tao ra một biến số nguyên (current) và duyệt
																		// qua mảng canditamovecoordinate
			// tọa độ điểm đến
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset; // vị trí hiện tại của ngựa cộng cho
																					// phần tử trong mảng

			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) { //=true
				
				//cac truong hop nay bo qua 1 lan lap trong for
				if(isFirstColumnExlusion(this.piecePosition, currentCandidateOffset)||
						isSecondColumnExlusion(this.piecePosition, currentCandidateOffset)||
						isSeventhColumnExlusion(this.piecePosition, currentCandidateOffset)||
						isEighthColumnExlusion(this.piecePosition, currentCandidateOffset)) 
						{
					
					continue;
				}
				
				
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if (!candidateDestinationTile.isTileOccupied()) { //nếu ô chưa bị chiếm giữ thì thêm vào danh sách hợp lệ
					
					
					legalMoves.add(new MajorMove(board ,this, candidateDestinationCoordinate));
					
				} else { // nếu ô bị chiêm thì xem nó là bên đen hay trắng
					final Piece pieceAtDestination = candidateDestinationTile.getPiece(); // quân cờ ở điểm đến
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); // màu của quân cờ ở điêm đến

					if (this.pieceAlliance != pieceAlliance) { //nếu con ngựa khác màu vs quân cờ ở ô đến thì hợp lệ
						
						legalMoves.add(new MajorAttackMove(board ,this, candidateDestinationCoordinate,pieceAtDestination));
					}
				}
			}

		}

		return ImmutableList.copyOf(legalMoves);
	}


	@Override
	public Knight movePiece(final Move move) {
		return new Knight(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
	}

	
	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}
	
	
	private static boolean isFirstColumnExlusion(final int currentPosition, final int candidateOffset) {
		//cot thu nhat 0 8 16 24 32 40 48 56 cong tru cac gia tri nay se ko hop lệ
		return BoardUtils.FIRST_COLUMN[currentPosition]&&(candidateOffset==-17||candidateOffset==-10||candidateOffset==6||candidateOffset==15);
	}

	private static boolean isSecondColumnExlusion(final int currentPosition, final int candidateOffset) {
		//cot thu nhat 1 9 17 25 33 41 49 57 cong tru cac gia tri nay se ko hop lệ
		return BoardUtils.SECOND_COLUMN[currentPosition]&&(candidateOffset==6||candidateOffset==-10);
	}
	
	private static boolean isSeventhColumnExlusion(final int currentPosition, final int candidateOffset) {
		//cot thu bay 6 14 22 ... 62 cong tru cac gia tri nay se ko hop lệ
		return BoardUtils.SEVENTH_COLUMN[currentPosition]&&(candidateOffset==-6||candidateOffset==10);
	}
	
	private static boolean isEighthColumnExlusion(final int currentPosition, final int candidateOffset) {
		//cot thu tam 7 15 23... 63 cong tru cac gia tri nay se ko hop lệ
		return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(candidateOffset==-6||candidateOffset==-15||candidateOffset==10||candidateOffset==17);
	}
	
	

}
