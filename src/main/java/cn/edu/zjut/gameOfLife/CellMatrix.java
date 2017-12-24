package cn.edu.zjut.gameOfLife;

public class CellMatrix {
    private int numberOfRows;//矩阵的行数
    private int numberOfColumns;//矩阵的列数
    private int[][] stateMatrix;//状态矩阵，1代表存活，0代表死亡

    public CellMatrix(int numberOfRows, int numberOfColumns, int[][] stateMatrix) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.stateMatrix = stateMatrix;
    }

    //上一个状态到下一个状态的转化
    public void transform(){
        int[][] nextStateMatrix = new int[numberOfRows][numberOfColumns];//下一个状态
        int rowNum;//矩阵的行号
        int columnNum;//矩阵的列号
        for (rowNum = 0; rowNum < numberOfRows; rowNum++) {
            for (columnNum = 0; columnNum < numberOfColumns; columnNum++) {
                nextStateMatrix[rowNum][columnNum]=0;
                int numOfSurvivalCells = countTheNumOfSurvivalCells(rowNum,columnNum);//该细胞周围存活细胞数目
                //若该细胞周围存活细胞数为3,该细胞的下一个状态一定为存活状态
                if(numOfSurvivalCells == 3){
                    nextStateMatrix[rowNum][columnNum] = 1;
                }
                //若该细胞周围存活细胞数为2,该细胞的下一个状态与上一状态相同
                else if(numOfSurvivalCells == 2){
                    nextStateMatrix[rowNum][columnNum] = stateMatrix[rowNum][columnNum];
                }
            }
        }
        stateMatrix = nextStateMatrix;
    }

    //统计该细胞周围存活细胞个数
    public int countTheNumOfSurvivalCells(int rowNum,int columnNum) {
        int num = 0;
        //位于该细胞正左方的细胞
        if (columnNum != 0) {
            num += stateMatrix[rowNum][columnNum - 1];
        }
        //位于该细胞左上方的细胞
        if ((rowNum != 0) && (columnNum != 0)) {
            num += stateMatrix[rowNum - 1][columnNum - 1];
        }
        //位于该细胞左下方的细胞
        if ((rowNum != numberOfRows - 1) && (columnNum != 0)) {
            num += stateMatrix[rowNum + 1][columnNum - 1];
        }
        //位于该细胞正上方的细胞
        if (rowNum != 0) {
            num += stateMatrix[rowNum - 1][columnNum];
        }
        //位于该细胞正下方的细胞
        if (rowNum != numberOfRows - 1) {
            num += stateMatrix[rowNum + 1][columnNum];
        }
        //位于该细胞右上方的细胞
        if ((rowNum != 0) && (columnNum != numberOfColumns - 1)) {
            num += stateMatrix[rowNum - 1][columnNum + 1];
        }
        //位于该细胞正右方的细胞
        if (columnNum != numberOfColumns - 1) {
            num += stateMatrix[rowNum][columnNum + 1];
        }
        //位于该细胞右下方的细胞
        if ((rowNum != numberOfRows - 1) && (columnNum != numberOfColumns - 1 )) {
            num += stateMatrix[rowNum + 1][columnNum + 1];
        }
        return num;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public int[][] getStateMatrix() {
        return stateMatrix;
    }
}
