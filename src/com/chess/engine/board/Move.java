package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;

public abstract class Move {


	protected final Board board;
	protected final Piece movedPiece;
	protected final int destinationCoordinate;
	protected final boolean isFirstMove;

	public static final Move NULL_MOVE= new NullMove();
	
	private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
		this.board=board;
		this.movedPiece=movedPiece;
		this.destinationCoordinate=destinationCoordinate;
		this.isFirstMove=movedPiece.isFristMove();
	}

	private  Move(final Board board, final int destinationCoordinate){

		this.board=board;
		this.destinationCoordinate=destinationCoordinate;
		this.movedPiece=null;
		this.isFirstMove=false;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + this.destinationCoordinate;
		result = prime * result + this.movedPiece.hashCode();
		result = prime * result +this.movedPiece.getPiecePosition();
		return result;

	}

	@Override
	public boolean equals(final Object other){
		if(this==other){
			return true;
		}
		if(!(other instanceof Move)){
			return false;
		}
		final Move otherMove = (Move)other;
		return getCurrentCoordinate() == otherMove.getCurrentCoordinate()&&
				getDestinationCoordinate()==otherMove.getDestinationCoordinate() &&
				getMovedPiece().equals(otherMove.getMovedPiece());
	}

	public Board getBoard(){
		return this.board;
	}


	//lay vi tri hien tai
	public int getCurrentCoordinate(){
		return this.getMovedPiece().getPiecePosition();
	}

	//tra ve vi tri DICH
	public int getDestinationCoordinate(){
		return  this.destinationCoordinate;
	}


	//lay ra movedPiece
	public Piece getMovedPiece(){
		return this.movedPiece;
	}


	//co phai di chuyen tan cong
	public boolean isAttack(){
		return false;
	}

	//co phai nhap thanh
	public boolean isCastlingMove(){
		return false;
	}

	//lay quan co bi tan cong
	public Piece getAttackedPiece(){
		return null;
	}

	//tao va tra ve 1 bang moi dua tren việc thực hiện 1 nước đi trên 1 quân cờ đối với 1 bàn cờ
	public Board execute() {
		final Builder builder = new Builder();
		//duyet qua nguoi choi hien tai
		for(final  Piece piece : this.board.currentPlayer().getActivePieces()){
			//TODO hashcode and equals for pieces
			//neu ko phai la quan co da di chuyen
			if(!this.movedPiece.equals(piece)){
				//đặt họ vào bảng mới
				builder.setPiece(piece);
			}
		}

		//tuong tu nhu tren
		for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
			builder.setPiece(piece);
		}
		// move the moved piece !
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());


		return builder.build();
	}

	public static class MajorAttackMove extends  AttackMove{

		public MajorAttackMove(final Board board,
							   final Piece pieceMoved,
							   final int destinationCoordinate,
							   final Piece pieceAttacked){
			super(board,pieceMoved,destinationCoordinate,pieceAttacked);
		}

		@Override
		public boolean equals(final Object other){
			return this==other||other instanceof  MajorAttackMove && super.equals(other);
		}

		@Override
		public String toString(){
			return movedPiece.getPieceType()+BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}



	//class di chuyen den o trong
	public static final class MajorMove extends    Move{
		
		public MajorMove(final Board board,
						 final Piece movedPiece, 
						 final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}

		@Override
		public boolean equals(final Object other){
			return this==other || other instanceof MajorMove && super.equals(other);
		}

		@Override
		public String toString(){
			return movedPiece.getPieceType().toString()+BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}


	}
	
	
	//class di chuyen tan cong
	public static class AttackMove extends Move{
			
			final Piece attackedPiece;
			public AttackMove(final Board board,
							 final Piece movedPiece, 
							 final int destinationCoordinate,
							 final Piece attackedPiece) {
				super(board, movedPiece, destinationCoordinate);
				this.attackedPiece=attackedPiece;
			}

		@Override
		public int hashCode(){
				return this.attackedPiece.hashCode() + super.hashCode();
		}

		@Override
		public boolean equals(final Object other){
				if(this==other){
					return true;
				}
				if(!(other instanceof AttackMove)){
					return false;
				}
				final AttackMove otherAttackMove = (AttackMove)other;
				return super.equals(otherAttackMove)&& getAttackedPiece().equals(otherAttackMove.getAttackedPiece());

		}



		@Override
		public boolean isAttack(){
				return true;
		}

		@Override
		public Piece getAttackedPiece(){
				return this.attackedPiece;
		}

	}

	public static final class PawnMove extends    Move{

		public PawnMove(final Board board,
						 final Piece movedPiece,
						 final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
		@Override
		public boolean equals(final Object other){
			return this==other || other instanceof  PawnMove && super.equals(other);
		}

		@Override
		public String toString(){
			return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}


	}

	public static  class PawnAttackMove extends    AttackMove{

		public PawnAttackMove(final Board board,
							  final Piece movedPiece,
							  final int destinationCoordinate,
							  final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate,attackedPiece);
		}

		@Override
		public boolean equals(final Object other){
			return this==other || other instanceof  PawnAttackMove && super.equals(other);
		}

		@Override
		public String toString(){
			return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1)+"x"+
					BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}

	//chot qua duong tan cong
	public static final class PawnEnPassantAttackMove extends   PawnAttackMove{

		public PawnEnPassantAttackMove(final Board board,
							  final Piece movedPiece,
							  final int destinationCoordinate,
							  final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate,attackedPiece);
		}

		@Override
		public boolean equals(final Object other){
			return this==other|| other instanceof PawnEnPassantAttackMove && super.equals(other);
		}

		@Override
		public Board execute(){
			final  Builder builder = new Builder();
			for(final Piece piece : this.board.currentPlayer().getActivePieces()){
				if(!this.movedPiece.equals(piece)){
					builder.setPiece(piece);
				}
			}
			for(final Piece piece:this.board.currentPlayer().getOpponent().getActivePieces()){
				if(!piece.equals(this.getAttackedPiece())){
					builder.setPiece(piece);
				}
			}
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}

	}

		//Phong co
	public static class PawnPromotion extends Move{
		final Move decoratedMove;
		final Pawn promotedPawn;

		public PawnPromotion(final Move decoratedMove){
			super(decoratedMove.getBoard(),decoratedMove.getMovedPiece(),decoratedMove.getDestinationCoordinate());

			this.decoratedMove=decoratedMove;
			this.promotedPawn=(Pawn) decoratedMove.getMovedPiece();
		}

		@Override
		public int hashCode(){
			return decoratedMove.hashCode()+(31*promotedPawn.hashCode());
		}

		@Override
		public boolean equals(final Object other){
			return this==other || other instanceof PawnPromotion && (super.equals(other));
		}

		@Override
		public Board execute(){

			final Board pawnMovedBoard =this.decoratedMove.execute();
			final  Board.Builder builder= new Builder();
			for(final Piece piece: pawnMovedBoard.currentPlayer().getActivePieces()){
				if(!this.promotedPawn.equals(piece)){
					builder.setPiece(piece);
				}
			}
			for(final Piece piece: pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()){
				builder.setPiece(piece);
			}
			builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
			builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
			return builder.build();


		}

		@Override
		public boolean isAttack(){
			return this.decoratedMove.isAttack();
		}

		@Override
		public Piece getAttackedPiece(){
			return this.decoratedMove.getAttackedPiece();
		}

		@Override
		public String toString(){
			return "";

		}


	}

	public static final class PawnJump extends    Move{

		public PawnJump(final Board board,
						final Piece movedPiece,
						final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}

		@Override
		public Board execute(){
			final Builder builder = new Builder();
			for(final Piece piece : this.board.currentPlayer().getActivePieces()){
				if(!this.movedPiece.equals(piece)){
					builder.setPiece(piece);
				}
			}
			for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
				builder.setPiece(piece);
			}
			final Pawn movePawn =(Pawn)this.movedPiece.movePiece(this);
			builder.setPiece(movePawn);
			builder.setEnPassantPawn(movePawn);//bat chot qua duong
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		@Override
		public String toString(){
			return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}


	}

	static abstract class CastleMove extends Move{

		protected final Rook castleRook; //quan xe se tham gia nhap thanh
		protected final int castleStart; //toa do ban dau
		protected final int castleDestination; //toa do diem den

		public CastleMove(final Board board,
						 final Piece movedPiece,
						 final int destinationCoordinate,
						  final Rook castleRook,
						  final int castleStart,
						  final int castleDestination) {
			super(board, movedPiece, destinationCoordinate);
			this.castleRook = castleRook;
			this.castleStart = castleStart;
			this.castleDestination = castleDestination;
		}

		//lay ra castle rook
		public Rook getCastleRook(){
			return this.castleRook;
		}

		//co dang duy chuyen nhap thanh
		@Override
		public boolean isCastlingMove(){
			return true;
		}

		@Override
		public Board execute(){
			final Builder builder = new Builder();
			for(final Piece piece : this.board.currentPlayer().getActivePieces()){
				if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
					builder.setPiece(piece);
				}
			}
			for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
				builder.setPiece(piece);
			}

			builder.setPiece(this.movedPiece.movePiece(this));
			//TODO look into the first move on normal pieces
			builder.setPiece(new Rook(this.castleRook.getPieceAlliance(),this.castleDestination));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}

		@Override
		public int hashCode(){
			final int prime=31;
			int result =super.hashCode();
			result=prime*result+this.castleRook.hashCode();
			result=prime*result+this.castleDestination;
			return  result;
		}

		@Override
		public  boolean equals(final Object other){
			if(this==other){
				return true;
			}
			if(!(other instanceof CastleMove)){
				return false;
			}
			final CastleMove otherCastleMove =(CastleMove)other;
			return  super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
		}


	}

	public static final class KingSideCastleMove extends    CastleMove{

		public KingSideCastleMove(final Board board,
								  final Piece movedPiece,
								  final int destinationCoordinate,
								  final Rook castleRook,
								  final int castleStart,
								  final int castleDestination) {
			super(board, movedPiece, destinationCoordinate, castleRook,castleStart,castleDestination);
		}

		@Override
		public boolean equals(final Object other){
			return this==other||other instanceof KingSideCastleMove && super.equals(other);
		}

		@Override
		public String toString(){
			return "0-0";
		}

	}
	public static final class QueenSideCastleMove extends    CastleMove{

		public QueenSideCastleMove(final Board board,
								   final Piece movedPiece,
								   final int destinationCoordinate,
								   final Rook castleRook,
								   final int castleStart,
								   final int castleDestination) {
			super(board, movedPiece, destinationCoordinate, castleRook,castleStart,castleDestination);
		}

		@Override
		public boolean equals(final Object other){
			return this==other||other instanceof QueenSideCastleMove && super.equals(other);
		}

		@Override
		public String toString(){
			return "0-0-0";
		}

	}

	public static final class NullMove extends    Move{

		public NullMove() {

			super(null, -1);
		}

		@Override
		public Board execute() {
			throw  new RuntimeException("cannot execute the nulll move!");
		}

		@Override
		public int getCurrentCoordinate(){

			return -1;
		}

	}

	//class move fatory
	public static  class MoveFactory{
		private MoveFactory(){
			throw new RuntimeException("not instantiable");
		}
		public static  Move createMove(final Board board,
									   final int currentCoordinate,
									   final int destinationCoordinate){
			for(final Move move : board.getAllLegalMoves()){
				if(move.getCurrentCoordinate()==currentCoordinate &&
						move.getDestinationCoordinate()==destinationCoordinate){
					return move;
				}
			}
			return NULL_MOVE;
		}
	}




}//last
