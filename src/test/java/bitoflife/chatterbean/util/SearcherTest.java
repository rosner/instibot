/*
Copyleft (C) 2005 H�lio Perroni Filho
xperroni@bol.com.br
ICQ: 2490863

This file is part of ChatterBean.

ChatterBean is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

ChatterBean is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with ChatterBean (look at the Documents/ directory); if not, either write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA, or visit (http://www.gnu.org/licenses/gpl.txt).
 */

package bitoflife.chatterbean.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.TestCase;

import org.junit.Before;

public class SearcherTest extends TestCase {

    protected Searcher searcher;
    
    protected static final String RESOURCE_PATH = "bitoflife/chatterbean/alice/bots";

    @Before
    protected void setUp() {
	searcher = new Searcher();
    }
    
//    public void testRecursiveResourceSearching() throws IOException {
//	InputStream[] paths = searcher.search(RESOURCE_PATH);
//
//	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paths[0]));
//	String line = null;
//	StringBuffer stringBuffer = new StringBuffer();
//	if ((line = bufferedReader.readLine()) != null) {
//	    
//	}
//	assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>
//
//<aiml>
//  <category>
//    <pattern>_ ALICE</pattern>
//    <template><sr/></template>
//  </category>
//  <category>
//    <pattern>_ AGAIN</pattern>
//    <template>Once more? <sr/></template>
//  </category>
//  <category>
//    <pattern>YOU MAY *</pattern>
//    <template><sr/></template>
//  </category>
//  <category>
//    <pattern>SAY *</pattern>
//    <template>&quot;<star/>&quot;.</template>
//  </category>
//</aiml>
//", stringBuffer.toString());
//	assertEquals("Again.aiml", paths[0]);
//	assertEquals("Alice.aiml", paths[1]);
//	assertEquals("Astrology.aiml", paths[2]);
//    }
}
