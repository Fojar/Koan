package koan.gui

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
        if (leaf != null) tree.setSelectionPath(TreePath(leaf.path));
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

        val dirNodes = ArrayDeque<DefaultMutableTreeNode>()

        Files.walkFileTree(Resources.drawingsPath, emptySet(), 2, object : SimpleFileVisitor<Path>() {

            override fun visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult {
                val filename = path.fileName.toString()
                if (attrs.isRegularFile && !filename.contains('$') && filename.endsWith(".class")) {
                    val userObject = DrawingClassNode(path)
                    dirNodes.peek().add(DefaultMutableTreeNode(userObject))
                }
                return FileVisitResult.CONTINUE
            }

            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                dirNodes.push(DefaultMutableTreeNode(dir.fileName))
                return FileVisitResult.CONTINUE
            }

            override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
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

class DrawingClassNode(drawingClassPath: Path) {

    val qualifiedClassName = Resources.toClassName(drawingClassPath)
    val simpleName = drawingClassPath.fileName.toString().removeSuffix(".class")

    override fun toString() = simpleName
}

