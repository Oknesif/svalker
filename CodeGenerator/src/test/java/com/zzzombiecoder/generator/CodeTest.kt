package com.zzzombiecoder.generator

import com.zzzombiecoder.code.generator.Code
import com.zzzombiecoder.code.generator.generateCode
import org.junit.Test
import java.util.*

class CodeTest {

    @Test
    fun someTest() {
        val random = Random()
        repeat(20) {
            println(Code.DEACTIVATE_RECEIVER.char.generateCode(random))
        }
        assert(true)
    }
}