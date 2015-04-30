//Eric Franklin
package edu.gmu.teemw.junit;

import static org.junit.Assert.*;

import org.junit.Test;


import edu.gmu.teemw.ui.Menu;

public class MenuTest {

	@Test
	public void Test1() {
		Menu m = new Menu();
		assertEquals(m instanceof Menu, true);
	}
	@Test
	public void Test2() {
		Menu m = new Menu();
		assertEquals(m == null, false);
	}
}
