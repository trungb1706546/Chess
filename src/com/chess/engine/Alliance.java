package com.chess.engine;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {
	
	WHILE {
		@Override
		public int getDirection() {
			// TODO Auto-generated method stub
			return -1;
		}

		@Override
		public int getOppositeDirection(){
			return 1;
		}

		@Override
		public boolean isBlack() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isWhite() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isPawnPromotionSquare(int position) {
			return BoardUtils.EIGHTH_RANK[position];
		}

		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
			return whitePlayer;
		}

	},
	 BLACK{
			@Override
		public	int getDirection() {
				// TODO Auto-generated method stub
				return 1;
			}

		 @Override
		 public int getOppositeDirection(){
			 return -1;
		 }

		 @Override
		 public boolean isBlack() {
				// TODO Auto-generated method stub
			 return true;
			}

			@Override
			public boolean isWhite() {
				// TODO Auto-generated method stub
				return false;
			}

		 @Override
		 public boolean isPawnPromotionSquare(int position) {
			 return BoardUtils.FIRST_RANK[position];
		 }

		 @Override
		 public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
			 return blackPlayer;
		 }
	 };

	public abstract int getDirection();
	public abstract int getOppositeDirection(); // dinh huong // su dung cho bat chot qua duong
	public abstract boolean isBlack();
	public abstract boolean isWhite();

	public abstract boolean isPawnPromotionSquare(int position);

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
