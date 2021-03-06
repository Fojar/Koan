package koan.gui

import koan.Drawing
import koan.DrawingClassLoader
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.tree.*

object Resources {

    val classesRootPath = Paths.get(javaClass.getResource("/").toURI())
    val drawingsPath = classesRootPath.resolve("drawings")

    fun toClassName(path: Path): String {
        return drawingsPath.relativize(path).toString()
            .removeSuffix(".class")
            .replace(File.separatorChar, '.')
    }
}

class DrawingTreeView(changeHandler: (className: String) -> Unit) : JScrollPane() {

    val tree = JTree(availableDrawings()).apply tree@{
        isRootVisible = false
        selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION

        addTreeSelectionListener {
            val node = this@tree.lastSelectedPathComponent as DefaultMutableTreeNode?
            (node?.userObject as? DrawingClassNode)?.let {
                changeHandler(it.qualifiedClassName)
            }
        }
    }

    init {
        setViewportView(tree)
        selectFirstDrawing()
    }

    fun selectFirstDrawing() {
        val root = tree.model.root as DefaultMutableTreeNode
        val leaf = findFirstLeaf(root)
        if (leaf != null) tree.selectionPath = TreePath(leaf.path);
    }

    fun findFirstLeaf(node: DefaultMutableTreeNode): DefaultMutableTreeNode? {
        if (node.isLeaf) return node

        val deepNodes = mutableListOf<TreeNode>()

        for (child in node.children()) {
            child as DefaultMutableTreeNode
            if (child.isLeaf) return child
            else deepNodes.add(child)
        }

        for (child in deepNodes) {
            child as DefaultMutableTreeNode
            val result = findFirstLeaf(child)
            if (result != null) return result
        }

        return null
    }


    fun refreshModel() {
        tree.model = availableDrawings()
    }

    private fun availableDrawings(): TreeModel {

        // A stack of directory nodes built as we walk the file tree.
        val dirNodes = ArrayDeque<DefaultMutableTreeNode>()

        val classLoader = DrawingClassLoader(javaClass.classLoader)

        Files.walkFileTree(Resources.drawingsPath, emptySet(), 3, object : SimpleFileVisitor<Path>() {

            override fun visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult {
                val filename = path.fileName.toString()
                if (attrs.isRegularFile && !filename.contains('$') && filename.endsWith(".class")) {

                    // Only add drawings to the list of classes.
                    val userObject = DrawingClassNode(path)
                    val className = "drawings.${userObject.qualifiedClassName}"
                    val loadedClass = classLoader.loadClass(className)

                    if (Drawing::class.java.isAssignableFrom(loadedClass)) {
                        dirNodes.peek().add(DefaultMutableTreeNode(userObject))
                    }
                }
                return FileVisitResult.CONTINUE
            }

            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                dirNodes.push(DefaultMutableTreeNode(dir.fileName))
                return FileVisitResult.CONTINUE
            }

            override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {

                dirNodes.peek().sortContentsByType()

                if (dirNodes.size > 1) {
                    val finishedDir = dirNodes.pop()
                    if (finishedDir.childCount > 0) dirNodes.peek().add(finishedDir)
                }

                return FileVisitResult.CONTINUE
            }

        })

        return DefaultTreeModel(dirNodes.peek())
    }

}

fun DefaultMutableTreeNode.sortContentsByType() {

    if (childCount == 0) return

    val dirs = mutableListOf<DefaultMutableTreeNode>()
    val files = mutableListOf<DefaultMutableTreeNode>()

    for (child in children() as Enumeration<DefaultMutableTreeNode>) {
        (if (child.userObject is DrawingClassNode) files else dirs).add(child)
    }

    removeAllChildren()
    dirs.forEach { add(it) }
    files.forEach { add(it) }
}

class DrawingClassNode(drawingClassPath: Path) : Comparable<DrawingClassNode> {
    val qualifiedClassName = Resources.toClassName(drawingClassPath)
    val simpleName = drawingClassPath.fileName.toString().removeSuffix(".class")

    override fun toString() = simpleName
    override fun compareTo(other: DrawingClassNode) = simpleName.compareTo(other.simpleName)
}

