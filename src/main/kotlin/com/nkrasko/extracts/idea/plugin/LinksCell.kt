package com.nkrasko.extracts.idea.plugin

class LinksCell(val links: Collection<Link>) {
    class Link(val text: String, val url: String)

    companion object {
        val EMPTY = LinksCell(emptyList())
    }
}