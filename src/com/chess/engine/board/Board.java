package com.chess.engine.board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import java.util.*;
import com.chess.engine.Alliance;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.google.common.collect.Iterables;

public class Board {

	private final List<Tile>gameBoard; // tao ra ban co bang list
	private final Collection<Piece> whitePieces; //tap hop các quân cờ trắng
	private final Collection<Piece> blackPieces; // tập hợp các quân cờ đen

	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private  final Player currentPlayer;

	// ham xay dung
	private Board(final Builder builder) {
		this.gameBoard=createGameBoard(builder);
		this.whitePieces= calculateActivePieces(this.gameBoard, Alliance.WHILE);
		this.blackPieces= calculateActivePieces(this.gameBoard, Alliance.BLACK);
		
		
		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.whitePieces);

		this.whitePlayer= new WhitePlayer(this, whiteStandardLegalMoves,blackStandardLegalMoves);
		this.blackPlayer=new BlackPlayer(this, whiteStandardLegalMoves,blackStandardLegalMoves);

		this.currentPlayer= builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
	}

	//ghi de ham toString để hiển thị bàn cờ
	@Override 
	public String toString() {
	
		final StringBuilder builder = new StringBuilder(); //class Stringbuilder tạo ra 1 chuỗi có thể thay đổi
		for(int i=0; i<BoardUtils.NUM_TILES; i++) {
			final String tileText = this.gameBoard.get(i).toString(); //goi ham toString trong lop tile
			builder.append(String.format("%3s", tileText)); //append nối chuỗi
			if((i+1) % BoardUtils.NUM_TILES_PER_ROW==0) {
				builder.append("\n");
			}
			
		}
		return builder.toString();
	}
	//tra ve nguoi choi TRANG
	public Player whitePlayer(){
		return this.whitePlayer;
	}

	//tra ve nguoi choi DEN
	public Player blackPlayer(){ return this.blackPlayer;
	}

	public Player currentPlayer(){
		return  this.currentPlayer;
	}

	//tra ve cac quan den
	public  Collection<Piece> getBlackPieces(){
		return this.blackPieces;
	}

	//tra ve cac quan trang
	public  Collection<Piece> getWhitePieces(){
		return this.whitePieces;
	}

		
	//tính toán các bước di chuyển hợp pháp
	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final Piece piece : pieces) {
			
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		
		return ImmutableList.copyOf(legalMoves);
	}

	// tinh toan cac quan co hoat dong
	private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,final Alliance alliance) {
		final List<Piece> activePieces = new ArrayList<>(); //tao list
		for(final Tile tile : gameBoard) { //duyet qua list gameBoard nếu Ô đã bị chiếm và có cùng liên minh vs tham số thì thêm vào LIST activePiece
			if(tile.isTileOccupied()) {
				final Piece piece = tile.getPiece();
				if(piece.getPieceAlliance()==alliance) {
					activePieces.add(piece);
				}
			}
		}
		return ImmutableList.copyOf(activePieces);
	}

	//lấy ô
	public Tile getTile(int tileCoordinate) { // lay o
		
		return gameBoard.get(tileCoordinate); // trả về đối tượng được lưu trữ tại chỉ số index trong list gamrBoard
	}

	//tao ra ban cờ
	private static List<Tile> createGameBoard(final Builder builder){ //phuong thuc nay dien vao danh sach cac ô tu 0 den 63
		final Tile[] tiles= new Tile[BoardUtils.NUM_TILES]; // tao mang Tile 64 phan tu
		for(int i=0; i<BoardUtils.NUM_TILES; i++) {
			tiles[i] =Tile.createTile(i, builder.boardConfig.get(i));  //ham get(i) cua MAP => lay gia tri tuong ung vs khoa i //ham createTile cua lop Tile
		}
		return ImmutableList.copyOf(tiles);
	}

	//tạo ra bàn cờ tiêu chuẩn
	public static Board createStanderBoard() {
		final Builder builder= new Builder();
		//BLACK
		builder.setPiece(new Rook(Alliance.BLACK, 0));
		builder.setPiece(new Knight(Alliance.BLACK, 1));
		builder.setPiece(new Bishop(Alliance.BLACK, 2));
		builder.setPiece(new Queen(Alliance.BLACK, 3));
		builder.setPiece(new King(Alliance.BLACK, 4));
		builder.setPiece(new Bishop(Alliance.BLACK, 5));
		builder.setPiece(new Knight(Alliance.BLACK, 6));
		builder.setPiece(new Rook(Alliance.BLACK, 7));
		builder.setPiece(new Pawn(Alliance.BLACK, 8));
		builder.setPiece(new Pawn(Alliance.BLACK, 9));
		builder.setPiece(new Pawn(Alliance.BLACK, 10));
		builder.setPiece(new Pawn(Alliance.BLACK, 11));
		builder.setPiece(new Pawn(Alliance.BLACK, 12));
		builder.setPiece(new Pawn(Alliance.BLACK, 13));
		builder.setPiece(new Pawn(Alliance.BLACK, 14));
		builder.setPiece(new Pawn(Alliance.BLACK, 15));
		
		//WHILE
		builder.setPiece(new Pawn(Alliance.WHILE, 48));
		builder.setPiece(new Pawn(Alliance.WHILE, 49));
		builder.setPiece(new Pawn(Alliance.WHILE, 50));
		builder.setPiece(new Pawn(Alliance.WHILE, 51));
		builder.setPiece(new Pawn(Alliance.WHILE, 52));
		builder.setPiece(new Pawn(Alliance.WHILE, 53));
		builder.setPiece(new Pawn(Alliance.WHILE, 54));
		builder.setPiece(new Pawn(Alliance.WHILE, 55));
		builder.setPiece(new Rook(Alliance.WHILE, 56));
		builder.setPiece(new Knight(Alliance.WHILE, 57));
		builder.setPiece(new Bishop(Alliance.WHILE, 58));
		builder.setPiece(new Queen(Alliance.WHILE, 59));
		builder.setPiece(new King(Alliance.WHILE, 60));
		builder.setPiece(new Bishop(Alliance.WHILE, 61));
		builder.setPiece(new Knight(Alliance.WHILE, 62));
		builder.setPiece(new Rook(Alliance.WHILE, 63));
		//white to move
		builder.setMoveMaker(Alliance.WHILE); //Trang di truoc
		
		return builder.build();
	}

	//Iterator cho bạn khả năng để tuần hoàn qua một tập hợp, kiếm được và gỡ bỏ các phần tử
	//Phương thức Iterables.concat () là một trong những phương thức tiện lợi trong Guava được sử dụng để hợp nhất tập hợp
	public Iterable<Move> getAllLegalMoves() {
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),this.blackPlayer.getLegalMoves()));
	}


	public static class Builder{
		
		Map<Integer, Piece>boardConfig; // MAP ban co ID và Quân Cờ
		Alliance nextMoveMaker; // nguoi di chuyen tiep theo
		Pawn enPassantPawn; // bat tot qua duong

		//ham xay dung
		public Builder () {

			this.boardConfig=new HashMap<>();
		}

		//thiet lập quân cờ
		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(),piece);
			return this;
		}

		//nguoi di chuyen tiep theo
		public Builder setMoveMaker(final Alliance nextMoveMaker) {
			this.nextMoveMaker=nextMoveMaker;
			return this;
		}
		
		
		public Board build() {
			return new Board(this);
		}

		public void setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn=enPassantPawn;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}//class Board
