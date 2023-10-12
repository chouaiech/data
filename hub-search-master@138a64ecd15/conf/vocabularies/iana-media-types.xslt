<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:iana="http://www.iana.org/assignments" exclude-result-prefixes="iana">
    <xsl:output method="text" />
    <xsl:template match="/">
        {
        "id": "iana-media-types",
        "resource": "https://www.iana.org/assignments/media-types",
        "vocab": [
        <xsl:for-each select="/iana:registry/iana:registry">
            <xsl:variable name="parent_position" select="position()" />
            <xsl:variable name="parent_last" select="last()" />
            <xsl:variable name="in_scheme">https://www.iana.org/assignments/media-types/<xsl:value-of select="iana:title"/></xsl:variable>
            <xsl:for-each select="iana:record">
                <xsl:variable name="file"><xsl:value-of select="iana:file"/></xsl:variable>
                <xsl:if test="$file != ''">
                    {
                    "id": "<xsl:value-of select="replace($file,'[+./]','-')"/>",
                    "pref_label": {
                        "en": "<xsl:value-of select="$file"/>"
                    },
                    "resource": "https://www.iana.org/assignments/media-types/<xsl:value-of select="$file"/>",
                    "in_scheme": [
                        "<xsl:value-of select="$in_scheme"/>"
                    ]
                    }<xsl:choose>
                    <xsl:when test="$parent_position != $parent_last">,</xsl:when>
                    <xsl:otherwise><xsl:if test="position() != last()">,</xsl:if></xsl:otherwise>
                </xsl:choose>
                </xsl:if>
            </xsl:for-each>
        </xsl:for-each>
        ]
        }
    </xsl:template>
</xsl:stylesheet>
