import peggyTWI.*;
import processing.serial.*;

PeggyTWI peggy;

void setup() {
  size(250,250);

  peggy = new PeggyTWI(this);
  peggy.setArduino( "/dev/cu.Bluetooth-PDA-Sync" );
}

void draw() {
  background(0);
}

