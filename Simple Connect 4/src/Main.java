import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private CircleButton[][] boardButtons;
    private JLabel statusLabel;

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int CELL_SIZE = 100;

    private int[][] board;
    private int currentPlayer;

    public Main() {
        board = new int[ROWS][COLS];
        currentPlayer = 1;

        setTitle("Connect 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(ROWS, COLS));
        boardButtons = new CircleButton[ROWS][COLS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                boardButtons[row][col] = new CircleButton();
                boardButtons[row][col].setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                boardButtons[row][col].setEnabled(true);
                final int column = col;
                boardButtons[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dropPiece(column);
                    }
                });
                boardPanel.add(boardButtons[row][col]);
            }
        }

        statusLabel = new JLabel("Player 1's turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setSize(COLS * CELL_SIZE, ROWS * CELL_SIZE + 50);
        setVisible(true);
    }

    private void dropPiece(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = currentPlayer;
                boardButtons[row][col].setColor(currentPlayer == 1 ? Color.RED : Color.YELLOW);
                checkWinner(row, col);
                currentPlayer = currentPlayer == 1 ? 2 : 1;
                statusLabel.setText("Player " + currentPlayer + "'s turn");
                return;
            }
        }
    }

    private void checkWinner(int row, int col) {
        int player = board[row][col];
        // Horizontal check
        for (int i = 0; i <= COLS - 4; i++) {
            if (board[row][i] == player && board[row][i + 1] == player && board[row][i + 2] == player && board[row][i + 3] == player) {
                displayWinner(player);
                return;
            }
        }
        // Vertical check
        for (int j = 0; j <= ROWS - 4; j++) {
            if (board[j][col] == player && board[j + 1][col] == player && board[j + 2][col] == player && board[j + 3][col] == player) {
                displayWinner(player);
                return;
            }
        }
        // Diagonal check
        for (int j = 0; j <= ROWS - 4; j++) {
            for (int i = 0; i <= COLS - 4; i++) {
                if (board[j][i] == player && board[j + 1][i + 1] == player && board[j + 2][i + 2] == player && board[j + 3][i + 3] == player) {
                    displayWinner(player);
                    return;
                }
            }
        }
        for (int j = 0; j <= ROWS - 4; j++) {
            for (int i = 3; i < COLS; i++) {
                if (board[j][i] == player && board[j + 1][i - 1] == player && board[j + 2][i - 2] == player && board[j + 3][i - 3] == player) {
                    displayWinner(player);
                    return;
                }
            }
        }
    }

    private void displayWinner(int player) {
        JOptionPane.showMessageDialog(this, "Player " + player + " wins!");
        resetBoard();
    }

    private void resetBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = 0;
                boardButtons[row][col].setColor(Color.WHITE);
                boardButtons[row][col].setEnabled(true);
            }
        }
        currentPlayer = 1;
        statusLabel.setText("Player 1's turn");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }

    private class CircleButton extends JButton {
        private Color color = Color.WHITE;

        public CircleButton() {
            setContentAreaFilled(false);
            setOpaque(true);
        }

        public void setColor(Color color) {
            this.color = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            int diameter = Math.min(getWidth(), getHeight());
            g.fillOval((getWidth() - diameter) / 2, (getHeight() - diameter) / 2, diameter, diameter);
        }
    }
}
