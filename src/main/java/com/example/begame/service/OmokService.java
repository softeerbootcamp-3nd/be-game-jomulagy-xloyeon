package com.example.begame.service;

import com.example.begame.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class OmokService {
    private final Board board;
    //돌 놓기
    public Boolean updateBoard(int player, int xpos, int ypos){
        if(board.isValidate(xpos,ypos)){
            board.update(player,xpos,ypos);
            return true;
        }
        else{
            return false;
        }
    }

    //금수인지 확인
    public boolean is_Forbidden(int xpos, int ypos, boolean playerA){
        ForbiddenStone forbiddenStone = new ForbiddenStone(board.getBoard() , xpos , ypos , playerA);
        boolean result = forbiddenStone.fiveStone(); //흑의 경우 참고로 삼삼,사사가 이루어져도 5목이달성되면 이긴다. 렌주룰
        if(result) {
            //전달받은 플레이어턴에 끝난거므로.
            return true;
        }

        result = forbiddenStone.samsam();
        if(result) {
            return true;
        }

        result = forbiddenStone.sasa();
        if(result) {
            return true;
        }

        result = forbiddenStone.jangmok();
        if(result) {
            return true;
        }
    }
    //오목이 완성됐는지 확인
//    public isFive()
}
