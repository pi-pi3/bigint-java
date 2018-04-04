import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AppTest {

	GrosseZahl i0;
	GrosseZahl s0;
	GrosseZahl i1;
	GrosseZahl s1;
	GrosseZahl i55;
	GrosseZahl s55;
	GrosseZahl i60;
	GrosseZahl s60;
	GrosseZahl i99;
	GrosseZahl s99;
	GrosseZahl i100;
	GrosseZahl s100;
	GrosseZahl i12345;
	GrosseZahl s12345;
	GrosseZahl s12345678901234567890;
	GrosseZahl s10345678901234567891;
	GrosseZahl i283935;
	GrosseZahl s2736;
	GrosseZahl s2737;


	@Before
	public void setUp() throws Exception {
		i0 = new GrosseZahl(0);
		s0 = new GrosseZahl("0");
		i1 = new GrosseZahl(1);
		s1 = new GrosseZahl("1");
		i55 = new GrosseZahl(55);
		s55 = new GrosseZahl("55");
		i60 = new GrosseZahl(60);
		s60 = new GrosseZahl("60");
		i99 = new GrosseZahl(99);
		s99 = new GrosseZahl("99");
		i100 = new GrosseZahl(100);
		s100 = new GrosseZahl("100");
		i12345 = new GrosseZahl(12345);
		s12345 = new GrosseZahl("12345");
		s12345678901234567890 = new GrosseZahl("12345678901234567890");
		s10345678901234567891 = new GrosseZahl("10345678901234567891");
		i283935 = new GrosseZahl("283935");
		s2736 = new GrosseZahl("2736");
		s2737 = new GrosseZahl("2737");
	}

	@Test
	public void testKonstruktor() {
		assertEquals("0", i0.toString());
		assertEquals("0", s0.toString());
		assertEquals("1", i1.toString());
		assertEquals("1", s1.toString());
		assertEquals("55", i55.toString());
		assertEquals("55", s55.toString());
		assertEquals("60", i60.toString());
		assertEquals("60", s60.toString());
		assertEquals("99", i99.toString());
		assertEquals("99", s99.toString());
		assertEquals("100", i100.toString());
		assertEquals("100", s100.toString());
		assertEquals("12345", i12345.toString());
		assertEquals("12345", s12345.toString());
		assertEquals("12345678901234567890", s12345678901234567890.toString());
		assertEquals("10345678901234567891", s10345678901234567891.toString());
		
	}
	
	@Test
	public void testLess() {
		assertTrue(!i0.less(s0));
		assertTrue(i0.less(i1));
		assertTrue(!i1.less(i0));
		
		assertTrue(!i55.less(s55));
		assertTrue(i55.less(i60));
		assertTrue(!i60.less(i55));
		
		assertTrue(!i99.less(s99));
		assertTrue(i99.less(i100));
		assertTrue(!i100.less(i99));
		
		assertTrue(!s12345678901234567890.less(s12345678901234567890));
		assertTrue(s10345678901234567891.less(s12345678901234567890));
		assertTrue(!s12345678901234567890.less(s10345678901234567891));
	}

	@Test
	public void testAdd() {
		assertEquals("0",i0.add(i0).toString());
		assertEquals("1",i1.add(i0).toString());
		assertEquals("1",i0.add(i1).toString());
		assertEquals("2",i1.add(s1).toString());
		
		assertEquals("115",i55.add(s60).toString());
		assertEquals("115",i60.add(s55).toString());
		
		assertEquals("115",s55.add(i60).toString());
		assertEquals("115",s60.add(i55).toString());
		
		assertEquals("12444",i99.add(i12345).toString());
		assertEquals("12444",i12345.add(i99).toString());
		
		assertEquals("22691357802469135781",s12345678901234567890.add(s10345678901234567891).toString());
		assertEquals("22691357802469135781",s10345678901234567891.add(s12345678901234567890).toString());
		
		GrosseZahl z = new GrosseZahl(0);
		for (int i=1; i<=125; i++)
			z=z.add(i99);
		assertEquals("12375",z.toString());
	}

	@Test
	public void testSub() {
		assertEquals("0",i0.sub(i0).toString());
		assertEquals("1",i1.sub(i0).toString());
		assertEquals("0",i1.sub(s1).toString());
		
		assertEquals("5",i60.sub(s55).toString());
		
		assertEquals("5",s60.sub(i55).toString());
		
		assertEquals("12246",i12345.sub(i99).toString());
		
		assertEquals("1999999999999999999",s12345678901234567890.sub(s10345678901234567891).toString());
	}
	
	@Test
	public void testMult() {
		assertEquals("0",i0.mult(i0).toString());
		assertEquals("1",i1.mult(i1).toString());
		
		assertEquals("3300",i55.mult(s60).toString());
		assertEquals("3300",i60.mult(s55).toString());
		
		assertEquals("3300",s55.mult(i60).toString());
		assertEquals("3300",s60.mult(i55).toString());
		
		assertEquals("1222155",s12345.mult(s99).toString());
		assertEquals("1222155",i12345.mult(i99).toString());
		
		assertEquals("152407406035740740602050",s12345678901234567890.mult(s12345).toString());
		assertEquals("152407406035740740602050",s12345678901234567890.mult(i12345).toString());
		
		GrosseZahl z = new GrosseZahl(1);
		for (int i=1; i<=9; i++)
			z=z.mult(i99);
		assertEquals("913517247483640899",z.toString());
	}
	
	@Test
	public void testDiv() {
		assertEquals(null,i0.divmod(i0));
		assertEquals("1",i1.divmod(i1).div.toString());
		
		assertEquals("0",i55.divmod(s60).div.toString());
		assertEquals("1",i60.divmod(s55).div.toString());
		
		assertEquals("0",s55.divmod(i60).div.toString());
		assertEquals("1",s60.divmod(i55).div.toString());
		
		assertEquals("124",s12345.divmod(s99).div.toString());
		assertEquals("124",i12345.divmod(i99).div.toString());
	}
	
	@Test
	public void testMod() {
		assertEquals(null,i0.divmod(i0));
		assertEquals("0",i1.divmod(i1).mod.toString());
		
		assertEquals("55",i55.divmod(s60).mod.toString());
		assertEquals("5",i60.divmod(s55).mod.toString());
		
		assertEquals("55",s55.divmod(i60).mod.toString());
		assertEquals("5",s60.divmod(i55).mod.toString());
		
		assertEquals("69",s12345.divmod(s99).mod.toString());
		assertEquals("69",i12345.divmod(i99).mod.toString());
	}
	
	@Test
	public void testGGT() {
		assertEquals("23",i283935.ggT(s2737).toString());
		assertEquals("23",s2737.ggT(i283935).toString());
		
		assertEquals("2737",s2737.ggT(s2737).toString());
		assertEquals("1",s2736.ggT(s2737).toString());
	}
}
