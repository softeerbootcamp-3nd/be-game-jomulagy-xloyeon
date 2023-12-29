package com.example.begame.domain;


import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Setter
@Getter
@Component
public class Board {
    private int[][] board;

    private int emptyNum;

    public Board(){
        board = new int[19][19];
        emptyNum = 19*19;
    }

    //초기화 기능
    public void clear(){
        Arrays.stream(board).map(b -> {
            Arrays.fill(b, 0);
            return b;
        });

        emptyNum = 19*19;
    }

    //돌 놓기
    public void update(int k, int x, int y){
        board[x][y] = k;
    }

    //빈 공간 확인
    public boolean isFull(){
        return emptyNum == 0;
    }

    //놓으려는 곳에 돌이 있는지 확인
    public boolean isValidate(int x, int y){
        if(board[x][y] != 0)
            return false;
        return true;
    }

    public boolean isSame(int k, int x, int y){
        return board[y][x] == k;
    }
}
