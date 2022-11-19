<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- import the default junit-frames.xsl stylesheet
         maven-dependency-plugin unpacks this default stylesheet from ant-optional-1.5.3-1.jar,
         renames it and puts into target folder-->
    <xsl:import href="target/junit-frames-base.xsl"/>

    <!-- override the template producing the test table header -->
    <xsl:template name="testcase.test.header">
        <xsl:param name="show.class" select="''"/>
        <tr valign="top">
            <xsl:if test="boolean($show.class)">
                <th>Class</th>
            </xsl:if>
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
        <xsl:param name="show.class" select="''"/>
        <tr valign="top">
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="error">Error</xsl:when>
                    <xsl:when test="failure">Failure</xsl:when>
                    <xsl:otherwise>TableRowColor</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:variable name="class.href">
                <xsl:value-of select="concat(translate(../@package,'.','/'), '/', ../@id, '_', ../@name, '.html')"/>
            </xsl:variable>
            <xsl:if test="boolean($show.class)">
                <td><a href="{$class.href}"><xsl:value-of select="../@name"/></a></td>
            </xsl:if>
            <td>
                <a name="{@name}"/>
                <xsl:choose>
                    <xsl:when test="boolean($show.class)">
                        <a href="{concat($class.href, '#', @name)}"><xsl:value-of select="@name"/></a>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="@name"/>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
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

<!-- Added screenshot link for failed tests https://stackoverflow.com/questions/1727616/custom-junit-report -->
            <td>
                <!-- "org.groundwork.tests.ElementsStatsTest" -> "org/groundwork/tests/ElementsStatsTest" -->
                <xsl:variable name="class.name">
                    <xsl:value-of select="translate(@classname,'.','/')"/>
                </xsl:variable>
                <!-- "../@package" (= package attribute of testsuite element [testcase's parent])
                     "org.groundwork.tests"  -> "../../../" -->
                <xsl:variable name="junit.base">
                    <xsl:call-template name="path"><xsl:with-param name="path" select="../@package"/></xsl:call-template>
                </xsl:variable>
                <xsl:choose>
                    <!--location of ElementsStatsTest.html file is
                            selenium-junit-practice/target/site/junitreport/org/groundwork/tests
                        location of screenshot for test org.groundwork.tests.ElementsStatsTest#testStats
                            selenium-junit-practice/target/site/junitreport/org/groundwork/tests/ElementsStatsTest/testStats.png
                        link to screenshot in ElementsStatsTest.html is the relative path
                            ../../../org/groundwork/tests/ElementsStatsTest/testStats.png-->
                    <xsl:when test="failure">
                        <a href="{concat($junit.base,$class.name,'/',@name,'.png')}"><xsl:value-of select="@name"/></a>
                    </xsl:when>
                    <xsl:when test="error">
                        <a href="{concat($junit.base,$class.name,'/',@name,'.png')}"><xsl:value-of select="@name"/></a>
                    </xsl:when>
                </xsl:choose>
            </td>

        </tr>
    </xsl:template>

</xsl:stylesheet>

