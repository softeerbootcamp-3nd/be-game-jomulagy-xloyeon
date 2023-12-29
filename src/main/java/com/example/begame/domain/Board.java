package com.example.begame.domain;

public class Board {
    private int[][] board;

    private int emptyNum;

    public Board(){
        board = new int[19][19];
        emptyNum = 19*19;
    }

    //초기화 기능
    public void clear(){

    }

    //돌 놓기
    public void update(int k, int x, int y){

    }

    //빈 공간 확인
    public boolean isFull(){
        return false;
    }
}
