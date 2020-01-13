import kotlin.math.abs
import kotlin.properties.Delegates

private fun sqr(x: Int) = x * x

private fun sqr(x: Double) = x * x

sealed class Node {
    abstract fun printNode()

    abstract fun traverse(vertexNumber: Int): Int

    var vertexNumber by Delegates.notNull<Int>()
}

class InnerNode(
        private val left: Node,
        private val right: Node,
        private val splitFeature: Int,
        private val splitValue: Double
) : Node() {
    override fun printNode() {
        println("Q $splitFeature $splitValue ${left.vertexNumber} ${right.vertexNumber}")
        left.printNode()
        right.printNode()
    }

    override fun traverse(vertexNumber: Int): Int {
        this.vertexNumber = vertexNumber
        val leftVertexNumber = vertexNumber + 1
        val leftTreeSize = left.traverse(leftVertexNumber)

        val rightVertexNumber = leftVertexNumber + leftTreeSize
        val rightTreeSize = right.traverse(rightVertexNumber)

        return leftTreeSize + 1 + rightTreeSize
    }
}

class LeafNode(private val resultClass: Int) : Node() {
    override fun printNode() {
        println("C $resultClass")
    }

    override fun traverse(vertexNumber: Int): Int {
        this.vertexNumber = vertexNumber
        return 1
    }
}

data class ObjectWithClass(val features: IntArray, val objectClass: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ObjectWithClass

        if (!features.contentEquals(other.features)) return false
        if (objectClass != other.objectClass) return false

        return true
    }

    override fun hashCode(): Int {
        var result = features.contentHashCode()
        result = 31 * result + objectClass
        return result
    }
}

class DecisionTreeClassifier(
        private val maxHeight: Int,
        private val classesCount: Int,
        private val featuresCount: Int
) {
    lateinit var root: Node

    fun fit(objects: Array<ObjectWithClass>) {
        root = buildTree(objects, 0)
    }

    private fun buildTree(objects: Array<ObjectWithClass>, curHeight: Int): Node {
        val objectsCountByClass = IntArray(classesCount)
        for (obj in objects) {
            objectsCountByClass[obj.objectClass - 1] += 1
        }
        for (i in 0 until classesCount) {
            if (objectsCountByClass[i] == objects.size) {
                return LeafNode(i + 1)
            }
        }
        if (curHeight == maxHeight) {
            var maxObjectsInClass = -1
            var biggestClass = -1
            for (i in 0 until classesCount) {
                if (maxObjectsInClass < objectsCountByClass[i]) {
                    biggestClass = i
                    maxObjectsInClass = objectsCountByClass[i]
                }
            }
            return LeafNode(biggestClass + 1)
        }

        var splitFeature = -1
        var splitIndex = -1
        var splitValue = -1.0
        var maxCriterion = -1.0

        for (curFeatureIndex in 0 until featuresCount) {
            objects.sortBy { it.features[curFeatureIndex] }

            val objectsCountByClassLeft = IntArray(classesCount)
            val objectsCountByClassRight = IntArray(classesCount) { i -> objectsCountByClass[i] }
            var criterionLeft = 0.0
            var criterionRight = 0.0
            for (i in 0 until classesCount) {
                criterionRight += sqr(objectsCountByClassRight[i]).toDouble()
            }
            criterionRight /= objects.size.toDouble()
            var maxLeftObjectIndex = 0

            while (maxLeftObjectIndex < objects.size - 1) {
                if (objects[maxLeftObjectIndex].features[curFeatureIndex] ==
                        objects[objects.size - 1].features[curFeatureIndex]
                ) {
                    break
                }

                fun processSingleElement() {
                    val curClass = objects[maxLeftObjectIndex].objectClass - 1
                    criterionRight =
                            criterionRight * (objects.size - maxLeftObjectIndex) - sqr(objectsCountByClassRight[curClass])
                    objectsCountByClassRight[curClass] -= 1
                    criterionRight = (criterionRight + sqr(objectsCountByClassRight[curClass])) /
                            (objects.size - maxLeftObjectIndex - 1)
                    criterionLeft = criterionLeft * maxLeftObjectIndex - sqr(objectsCountByClassLeft[curClass])
                    objectsCountByClassLeft[curClass] += 1
                    criterionLeft = (criterionLeft + sqr(objectsCountByClassLeft[curClass])) /
                            (maxLeftObjectIndex + 1)
                }

                while (maxLeftObjectIndex != objects.size - 2 &&
                        objects[maxLeftObjectIndex].features[curFeatureIndex] ==
                        objects[maxLeftObjectIndex + 1].features[curFeatureIndex]
                ) {
                    processSingleElement()
                    maxLeftObjectIndex++
                }
                processSingleElement()
                if (criterionLeft + criterionRight > maxCriterion ||
                        (criterionLeft + criterionRight == maxCriterion &&
                                abs(objects.size / 2 - splitIndex) >
                                abs(objects.size / 2 - maxLeftObjectIndex - 1))
                ) {
                    maxCriterion = criterionLeft + criterionRight
                    splitFeature = curFeatureIndex + 1
                    splitValue = (objects[maxLeftObjectIndex].features[curFeatureIndex] +
                            objects[maxLeftObjectIndex + 1].features[curFeatureIndex]) / 2.0
                    splitIndex = maxLeftObjectIndex + 1
                }
                maxLeftObjectIndex++
            }
        }

        objects.sortBy { it.features[splitFeature - 1] }
        val leftObjects = Array(splitIndex) { i -> objects[i] }
        val rightObjects = Array(objects.size - splitIndex) { i -> objects[splitIndex + i] }
        val leftNode = buildTree(leftObjects, curHeight + 1)
        val rightNode = buildTree(rightObjects, curHeight + 1)

        return InnerNode(leftNode, rightNode, splitFeature, splitValue)
    }
}

fun main() {
    val (m, k, h) = readLine()!!.split(" ").map { it.toInt() }
    val n = readLine()!!.toInt()
    val objects = Array(n) { _ ->
        val line = readLine()!!.split(" ").map { it.toInt() }
        val features = line.subList(0, line.size - 1).toIntArray()
        val objectClass = line[line.size - 1]
        ObjectWithClass(features, objectClass)
    }
    val tree = DecisionTreeClassifier(h, k, m)
    tree.fit(objects)
    println(tree.root.traverse(1))
    tree.root.printNode()
}