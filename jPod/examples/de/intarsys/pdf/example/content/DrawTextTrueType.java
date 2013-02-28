/*
 * Copyright (c) 2007, intarsys consulting GmbH
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of intarsys nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.intarsys.pdf.example.content;

import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Seconds;

import de.intarsys.pdf.content.common.CSCreator;
import de.intarsys.pdf.example.common.CommonJPodExample;
import de.intarsys.pdf.font.PDFont;
import de.intarsys.pdf.font.outlet.FontFactoryException;
import de.intarsys.pdf.font.outlet.FontOutlet;
import de.intarsys.pdf.font.outlet.FontQuery;
import de.intarsys.pdf.font.outlet.IFontFactory;
import de.intarsys.pdf.pd.PDPage;

/**
 * Create a new document and its content. The content is text with two different
 * TrueType fonts.
 */
public class DrawTextTrueType extends CommonJPodExample {

	public static void main(String[] args) {
		DateTime dt1 = new DateTime();
		
		DrawTextTrueType client = new DrawTextTrueType();
		try {
			client.run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DateTime dt2 = new DateTime();
		
		int seconds = Seconds.secondsBetween(dt1, dt2).getSeconds();
		System.out.println(seconds);
	}

	public PDPage addPage() {
		// Then create the first page.
		PDPage page = (PDPage) PDPage.META.createNew();
		// add page to the document.
		getDoc().addPageNode(page);
		return page;
	}

	public void draw(PDPage page) {
		//
		IFontFactory factory = FontOutlet.get().lookupFontFactory(getDoc());
		FontQuery query;
		PDFont font;
		float fontSize;

		// open a device to the page content stream
		CSCreator creator = CSCreator.createNew(page);
		//
		query = new FontQuery("Arial", ""); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			font = factory.getFont(query);
		} catch (FontFactoryException e) {
			e.printStackTrace();
			return;
		}
		fontSize = 20f;
		//
		creator.textSetFont(null, font, fontSize);
		creator.textLineMoveTo(100, 700);
		creator.textShow("Hello, world."); //$NON-NLS-1$
		//

		query = new FontQuery("Arial", ""); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			font = factory.getFont(query);
		} catch (FontFactoryException e) {
			e.printStackTrace();
			return;
		}
		fontSize = 20f;
		//
		creator.textSetFont(null, font, fontSize);
		creator.textLineMoveTo(100, 600);
		creator.textShow("...feel the difference"); //$NON-NLS-1$

		// Don't forget to flush the content.
		creator.close();
	}

	public void run(String[] args) throws Exception {
		if (args.length < 1) {
			usage();
			return;
		}
		try {
			String outputFileName = args[0];
			create();
			for(int i=0;i<100;i++){
				PDPage page = addPage();
				draw(page);	
			}

			save(outputFileName);
		} finally {
			close();
		}
	}

	/**
	 * Help the user.
	 */
	public void usage() {
		System.out.println("usage: java.exe " + getClass().getName() //$NON-NLS-1$
				+ " <output-filename>"); //$NON-NLS-1$
	}
}
