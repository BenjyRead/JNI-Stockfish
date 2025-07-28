import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EngineGame : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundManager = SoundManager(this)
        val stockfish = StockfishEngine
        stockfish.start(this, "nn-1c0000000000.nnue", "nn-37f18f62d772.nnue")
        val timeControlMain = 300
        val increment = 5
        val stockfishElo = 3000

        setContent {
            val board = remember {
                mutableStateOf(
                        Board().apply { intent.getStringExtra("boardFEN")?.let { loadFromFen(it) } }
                )
            }
            Log.d("EngineGame", "Initial FEN: ${board.value.fen}")

            LaunchedEffect(Unit) {
                CoroutineScope(Dispatchers.IO).launch {
                    stockfish.inputChannel.send("position fen ${board.value.fen}")
                    stockfish.inputChannel.send("setoption name UCI_ELO value $stockfishElo")
                }
            }

    }

    override fun onDestroy() {
        Log.d("EngineGame", "onDestroy called")
        super.onDestroy()
        soundManager.releaseAll()
    }
}

    fun doEngineMove(
        stockfish: StockfishEngine,
        playerTime: MutableState<Int>,
        engineTime: MutableState<Int>,
        increment: Int,
        playerColor: Side,
        board: MutableState<Board>,
    ) {
        val playerColorString = if (playerColor == Side.WHITE) "w" else "b"
        val engineColorString = if (playerColor == Side.WHITE) "b" else "w"

        if (board.value.sideToMove == playerColor) {
            return
        }
        stockfish.inputChannel.trySend("position fen ${board.value.fen}")
        stockfish.inputChannel.trySend(
            "go ${playerColorString}time ${playerTime.value} ${engineColorString}time ${engineTime.value}inc $increment ${playerTime.value}inc $increment"
        )

        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val lineSplit =
                    (stockfish.outputChannel.tryReceive().getOrNull() ?: continue).split(" ")
                if (lineSplit[0] == "bestmove") {
                    val moveString = lineSplit[1]
                    board.value.doMove(moveString)
                    break
                }
            }
        }
    }
