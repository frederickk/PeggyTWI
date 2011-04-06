package peggyTWI;

/*
 *  PeggyTWI 000
 *  
 *	This library is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU Lesser General Public
 *	License as published by the Free Software Foundation; either
 *	version 2.1 of the License, or (at your option) any later version.
 * 
 *	This library is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *	Lesser General Public License for more details.
 * 
 *	You should have received a copy of the GNU Lesser General
 *	Public License along with this library; if not, write to the
 *	Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *	Boston, MA  02111-1307  USA
 *
 *
 *	All Code by
 *	Jay Clegg
 *
 *	http://planetclegg.com/projects/
 *
 *
 *	Library by
 *  Ken Frederick
 *  ken.frederick@gmx.de
 *
 *  http://cargocollective.com/kenfrederick/
 *  http://kenfrederick.blogspot.com/
 *
 */

//-----------------------------------------------------------------------------
//libraries
//-----------------------------------------------------------------------------
import processing.core.PApplet;
import processing.core.PImage;
import processing.serial.*;

public class PeggyTWI {
	//-----------------------------------------------------------------------------
	//properties
	//-----------------------------------------------------------------------------
	protected static PApplet p5;
	private Serial arduino;

	//-----------------------------------------------------------------------------
	//arduino -> peggy
	//-----------------------------------------------------------------------------
	private PImage peggyImage = new PImage(25,25);
	private byte [] peggyHeader = new byte[] { 
			(byte)0xde, (byte)0xad, (byte)0xbe,(byte)0xef, 1, 0};
	private byte [] peggyFrame = new byte[13*25];

	//-----------------------------------------------------------------------------
	//constructor
	//-----------------------------------------------------------------------------
	/**
	 * @example PeggyTWI
	 * 		@param _p5
	 */
	public PeggyTWI(PApplet _p5) {
		p5 = _p5;
		welcome();
		
		for(int i=0; i<Serial.list().length; i++) {
			System.out.println( "[" + i + "]\t" + Serial.list()[i] );
		}
	}

	/**
	 * @example PeggyTWI
	 * 		@param _p5
	 */
	public PeggyTWI(PApplet _p5, Serial _arduino) {
		p5 = _p5;
		setArduino(_arduino);

		welcome();
		System.out.println( Serial.list() );
	}

	/**
	 * @example PeggyTWI
	 * 		@param _p5
	 * 		@param _portName
	 */
	public PeggyTWI(PApplet _p5, String _portName) {
		p5 = _p5;
		setArduino(_portName);
		
		welcome();
		System.out.println( Serial.list() );
	}

	/**
	 * @example PeggyTWI
	 * 		@param _p5
	 * 		@param _portName
	 * 		@param _baud 
	 */
	public PeggyTWI(PApplet _p5, String _portName, int _baud) {
		p5 = _p5;
		setArduino(_portName, _baud);
		
		welcome();
		System.out.println( Serial.list() );
	}
	
	//-----------------------------------------------------------------------------
	//methods
	//-----------------------------------------------------------------------------
	private void welcome() {
		System.out.println( "" );
		System.out.println( "-----------------------------------------------------------------------------" );
		System.out.println( "##name## ##version##" );
		System.out.println( "##author##" );
		System.out.println( "http://kenfrederick.blogspot.com/\n" );
	}


	/**
	 * render a PImage to peggy
	 * 		@param _srcImg
	 */
	public void renderToPeggy(PImage srcImg) {
		/*  
		 * render a PImage to the Peggy by transmitting it serially.  
		 * If it is not already sized to 25x25, this method will 
		 * create a downsized version to send...
		 */

		int idx = 0;
		PImage destImg = srcImg; 

		// iterate over the image, pull out pixels and 
		// build an array to serialize to the peggy
		for (int y =0; y < 25; y++) {
			byte val = 0;
			for (int x=0; x < 25; x++) {
				int c = destImg.get(x,y);
				int br = ((int) p5.brightness(c))>>4;

			if(x % 2 ==0) {
				val = (byte)br;
			} else {
				val = (byte) ((br<<4)|val);
				peggyFrame[idx++]= val;
			}

			}
			peggyFrame[idx++]= val;  // write that one last leftover half-byte
		}

		arduino.write(peggyHeader);
		arduino.write(peggyFrame);
	}

	/**
	 * Set the Serial
	 * 		@param _arduino
	 */
	public void setArduino(Serial _arduino) {
		arduino = _arduino;
	}

	/**
	 * Set the arduino port
	 * 		@param _portName
	 * 
	 * Set the baud rate
	 * 		@param _baud
	 */
	public void setArduino(String _portName, int _baud) {
		arduino = new Serial(p5, _portName, _baud);
	}

	/**
	 * Set the arduino port
	 * the baud rate is set to 115200 by default
	 * 		@param _portName
	 * 
	 */
	public void setArduino(String _portName) {
		arduino = new Serial(p5, _portName, 115200);
	}

	
	
}

