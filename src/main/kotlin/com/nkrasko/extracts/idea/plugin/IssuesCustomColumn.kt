package com.nkrasko.extracts.idea.plugin

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vcs.IssueNavigationConfiguration
import com.intellij.openapi.vcs.IssueNavigationConfiguration.LinkMatch
import com.intellij.openapi.vcs.VcsBundle
import com.intellij.vcs.log.graph.DefaultColorGenerator
import com.intellij.vcs.log.paint.GraphCellPainter
import com.intellij.vcs.log.paint.SimpleGraphCellPainter
import com.intellij.vcs.log.ui.render.GraphCommitCell
import com.intellij.vcs.log.ui.render.GraphCommitCellRenderer
import com.intellij.vcs.log.ui.table.GraphTableModel
import com.intellij.vcs.log.ui.table.VcsLogGraphTable
import com.intellij.vcs.log.ui.table.column.VcsLogCustomColumn
import javax.swing.table.TableCellRenderer

@Suppress("UnstableApiUsage")
class IssuesCustomColumn : VcsLogCustomColumn<GraphCommitCell> {
    override val id: String = javaClass.canonicalName
    override val isDynamic: Boolean = true
    override val localizedName: String = VcsBundle.message("issue.link.issue.column")

    override fun createTableCellRenderer(table: VcsLogGraphTable): TableCellRenderer {
        val graphCellPainter: GraphCellPainter = object : SimpleGraphCellPainter(DefaultColorGenerator()) {
            override fun getRowHeight(): Int = table.rowHeight
        }

        return GraphCommitCellRenderer(table.logData, graphCellPainter, table)
    }

    override fun getStubValue(model: GraphTableModel): GraphCommitCell = GraphCommitCell("", emptyList(), emptyList())

    override fun getValue(model: GraphTableModel, row: Int): GraphCommitCell {
        val issueNavigationConfiguration = IssueNavigationConfiguration.getInstance(model.logData.project)
        val fullMessage = model.getCommitMetadata(row).fullMessage
        val issueLinks: List<LinkMatch> = issueNavigationConfiguration.findIssueLinks(fullMessage)
        val text = linksToText(fullMessage, issueLinks)

        return GraphCommitCell(
            text,
            emptyList(),
            emptyList()
        )
    }

    companion object {
        private fun TextRange.toKotlinRange(): IntRange = startOffset until endOffset

        fun linksToText(message: String, links: List<LinkMatch>): String {
            return links
                .map { link ->
                    message.substring(link.range.toKotlinRange())
                }
                .toSet()
                .joinToString(" ")
        }
    }
}