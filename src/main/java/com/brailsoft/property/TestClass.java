package com.brailsoft.property;

import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.ApplicationDecsriptor;
import com.brailsoft.model.Address;
import com.brailsoft.model.PostCode;
import com.brailsoft.model.Property;

public class TestClass {

	public static void main(String[] args) {
		System.out.println("Hello");
		ApplicationDecsriptor app = new ApplicationDecsriptor("TestClass");
		ApplicationConfiguration.registerApplication(app, "C:/Users/Nevil");
		PostCode pc = new PostCode("BH21 4EZ");
		Address addr = new Address(pc, new String[] { "4 Primrose Close", "Wimborne", "Dorset" });
		Property prop = new Property(addr);

		System.out.println(prop.address());
	}

}
