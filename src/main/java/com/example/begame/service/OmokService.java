package com.example.begame.service;

import com.example.begame.domain.Board;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OmokService {

    private final Board board;
    private int x;
    private int y;
    private int currentPlayer; //흑 : 1
    private int otherPlayer; //백 : 2

    public void initBoard(){
        board.clear();
    }
    //돌 놓기
    public boolean updateBoard(int player, int xpos, int ypos){
        if(board.isValidate(xpos,ypos)){
            board.update(player,xpos,ypos);
            return true;
        }

        return false;
    }

    //금수인지 확인
    public boolean is_Forbidden(int xpos, int ypos, int playerA){
        x = xpos;
        y = ypos;

        currentPlayer = playerA; //1: 흑
        otherPlayer = 3-playerA;
        //보드판에 일단기록
        board.update(currentPlayer, y, x);

        if(samsam())
            return true;

        if(sasa())
            return true;


        if(jangmok())
            return true;

        board.update(0, y, x);
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
        System.out.println("allStone = " + allStone);
        System.out.println("stone1 = " + stone1);
        System.out.println("stone2 = " + stone2);
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


//    public isFive()

    //33
    //열린 3이 2개이상이면 쌍삼으로 간주하여 true리턴
    private boolean samsam() {
        int open_sam_count = find1() + find2() + find3() + find4();

        return open_sam_count >=2;
    }

    // ← → 탐색
    // ─ 탐색 : 열린3이 되면 1을 리턴 아니면 0 리턴
    private int find1() {
        int stone1 = 0;
        int stone2 = 0;
        int allStone = 0;
        //열린 3인지 체크하기위한것..
        int blink1 = 1;

        // ←
        int xx = x-1; //달라지는 좌표
        boolean check = false;

        left :
        while(true) {
            //좌표끝도달
            if(xx == -1)
                break left;

            //check를 false로 바꿈으로 두번연속으로 만나는지 확인할수있게.
            if(board.isSame(currentPlayer, y, xx))
            {
                check = false;
                stone1++;
            }

            //상대돌을 만나면 탐색중지
            if(board.isSame(otherPlayer, y, xx))
                break left;

            if(board.isSame(0, y, xx)) {        //빈 공간일 때
                if(check == false) {
                    check = true;
                    if(blink1 == 1)
                        blink1--;
                    else
                        break left; //빈공간을만났으나 빈공간을 두번만나면 끝임
                }else {
                    blink1++;
                    break left;
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

            if(board.isSame(currentPlayer, y, xx))
            {
                check = false;
                stone2++;
            }

            //상대돌을 만나면 탐색중지
            if(board.isSame(otherPlayer, y, xx))
                break right;

            if(board.isSame(0, y, xx)) {
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
        if(allStone != 2) {
            return 0;
        }
        //돌갯수가 3이면 열린 3인지 파악.

        int left = (stone1 + blink1);
        int right = (stone2 + blink2);

        //벽으로 막힌경우 - 열린3이 아님
        if(x - left == 0 || x + right == 18) {
            return 0;
        }else //상대돌로 막힌경우 - 열린3이 아님
            if(board.isSame(otherPlayer, y, x-left-1)|| board.isSame(otherPlayer, y, x+right+1)) {
                return 0;
            }else {
                return 1; //열린3 일때 1 리턴
            }

    }

    // ↖ ↘ 탐색
    private int find2() {
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

            if(board.isSame(p, yy, xx))
            {
                check = false;
                stone1++;
            }

            if(board.isSame(o, yy, xx))
                break leftUp;

            if(board.isSame(0, yy, xx)) {
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

            if(board.isSame(p, yy, xx))
            {
                check = false;
                stone2++;
            }

            if(board.isSame(o, yy, xx))
                break rightDown;

            if(board.isSame(0, yy, xx)) {
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
        if(allStone != 2) {
            return 0;
        }

        int leftUp = (stone1 + blink1);
        int rightDown = (stone2 + blink2);

        if(y - leftUp == 0 || x - leftUp == 0 || y + rightDown == 18 || x + rightDown == 18) {
            return 0;
        }else
        if(board.isSame(otherPlayer, y-leftUp-1, x-leftUp-1)|| board.isSame(otherPlayer, y+rightDown+1, x+rightDown+1)) {
            return 0;
        }else {
            return 1;
        }


    }
    // ↑ ↓ 탐색
    private int find3() {
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

            if(board.isSame(currentPlayer, yy, x))
            {
                check = false;
                stone1++;
            }

            if(board.isSame(otherPlayer, yy, x))
                break up;

            if(board.isSame(0, yy, x)) {
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

            if(board.isSame(currentPlayer, yy, x))
            {
                check = false;
                stone2++;
            }

            if(board.isSame(otherPlayer, yy, yy))
                break down;

            if(board.isSame(0, yy, x)) {
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
        if(allStone != 2) {
            return 0;
        }

        int up = (stone1 + blink1);
        int down = (stone2 + blink2);

        if(y - up == 0 || y + down == 18) {
            return 0;
        }else
        if(board.isSame(otherPlayer, y-up-1, x)|| board.isSame(otherPlayer, y+down+1, x)) {
            return 0;
        }else {
            return 1;
        }
    }
    // ／ 탐색
    // ↙ ↗ 탐색
    private  int find4() {
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

            if(board.isSame(currentPlayer, yy, xx))
            {
                check = false;
                stone1++;
            }

            if(board.isSame(otherPlayer, yy, xx))
                break leftDown;

            if(board.isSame(0, yy, xx)) {
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

            if(board.isSame(currentPlayer, yy, xx))
            {
                check = false;
                stone2++;
            }

            if(board.isSame(otherPlayer, yy, xx))
                break rightUp;

            if(board.isSame(0, yy, xx)) {
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
        if (allStone != 2) {

            return 0;
        }


        int leftDown = (stone1 + blink1);
        int rightUp = (stone2 + blink2);

        if(x - leftDown == 0 || y - rightUp == 0|| y + leftDown == 18 || x + rightUp == 18) {
            return 0;
        }else
        if(board.isSame(otherPlayer, y+leftDown+1, x-leftDown-1)|| board.isSame(otherPlayer, y-rightUp-1, x+rightUp+1)) {
            return 0;
        }else {
            return 1;
        }

    }



    //44
    //열리는건 문제 x 그냥 4의 갯수를 담는변수가 2개이상이면 44
    //똑같이 빈공간은 하나만 허용
    // 4가지 부분으로 로나누어 풀수있음
    public boolean sasa() {
        int fourStone = 0;

        fourStone += fourORjang1(1);
        fourStone += fourORjang2(1);
        fourStone += fourORjang3(1);
        fourStone += fourORjang4(1);


        if(fourStone >= 2)
            return true;
        else
            return false;
    }


    // ← → 탐색
    private int fourORjang1(int trigger) {
        int p = currentPlayer;
        int o = otherPlayer;
        int stone1 = 0;
        int stone2 = 0;
        int allStone = 0;
        //열린4인지는 상관은없음. 다만 코드상 빈공간만을 의미.
        int blink1 = 1;
        if(trigger == 3) // 5목달성조건은 빈공간없이 5개가 이어져야함.
            blink1 = 0;
        // ←  탐색
        int yy = y;
        int xx = x - 1;
        boolean check = false;

        System.out.println("← → 탐색");


        left :
        while(true) {
            if(xx == -1)
                break left;

            System.out.println("("+yy+" ,"+xx+")");
            System.out.println(board.getBoard()[yy][xx]+"->"+board.isSame(p, yy, xx));

            if(board.isSame(p, yy, xx)) {
                check = false;
                stone1++;
            }

            if(board.isSame(o, yy, xx))
                break left;

            if(board.isSame(0, yy, xx)) {
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

            xx--;
        }

        // → 탐색
        xx = x + 1;
        yy = y;
        int blink2 = blink1;
        check = false;
        right :
        while(true) {
            if(xx == 19)
                break right;

            System.out.println("("+yy+" ,"+xx+")");
            System.out.println(board.getBoard()[yy][xx]+"->"+board.isSame(p, yy, xx));

            if(board.isSame(p, yy, xx)) {
                check = false;
                stone2++;
            }

            if(board.isSame(o, yy, xx))
                break right;

            if(board.isSame(0, yy, xx)) {
                if(check == false) {
                    check = true;
                }else {
                    blink2++;
                    break right;
                }

                if(blink2 == 1) {
                    blink2--;
                }else {
                    break right;
                }


            }

            xx++;
        }


        allStone = stone1 + stone2;

        System.out.println("allStone = " + allStone);
        System.out.println("stone1 = " + stone1);
        System.out.println("stone2 = " + stone2);
        //사사찾는 트리거
        if (trigger == 1) {
            if (allStone != 3)
                return 0; //놓은돌제외 3개아니면 4가아니니까.
            else
                return 1; //놓은돌제외 3개면 4임. 닫히고 열린지는 상관없음.
        }

        //장목찾는 트리거
        if (trigger == 2) {
            //현재놓은돌 +1 +5 => 6목이상은 장목. 여기서 놓은돌기준 두방향모두 돌이있어야 장목
            if(allStone >= 5 && stone1 != 0 && stone2 != 0)
                return 1;
            else
                return 0;
        }

        if(trigger == 3) {
            //놓은돌포함 5개의돌이완성되면.
            if(allStone == 4)
                return 1;
            else
                return 0;
        }

        //그럴일을없지만 1 도 2도아니면 0리턴
        return 0;
    }
    // ↖ ↘ 탐색
    private  int fourORjang2(int trigger) {
        int p = currentPlayer;
        int o = otherPlayer;
        int stone1 = 0;
        int stone2 = 0;
        int allStone = 0;
        int blink1 = 1;
        if(trigger == 3)
            blink1 = 0;

        // ↖  탐색
        int yy = y - 1;
        int xx = x - 1;
        boolean check = false;
        leftUp :
        while(true) {
            if(xx == -1 || yy == -1)
                break leftUp;

            if(board.isSame(p, yy, xx)) {
                check = false;
                stone1++;
            }

            if(board.isSame(o, yy, xx))
                break leftUp;

            if(board.isSame(0, yy, xx)) {
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

        // ↘  탐색
        yy = y + 1;
        xx = x + 1;
        check = false;
        int blink2 = blink1;
        leftDown :
        while(true) {
            if(xx == 19 || yy == 19)
                break leftDown;

            if(board.isSame(p, yy, xx)) {
                check = false;
                stone2++;
            }

            if(board.isSame(o, yy, xx))
                break leftDown;

            if(board.isSame(0, yy, xx)) {
                if(check == false) {
                    check = true;
                }else {
                    blink2++;
                    break leftDown;
                }

                if(blink2 == 1) {
                    blink2--;
                }else {
                    break leftDown;
                }


            }

            xx++;
            yy++;
        }


        allStone = stone1 + stone2;

        if (trigger == 1) {
            if (allStone != 3)
                return 0;
            else
                return 1;
        }

        if (trigger == 2) {
            if(allStone >= 5 && stone1 != 0 && stone2 != 0)
                return 1;
            else
                return 0;
        }

        if(trigger == 3) {
            if(allStone == 4)
                return 1;
            else
                return 0;
        }

        return 0;
    }
    // ↑ ↓ 탐색
    private int fourORjang3(int trigger) {
        int p = currentPlayer;
        int o = otherPlayer;
        int stone1 = 0;
        int stone2 = 0;
        int allStone = 0;
        int blink1 = 1;
        if(trigger == 3)
            blink1 = 0;

        System.out.println("↑ ↓ 탐색");

        // ↑  탐색
        int yy = y - 1;
        int xx = x;
        boolean check = false;
        up :
        while(true) {
            if(yy == -1)
                break up;

            System.out.println("("+yy+" ,"+xx+")");
            System.out.println(board.getBoard()[yy][xx]+"->"+board.isSame(p, yy, xx));
            if(board.isSame(p, yy, xx)) {
                check = false;
                stone1++;
            }

            if(board.isSame(o, yy, xx))
                break up;

            if(board.isSame(0, yy, xx)) {
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

        // ↓  탐색
        yy = y + 1;
        xx = x;
        check = false;
        int blink2 = blink1;
        down :
        while(true) {
            if(yy == 19)
                break down;

            System.out.println("("+yy+" ,"+xx+")");
            System.out.println(board.getBoard()[yy][xx]+"->"+board.isSame(p, yy, xx));

            if(board.isSame(p, yy, xx)) {
                check = false;
                stone2++;
            }

            if(board.isSame(o, yy, xx))
                break down;

            if(board.isSame(0, yy, xx)) {
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
        System.out.println("allStone = " + allStone);
        System.out.println("stone1 = " + stone1);
        System.out.println("stone2 = " + stone2);
        if (trigger == 1) {
            if (allStone != 3)
                return 0;
            else
                return 1;
        }

        if (trigger == 2) {
            if(allStone >= 5 && stone1 != 0 && stone2 != 0)
                return 1;
            else
                return 0;
        }

        if(trigger == 3) {
            if(allStone == 4)
                return 1;
            else
                return 0;
        }

        return 0;
    }
    // ↗ ↙ 탐색
    private  int fourORjang4(int trigger) {
        int p = currentPlayer;
        int o = otherPlayer;
        int stone1 = 0;
        int stone2 = 0;
        int allStone = 0;
        int blink1 = 1;
        if(trigger == 3)
            blink1 = 0;

        // ↗ 탐색
        int yy = y - 1;
        int xx = x + 1;
        boolean check = false;
        rightup :
        while(true) {
            if(xx == 19 || yy == -1)
                break rightup;

            if(board.isSame(p, yy, xx)) {
                check = false;
                stone1++;
            }

            if(board.isSame(o, yy, xx))
                break rightup;

            if(board.isSame(0, yy, xx)) {
                if(check == false) {
                    check = true;
                }else {
                    blink1++;
                    break rightup;
                }

                if(blink1 == 1) {
                    blink1--;
                }else {
                    break rightup;
                }


            }

            xx++;
            yy--;
        }

        // ↙ 탐색
        yy = y + 1;
        xx = x - 1;
        check = false;
        int blink2 = blink1;
        leftdown :
        while(true) {
            if(xx == -1 || yy == 19)
                break leftdown;

            if(board.isSame(p, yy, xx)) {
                check = false;
                stone2++;
            }

            if(board.isSame(o, yy, xx))
                break leftdown;

            if(board.isSame(0, yy, xx)) {
                if(check == false) {
                    check = true;
                }else {
                    blink2++;
                    break leftdown;
                }

                if(blink2 == 1) {
                    blink2--;
                }else {
                    break leftdown;
                }


            }

            xx--;
            yy++;
        }



        allStone = stone1 + stone2;

        if (trigger == 1) {
            if (allStone != 3)
                return 0;
            else
                return 1;
        }

        if (trigger == 2) {
            if(allStone >= 5 && stone1 != 0 && stone2 != 0)
                return 1;
            else
                return 0;
        }

        if(trigger == 3) {
            if(allStone == 4)
                return 1;
            else
                return 0;
        }

        return 0;
    }


    //장목
    public boolean jangmok() {
        int result = 0;

        result += fourORjang1(2);
        result += fourORjang2(2);
        result += fourORjang3(2);
        result += fourORjang4(2);

        if(result >= 1)  //하나라도 장목수가있으면
            return true;

        return false;
    }

    public boolean fiveStone(int currentPlayer,int xpos, int ypos) {
        int result = 0;
        setMethods(currentPlayer,3-currentPlayer,xpos,ypos);

        for(int i=0;i<19;i++){
            for(int j = 0; j<19;j++){
                System.out.print(board.getBoard()[j][i]);
            }
            System.out.println();
        }

        System.out.println("player "+currentPlayer+" turn");


        result += fourORjang1(3);
        result += fourORjang2(3);
        result += fourORjang3(3);
        result += fourORjang4(3);

        if(result >= 1) //하나라도 오목이 달성되면.
            return true;


        return false;
    }

    private void setMethods(int currentPlayer, int otherPlayer,int x,int y){
        this.currentPlayer = currentPlayer;
        this.otherPlayer = otherPlayer;
        this.x = x;
        this.y = y;
    }




//    static boolean main(int[][] board , int x , int y , boolean playerA) {
//        //생성자호출
//        //초기화작업
//        new ForbiddenStone(board , x , y , playerA);
//        boolean result = fiveStone(); //흑의 경우 참고로 삼삼,사사가 이루어져도 5목이달성되면 이긴다. 렌주룰
//        if(result) {
//            //전달받은 플레이어턴에 끝난거므로.
//            return true;
//        }
//
//        result = samsam();
//        if(result) {
//            return true;
//        }
//
//        result = sasa();
//        if(result) {
//            return true;
//        }
//
//        result = jangmok();
//        if(result) {
//            return true;
//        }
//
//
//        return false;
//    }

}
