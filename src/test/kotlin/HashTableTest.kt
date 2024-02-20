package edu.ucdavis.cs.ecs036c

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

class HashTableTest {

    @Test
    fun testGet() {
        var table = HashTable<Int, String>()
        for (i in 0 until 10000) {
            table[i] = "test$i"
            assert(table[i] == "test$i")
        }
    }

    @Test
    fun testGetAndSet() {
        val h = HashTable<String, Int>()
        val iterations = 10000
        var strings : Array<String?> = arrayOfNulls(iterations)
        for (i in 0 ..< iterations){
            strings[i] = i.toString()
        }
        @Suppress("UNCHECKED_CAST")
        strings as Array<String>
        strings.shuffle()
        var i = 0
        for (s in strings) {
            assert(s !in h)
            h[s] = i
            assert(s in h)
            assert(h[s] == i)
            assert(h.size == i + 1)
            i++
        }
    }

    @Test
    fun testRemoveFunction() {
        val hashTable = HashTable<String, Int>()

        // Step 1: Insert key-value pairs
        hashTable["one"] = 1
        hashTable["two"] =  2
        hashTable["three"] = 3
        hashTable["four"] = 4
        hashTable["five"] = 5
        hashTable["six"] = 6
        hashTable["seven"] = 7
        hashTable["eight"] = 8
        hashTable["nine"] = 9

        assert(hashTable.contains("two"))
        assert(hashTable.size == 9)

        hashTable.remove("two")


        assert(!hashTable.contains("two"))
        assert(hashTable.size == 8)

        val remainingKeys = listOf("one", "three", "four", "five", "six", "seven", "eight", "nine")
        var allPresent = true
        for (key in remainingKeys) {
            if (!hashTable.contains(key)) {
                println("Missing key after removal: $key")
                allPresent = false
            }
        }

        assert(allPresent)

        hashTable.remove("non-existent")
        assert(hashTable.size == 8)
        assert(hashTable.occupied == 9)
    }

    @Test
    fun testContains() {
        val hashTable = HashTable<String, Int>()

        // Step 1: Test with a range of keys and values
        println("Inserting a wide range of keys and values...")
        for (i in 1..1000) {
            hashTable.set("key$i", i)
        }

        // Verify presence for a subset of inserted keys
        println("Verifying presence for a subset of inserted keys...")
        listOf(1, 100, 500, 999, 1000).forEach {
            assert(hashTable.contains("key$it")) { "Contains failed for 'key$it'" }
        }

        // Step 2: Test with special and edge case keys
        println("Testing with special and edge case keys...")
        val specialKeys = listOf(" ", "", "null", "NaN", "Infinity", "\n", "\u0000", "🍎")
        specialKeys.forEach { key ->
            hashTable.set(key, 0) // Using 0 as a placeholder value
            assert(hashTable.contains(key)) { "Contains failed for special key '$key'" }
        }

        // Step 3: Remove keys and verify they are no longer present
        println("Removing keys and verifying absence...")
        listOf("key100", "key500", " ").forEach { key ->
            hashTable.remove(key)
            assert(!hashTable.contains(key)) { "Contains incorrectly found removed key '$key'" }
        }

        // Step 4: Verify absence of never-inserted keys
        println("Verifying absence of never-inserted keys...")
        listOf("key1001", "unknown", "void").forEach { key ->
            assert(!hashTable.contains(key)) { "Contains incorrectly found never-inserted key '$key'" }
        }

        // Step 5: Insert enough items to ensure resizing happens, if applicable
        // This is to test that `contains` behaves correctly before and after a resize
        println("Testing behavior around resizing...")
        val initialSize = hashTable.size
        (1001..2000).forEach { i ->
            hashTable.set("key$i", i)
        }
        assert(hashTable.size > initialSize) { "Expected the hash table to grow in size" }
        // Verify a few keys across the resize boundary
        listOf("key1", "key1500", "key2000").forEach { key ->
            assert(hashTable.contains(key)) { "Contains failed for '$key' after resizing" }
        }

        println("All comprehensive contains tests passed.")
    }


    @Test
    fun testLots() {
        for (x in 0..<100) {
            val h = HashTable<String, Int>()
            val tweak = "Tweak:" + Random.nextInt().toString()
            val iterations = 10000
            var strings : Array<String?> = arrayOfNulls(iterations)
            for (i in 0 ..< iterations){
                strings[i] = i.toString() + tweak
            }
            @Suppress("UNCHECKED_CAST")
            strings as Array<String>
            strings.shuffle()
            var i = 0
            for (s in strings) {
                assert(s !in h)
                h[s] = i
                assert(s in h)
                assert(h[s] == i)
                assert(h.size == i + 1)
                i++
            }
            strings.shuffle()
            i = 0
            for (s in strings) {
                assert(s in h)
                h.remove(s)
                assert(s !in h)
                assert(h[s] == null)
                if(i < iterations -1 ) {
                    h[strings[i + 1]] = i
                    assert(h[strings[i+1]] == i)
                }
                assert(h.size == iterations - 1 - i)
                assert(h.occupied == iterations)
                i++
            }
            val savedSize = h.storage.size
            strings.shuffle()
            i = 0
            for (s in strings) {
                assert(s !in h)
                h[s] = i
                assert(h.size == i + 1)
                assert(h[s] == i)
                assert(h.occupied == iterations)
                i++
            }
            // There shouldn't be any growth in the internal storage
            // as deleted items just get re-overwritten.
            assert(h.storage.size == savedSize)
        }
    }
}