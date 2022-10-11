package com.nkrasko.extracts.idea.plugin

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vcs.IssueNavigationConfiguration
import com.intellij.openapi.vcs.VcsBundle
import com.intellij.vcs.log.ui.table.GraphTableModel
import com.intellij.vcs.log.ui.table.VcsLogGraphTable
import com.intellij.vcs.log.ui.table.column.VcsLogCustomColumn
import javax.swing.table.TableCellRenderer

@Suppress("UnstableApiUsage")
class LinksColumn: VcsLogCustomColumn<LinksCell> {
    override val id: String = javaClass.canonicalName
    override val isDynamic: Boolean = true
    override val localizedName: String = VcsBundle.message("issue.link.issue.column")

    override fun createTableCellRenderer(table: VcsLogGraphTable): TableCellRenderer {
        return LinksCellRenderer()
    }

    override fun getStubValue(model: GraphTableModel): LinksCell {
        return LinksCell.EMPTY
    }

    override fun getValue(model: GraphTableModel, row: Int): LinksCell {
        val issueNavigationConfiguration = IssueNavigationConfiguration.getInstance(model.logData.project)
        val fullMessage = model.getCommitMetadata(row).fullMessage

        val issueLinks: List<IssueNavigationConfiguration.LinkMatch> =
            issueNavigationConfiguration.findIssueLinks(fullMessage)
        if (issueLinks.isEmpty()) return LinksCell.EMPTY

        val links = issueLinks.mapNotNull { match ->
            val text = fullMessage.substring(match.range.toKotlinRange())
            if (text != match.targetUrl) {
                LinksCell.Link(text, match.targetUrl)
            } else {
                // Ignore direct links
                null
            }
        }

        return LinksCell(links)
    }

    companion object {
        private fun TextRange.toKotlinRange(): IntRange = startOffset until endOffset
    }
}