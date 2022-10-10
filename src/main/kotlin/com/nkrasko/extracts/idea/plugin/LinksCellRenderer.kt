package com.nkrasko.extracts.idea.plugin

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.ui.JBUI
import com.intellij.vcs.log.ui.render.GraphCommitCellRenderer
import com.intellij.vcs.log.ui.table.VcsLogCellRenderer
import com.intellij.vcs.log.ui.table.VcsLogGraphTable
import com.intellij.vcs.log.util.VcsLogUiUtil
import java.awt.Color
import java.awt.Font
import javax.swing.JTable
import javax.swing.UIManager

class LinksCellRenderer : ColoredTableCellRenderer(), VcsLogCellRenderer {
    init {
        cellState = GraphCommitCellRenderer.BorderlessTableCellState()
    }

    override fun customizeCellRenderer(
        table: JTable,
        value: Any?,
        selected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ) {
        if (value !is LinksCell || table !is VcsLogGraphTable) {
            return
        }

        val linkAttributes = getLinkAttributes()
        for (link in value.links) {
            append(link.text, linkAttributes, BrowserLauncherTag(link.url))
        }
    }

    override fun getPreferredWidth(table: JTable): Int {
        val font = UIManager.getFont("Table.font")
        return table.getFontMetrics(font.deriveFont(Font.BOLD)).stringWidth("XXXX-XXXXXX") +
                VcsLogUiUtil.getHorizontalTextPadding(this)
    }

    companion object {
        private fun getLinkAttributes(): SimpleTextAttributes {
            val baseStyle = SimpleTextAttributes.REGULAR_ATTRIBUTES
            val color = baseStyle.fgColor
            val alpha = color?.alpha ?: 255
            val linkColor = JBUI.CurrentTheme.Link.Foreground.ENABLED
            val resultColor = Color(linkColor.red, linkColor.green, linkColor.blue, alpha)
            return SimpleTextAttributes(baseStyle.style or SimpleTextAttributes.STYLE_UNDERLINE, resultColor)
        }
    }
}