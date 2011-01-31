/*
Copyleft (C) 2005 H�lio Perroni Filho
xperroni@yahoo.com
ICQ: 2490863

This file is part of ChatterBean.

ChatterBean is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

ChatterBean is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with ChatterBean (look at the Documents/ directory); if not, either write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA, or visit (http://www.gnu.org/licenses/gpl.txt).
 */

package bitoflife.chatterbean;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import bitoflife.chatterbean.parser.AliceBotParser;
import bitoflife.chatterbean.util.Searcher;

public class AliceBotMother {
    
    private static final String ALICE_BOT_RESOURCE_PATH = "bitoflife/chatterbean/alice/bots";
    
    protected ByteArrayOutputStream gossip;

    public void setUp() {
	gossip = new ByteArrayOutputStream();
    }

    public String gossip() {
	return gossip.toString();
    }

    public AliceBot newInstance() throws Exception {
	Searcher searcher = new Searcher();
	AliceBotParser parser = new AliceBotParser();
	
	InputStream contextInputStream = AliceBot.class.getResourceAsStream("context.xml");
	InputStream splittersInputStream = AliceBot.class.getResourceAsStream("splitters.xml");
	InputStream substitutionsInputStream = AliceBot.class.getResourceAsStream("substitutions.xml");
	
	AliceBot bot = parser.parse(
		contextInputStream, 
		splittersInputStream,
		substitutionsInputStream,
		searcher.search(ALICE_BOT_RESOURCE_PATH));

	Context context = bot.getContext();
	context.outputStream(gossip);
	return bot;
    }
}
