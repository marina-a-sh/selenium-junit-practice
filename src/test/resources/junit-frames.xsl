<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!--
 The Apache Software License, Version 1.1

 Copyright (c) 2001-2002 The Apache Software Foundation.  All rights
 reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in
    the documentation and/or other materials provided with the
    distribution.

 3. The end-user documentation included with the redistribution, if
    any, must include the following acknowlegement:
       "This product includes software developed by the
        Apache Software Foundation (http://www.apache.org/)."
    Alternately, this acknowlegement may appear in the software itself,
    if and wherever such third-party acknowlegements normally appear.

 4. The names "Ant" and "Apache Software
    Foundation" must not be used to endorse or promote products derived
    from this software without prior written permission. For written
    permission, please contact apache@apache.org.

 5. Products derived from this software may not be called "Apache"
    nor may "Apache" appear in their names without prior written
    permission of the Apache Group.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
 ====================================================================

 This software consists of voluntary contributions made by many
 individuals on behalf of the Apache Software Foundation.  For more
 information on the Apache Software Foundation, please see
 <http://www.apache.org/>.
 -->

<!--

 Sample stylesheet to be used with An JUnitReport output.

 It creates a set of HTML files a la javadoc where you can browse easily
 through all packages and classes.

 @author Stephane Bailliez <a href="mailto:sbailliez@apache.org"/>
 @author Erik Hatcher <a href="mailto:ehatcher@apache.org"/>

-->

<!--
 Sample stylesheet to be used with JUnitReport output
 with addition of links to screenshots on failure and error
-->

    <!-- maven-dependency-plugin unpacks sample stylesheet junit-frames.xsl from ant-optional-1.5.3-1.jar,
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

