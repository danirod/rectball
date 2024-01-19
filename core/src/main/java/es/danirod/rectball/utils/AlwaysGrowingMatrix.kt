package es.danirod.rectball.utils

class AlwaysGrowingMatrix<T>(private val generator: (x: Int, y: Int) -> T) {

    private var items: MutableList<MutableList<T?>> = mutableListOf()

    fun get(x: Int, y: Int): T {
        while (y >= items.size) {
            items.add(mutableListOf())
        }
        val row = items[y]
        while (x >= row.size) {
            row.add(null)
        }
        return row[x] ?: generator(x, y).also { row[x] = it }
    }

    fun set(x: Int, y: Int, value: T) {
        while (y >= items.size) {
            items.add(mutableListOf())
        }
        val row = items[y]
        while (x >= row.size) {
            row.add(null)
        }
        row[x] = value
    }
}
