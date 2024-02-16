package edu.ucdavis.cs.ecs036c

import kotlin.math.absoluteValue

class HashTable<K, V>(var initialCapacity: Int = 8) {
    data class HashTableEntry<K, V>(val key: K, var value: V, var deleted : Boolean = false);
    // The number of elements in the storage that exist, whether or not they are marked deleted
    internal var occupied = 0

    // The number of non-deleted elements.
    internal var privateSize = 0

    // And the internal storage array
    internal var storage: Array<HashTableEntry<K, V>?> = arrayOfNulls(initialCapacity)

    val size: Int
        get() = privateSize

    // An iterator of key/value pairs, done by using a sequence and calling yield
    // on each pair that is in the table and VALID
    operator fun iterator() : Iterator<Pair<K, V>> =
        sequence<Pair<K, V>> {
            TODO()
    }.iterator()

    override fun toString() : String = this.iterator().asSequence().joinToString(prefix="{", postfix="}",
        limit = 200) { "[${it.first}/${it.second}]" }


    // Internal resize function.  It should copy all the
    // valid entries but ignore the deleted entries.
    private fun resize(){
        TODO()
    }

    operator fun contains(key: K): Boolean {
        TODO()
    }

    // Get returns null if the key doesn't exist
    operator fun get(key: K): V? {
        TODO()
    }

    // IF the key exists just update the corresponding data.
    // If the key doesn't exist, find a spot to insert it.
    // If you need to insert into a NEW entry, resize if
    // the occupancy (active & deleted entries) is >75%
    operator fun set(key: K, value: V) {
        TODO()
    }

    // If the key doesn't exist remove does nothing
    fun remove(key: K) {
        TODO()
    }

}