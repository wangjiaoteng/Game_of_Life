package cn.edu.zjut.test;

import cn.edu.zjut.gameOfLife.CellMatrix;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CellMatrixTest {
    private int[][] stateMatrix = {{0,0,0,0},{1,1,1,1},{0,0,0,0}};//状态矩阵，1代表存活，0代表死亡
    CellMatrix cellMatrix;
    @Before
    public void setUp() throws Exception {
        cellMatrix = new CellMatrix(3,4,stateMatrix);
    }

    @Test
    public void transform() throws Exception {
        cellMatrix.transform();
        int expected[][]={{0,1,1,0},{0,1,1,0},{0,1,1,0}};
        assertArrayEquals(expected,cellMatrix.getStateMatrix());
    }

    @Test
    public void countTheNumOfSurvivalCells() throws Exception {

        int numOfSurvivalCells = cellMatrix.countTheNumOfSurvivalCells(1,1);
        assertEquals(2,numOfSurvivalCells);
    }

}