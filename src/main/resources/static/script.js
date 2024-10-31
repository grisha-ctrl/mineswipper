const API_URL = 'http://localhost:8080/api';
let gameId;
let rows, cols;
let board = [];

async function initGame() {
    const modal = document.getElementById('gameOverModal');
    modal.style.display = 'none';
    board = [];
    const response = await fetch(`${API_URL}/game`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
    });
    const data = await response.json();
    gameId = data.id;
    rows = 10;
    cols = 10;
    createBoard();
    updateBoard(data.board);
    updateFlagCount(data.flagCount);
}

async function revealCell(row, col) {
    const response = await fetch(`${API_URL}/game/${gameId}/reveal?row=${row}&col=${col}`, {
        method: 'POST',
    });
    const data = await response.json();
    updateBoard(data.board);

    if (data.gameOver) {
        showGameOverModal();
    }
}

function createBoard() {
    const boardElement = document.getElementById('board');
    boardElement.innerHTML = '';
    boardElement.style.gridTemplateColumns = `repeat(${cols}, 30px)`;
    boardElement.style.gridTemplateRows = `repeat(${rows}, 30px)`;

    for (let r = 0; r < rows; r++) {
        board[r] = [];
        for (let c = 0; c < cols; c++) {
            const cell = document.createElement('div');
            cell.classList.add('cell');

            // Левый клик - раскрыть клетку
            cell.addEventListener('click', () => revealCell(r, c));

            // Правый клик - поставить или снять флаг
            cell.addEventListener('contextmenu', (e) => {
                e.preventDefault();
                toggleFlag(r, c);
            });

            board[r][c] = { element: cell, revealed: false, flagged: false, mine: false };
            boardElement.appendChild(cell);
        }
    }
}

async function toggleFlag(row, col) {
    const response = await fetch(`${API_URL}/game/${gameId}/toggle-flag?row=${row}&col=${col}`, {
        method: 'POST',
    });
    const data = await response.json();
    updateBoard(data.board);
    updateFlagCount(data.flagCount);

    if (data.flaggedMines === data.minesCount) {
        alert('Вы победили!');
        board = [];
        initGame();
    }
}

function updateBoard(boardData) {
    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            const cellData = boardData[r][c];
            const cell = board[r][c].element;

            if (cellData.revealed) {
                cell.classList.add('revealed');
                if (cellData.mine) {
                    cell.classList.add('mine');
                } else if (cellData.adjacentMines > 0) {
                    cell.textContent = cellData.adjacentMines;
                }
            } else if (cellData.flagged) {
                cell.classList.add('flagged');
                cell.textContent = '🚩'; // Отображаем флаг
            } else {
                cell.classList.remove('flagged');
                cell.textContent = '';
            }
        }
    }
}

function updateFlagCount(count) {
    const flagCountElement = document.getElementById('flagCount');
    flagCountElement.textContent = count;
}

function showGameOverModal() {
    const modal = document.getElementById('gameOverModal');
    modal.style.display = 'flex';
}

window.onload = initGame;