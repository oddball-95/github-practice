import java.awt.*;
import java.util.*;

public class Player_10 extends Player {
    public Player_10(int myNum, int SIZE_OF_BOARD, int[][] map) {
        super(myNum, SIZE_OF_BOARD, map);
    }

    static final int DEPTH = 8;//상태 트리 깊이
    static int[] temp;
    static int[] temp2X2 = new int[4]; //배열 복사를 위한 임시 배열
    static int[] temp4X4 = new int[16];


    //평가 모델
    static int eval2X2[][] = {{0, 1, 1, 1}, {1, 0, 1, 1}, {1, 1, 1, 0},
            {1, 1, 0, 1}};
    static int eval4X4[][] = {{1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 1, 0, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 0, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, 1, 1, 0, 1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, 1, 0, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, 0, 1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 0, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 0, 1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, 1, 0, 1, 1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 0},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 0, 1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 0, 1, 1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1},
            {0, -1, -1, -1, -1, 1, -1, -1, -1, -1, 1, -1, -1, -1, -1, 1},
            {1, -1, -1, -1, -1, 0, -1, -1, -1, -1, 1, -1, -1, -1, -1, 1},
            {1, -1, -1, -1, -1, 1, -1, -1, -1, -1, 0, -1, -1, -1, -1, 1},
            {1, -1, -1, -1, -1, 1, -1, -1, -1, -1, 1, -1, -1, -1, -1, 0},
            {-1, -1, -1, 0, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, -1},
            {-1, -1, -1, 1, -1, -1, 0, -1, -1, 1, -1, -1, 1, -1, -1, -1},
            {-1, -1, -1, 1, -1, -1, 1, -1, -1, 0, -1, -1, 1, -1, -1, -1},
            {-1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 0, -1, -1, -1},
            {0, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1},
            {1, -1, -1, -1, 0, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1},
            {1, -1, -1, -1, 1, -1, -1, -1, 0, -1, -1, -1, 1, -1, -1, -1},
            {1, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 0, -1, -1, -1},
            {-1, 0, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1},
            {-1, 1, -1, -1, -1, 0, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1},
            {-1, 1, -1, -1, -1, 1, -1, -1, -1, 0, -1, -1, -1, 1, -1, -1},
            {-1, 1, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 0, -1, -1},
            {-1, -1, 0, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 1, -1},
            {-1, -1, 1, -1, -1, -1, 0, -1, -1, -1, 1, -1, -1, -1, 1, -1},
            {-1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 0, -1, -1, -1, 1, -1},
            {-1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 0, -1},
            {-1, -1, -1, 0, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 1},
            {-1, -1, -1, 1, -1, -1, -1, 0, -1, -1, -1, 1, -1, -1, -1, 1},
            {-1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 0, -1, -1, -1, 1},
            {-1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 1, -1, -1, -1, 0}};


    public Point nextPosition(Point lastPosition) {
        return AlphaBetaPlunge(DEPTH, map, lastPosition, myNum);//알파베타 가지치기를 통해 다음 수 결정
    }

    //놓을 수 있는 위치 반환, PixelEnv의 함수를 가져옴.
    public ArrayList<Point> get_available_positions(Point lastPos, int[][] map) {
        ArrayList<Point> positions = new ArrayList<>();

        int last_X = (int) lastPos.getX();
        int last_Y = (int) lastPos.getY();

        for(int i = 0; i < SIZE_OF_BOARD; i++) {
            if (map[i][last_Y] == 0)
                positions.add(new Point(i, last_Y));

            if (map[last_X][i] == 0)
                positions.add(new Point(last_X, i));
        }

        return positions;
    }

    //알파베타 가지치기 함수
    private Point AlphaBetaPlunge(int depth, int[][] map, Point lastPosition, int player) {
        int max = Integer.MIN_VALUE;//최소 정수 값을 할당해 탐색이 중지 되지 않도록 함.

        Point nextPosition = new Point(0, 0);
        ArrayList<Point> available_position = get_available_positions(lastPosition, map);

        for(Point i : available_position) {
            int[][] nextMap = deepCopy(map);

            nextMap[(int) i.getX()][(int) i.getY()] = player;//다음 경우의 수 생성

            int score = MinValue(depth, nextMap, i, player, -100, 100);
            //score가 max 보다 크면 max값을 바꾼다.
            if (score > max) {
                max = score;
                nextPosition.setLocation(i.getX(), i.getY());
            }
        }
        return nextPosition;
    }

    //민맥스 알고리즘에서 맥스값을 구하는 함수
    private int MaxValue(int depth, int[][] map, Point lastPosition, int player, int alpha, int beta) {
        if (isVictory(lastPosition, player, map)) return Integer.MIN_VALUE + 1;
        if (depth == 0 && selectModel(lastPosition, player, map) == 2) return eval(map, myNum);
        if (depth == 0 && selectModel(lastPosition, player, map) == 1) return evalSquare(map, myNum);//4목 모양 있으면 사각형모양으로 평가
        if (depth == 0 && selectModel(lastPosition, player, map) == 0) return evalLine(map, myNum);//사각형 모양 있으면 4목 모양으로 평가
        if (depth == 0) return eval(map, myNum);
        int max = -100;

        ArrayList<Point> available_position = get_available_positions(lastPosition, map);

        // 트리 노드 생성
        for(Point i : available_position) {
            int[][] myMap = deepCopy(map);
            myMap[(int) i.getX()][(int) i.getY()] = oppoTurn(player);
            int score = MinValue(depth - 1, myMap, i, oppoTurn(player), alpha, beta);//재귀적으로 함수 호출

            if (score > max) max = score;//score가 max보다 크면 score값으로 max 값 갱신
            if (max >= beta) return max;//max가 베타보다 크면 유망하지 않으므로 가지치기
            if (max > alpha) alpha = max;//max가 알파보다 크면 알파 값 갱신
        }
        return max;
    }

    //평가 방법 결정 함수
    int selectModel(Point lastPosition, int player, int[][] map) {
        boolean isSquare = isSquare(lastPosition, player, map);
        boolean isFour = isFour(lastPosition, player, map);

        if (isSquare && !isFour) return 0;
        if (!isSquare && isFour) return 1;
        return 2;
    }


    //민맥스 알고리즘에서 민값을 반환하는 함수
    private int MinValue(int depth, int[][] map, Point lastPosition, int player, int alpha, int beta) {
        if (isVictory(lastPosition, player, map)) return Integer.MAX_VALUE - 1;
        if (depth == 0 && selectModel(lastPosition, player, map) == 2) return eval(map, myNum);
        if (depth == 0 && selectModel(lastPosition, player, map) == 1) return evalSquare(map, myNum);//사목 모양 있으면 사각형 모양으로 평가
        if (depth == 0 && selectModel(lastPosition, player, map) == 0) return evalLine(map, myNum);//사각형 모양 있으면 사목 모양으로 평가
        else if (depth == 0) return eval(map, myNum);
        int min = Integer.MAX_VALUE - 1;

        ArrayList<Point> available_position = get_available_positions(lastPosition, map);
        for(Point i : available_position) {
            int[][] nextMap = deepCopy(map); // myMap에 현재 맵 복사
            nextMap[(int) i.getX()][(int) i.getY()] = oppoTurn(player);
            int score = MaxValue(depth - 1, nextMap, i, oppoTurn(player), alpha, beta);//재귀적으로 함수 호출

            if (score < min) min = score;//min이 score보다 크면 min 값 갱신
            if (min <= alpha) return min;//min이 알파보다 작으면 유망하지 않으므로 가지치기
            if (min < beta) beta = min;//min이 beta보다 작으면 beta값 갱신
        }
        return min;
    }

    //사목 모양이 만들어 졌을 경우 사용하는 평가함수
    int evalSquare(int[][] map, int player) {
        int myScore = 0;    //내 점수
        int oppoScore = 0;  //상대 점수
        int score = 0;
        int temp[] = null;

        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                temp = copyModel(4, i, j, map);
                oppoScore +=modelCompare(oppoTurn(player), temp, eval4X4);//사각형 모양은 가지고 있으므로 상대방만 평가
            }
        }

        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                temp = copyModel(2, i, j, map);
                myScore += modelCompare(player, temp, eval2X2);
                oppoScore += modelCompare(oppoTurn(player), temp, eval2X2);
            }
        }
        score = myScore - oppoScore;//내 점수 에서 상대 점수를 빼서 노드의 최종 점수를 결정한다.
        return score;
    }

    //사각형 모양이 만들어졌을 때 사용하는 평가함수
    int evalLine(int[][] map, int player) {
        int myScore = 0;
        int oppoScore = 0;
        int score = 0;
        int temp[] = null;
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                temp = copyModel(2, i, j, map);
                oppoScore += modelCompare(oppoTurn(player), temp, eval2X2);//사각형 모델은 가지고 있으므로 4목 모양은 상대방만 평가
            }
        }

        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                temp = copyModel(4, i, j, map);   //3*3에서 이길수 있는 모형들을 copyPattern이라는 함수를 사용해 1차원 배열인 temp에 저장한다.
                myScore += modelCompare(player, temp, eval4X4);
                oppoScore += modelCompare(oppoTurn(player), temp, eval4X4);
            }
        }
        score = myScore - oppoScore;//내 점수에서 상대방 점수를 빼서 평가
        return score;
    }

    //사각형, 사목모양 모두 없을 때 사용하는 평가함수
    private int eval(int[][] map, int player) {
        int myCount = 0;    //내가 가지고 있는 모형의 갯수.
        int oppoCount = 0;  //상대가 가지고 있는 모형의 갯수.
        int score = 0;
        int temp[] = null;
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                temp = copyModel(2, i, j, map);
                myCount += modelCompare(player, temp, eval2X2);
                oppoCount += modelCompare(oppoTurn(player), temp, eval2X2); // 같은 과정을 oppoCount에도 적용한다.
            }
        }
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                temp = copyModel(4, i, j, map);   //3*3에서 이길수 있는 모형들을 copyPattern이라는 함수를 사용해 1차원 배열인 temp에 저장한다.
                myCount += modelCompare(player, temp, eval4X4);
                oppoCount += modelCompare(oppoTurn(player), temp, eval4X4); //현재 맵을 3*3로 자르는 onePattern3X3 배열과 1차원 배열인 temp의 비교를 통해서 맵 전체에서 3*3 상황에서의 이길 수 있는 모형의 개수를 myCount 인덱스에 저장해준다.
            }
        }
        score = myCount - oppoCount;   // myCount에서 oppoCount를 빼서 result에 저장한다. 이 과정을 통해서 뎁스가 0 이 되었을 때, 누가 더 유리한 상황인지를 알 수 있다.
        return score;
    }

    //2차원 배열을 1차원으로 복사하는 함수
    private int[] copyModel(int size, int row, int col, int[][] map) {
        if (size == 2) temp = temp2X2;
        else temp = temp4X4;
        int cut = 0; //cut이라는 인덱스를 생성한다. temp는 1차원배열, map은 2차원 배열이기 때문에, map의 값을 temp에 받기 위한 인덱스로 사용한다.
        for(int i = row; i < row + size; i++) {
            for(int j = col; j < col + size; j++) {
                temp[cut] = map[i][j];
                cut++;
            }
        }
        return temp; //temp 반환
    }

    //평가 모델과 맵을 행을 기준으로 비교해주는 함수
    private int modelCompare(int player, int[] temp, int[][] model) {
        int cnt = 0, rowCnt = 0;
        for(int i = 0; i < model.length; i++) {
            rowCnt = 0; //중간카운트, 한 행을 검사할 때마다 0으로 초기화
            for(int j = 0; j < model[0].length; j++) {   //행의 열을 검사
                if (model[i][j] == -1) rowCnt++;
                else if (model[i][j] == 1 && temp[j] == player) rowCnt++;
                else if (model[i][j] == 0 && temp[j] == 0) rowCnt++;
                else break;
            }
            if (rowCnt == model[0].length) cnt++; //rowCnt와 패턴의 길이의 값이 같으면 행이 같다는 뜻이므로 cnt값 증가
        }
        return cnt;
    }

    //2차원 배열의 깊은 복사를 위해 만든 함수
    int[][] deepCopy(int[][] original) {
        if (original == null) {
            return null;
        }

        int[][] retval = new int[original.length][original[0].length];
        for(int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, retval[i], 0, original[i].length);
        }
        return retval;
    }

    //상대 플레이어 번호 반환 함수
    int oppoTurn(int player) {
        return player == 1 ? 2 : 1;
    }

    //승리 여부 반환 함수. PixelEnv에서 가져왔습니다.
    public boolean isVictory(Point position, int turn, int[][] map) {
        boolean isFour = false;
        boolean isSquare = false;

        for(int i = 0; i < SIZE_OF_BOARD; i++)
            for(int j = 0; j < SIZE_OF_BOARD - 3; j++)
                if (map[i][j] == turn && map[i][j + 1] == turn && map[i][j + 2] == turn && map[i][j + 3] == turn)
                    isFour = true;

        for(int i = 0; i < SIZE_OF_BOARD - 3; i++)
            for(int j = 0; j < SIZE_OF_BOARD; j++)
                if (map[i][j] == turn && map[i + 1][j] == turn && map[i + 2][j] == turn && map[i + 3][j] == turn)
                    isFour = true;

        for(int i = 0; i < SIZE_OF_BOARD - 3; i++)
            for(int j = 0; j < SIZE_OF_BOARD - 3; j++)
                if (map[i][j] == turn && map[i + 1][j + 1] == turn && map[i + 2][j + 2] == turn && map[i + 3][j + 3] == turn)
                    isFour = true;

        for(int i = 0; i < SIZE_OF_BOARD - 3; i++)
            for(int j = 3; j < SIZE_OF_BOARD; j++)
                if (map[i][j] == turn && map[i + 1][j - 1] == turn && map[i + 2][j - 2] == turn && map[i + 3][j - 3] == turn)
                    isFour = true;

        for(int i = 0; i < SIZE_OF_BOARD - 1; i++)
            for(int j = 0; j < SIZE_OF_BOARD - 1; j++)
                if (map[i][j] == turn && map[i][j + 1] == turn && map[i + 1][j] == turn && map[i + 1][j + 1] == turn)
                    isSquare = true;

        return isFour && isSquare;
    }

    //2x2모양이 만들어 졌는지 반환하는 함수
    public boolean isSquare(Point position, int turn, int[][] map) {
        boolean isSquare = false;
        for(int i = 0; i < SIZE_OF_BOARD - 1; i++)
            for(int j = 0; j < SIZE_OF_BOARD - 1; j++)
                if (map[i][j] == turn && map[i][j + 1] == turn && map[i + 1][j] == turn && map[i + 1][j + 1] == turn)
                    isSquare = true;
        return isSquare;
    }

    //4목 모양이 있는지 반환하는 함수
    public boolean isFour(Point position, int turn, int[][] map) {
        boolean isFour = false;
        for(int i = 0; i < SIZE_OF_BOARD; i++)
            for(int j = 0; j < SIZE_OF_BOARD - 3; j++)
                if (map[i][j] == turn && map[i][j + 1] == turn && map[i][j + 2] == turn && map[i][j + 3] == turn)
                    isFour = true;

        for(int i = 0; i < SIZE_OF_BOARD - 3; i++)
            for(int j = 0; j < SIZE_OF_BOARD; j++)
                if (map[i][j] == turn && map[i + 1][j] == turn && map[i + 2][j] == turn && map[i + 3][j] == turn)
                    isFour = true;

        for(int i = 0; i < SIZE_OF_BOARD - 3; i++)
            for(int j = 0; j < SIZE_OF_BOARD - 3; j++)
                if (map[i][j] == turn && map[i + 1][j + 1] == turn && map[i + 2][j + 2] == turn && map[i + 3][j + 3] == turn)
                    isFour = true;

        for(int i = 0; i < SIZE_OF_BOARD - 3; i++)
            for(int j = 3; j < SIZE_OF_BOARD; j++)
                if (map[i][j] == turn && map[i + 1][j - 1] == turn && map[i + 2][j - 2] == turn && map[i + 3][j - 3] == turn)
                    isFour = true;

        return isFour;
    }
}