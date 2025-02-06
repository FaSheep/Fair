package org.fasheep.fair.core.blockchain.model

data class Role(
    val name: String,
    val num: Int
) {
    internal fun toStruct(): RoleStruct = RoleStruct(name, num.toBigInteger())
}
