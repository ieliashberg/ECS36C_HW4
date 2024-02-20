package edu.ucdavis.cs.ecs036c

import kotlin.math.absoluteValue

class HashTable<K, V>(var initialCapacity: Int = 8) {
    data class HashTableEntry<K, V>(val key: K, var value: V, var deleted : Boolean = false)
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
            for (ndx in storage.indices) {
                if (storage[ndx] != null && !storage[ndx]!!.deleted) {
                    yield(Pair(storage[ndx]!!.key, storage[ndx]!!.value))
                }
            }
    }.iterator()

    override fun toString() : String = this.iterator().asSequence().joinToString(prefix="{", postfix="}",
        limit = 200) { "[${it.first}/${it.second}]" }


    // Internal resize function.  It should copy all the
    // valid entries but ignore the deleted entries.




     private fun resize(){
         var newStorage : Array<HashTableEntry<K, V>?> = arrayOfNulls(storage.size * 2)

         var ps = 0
         for((key, value) in this) {
             var newIndex = (key.hashCode() and 0x7FFFFFFF) % newStorage.size

             while(newStorage[newIndex] != null) {
                 newIndex = (newIndex + 1) % newStorage.size
             }

             newStorage[newIndex] = HashTableEntry(key, value)
             ps++
         }
         privateSize = ps
         occupied = privateSize
         storage = newStorage
     }



    operator fun contains(key: K): Boolean {
        return (get(key)!=null)
    }


    // Get returns null if the key doesn't exist
    operator fun get(key: K): V? {
        if (size == 0){
            return null
        }
        var index = (key.hashCode() and 0x7FFFFFFF) % storage.size
        var numLookedAt = 0
        while(numLookedAt < storage.size) {
            val at = storage[index]
            if (at == null) {
                return null
            } else if ((!at.deleted) && (at.key == key)) {
                return at.value
            }
            numLookedAt++
            index = (index+1) % storage.size
        }
        return null
    }

    // IF the key exists just update the corresponding data.
    // If the key doesn't exist, find a spot to insert it.
    // If you need to insert into a NEW entry, resize if
    // the occupancy (active & deleted entries) is >75%



    operator fun set(key: K, value: V) {
        if(!contains(key) ) {
            if (occupied.toDouble() / storage.size > 0.75) {
                resize()
            }
            var ndx = (key.hashCode() and 0x7FFFFFFF) % storage.size

            var updated = false
            while (!updated) {
                val entry = storage[ndx]
                if (entry == null) {
                    storage[ndx] = HashTableEntry(key, value, deleted = false)
                    occupied++
                    updated = true
                } else if (entry.deleted) {
                    storage[ndx] = HashTableEntry(key, value, deleted = false)
                    updated = true
                }
                ndx = (ndx + 1) % storage.size
            }
            privateSize++
        } else {
            var ndx = (key.hashCode() and 0x7FFFFFFF) % storage.size
            var at = storage[ndx]
            while(key != at!!.key) {
                ndx = (ndx + 1) % storage.size
                at = storage[ndx]
            }
            storage[ndx]!!.value = value
        }


    /*        while(storage[ndx] != null && !storage[ndx]!!.deleted) {
                ndx = (ndx + 1) % storage.size
            }
            storage[ndx] = HashTableEntry(key, value, deleted = false)
            occupied++
            privateSize++
        } else {
            var ndx = key.hashCode() % storage.size
            var at = storage[ndx]
            while(key != at!!.key) {
                ndx = (ndx + 1) % storage.size
                at = storage[ndx]
            }
            storage[ndx]!!.value = value
        }

     */
    }



    // If the key doesn't exist remove does nothing
    fun remove(key: K) {
        if(contains(key)) {
            var index = (key.hashCode() and 0x7FFFFFFF) % storage.size

            while (storage[index] != null) {
                val entry = storage[index]
                if (!entry!!.deleted && entry.key == key) {
                    entry.deleted = true
                    privateSize--
                    break
                }
                index = (index + 1) % storage.size
            }
        }
    }
}