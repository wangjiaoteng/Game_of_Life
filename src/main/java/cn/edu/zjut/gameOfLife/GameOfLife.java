package cn.edu.zjut.gameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class GameOfLife extends JFrame{
    private JPanel gridPanel = new JPanel();
    private JMenuBar menubar = new JMenuBar();
    private JMenu fileMenu = new JMenu("文件");
    private JMenu controlMenu = new JMenu("控制");
    private JMenu helpMenu = new JMenu("帮助");
    private JMenuItem openFile = new JMenuItem("选择文件");
    private JMenuItem exit = new JMenuItem("退出");
    private JMenuItem start = new JMenuItem("开始游戏");
    private JMenuItem pause = new JMenuItem("暂停游戏");
    private JMenuItem resume = new JMenuItem("继续游戏");
    private JMenuItem speed = new JMenuItem("速度调节");
    private JMenuItem about = new JMenuItem("关于游戏");

    private boolean isPause = false;//游戏暂停的标志
    private int timeInterval = 200;//状态变化显示的时间间隔(默认为200ms)

    private CellMatrix cellMatrix;
    private JTextField[][] textMatrix;

    public GameOfLife() {
        this.setTitle("生命游戏");
        openFile.addActionListener(new OpenFileListener());
        exit.addActionListener(new ExitListener());
        fileMenu.add(openFile);
        fileMenu.add(exit);

        start.addActionListener(new StartListener());
        pause.addActionListener(new PauseListener());
        resume.addActionListener(new ResumeListener());
        speed.addActionListener(new SpeedListener());
        pause.setEnabled(false);
        resume.setEnabled(false);
        controlMenu.add(start);
        controlMenu.add(pause);
        controlMenu.add(resume);
        controlMenu.add(speed);
        controlMenu.setEnabled(false);

        about.addActionListener(new AboutListener());
        helpMenu.add(about);

        menubar.add(fileMenu);
        menubar.add(controlMenu);
        menubar.add(helpMenu);

        this.getContentPane().add("North", menubar);
        this.setSize(500, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    private class OpenFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fcDlg = new JFileChooser(".");
            fcDlg.setDialogTitle("请选择初始配置文件");
            int returnVal = fcDlg.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                isPause = true;
                start.setEnabled(true);
                pause.setEnabled(false);
                resume.setEnabled(false);
                controlMenu.setEnabled(true);

                String filePath = fcDlg.getSelectedFile().getPath();
                cellMatrix = initMatrixFromFile(filePath);
                initGridLayout();
                showStateMatrix();
                gridPanel.updateUI();
            }
        }
    }

    private class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            isPause = false;
            new Thread(new GameControlTask()).start();
            start.setEnabled(false);
            pause.setEnabled(true);
            speed.setEnabled(false);
        }
    }

    private class PauseListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            isPause = true;
            pause.setEnabled(false);
            resume.setEnabled(true);
            speed.setEnabled(true);
        }
    }

    private class ResumeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            isPause = false;
            new Thread(new GameControlTask()).start();
            pause.setEnabled(true);
            resume.setEnabled(false);
            speed.setEnabled(false);
        }
    }

    private class SpeedListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            timeInterval = Integer.parseInt(JOptionPane.showInputDialog("请输入状态变化显示的时间间隔（单位:ms）："));
        }
    }

    private class AboutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,
                            "生命游戏规则如下:\n"
                            +"1．如果一个细胞周围有3个细胞存活，则该细胞存活;\n"
                            +"2．如果一个细胞周围有2个细胞存活，则该细胞的状态保持不变;\n"
                            +"3．在其它情况下，该细胞死亡。"
                            , "关于游戏", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showStateMatrix() {

        int[][] stateMatrix = cellMatrix.getStateMatrix();
        for (int y = 0; y < stateMatrix.length; y++) {
            for (int x = 0; x < stateMatrix[0].length; x++) {
                if (stateMatrix[y][x] == 1) {
                    textMatrix[y][x].setBackground(Color.BLACK);
                } else {
                    textMatrix[y][x].setBackground(Color.WHITE);
                }
            }
        }
    }

    // 创建显示的gridlayout布局
    private void initGridLayout() {
        int numberOfRows = cellMatrix.getNumberOfRows();
        int numberOfColumns = cellMatrix.getNumberOfColumns();
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(numberOfRows, numberOfColumns));
        textMatrix = new JTextField[numberOfRows][numberOfColumns];
        for (int y = 0; y < numberOfRows; y++) {
            for (int x = 0; x < numberOfColumns; x++) {
                JTextField text = new JTextField();
                textMatrix[y][x] = text;
                textMatrix[y][x].setEditable(false);
                gridPanel.add(text);
            }
        }
        this.add("Center", gridPanel);
    }

    private CellMatrix initMatrixFromFile(String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line = reader.readLine();
            String[] array = line.split(" ");
            int numberOfRows = Integer.parseInt(array[0]);
            int numberOfColumns= Integer.parseInt(array[1]);
            int[][] matrix = new int[numberOfRows][numberOfColumns];
            for (int i = 0; i < numberOfRows; i++) {
                line = reader.readLine();
                array = line.split(" ");
                for (int j = 0; j < array.length; j++) {
                    matrix[i][j] = Integer.parseInt(array[j]);
                }
            }

            return new CellMatrix(numberOfRows, numberOfColumns, matrix);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private class GameControlTask implements Runnable {
        public void run() {
            while (!isPause) {
                cellMatrix.transform();
                showStateMatrix();
                try {
                    TimeUnit.MILLISECONDS.sleep(timeInterval);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
            new GameOfLife();
    }
}
