package com.example.begame.service;

public class ForbiddenStone {
    //board[y][x]..
    private static int[][] board;
    private static int x;
    private static int y;
    private static int currentPlayer; //현재 돌. 1:흑 2:백
    //금수제한은 흑에게만있으므로 사실상 currentPlayer = 1.이다.
    private static int otherPlayer; //상대방돌.                       //otherPlayer = 2;

    //33
    //열린 3이 2개이상이면 쌍삼으로 간주하여 true리턴
    public static boolean samsam() {
        int open_sam_count = 0;
        open_sam_count += find1();
        open_sam_count += find2();
        open_sam_count += find3();
        open_sam_count += find4();

        if(open_sam_count >= 2)
            return true;
        else
            return false;
    }

    // ← → 탐색
    // ─ 탐색 : 열린3이 되면 1을 리턴 아니면 0 리턴
    private static int find1() {
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
            if(board[y][xx] == p)
            {
                check = false;
                stone1++;
            }

            //상대돌을 만나면 탐색중지
            if(board[y][xx] == o)
                break left;

            if(board[y][xx] == 0) {
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
            if(xx == 16)
                break right;

            if(board[y][xx] == p)
            {
                check = false;
                stone2++;
            }

            //상대돌을 만나면 탐색중지
            if(board[y][xx] == o)
                break right;

            if(board[y][xx] == 0) {
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
        if(x - left == 0 || x + right == 15) {
            return 0;
        }else //상대돌로 막힌경우 - 열린3이 아님
            if(board[y][x - left - 1] == o || board[y][x + right + 1] == o) {
                return 0;
            }else {
                return 1; //열린3 일때 1 리턴
            }

    }
    // ↖ ↘ 탐색
    private static int find2() {
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

            if(board[yy][xx] == p)
            {
                check = false;
                stone1++;
            }

            if(board[yy][xx] == o)
                break leftUp;

            if(board[yy][xx] == 0) {
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
            if(xx == 16 || yy == 16)
                break rightDown;

            if(board[yy][xx] == p)
            {
                check = false;
                stone2++;
            }

            if(board[yy][xx] == o)
                break rightDown;

            if(board[yy][xx] == 0) {
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

        if(y - leftUp == 0 || x - leftUp == 0 || y + rightDown == 15 || x + rightDown == 15) {
            return 0;
        }else
        if(board[y - leftUp -1][x - leftUp - 1] == o || board[y + rightDown + 1][x + rightDown + 1] == o) {
            return 0;
        }else {
            return 1;
        }


    }
    // ↑ ↓ 탐색
    private static int find3() {
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

            if(board[yy][x] == p)
            {
                check = false;
                stone1++;
            }

            if(board[yy][x] == o)
                break up;

            if(board[yy][x] == 0) {
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
            if(yy == 16)
                break down;

            if(board[yy][x] == p)
            {
                check = false;
                stone2++;
            }

            if(board[yy][yy] == o)
                break down;

            if(board[yy][x] == 0) {
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

        if(y - up == 0 || y + down == 15) {
            return 0;
        }else
        if(board[y - up - 1][x] == o || board[y + down + 1][x] == o) {
            return 0;
        }else {
            return 1;
        }
    }
    // ／ 탐색
    // ↙ ↗ 탐색
    private static int find4() {
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
            if(xx == -1 || yy == 16)
                break leftDown;

            if(board[yy][xx] == p)
            {
                check = false;
                stone1++;
            }

            if(board[yy][xx] == o)
                break leftDown;

            if(board[yy][xx] == 0) {
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
            if(xx == 16 || yy == -1)
                break rightUp;

            if(board[yy][xx] == p)
            {
                check = false;
                stone2++;
            }

            if(board[yy][xx] == o)
                break rightUp;

            if(board[yy][xx] == 0) {
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

        if(x - leftDown == 0 || y - rightUp == 0|| y + leftDown == 15 || x + rightUp == 15) {
            return 0;
        }else
        if(board[y + leftDown + 1][x - leftDown - 1] == o || board[y - rightUp - 1][x + rightUp +1] == o) {
            return 0;
        }else {
            return 1;
        }

    }



    //44
    //열리는건 문제 x 그냥 4의 갯수를 담는변수가 2개이상이면 44
    //똑같이 빈공간은 하나만 허용
    // 4가지 부분으로 로나누어 풀수있음
    public static boolean sasa() {
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
    private static int fourORjang1(int trigger) {
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
        left :
        while(true) {
            if(xx == -1)
                break left;

            if(board[yy][xx] == p) {
                check = false;
                stone1++;
            }

            if(board[yy][xx] == o)
                break left;

            if(board[yy][xx] == 0) {
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
            if(xx == 16)
                break right;

            if(board[yy][xx] == p) {
                check = false;
                stone2++;
            }

            if(board[yy][xx] == o)
                break right;

            if(board[yy][xx] == 0) {
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
    private static int fourORjang2(int trigger) {
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

            if(board[yy][xx] == p) {
                check = false;
                stone1++;
            }

            if(board[yy][xx] == o)
                break leftUp;

            if(board[yy][xx] == 0) {
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
            if(xx == 16 || yy == 16)
                break leftDown;

            if(board[yy][xx] == p) {
                check = false;
                stone2++;
            }

            if(board[yy][xx] == o)
                break leftDown;

            if(board[yy][xx] == 0) {
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
    private static int fourORjang3(int trigger) {
        int p = currentPlayer;
        int o = otherPlayer;
        int stone1 = 0;
        int stone2 = 0;
        int allStone = 0;
        int blink1 = 1;
        if(trigger == 3)
            blink1 = 0;

        // ↑  탐색
        int yy = y - 1;
        int xx = x;
        boolean check = false;
        up :
        while(true) {
            if(yy == -1)
                break up;

            if(board[yy][xx] == p) {
                check = false;
                stone1++;
            }

            if(board[yy][xx] == o)
                break up;

            if(board[yy][xx] == 0) {
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
            if(yy == 16)
                break down;

            if(board[yy][xx] == p) {
                check = false;
                stone2++;
            }

            if(board[yy][xx] == o)
                break down;

            if(board[yy][xx] == 0) {
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
    private static int fourORjang4(int trigger) {
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
            if(xx == 16 || yy == -1)
                break rightup;

            if(board[yy][xx] == p) {
                check = false;
                stone1++;
            }

            if(board[yy][xx] == o)
                break rightup;

            if(board[yy][xx] == 0) {
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
            if(xx == -1 || yy == 16)
                break leftdown;

            if(board[yy][xx] == p) {
                check = false;
                stone2++;
            }

            if(board[yy][xx] == o)
                break leftdown;

            if(board[yy][xx] == 0) {
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
    public static boolean jangmok() {
        int result = 0;

        result += fourORjang1(2);
        result += fourORjang2(2);
        result += fourORjang3(2);
        result += fourORjang4(2);

        if(result >= 1)  //하나라도 장목수가있으면
            return true;

        return false;
    }

    public static boolean fiveStone() {
        int result = 0;

        result += fourORjang1(3);
        result += fourORjang2(3);
        result += fourORjang3(3);
        result += fourORjang4(3);

        if(result >= 1) //하나라도 오목이 달성되면.
            return true;


        return false;
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

    //playerA는 흑이고 1의값을가짐
    //false면 playerB이고 백이고 2의값을가짐.
    public ForbiddenStone(int[][] board , int x , int y , boolean playerA) {
        this.board = board;
        this.x = x;
        this.y = y;

        if(playerA) {
            currentPlayer = 1; //1: 흑
            otherPlayer = 2;
        }else {
            currentPlayer = 2; //2: 백
            otherPlayer = 1;
        }
        //보드판에 일단기록
        this.board[y][x] = currentPlayer;
    }

}