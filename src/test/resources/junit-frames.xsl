<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- import the default junit-frames.xsl stylesheet
         maven-dependency-plugin unpacks this default stylesheet from ant-optional-1.5.3-1.jar,
         renames it and puts into target folder-->
    <xsl:import href="../../../target/junit-frames-base.xsl"/>

    <!-- override the template producing the test table header -->
    <xsl:template name="testcase.test.header">
        <tr valign="top">
            <th>Name</th>
            <th>Status</th>
            <th width="80%">Type</th>
            <th nowrap="nowrap">Time(s)</th>

            <!-- ADDED -->
            <th>Screenshot</th>
        </tr>
    </xsl:template>

    <!-- override the template producing a test table row -->
    <xsl:template match="testcase" mode="print.test">
        <tr valign="top">
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="error">Error</xsl:when>
                    <xsl:when test="failure">Failure</xsl:when>
                    <xsl:otherwise>TableRowColor</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <td><xsl:value-of select="@name"/></td>
            <xsl:choose>
                <xsl:when test="failure">
                    <td>Failure</td>
                    <td><xsl:apply-templates select="failure"/></td>
                </xsl:when>
                <xsl:when test="error">
                    <td>Error</td>
                    <td><xsl:apply-templates select="error"/></td>
                </xsl:when>
                <xsl:otherwise>
                    <td>Success</td>
                    <td></td>
                </xsl:otherwise>
            </xsl:choose>
            <td>
                <xsl:call-template name="display-time">
                    <xsl:with-param name="value" select="@time"/>
                </xsl:call-template>
            </td>

            <!-- Added screenshot link for failed and error tests  -->
            <td>
                <xsl:variable name="class_name">
                    <xsl:call-template name="substring-after-last-dot">
                        <xsl:with-param name="str" select="@classname"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="failure">
                        <a href="{concat($class_name,'/',@name,'.png')}"><xsl:value-of select="@name"/></a>
                    </xsl:when>
                    <xsl:when test="error">
                        <a href="{concat($class_name,'/',@name,'.png')}"><xsl:value-of select="@name"/></a>
                    </xsl:when>
                </xsl:choose>
            </td>

        </tr>
    </xsl:template>

    <xsl:template name="substring-after-last-dot">
        <xsl:param name="str"/>
        <xsl:if test="contains($str,'.')">
            <xsl:call-template name="substring-after-last-dot">
                <xsl:with-param name="str" select="substring-after($str,'.')"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="not(contains($str,'.'))">
            <xsl:value-of select="$str"/>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>

