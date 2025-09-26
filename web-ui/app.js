const API_URL = 'http://localhost:8080/api/game/move'; // твой backend

const sizeInput = document.getElementById('sizeInput');
const playerColorInput = document.getElementById('playerColor');
const modeSelect = document.getElementById('modeSelect');
const newGameBtn = document.getElementById('newGameBtn');
const statusEl = document.getElementById('status');
const boardEl = document.getElementById('board');

let state = {
    size: parseInt(sizeInput.value, 10) || 6,
    board: [],
    nextPlayer: 'W',
    myColor: playerColorInput.value,
    mode: modeSelect.value
};

function boardToDataString(board) {
    return board.join('');
}

function setStatus(text) {
    statusEl.textContent = text;
}

function updateCell(x, y, ch) {
    const row = state.board[x].split('');
    row[y] = ch;
    state.board[x] = row.join('');
}

function renderBoard() {
    const n = state.size;
    boardEl.style.gridTemplateColumns = `repeat(${n}, var(--cell))`;
    boardEl.innerHTML = '';
    for (let i = 0; i < n; i++) {
        for (let j = 0; j < n; j++) {
            const ch = state.board[i].charAt(j);
            const cell = document.createElement('div');
            cell.className = 'cell ' + (ch === ' ' ? 'empty' : ch);
            cell.textContent = ch === ' ' ? ' ' : ch;
            cell.dataset.x = i;
            cell.dataset.y = j;

            if (ch === ' ' && !isGameFinished()) {
                if (state.mode !== 'c-vs-c') {
                    cell.addEventListener('click', onCellClick);
                }
            } else {
                cell.classList.add('disabled');
            }
            boardEl.appendChild(cell);
        }
    }
}

function isGameFinished() {
    const s = statusEl.textContent.toLowerCase();
    return s.includes('победил') || s.includes('окончена') || s.includes('ничья');
}

async function requestServerMove() {
    const payload = {
        size: state.size,
        data: boardToDataString(state.board),
        nextPlayerColor: state.nextPlayer.toLowerCase()
    };

    const res = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) {
        const text = await res.text();
        throw new Error('Ошибка сервера: ' + res.status + ' ' + text);
    }
    return await res.json();
}

function applyMoveFromServer(move) {
    if (state.board[move.x].charAt(move.y) !== ' ') {
        setStatus(`Сервер предложил занятую клетку (${move.x},${move.y}) — игнорируется.`);
        return;
    }
    updateCell(move.x, move.y, move.color.toUpperCase());
    state.nextPlayer = (move.color.toUpperCase() === 'W') ? 'B' : 'W';
    renderBoard();
    if (checkWinner()) return;
}

function onCellClick(e) {
    if (isGameFinished()) return;
    const x = parseInt(e.currentTarget.dataset.x, 10);
    const y = parseInt(e.currentTarget.dataset.y, 10);

    if (state.board[x].charAt(y) !== ' ') {
        setStatus('Клетка занята');
        return;
    }

    updateCell(x, y, state.nextPlayer);
    renderBoard();
    if (checkWinner()) return;
    setStatus(`Игрок ${state.nextPlayer} сходил в (${x},${y})`);
    state.nextPlayer = (state.nextPlayer === 'W') ? 'B' : 'W';

    if (state.mode === 'u-vs-c' && state.nextPlayer !== state.myColor) {
        doComputerMove();
    }
}

async function doComputerMove() {
    if (isGameFinished()) return;
    try {
        setStatus(`Зловещий интеллект продумывает ход для ${state.nextPlayer}...`);
        const move = await requestServerMove();
        applyMoveFromServer(move);

        if (state.mode === 'c-vs-c' && !isGameFinished()) {
            setTimeout(doComputerMove, 500);
        }
    } catch (err) {
        console.error(err);
        setStatus('Ошибка: ' + err.message);
    }
}

function initBoard(size) {
    state.size = size;
    state.board = Array.from({ length: size }, () => ' '.repeat(size));
    state.nextPlayer = 'W';
    renderBoard();
    setStatus('Новая игра ' + size + '×' + size + '. Режим: ' + state.mode);
}

newGameBtn.addEventListener('click', () => {
    const n = Math.max(3, Math.min(20, parseInt(sizeInput.value, 10) || 6));
    state.myColor = playerColorInput.value;
    state.mode = modeSelect.value;
    initBoard(n);

    if (state.mode === 'c-vs-c') {
        doComputerMove();
    }
});

function findWinningSquare(board, color) {
    const n = board.length;
    const coords = [];

    for (let i = 0; i < n; i++) {
        for (let j = 0; j < n; j++) {
            if (board[i][j] === color) {
                coords.push([i, j]);
            }
        }
    }

    for (let a = 0; a < coords.length; a++) {
        for (let b = a + 1; b < coords.length; b++) {
            for (let c = b + 1; c < coords.length; c++) {
                for (let d = c + 1; d < coords.length; d++) {
                    const square = [coords[a], coords[b], coords[c], coords[d]];
                    if (isSquare(square)) return square;
                }
            }
        }
    }
    return null;
}

function isSquare(points) {
    function dist2([x1, y1], [x2, y2]) {
        return (x1 - x2) ** 2 + (y1 - y2) ** 2;
    }
    const d = [];
    for (let i = 0; i < 4; i++) {
        for (let j = i + 1; j < 4; j++) {
            d.push(dist2(points[i], points[j]));
        }
    }
    d.sort((a, b) => a - b);
    return d[0] > 0 && d[0] === d[1] && d[1] === d[2] && d[2] === d[3] && d[4] === d[5] && d[4] === 2 * d[0];
}

function highlightWinningSquare(square) {
    square.forEach(([x, y]) => {
        const cell = boardEl.querySelector(`.cell[data-x="${x}"][data-y="${y}"]`);
        if (cell) cell.classList.add('win');
    });
}

function checkWinner() {
    for (const color of ['W', 'B']) {
        const square = findWinningSquare(state.board.map(r => r.split('')), color);
        if (square) {
            setStatus(`Игра окончена. ${color} победил!`);
            highlightWinningSquare(square);
            return true;
        }
    }

    if (state.board.every(r => !r.includes(' '))) {
        setStatus("Игра окончена. Ничья");
        return true;
    }
    return false;
}

initBoard(state.size);