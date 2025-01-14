package org.fasheep.fair.core.blockchain.model

import io.ethers.abi.ContractStruct
import io.ethers.abi.StructFactory
import java.math.BigInteger

data class RoleStruct(val name: String, val count: BigInteger) : ContractStruct {
    override val tuple: Array<Any> = arrayOf(name, count)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoleStruct

        if (name != other.name) return false
        if (count != other.count) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + count.hashCode()
        return result
    }

    companion object : StructFactory<RoleStruct> {
        @JvmStatic
        override fun fromTuple(data: Array<out Any>): RoleStruct = RoleStruct(data[0] as String, data[1] as BigInteger)
    }
}