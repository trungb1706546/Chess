package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;


public class Table {

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private  Board chessBoard;

    private Tile sourceTile; //ô nguồn
    private Tile destinationTile; // ô đích
    private Piece humanMovedPiece;  // người đã di chuyển quân cờ
    private BoardDirection boardDirection;

    private  boolean highlightLegalMoves;


    //kich thuoc JFrame
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600); //kich thuoc của Frame
    private static final Dimension BOARD_PANEL_DEMENSION= new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION= new Dimension(10,10); // kich thuoc o cờ
    private static String linkHinhAnh ="hinhanh/covua/"; // duong dan hinh anh
    //private static String linkHinhAnh ="hinhanh/simple/";

    private  final Color lightTileColor = Color.decode("#e5e6d0");
    private  final Color darkTileColor = Color.decode("#496d98");

    //swing bên trong hàm xây dựng
    public Table(){
        this.gameFrame= new JFrame("Co Vua"); //tao Jfram mới
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar(); //tao ra 1 Jmenu mới
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION); //dặt kích thước cho Jfram
        this.chessBoard= Board.createStanderBoard(); // tao ra ban co tieu chuan

        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves=false;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true); //hiển thị Jfram


    }

    //them ham dung de them filemenu vao menubar
    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add((createFileMenu()));
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File"); //tao ra mot menu ten file

        //tạo menuitem TẢI FILE
        final JMenuItem openPGN = new JMenuItem("Tải File PGN"); //them mot menu item vao menufile ở trên
        //addActionListerner dc gọi khi click vao button hoặc menu item
        openPGN.addActionListener(new ActionListener() {
            //ghi de phuong thuc actionPerformed => in ra
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("open up that pgn file");
            }
        });
        fileMenu.add(openPGN); //them menuitem "openPGN" vao Jmenu

        //tạo menuitem THOÁT
        final JMenuItem exitMenuItem = new JMenuItem("Thoát");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("\nThoat Game");
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);


        return fileMenu;
    }

    private  JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferencese");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection= boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);

        preferencesMenu.addSeparator();

        final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight legal Move",false);

        legalMoveHighlighterCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckbox);
        return preferencesMenu;

    }

    public  enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel>boardTiles);
        abstract  BoardDirection opposite();
    }




    // Bảng điều khiển
    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel(){
            super(new GridLayout(8,8));// tao ra grid vs 8 dong 8 cot
            this.boardTiles = new ArrayList<>();
            for(int i=0; i< BoardUtils.NUM_TILES; i++){
                final TilePanel tilePanel = new TilePanel(this,i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DEMENSION);
            validate();
        }

        public void drawBoard(final Board board){
            removeAll();
            for(final TilePanel tilePanel: boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }

    // Ô điều khiển
    private class TilePanel extends JPanel {
        private final int tileId; //id từng ô

        TilePanel(final BoardPanel boardPanel, final int tileId){
            super(new GridBagLayout());
            this.tileId=tileId; //đánh số id cho ô
            setPreferredSize(TILE_PANEL_DIMENSION);//đặt kích thước cho ô
            assignTileColor(); // đặt màu cho các ô
            assignTilePieceIcon(chessBoard);

            //bắt sự kiện của chuột
            addMouseListener(new MouseListener() {
               //Được triệu hồi khi nút chuột đã được click (được nhấn và nhả ra) trên một thành phần.
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if(isRightMouseButton(e)){
                        System.out.println("chuot phai");
                        sourceTile=null;
                        destinationTile=null;
                        humanMovedPiece=null;
                    }else if(isLeftMouseButton(e)){
                        System.out.println("chuot trai");
                        if(sourceTile==null){
                            sourceTile=chessBoard.getTile(tileId);
                            humanMovedPiece=sourceTile.getPiece();
                            if(humanMovedPiece==null){
                                sourceTile=null;
                            }
                        }else{
                            destinationTile=chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard,sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()){
                                chessBoard = transition.getTransitionBoard();
                            }
                            sourceTile=null;
                            destinationTile=null;
                            humanMovedPiece=null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }

                }

                //Được triệu hồi khi một nút chuột đã được nhấn trên một thành phần.
                @Override
                public void mousePressed(final MouseEvent e) {

                }

                //Được triệu hồi khi một nút chuột đã được nhả ra trên một thành phần.
                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                //Được triệu hồi khi chuột nhập vào một thành phần.
                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                //Được triệu hồi khi chuột thoát ra khỏi một thành phần
                @Override
                public void mouseExited(final  MouseEvent e) {

                }
            });

            validate();
        }

        //
        public void drawTile(final Board board){
            assignTileColor();
            assignTilePieceIcon(board);
            hightLightLegals(board);
            validate();
            repaint();
        }


        //Lấy hình ảnh các quân cờ
        private  void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){// neu 1 ô bị chiếm nghĩa là có quân cờ trên đó
                try {
                    final BufferedImage image = ImageIO.read(new File(linkHinhAnh + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)+
                           board.getTile(this.tileId).getPiece().toString() +".png"));
                  // System.out.print(board.getTile(this.tileId).getPiece().getPieceAlliance().toString()+"\n");
                   // final BufferedImage image = ImageIO.read(new File(linkHinhAnh + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)+ //lay ra W or B
                     //       board.getTile(this.tileId).getPiece().toString() +".gif")); //lay ra Quan Cờ R K Q P ...
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        private  void hightLightLegals(final Board board){
            if(highlightLegalMoves){
                for(final Move move: pieceLegalMoves(board)){
                    if(move.getDestinationCoordinate()==this.tileId){
                        try{
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("hinhanh/misc/green_dot.png")))));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board){
            if(humanMovedPiece !=null && humanMovedPiece.getPieceAlliance()==board.currentPlayer().getAlliance()){
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        // Đặt màu cho tile
        private  void assignTileColor(){
            if(BoardUtils.EIGHTH_RANK[this.tileId] || BoardUtils.SIXTH_RANK[this.tileId] ||
                    BoardUtils.FOURTH_RANK[this.tileId] || BoardUtils.SECOND_RANK[this.tileId]){
                setBackground(this.tileId%2 == 0 ? lightTileColor : darkTileColor);

            }
            else if(BoardUtils.SEVENTH_RANK[this.tileId] || BoardUtils.FIFTH_RANK[this.tileId] ||
                    BoardUtils.THIRD_RANK[this.tileId] || BoardUtils.FIRST_RANK[this.tileId]){
                setBackground(this.tileId%2 != 0 ? lightTileColor : darkTileColor);

            }

        }

    }

}// class chính
