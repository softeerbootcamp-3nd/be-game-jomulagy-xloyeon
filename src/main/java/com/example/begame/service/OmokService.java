package com.example.begame.service;

import com.example.begame.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return false;
    }

    // ← → 탐색
    // ─ 탐색 : 열린3이 되면 1을 리턴 아니면 0 리턴
    private boolean find1(int currentPlayer, int otherPlayer, int x, int y) {
        int p = currentPlayer;
        int o = otherPlayer;
        int stone1 = 0;
        int stone2 = 0;
        int allStone = 0;
        //열린 3인지 체크하기위한것..
        int blink1 = 1;

        //blink2 는 blink1 과 같음 중간에서넣어줄거임.
        //int blink2 = blink1;


        // ←
        int xx = x-1; //달라지는 좌표
        boolean check = false;
        left :
        while(true) {

            //좌표끝도달
            if(xx == -1)
                break left;

            //check를 false로 바꿈으로 두번연속으로 만나는지 확인할수있게.
            if(board.getBoard()[y][xx] == p)
            {
                check = false;
                stone1++;
            }

            //상대돌을 만나면 탐색중지
            if(board.getBoard()[y][xx] == o)
                break left;

            if(board.getBoard()[y][xx] == 0) {
                //처음 빈공간을만나 check가 true가 됬는데
                //연달아 빈공간을만나면 탐색중지
                //두번연속으로 빈공간만날시 blink카운트를 되돌림.
                if(check == false) {
                    check = true;
                }else {
                    blink1++;
                    break left;
                }

                if(blink1 == 1) {
                    blink1--;
                }else {
                    break left; //빈공간을만났으나 빈공간을 두번만나면 끝임
                }
            }
            //계속탐색
            xx--;
        }


        // →
        xx = x+1; //달라지는 좌표
        int blink2 = blink1; //blink1남은거만큼 blink2,
        if(blink1 == 1) //빈공간을 만나지않은경우 없었음을기록
            blink1 = 0;
        check = false;
        right :
        while(true) {
            //좌표끝도달
            if(xx == 19)
                break right;

            if(board.getBoard()[y][xx] == p)
            {
                check = false;
                stone2++;
            }

            //상대돌을 만나면 탐색중지
            if(board.getBoard()[y][xx] == o)
                break right;

            if(board.getBoard()[y][xx] == 0) {
                //두번연속으로 빈공간만날시 blink카운트를 되돌림.
                if(check == false) {
                    check = true;
                }else {
                    blink2++;
                    break right;
                }

                if(blink2 == 1) {
                    blink2--;
                }else {
                    break right; //빈공간을만났으나 빈공간을 두번만나면 끝임
                }
            }
            xx++;
        }

        allStone = stone1 + stone2;
        //삼삼이므로 돌갯수가 2 + 1(현재돌)이아니면 0리턴
        //이부분이 43을 허용하게해줌. 33만 찾게됨
        if(allStone != 4) {
            return false;
        }
        //돌갯수가 3이면 열린 3인지 파악.

        int left = (stone1 + blink1);
        int right = (stone2 + blink2);

        //벽으로 막힌경우 - 열린3이 아님
        if(x - left == 0 || x + right == 18) {
            return false;
        }else //상대돌로 막힌경우 - 열린3이 아님
            if(board.getBoard()[y][x - left - 1] == o || board.getBoard()[y][x + right + 1] == o) {
                return false;
            }else {
                return true; //열린3 일때 1 리턴
            }

    }

    private boolean find2(int x, int y, int currentPlayer, int otherPlayer) {
        int p = currentPlayer;
        int o = otherPlayer;
        int stone1 = 0;
        int stone2 = 0;
        int allStone = 0;
        int blink1 = 1;


        // ↖
        int xx = x-1;
        int yy = y-1;
        boolean check = false;
        leftUp :
        while(true) {
            if(xx == -1 || yy == -1)
                break leftUp;

            if(board.getBoard()[yy][xx] == p)
            {
                check = false;
                stone1++;
            }

            if(board.getBoard()[yy][xx] == o)
                break leftUp;

            if(board.getBoard()[yy][xx] == 0) {
                if(check == false) {
                    check = true;
                }else {
                    blink1++;
                    break leftUp;
                }

                if(blink1 == 1) {
                    blink1--;
                }else {
                    break leftUp;
                }
            }
            xx--;
            yy--;
        }


        // ↘
        int blink2 = blink1;
        if(blink1 == 1)
            blink1 = 0;
        xx = x+1;
        yy = y+1;
        check = false;
        rightDown:
        while(true) {
            if(xx == 19 || yy == 19)
                break rightDown;

            if(board.getBoard()[yy][xx] == p)
            {
                check = false;
                stone2++;
            }

            if(board.getBoard()[yy][xx] == o)
                break rightDown;

            if(board.getBoard()[yy][xx] == 0) {
                if(check == false) {
                    check = true;
                }else {
                    blink2++;
                    break rightDown;
                }

                if(blink2 == 1) {
                    blink2--;
                }else {
                    break rightDown;
                }
            }

            xx++;
            yy++;
        }

        allStone = stone1 + stone2;
        if(allStone != 4) {
            return false;
        }

        int leftUp = (stone1 + blink1);
        int rightDown = (stone2 + blink2);

        if(y - leftUp == 0 || x - leftUp == 0 || y + rightDown == 18 || x + rightDown == 18) {
            return false;
        }else
        if(board.getBoard()[y - leftUp -1][x - leftUp - 1] == o || board.getBoard()[y + rightDown + 1][x + rightDown + 1] == o) {
            return false;
        }else {
            return true;
        }


    }

    private boolean find3(int otherPlayer, int currentPlayer, int y, int x) {
        int p = currentPlayer;
        int o = otherPlayer;
        int stone1 = 0;
        int stone2 = 0;
        int allStone = 0;
        int blink1 = 1;

        // ↑
        int yy = y-1;
        boolean check = false;
        up :
        while(true) {
            if(yy == -1)
                break up;

            if(board.getBoard()[yy][x] == p)
            {
                check = false;
                stone1++;
            }

            if(board.getBoard()[yy][x] == o)
                break up;

            if(board.getBoard()[yy][x] == 0) {
                if(check == false) {
                    check = true;
                }else {
                    blink1++;
                    break up;
                }

                if(blink1 == 1) {
                    blink1--;
                }else {
                    break up;
                }
            }
            yy--;
        }

        // ↓
        int blink2 = blink1;
        if(blink1 == 1)
            blink1 = 0;
        yy = y + 1;
        check = false;
        down :
        while(true) {
            if(yy == 19)
                break down;

            if(board.getBoard()[yy][x] == p)
            {
                check = false;
                stone2++;
            }

            if(board.getBoard()[yy][yy] == o)
                break down;

            if(board.getBoard()[yy][x] == 0) {
                if(check == false) {
                    check = true;
                }else {
                    blink2++;
                    break down;
                }

                if(blink2 == 1) {
                    blink2--;
                }else {
                    break down;
                }
            }

            yy++;
        }

        allStone = stone1 + stone2;
        if(allStone != 4) {
            return false;
        }

        int up = (stone1 + blink1);
        int down = (stone2 + blink2);

        if(y - up == 0 || y + down == 18) {
            return false;
        }else
        if(board.getBoard()[y - up - 1][x] == o || board.getBoard()[y + down + 1][x] == o) {
            return false;
        }else {
            return true;
        }
    }
    // ／ 탐색
    // ↙ ↗ 탐색
    private boolean find4(int currentPlayer, int otherPlayer, int x, int y) {
        int p = currentPlayer;
        int o = otherPlayer;
        int stone1 = 0;
        int stone2 = 0;
        int allStone = 0;
        int blink1 = 1;

        // ↙
        int xx = x-1;
        int yy = y+1;
        boolean check = false;
        leftDown :
        while(true) {
            if(xx == -1 || yy == 19)
                break leftDown;

            if(board.getBoard()[yy][xx] == p)
            {
                check = false;
                stone1++;
            }

            if(board.getBoard()[yy][xx] == o)
                break leftDown;

            if(board.getBoard()[yy][xx] == 0) {
                if(check == false) {
                    check = true;
                }else {
                    blink1++;
                    break leftDown;
                }

                if(blink1 == 1) {
                    blink1--;
                }else {
                    break leftDown;
                }
            }
            xx--;
            yy++;
        }

        // ↗
        int blink2 = blink1;
        if(blink1 == 1)
            blink1 = 0;
        xx = x + 1;
        yy = y - 1;
        check = false;
        rightUp :
        while(true) {
            if(xx == 19 || yy == -1)
                break rightUp;

            if(board.getBoard()[yy][xx] == p)
            {
                check = false;
                stone2++;
            }

            if(board.getBoard()[yy][xx] == o)
                break rightUp;

            if(board.getBoard()[yy][xx] == 0) {
                if(check == false) {
                    check = true;
                }else {
                    blink2++;
                    break rightUp;
                }

                if(blink2 == 1) {
                    blink2--;
                }else {
                    break rightUp;
                }
            }
            xx++;
            yy--;
        }

        allStone = stone1 + stone2;
        if (allStone != 4) {

            return false;
        }


        int leftDown = (stone1 + blink1);
        int rightUp = (stone2 + blink2);

        if(x - leftDown == 0 || y - rightUp == 0|| y + leftDown == 18 || x + rightUp == 18) {
            return false;
        }else
        if(board.getBoard()[y + leftDown + 1][x - leftDown - 1] == o || board.getBoard()[y - rightUp - 1][x + rightUp +1] == o) {
            return false;
        }else {
            return true;
        }

    }

    //오목이 완성됐는지 확인
    public boolean isFive(int player, int other, int xpos, int ypos){
        System.out.println(find1(player,other,xpos,ypos));
        System.out.println(find2(xpos,ypos,player,other));
        System.out.println(find3(other,player,ypos,xpos));
        System.out.println(find4(player,other,xpos,ypos));
        return find1(player,other,xpos,ypos) || find2(xpos,ypos,player,other) || find3(other,player,ypos,xpos) || find4(player,other,xpos,ypos);


    }
}
