package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy {

    private final  BoardEvalutaor boardEvalutaor;

    public MiniMax(){

        this.boardEvalutaor=null;
    }



    @Override
    public  String toString(){
        return "MiniMax";
    }

    @Override
    public Move execute(Board board, int depth) {

        final long startTime =System.currentTimeMillis();
        //do thoi gian lam 1 viec

        Move bestMove =null;

        int highestSeenValue=Integer.MIN_VALUE;
        int lowestSeenValue= Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.currentPlayer()+"Đang Suy Nghĩ với độ sâu = "+depth);

        int numMoves = board.currentPlayer().getLegalMoves().size();

        for(final  Move move: board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition =  board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                currentValue=board.currentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(),depth-1):
                        max(moveTransition.getTransitionBoard(),depth-1);
            }

        }

        return null;
    }

    public int min(final  Board board, final  int depth){

        if(depth==0 ){
            return  this.boardEvalutaor.evaluate(board,depth);
        }

        int lowestSeenValue= Integer.MAX_VALUE;
        for(final Move move: board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition=board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final  int currentValue = max(moveTransition.getTransitionBoard(),depth-1);
                if(currentValue<=lowestSeenValue){
                    lowestSeenValue=currentValue;
                }
            }
        }
        return lowestSeenValue;

    }

    public int max(final  Board board, final  int depth){

        if(depth==0 ){
            return  this.boardEvalutaor.evaluate(board,depth);
        }

        int highestSeenValue= Integer.MIN_VALUE;
        for(final Move move: board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition=board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final  int currentValue = min(moveTransition.getTransitionBoard(),depth-1);
                if(currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;

    }





}//end
