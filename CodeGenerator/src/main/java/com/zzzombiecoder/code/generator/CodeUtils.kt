package com.zzzombiecoder.code.generator

import java.util.*

fun Char.generateCode(
        random: Random = Random()
): String {
    return this.toIntCode().toCode(random)
}

fun String.getCode(): Code? {
    if (this.isCorrectCode()) {
        val char = this[1]
        for (code in Code.values()) {
            if (code.char == char) {
                return code
            }
        }
    }
    return null
}

fun String.isCorrectCode(): Boolean {
    val charArray = this.toCharArray()
    return if (charArray.size == 6) {
        val i0 = charArray[0].toIntCode()
        val i1 = charArray[1].toIntCode()
        val i2 = charArray[2].toIntCode()
        val i3 = charArray[3].toIntCode()
        val i4 = charArray[4].toIntCode()
        val i5 = charArray[5].toIntCode()
        return i2 == i1 * 23 % 36
                && i3 == i0 * 11 % 36
                && i4 == (i1 + i3 * 12) % 36
                && i5 == (i0 + i1 + i3) % 36
    } else {
        false
    }
}

private fun Char.toIntCode(): Int {
    val int = this.toInt()
    return if (int < 58) {
        int - 48
    } else {
        int - 87
    }
}

private fun Int.toCode(
        random: Random
): String {
    val i0 = random.nextInt(36)
    val i1 = this
    val i2 = i1 * 23 % 36
    val i3 = i0 * 11 % 36
    val i4 = (i1 + i3 * 12) % 36
    val i5 = (i0 + i1 + i3) % 36

    val charArray = charArrayOf(i0.toSymbol(), i1.toSymbol(), i2.toSymbol(), i3.toSymbol(), i4.toSymbol(), i5.toSymbol())
    return String(charArray)
}

private fun Int.toSymbol(): Char {
    return if (this < 10) {
        (this + 48).toChar()
    } else {
        (this + 87).toChar()
    }
}