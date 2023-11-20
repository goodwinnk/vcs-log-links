package com.nkrasko.extracts.idea.plugin

class LinksCell(linksRaw: Collection<Link>) {
    val links: List<Link> = linksRaw.distinctBy { it.text }.sortedBy { it.text }

    class Link(val text: String, val url: String)

    companion object {
        val EMPTY = LinksCell(emptyList())
    }

    override fun toString(): String {
        return links.joinToString(" ") { it.text }
    }
}