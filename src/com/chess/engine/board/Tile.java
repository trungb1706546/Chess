package com.chess.engine.board;
import java.util.HashMap;
import java.util.Map;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;


public abstract class Tile {
	
	protected final int tileCoordinate; // vi tri của ô
	
	private static final Map<Integer, EmptyTile>EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
	
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() { // Map với key la integer còn value là emptytitle
		
		final Map<Integer, EmptyTile>emptyTileMap = new HashMap<>();
		
		for(int i=0; i<BoardUtils.NUM_TILES; i++) { //Numbertiles = 64 định nghia trong lớp board
			emptyTileMap.put(i, new EmptyTile(i)); // thêm một phần từ mới vào Map bằng hàm put(key, value)
		}
		
		return ImmutableMap.copyOf(emptyTileMap); // sử dụng hàm của thư viện guava 18.0
	}
	
	public static Tile createTile(final int tileCoordinate, final Piece piece) { // cau len if else viet tắt   ! ?
		return piece != null ? new OccupiedTile(tileCoordinate, piece): EMPTY_TILES_CACHE.get(tileCoordinate); //phuong thuc get trả về giá trị tương ứng vs key nếu ko có trả về null
	}
	
	//ham xay dung
	private Tile(final int titleCoordinate){ // gan vi tri cho ô
		this.tileCoordinate=titleCoordinate;  
	}
	

	public abstract boolean isTileOccupied(); //lớp trừu tượng ô đã có cờ chưa
	
	public abstract Piece getPiece();// lớp trừu tượng trả về quân cờ ở ô đó

	//lay toa do o
	public int getTileCoordinate(){
		return this.tileCoordinate;
	}


	//class emtytile
	public static final class EmptyTile extends Tile{
		
		private EmptyTile(final int coordinate){
			super(coordinate);
		}

		@Override
		public String toString() {
			return "-";
		}
			
		@Override
		public boolean isTileOccupied() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Piece getPiece() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	//class occupiedtile
	public static final class OccupiedTile extends Tile{
		
		private final Piece pieceOnTile;
		OccupiedTile(int tileCoordinate, final Piece pieceOnTile){ //vị trí ô và quân cờ ở trên ô
			super(tileCoordinate);
			this.pieceOnTile=pieceOnTile;
			
		}
		

		@Override
		public String toString() {
			return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase():getPiece().toString();
			//neu quan co mau den thi Lower nguoc lai thi in HOA
		}
		
		@Override
		public boolean isTileOccupied() { //ô này có quân cờ nào chưa
			return true;
		}
		
		@Override
		public Piece getPiece() {
			return this.pieceOnTile; //tra ve quan cờ hiện tại trên ô
		}
		
			
	}
	
	
	
}//last
