<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" exclude-result-prefixes="owl rdf">
    <xsl:output method="text" />
    <xsl:template match="/">
        {
        "id": "spdx-checksum-algorithm",
        "resource": "http://spdx.org/rdf/terms#ChecksumAlgorithm",
        "vocab": [
        <xsl:for-each select="/rdf:RDF/owl:NamedIndividual[contains(@rdf:about, 'checksumAlgorithm')]">
            <xsl:variable name="id"><xsl:value-of select="substring-after(substring-after(@rdf:about, '#'), '_')"/></xsl:variable>
            {
            "id": "<xsl:value-of select="$id"/>",
            "pref_label": {
                "en": "<xsl:value-of select="upper-case($id)"/>"
            },
            "resource": "<xsl:value-of select="@rdf:about"/>",
            "in_scheme": [
                "http://spdx.org/rdf/terms#ChecksumAlgorithm"
            ]
            }<xsl:if test="position() != last()">,</xsl:if>
        </xsl:for-each>
        ]
        }
    </xsl:template>
</xsl:stylesheet>
