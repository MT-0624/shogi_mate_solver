import kotlin.math.abs

class Board {
    //持ち駒を表示する
    private fun showHand(turn: Boolean) {
        print(if (turn) "先手持駒:" else "後手持駒:")
        for (i in 6.downTo(0)) {
            if (turn) {
                print(if (handW[i] != 0) name.hand_w[i + 1] + "*" + handW[i] + " " else "")
            } else {
                print(if (handB[i] != 0) name.hand_b[i + 1] + "*" + handB[i] + " " else "")
            }
        }
        println()
    }

    //局面　双方の持駒全て表示する
    fun showAll() {
        //後手持駒
        showHand(false)

        //　将棋盤を出力する
        val grid = "+---+---+---+---+---+---+---+---+---+"
        var num: Int

        println(grid)
        for (i in 0..8) {
            print("|")
            for (j in 0..8) {
                num = board[i][j]

                print(" ")

                //マスの値が0なら空白　正の数なら先手駒　負の数なら後手駒
                val pName: String = if (num >= 0) name.piece_w[num] else name.piece_b[num * -1]

                print("%-2s".format(pName, 2))
                print("|")
            }
            println()
            println(grid)
        }

        //先手持駒
        showHand(true)
    }

    //駒を置く
    fun put(turn: Boolean, piece: Int, row: Int, col: Int) {
        //置けるかチェック
        if (turn) {
            if (handW[piece - 1] == 0) {
                throw Exception("持ち駒に%sがありません".format(name.hand_w[piece - 1]))
            }
        } else {
            if (handB[piece - 1] == 0) {
                throw Exception("持ち駒に%sがありません".format(name.hand_b[piece - 1]))
            }
        }

        //盤面の置く場所が空白であること
        if (board[row][col] != 0) {
            throw Exception("すでに駒が配置されています")
        }

        if (turn) {
            handW[piece - 1]--
            board[row][col] = piece
        } else {
            handB[piece - 1]--
            board[row][col] = piece * -1
        }
    }

    //バグチェック用のメソッド
    //sfen形式の文字列を局面として取り込む(すでにある局面は破棄される)
    fun readSfen(line: String) {
        fun getIndex(str: String): Int {
            val c: Char = str[0].toChar()
            val arr: Array<String>
            val bias: Int

            if (c.isUpperCase()) {
                bias = 1
                arr = name.piece_w
            } else {
                bias = -1
                arr = name.piece_b
            }

            for (s in 0..14) {
                if (arr[s].equals(str)) {
                    return s * bias

                }
            }
            throw Exception("%sは存在しません".format(str))
        }
        //盤面の初期化
        board = Array(9) { Array(9) { _ -> 0 } }
        //持ち駒の初期化
        handB = Array(7) { _ -> 0 }
        handW = Array(7) { _ -> 0 }


        //局面を書き換える
        var row: Int = 0
        var col: Int = 0
        var nowStr: String
        var pN: String
        var space: Int
        var pass: Boolean = false

        val num: Array<String> = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9")

        for (i in 0..line.length - 1) {
            if (pass) {
                pass = false
                continue
            }

            space = -1
            nowStr = line[i] + ""
            if (nowStr == " ") {
                break
            }
            if (nowStr == "/") {
                row++
                col = 0
                continue
            }
            if (nowStr == "+") {
                pN = line[i + 1] + nowStr
                board[row][col] = getIndex(pN)
                pass = true
                col++
                continue
            }

            for (n in 0..8) {
                if (num[n] == nowStr) {
                    space = n + 1
                    break
                }
            }

            if (space != -1) {
                col += space
            } else {
                board[row][col] = getIndex(nowStr)
                col++
            }

        }

        var cnt = 2
        var numeric: Boolean = false
        var pCount: Int = 1
        var turn: Boolean = true
        for (i in 0..line.length - 1) {
            numeric = false
            //持ち駒のところまでiを移動させる
            if (cnt != 0) {
                if (line[i].equals(' ')) {
                    cnt--
                }
                continue
            }

            for (n in 0..8) {
                if (num[n] == line[i] + "") {
                    pCount = n + 1
                    numeric = true
                    break
                }
            }
            //今走査した物が数字なら
            if (numeric) {
                continue
            }

            //駒名だけが来る
            //大文字かどうかでどちらの駒か判断
            turn = line[i].isUpperCase()

            val arr = if (turn) name.hand_w else name.hand_b

            for (a in 1..7) {
                if (line[i] + "" == arr[a]) {

                    if (turn) {
                        handW[a - 1] = pCount
                    } else {
                        handB[a - 1] = pCount
                    }
                    pCount = 1
                }
            }
        }
    }

    //駒を移動させる
    fun move(row1: Int, col1: Int, row2: Int, col2: Int, promote: Boolean = false) {
        //移動元に駒がなければエラー
        if (board[row1][col1] == 0) {
            throw Exception("[%s][%s]には移動させる駒がありません".format(row1, col1))
        }
        //移動させる駒
        var p: Int = board[row1][col1]

        //移動先の駒について
        if (board[row2][col2] != 0) {
            //敵駒をとる
            if ((p > 0) xor (board[row2][col2] > 0)) {
                var take = board[row2][col2]
                //取ろうとする駒が王や玉ならエラー
                if (abs(take) == 14) {
                    showAll()
                    println(row1)
                    println(col1)
                    println(row2)
                    println(col2)
                    throw Exception("移動先に王または玉があります")
                }
                //成り駒を取るなら絶対値を-7した値にするともとの駒に戻せる
                if (abs(take) > 7) {
                    take = (if (take > 0) take - 7 else take + 7)
                }
                if (take > 0) {
                    handB[abs(take) - 1]++
                } else {
                    handW[abs(take) - 1]++
                }
            }
            //移動先に味方の駒がある　エラー
            else {
                throw Exception("移動先に味方の駒があります")
            }
        }
        //成り
        if (promote) {
            if (abs(p) < 1 || abs(p) > 6) {
                throw Exception("%sは成れる駒ではありません".format(p))
            } else {
                p = (if (p > 0) p + 7 else p - 7)
            }
        }

        //移動
        board[row1][col1] = 0
        board[row2][col2] = p
    }


    //盤面
    var board = Array(9) { Array(9) { _ -> 0 } }


    //先手駒
    var handB = Array(7) { _ -> 0 }

    //後手駒、初期状態は全て持っているとする
    var handW = arrayOf(18, 4, 4, 4, 4, 2, 2)

    private val name = Name()
}

